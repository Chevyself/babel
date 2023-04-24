package com.github.chevyself.babel.api.scoreboard;

import com.github.chevyself.babel.adapters.ObjectiveAdapter;
import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.channels.PlayerChannel;
import com.github.chevyself.babel.api.util.Players;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import com.github.chevyself.starbox.util.Strings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

/** Implementation of {@link ChannelScoreboard} for {@link Player} channels. */
public class PlayerScoreboard implements ChannelScoreboard {

  @NonNull private static final Map<Integer, String> characters = new HashMap<>();
  @NonNull private static final ObjectiveAdapter objectiveAdapter = Players.getObjectiveAdapter();

  static {
    PlayerScoreboard.characters.put(10, "a");
    PlayerScoreboard.characters.put(11, "b");
    PlayerScoreboard.characters.put(12, "c");
    PlayerScoreboard.characters.put(13, "d");
    PlayerScoreboard.characters.put(14, "e");
  }

  @NonNull @Getter private final UUID owner;
  @NonNull @Getter private final Scoreboard scoreboard;
  @NonNull @Getter private final Objective objective;
  @NonNull @Getter private List<ScoreboardLine> layout;

  private PlayerScoreboard(
      @NonNull UUID owner,
      @NonNull Scoreboard scoreboard,
      @NonNull Objective objective,
      @NonNull List<ScoreboardLine> layout) {
    this.owner = owner;
    this.scoreboard = scoreboard;
    this.objective = objective;
    this.layout = layout;
  }

  /**
   * Create a new scoreboard for the given {@link Player}.
   *
   * @param owner the player to create the scoreboard for
   * @param layout the layout of the scoreboard
   * @return the created scoreboard
   */
  @NonNull
  public static PlayerScoreboard create(@NonNull UUID owner, @NonNull List<ScoreboardLine> layout) {
    ScoreboardManager scoreboardManager = Objects.requireNonNull(Bukkit.getScoreboardManager());
    Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
    return new PlayerScoreboard(
        owner,
        scoreboard,
        PlayerScoreboard.objectiveAdapter.create(scoreboard, owner.toString(), "dummy", null),
        layout);
  }

  @NonNull
  private static String getEntryName(int position) {
    return BukkitUtils.format(
        "&" + (position <= 9 ? position : PlayerScoreboard.characters.get(position)) + "&r");
  }

  /**
   * Get the owner of this scoreboard as the Bukkit entity.
   *
   * @return the owner of this scoreboard
   */
  @NonNull
  public Optional<Player> getBukkit() {
    return Optional.ofNullable(Bukkit.getPlayer(this.owner));
  }

  /**
   * Get the channel of the owner of this scoreboard.
   *
   * @return the channel of this scoreboard
   */
  @NonNull
  public PlayerChannel getChannel() {
    return Channel.of(this.owner);
  }

  @Override
  public @NonNull PlayerScoreboard initialize(@Nullable String title) {
    if (title != null) this.objective.setDisplayName(title);
    this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    this.setLayout(this.layout);
    this.getBukkit().ifPresent(player -> player.setScoreboard(this.scoreboard));
    return this;
  }

  @NonNull
  private Team getLineTeam(int position, @NonNull String entryName) {
    Team team = this.scoreboard.getTeam("line_" + position);
    if (team == null) {
      team = this.scoreboard.registerNewTeam("line_" + position);
      team.addEntry(entryName);
    }
    return team;
  }

  @Override
  public @NonNull PlayerScoreboard update() {
    this.layout.forEach(this::newLine);
    return this;
  }

  @NonNull
  private Optional<ScoreboardLine> getLine(int position) {
    return this.layout.stream().filter(line -> line.getPosition() == position).findFirst();
  }

  @Override
  public @NonNull PlayerScoreboard update(int position) {
    this.getLine(position).ifPresent(this::newLine);
    return this;
  }

  @Override
  public @NonNull PlayerScoreboard setLayout(@NonNull List<ScoreboardLine> layout) {
    Set<Team> edited = layout.stream().map(this::newLine).collect(Collectors.toSet());
    this.layout = layout;
    this.scoreboard.getTeams().stream()
        .filter(team -> !edited.contains(team))
        .forEach(Team::unregister);
    this.update();
    return this;
  }

  @NonNull
  private Team newLine(@NonNull ScoreboardLine line) {
    String entryName = PlayerScoreboard.getEntryName(line.getPosition());
    Team team = this.getLineTeam(line.getPosition(), entryName);
    String current = team.getPrefix() + team.getSuffix();
    String build = line.build(this.getOfflinePlayer());
    if (!current.equalsIgnoreCase(build)) {
      List<String> divide = Strings.divide(build, 16);
      String lastColor = "";
      if (divide.isEmpty()) {
        team.setPrefix("");
        team.setSuffix("");
      } else if (divide.size() == 1) {
        team.setSuffix("");
      }
      for (int i = 0; i < divide.size(); i++) {
        String string = divide.get(i);
        switch (i) {
          case 0:
            team.setPrefix(lastColor + string);
            break;
          case 1:
            team.setSuffix(lastColor + string);
            break;
        }
        lastColor = ChatColor.getLastColors(string);
      }
      this.objective.getScore(entryName).setScore(line.getPosition());
    }
    return team;
  }

  @NonNull
  private OfflinePlayer getOfflinePlayer() {
    return Bukkit.getOfflinePlayer(this.owner);
  }
}

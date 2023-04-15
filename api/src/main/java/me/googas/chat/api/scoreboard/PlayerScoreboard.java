package me.googas.chat.api.scoreboard;

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
import me.googas.chat.adapters.ObjectiveAdapter;
import me.googas.chat.api.Channel;
import me.googas.chat.api.PlayerChannel;
import me.googas.chat.api.util.Players;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class PlayerScoreboard implements ChannelScoreboard {

  @NonNull private static final Map<Integer, String> characters = new HashMap<>();
  @NonNull private static final ObjectiveAdapter objectiveAdapter = Players.getObjectiveAdapter();

  static {
    characters.put(10, "a");
    characters.put(11, "b");
    characters.put(12, "c");
    characters.put(13, "d");
    characters.put(14, "e");
  }

  @NonNull @Getter private final UUID owner;
  @NonNull @Getter private final Scoreboard scoreboard;
  @NonNull @Getter private final Objective objective;
  @NonNull @Getter private List<ScoreboardLine> layout;

  public PlayerScoreboard(
      @NonNull UUID owner,
      @NonNull Scoreboard scoreboard,
      @NonNull Objective objective,
      @NonNull List<ScoreboardLine> layout) {
    this.owner = owner;
    this.scoreboard = scoreboard;
    this.objective = objective;
    this.layout = layout;
  }

  @NonNull
  public static PlayerScoreboard create(@NonNull UUID owner, @NonNull List<ScoreboardLine> layout) {
    ScoreboardManager scoreboardManager = Objects.requireNonNull(Bukkit.getScoreboardManager());
    Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
    return new PlayerScoreboard(
        owner,
        scoreboard,
        objectiveAdapter.create(scoreboard, owner.toString(), "dummy", null),
        layout);
  }

  @NonNull
  private static String getEntryName(int position) {
    return BukkitUtils.format("&" + (position <= 9 ? position : characters.get(position)) + "&r");
  }

  @NonNull
  public Optional<Player> getBukkit() {
    return Optional.ofNullable(Bukkit.getPlayer(this.owner));
  }

  @NonNull
  public PlayerChannel getChannel() {
    return Channel.of(this.owner);
  }

  @Override
  public @NonNull PlayerScoreboard initialize(String title) {
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
    String entryName = getEntryName(line.getPosition());
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

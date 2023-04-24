import com.github.chevyself.babel.lookup.LookUp;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.util.Versions;

public class LookUpClassTest {

  public static void main(String[] args) {
    String name =
        LookUp.forClass()
            .since(8, PacketType.Play.ClientBound.PLAYER_INFO.getName() + "$EnumPlayerInfoAction")
            .since(19, 2, PacketType.Play.ClientBound.PLAYER_INFO_UPDATE.getName() + "$a")
            .getName(new Versions.BukkitVersion(19, 4));
    System.out.println(name);
  }
}

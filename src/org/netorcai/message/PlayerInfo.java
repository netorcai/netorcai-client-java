import org.json.*;

public class PlayerInfo
{
    public int playerID = 0;
    public String nickname = "";
    public String remoteAddress = "";
    public boolean isConnected = false;

    public static PlayerInfo parse(JSONObject o)
    {
        PlayerInfo p = new PlayerInfo();
        p.playerID = o.getInt("player_id");
        p.nickname = o.getString("nickname");
        p.remoteAddress = o.getString("remote_address");
        p.isConnected = o.getBoolean("is_connected");

        return p;
    }
}

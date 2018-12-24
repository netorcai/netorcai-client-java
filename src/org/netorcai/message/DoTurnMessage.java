import java.util.*;
import org.json.*;

public class DoTurnMessage
{
    public ArrayList<PlayerActions> playerActions;

    public static DoTurnMessage parse(JSONArray a)
    {
        DoTurnMessage m = new DoTurnMessage();

        for (int i = 0; i < a.length(); i++)
        {
            m.playerActions.add(PlayerActions.parse(a.getJSONObject(i)));
        }

        return m;
    }
}

package org.netorcai.message;

import java.util.*;
import org.json.*;

public class DoTurnMessage
{
    public ArrayList<PlayerActions> playerActions;

    public static DoTurnMessage parse(JSONObject o)
    {
        DoTurnMessage m = new DoTurnMessage();
        m.playerActions = new ArrayList<PlayerActions>();

        JSONArray a = o.getJSONArray("player_actions");
        for (int i = 0; i < a.length(); i++)
        {
            m.playerActions.add(PlayerActions.parse(a.getJSONObject(i)));
        }

        return m;
    }
}

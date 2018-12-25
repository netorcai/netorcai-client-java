package org.netorcai.message;

import org.json.*;

public class PlayerActions
{
    public int playerID;
    public int turnNumber;
    public JSONArray actions;

    public static PlayerActions parse(JSONObject o)
    {
        PlayerActions a = new PlayerActions();
        a.playerID = o.getInt("player_id");
        a.turnNumber = o.getInt("turn_number");
        a.actions = o.getJSONArray("actions");

        return a;
    }
}

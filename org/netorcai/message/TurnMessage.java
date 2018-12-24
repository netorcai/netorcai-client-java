package org.netorcai.message;

import java.util.*;
import org.json.*;

public class TurnMessage
{
    public int turnNumber;
    public ArrayList<PlayerInfo> playersInfo;
    public JSONObject gameState;

    public static TurnMessage parse(JSONObject o)
    {
        TurnMessage m = new TurnMessage();
        m.turnNumber = o.getInt("turn_number");
        m.playersInfo = PlayerInfo.parse(o.getJSONArray("players_info"));
        m.gameState = o.getJSONObject("game_state");

        return m;
    }
}

package org.netorcai.message;

import org.json.*;

public class GameEndsMessage
{
    public int winnerPlayerID;
    public JSONObject gameState;

    public static GameEndsMessage parse(JSONObject o)
    {
        GameEndsMessage m = new GameEndsMessage();
        m.winnerPlayerID = o.getInt("winner_player_id");
        m.gameState = o.getJSONObject("game_state");

        return m;
    }
}

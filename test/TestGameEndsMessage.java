package org.netorcai.message;

import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGameEndsMessage
{
    @Test
    public void testParse()
    {
        String s = "{ \"message_type\": \"GAME_ENDS\", \"winner_player_id\": 0, \"game_state\": {}}";
        JSONObject o = new JSONObject(s);
        GameEndsMessage m = GameEndsMessage.parse(o);
        assertEquals(0, m.winnerPlayerID);
        assertEquals(0, m.gameState.length());
    }
}

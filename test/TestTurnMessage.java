package org.netorcai.message;

import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTurnMessage
{
    @Test
    public void testParse()
    {
        String s = "{ \"message_type\": \"TURN\", \"turn_number\": 0, \"game_state\": {}, \"players_info\": [{\"player_id\": 0,           \"nickname\": \"jugador\", \"remote_address\": \"127.0.0.1:59840\", \"is_connected\": true}]}";
        JSONObject o = new JSONObject(s);
        TurnMessage m = TurnMessage.parse(o);
        assertEquals(0, m.turnNumber);
        assertEquals(0, m.gameState.length());
        assertEquals(0, m.playersInfo.get(0).playerID);
        assertEquals("jugador", m.playersInfo.get(0).nickname);
        assertEquals("127.0.0.1:59840", m.playersInfo.get(0).remoteAddress);
        assertEquals(true, m.playersInfo.get(0).isConnected);
    }
}

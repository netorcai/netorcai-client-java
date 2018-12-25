package org.netorcai.message;

import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDoTurnMessage
{
    @Test
    public void testParse()
    {
        String s = "{\"message_type\": \"DO_TURN\", \"player_actions\": [ { \"player_id\": 1, \"turn_number\": 2, \"actions\": [] }, { \"player_id\": 0, \"turn_number\": 3, \"actions\": [4] }]}";
        JSONObject o = new JSONObject(s);
        DoTurnMessage m = DoTurnMessage.parse(o);
        assertEquals(2, m.playerActions.size());
        assertEquals(1, m.playerActions.get(0).playerID);
        assertEquals(2, m.playerActions.get(0).turnNumber);
        assertEquals(0, m.playerActions.get(0).actions.length());
        assertEquals(0, m.playerActions.get(1).playerID);
        assertEquals(3, m.playerActions.get(1).turnNumber);
        assertEquals(1, m.playerActions.get(1).actions.length());
        assertEquals(4, m.playerActions.get(1).actions.getInt(0));
    }
}

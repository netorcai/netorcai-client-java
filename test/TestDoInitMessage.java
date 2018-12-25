package org.netorcai.test;

import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.netorcai.message.*;

public class TestDoInitMessage
{
    @Test
    void parse()
    {
        String s = "{\"message_type\": \"DO_INIT\", \"nb_players\": 4, \"nb_turns_max\": 100}";
        JSONObject o = new JSONObject(s);
        DoInitMessage m = DoInitMessage.parse(o);
        assertEquals(4, m.nbPlayers);
        assertEquals(100, m.nbTurnsMax);
    }
}

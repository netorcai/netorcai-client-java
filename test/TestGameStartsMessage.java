package org.netorcai.message;

import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGameStartsMessage
{
    @Test
    public void testParse()
    {
        String s = "{\"message_type\": \"GAME_STARTS\", \"player_id\": 0, \"players_info\": [ { \"player_id\": 0, \"nickname\": \"jugador\", \"remote_address\": \"127.0.0.1:59840\", \"is_connected\": true } ], \"nb_players\": 4, \"nb_turns_max\": 100, \"milliseconds_before_first_turn\": 1000, \"milliseconds_between_turns\": 1000, \"initial_game_state\": {}}";
        JSONObject o = new JSONObject(s);
        GameStartsMessage m = GameStartsMessage.parse(o);
        assertEquals(0, m.playerID);
        assertEquals(1, m.playersInfo.size());
        assertEquals(0, m.playersInfo.get(0).playerID);
        assertEquals("jugador", m.playersInfo.get(0).nickname);
        assertEquals("127.0.0.1:59840", m.playersInfo.get(0).remoteAddress);
        assertEquals(true, m.playersInfo.get(0).isConnected);
        assertEquals(4, m.nbPlayers);
        assertEquals(100, m.nbTurnsMax);
        assertEquals(1000, m.msBeforeFirstTurn);
        assertEquals(1000, m.msBetweenTurns);
        assertEquals(0, m.initialGameState.length());
    }
}

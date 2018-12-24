package org.netorcai.message;

import java.util.*;
import org.json.*;

public class GameStartsMessage
{
    public int playerID;
    public int nbPlayers;
    public int nbTurnsMax;
    public double msBeforeFirstTurn;
    public double msBetweenTurns;
    public ArrayList<PlayerInfo> playersInfo;
    public JSONObject initialGameState;

    public static GameStartsMessage parse(JSONObject o)
    {
        GameStartsMessage m = new GameStartsMessage();
        m.playerID = o.getInt("player_id");
        m.nbPlayers = o.getInt("nb_players");
        m.nbTurnsMax = o.getInt("nb_turns_max");
        m.msBeforeFirstTurn = o.getDouble("ms_before_first_turn");
        m.msBetweenTurns = o.getDouble("ms_between_turns");
        m.playersInfo = PlayerInfo.parse(o.getJSONArray("players_info"));
        m.initialGameState = o.getJSONObject("initial_game_state");

        return m;
    }
}

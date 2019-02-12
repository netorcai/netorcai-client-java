package org.netorcai.message;

import org.json.*;

public class DoInitMessage
{
    public int nbPlayers;
    public int nbSpecialPlayers;
    public int nbTurnsMax;

    public static DoInitMessage parse(JSONObject o)
    {
        DoInitMessage m = new DoInitMessage();
        m.nbPlayers = o.getInt("nb_players");
        m.nbSpecialPlayers = o.getInt("nb_special_players");
        m.nbTurnsMax = o.getInt("nb_turns_max");

        return m;
    }
}

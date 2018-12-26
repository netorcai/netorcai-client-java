package org.netorcai;

import java.io.*;
import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.netorcai.message.*;

public class TestClient
{
    class Netorcai
    {
        public Process process;
        public BufferedReader output;
    }

    private Netorcai launchNetorcaiWaitListening(int nbPlayers, int nbVisus) throws IOException
    {
        Netorcai netorcai = new Netorcai();

        String command = String.format("netorcai --simple-prompt --delay-first-turn=50 --delay-turns=50 --nb-turns-max=2 --nb-players-max=%d --nb-visus-max=%d --autostart",
            nbPlayers, nbVisus);

        netorcai.process = Runtime.getRuntime().exec(command);
        netorcai.output = new BufferedReader(new InputStreamReader(netorcai.process.getInputStream()));

        String firstLine = netorcai.output.readLine();
        if (!firstLine.contains("Listening incoming connections"))
        {
            throw new IOException("First netorcai's output is not about Listening incoming connections");
        }

        return netorcai;
    }

    @Test
    public void testEverythingGoesWell() throws IOException
    {
        try
        {
            Netorcai n = launchNetorcaiWaitListening(1, 0);

            Client gl = new Client();
            gl.connect();
            gl.sendLogin("gl", "game logic");
            gl.readLoginAck();

            Client player = new Client();
            player.connect();
            player.sendLogin("player", "player");
            player.readLoginAck();

            // Game should start automatically as one player is connected (--autostart)
            DoInitMessage doInit = gl.readDoInit();
            gl.sendDoInitAck(new JSONObject("{\"all_clients\": {\"gl\": \"Java™\"}})"));
            player.readGameStarts();

            for (int i = 1; i < doInit.nbTurnsMax; i++)
            {
                gl.readDoTurn();
                gl.sendDoTurnAck(new JSONObject("{\"all_clients\": {\"gl\": \"Java™\"}}"), -1);

                TurnMessage turn = player.readTurn();
                player.sendTurnAck(turn.turnNumber, new JSONArray("[{\"player\": \"Java™\"}]"));
            }

            gl.readDoTurn();
            gl.sendDoTurnAck(new JSONObject("{\"all_clients\": {\"gl\": \"Java™\"}}"), -1);

            player.readGameEnds();
            n.process.destroy();
        }
        finally
        {
            Runtime.getRuntime().exec("killall netorcai");
        }
    }
}

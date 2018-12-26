package org.netorcai;

import java.io.*;
import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private Client getKickedClient() throws IOException
    {
        Client c = new Client();
        c.connect();
        c.sendString("¿qué?");

        return c;
    }

    private Client getLoggedPlayer() throws IOException
    {
        Client c = new Client();
        c.connect();
        c.sendLogin("I", "player");

        return c;
    }

    @Test
    public void testEverythingGoesWell() throws IOException
    {
        Netorcai n = null;
        try
        {
            n = launchNetorcaiWaitListening(1, 0);

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
        }
        finally
        {
            if (n != null)
                n.process.destroy();
        }
    }

    @Test
    public void testKickedInsteadOfExpectedMessage()
    {
        Netorcai n = null;
        try
        {
            n = launchNetorcaiWaitListening(1, 0);
            String kickReason = "Kicked from netorcai. Reason: Invalid first message: Non-JSON message received";
            Throwable e;

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getKickedClient();
                c.readLoginAck();
            });
            assertEquals(kickReason, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getKickedClient();
                c.readLoginAck();
            });
            assertEquals(kickReason, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getKickedClient();
                c.readGameStarts();
            });
            assertEquals(kickReason, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getKickedClient();
                c.readTurn();
            });
            assertEquals(kickReason, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getKickedClient();
                c.readGameEnds();
            });
            assertEquals(kickReason, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getKickedClient();
                c.readDoInit();
            });
            assertEquals(kickReason, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getKickedClient();
                c.readDoTurn();
            });
            assertEquals(kickReason, e.getMessage());
        }
        catch (IOException e) {}
        finally
        {
            if (n != null)
                n.process.destroy();
        }
    }

    @Test
    public void testLoginAckInsteadOfExpected()
    {
        Netorcai n = null;
        try
        {
            n = launchNetorcaiWaitListening(10, 0);
            String exceptionMessage = "Unexpected message received: LOGIN_ACK";
            Throwable e;

            // LOGIN_ACK instead of something else
            e = assertThrows(RuntimeException.class, () -> {
                Client c = getLoggedPlayer();
                c.readGameStarts();
            });
            assertEquals(exceptionMessage, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getLoggedPlayer();
                c.readTurn();
            });
            assertEquals(exceptionMessage, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getLoggedPlayer();
                c.readGameEnds();
            });
            assertEquals(exceptionMessage, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getLoggedPlayer();
                c.readDoInit();
            });
            assertEquals(exceptionMessage, e.getMessage());

            e = assertThrows(RuntimeException.class, () -> {
                Client c = getLoggedPlayer();
                c.readDoTurn();
            });
            assertEquals(exceptionMessage, e.getMessage());
        }
        catch (IOException e) {}
        finally
        {
            if (n != null)
                n.process.destroy();
        }
    }

    @Test
    public void testUnexpectedMessageRead()
    {
        Netorcai n = null;
        try
        {
            n = launchNetorcaiWaitListening(2, 0);
            String exceptionMessage = "Unexpected message received: LOGIN_ACK";
            Throwable e;

            Client player1 = getLoggedPlayer();
            Client player2 = getLoggedPlayer();

            player1.readLoginAck();
            player2.readLoginAck();

            Client gl = new Client();
            gl.connect();
            gl.sendLogin("gl", "game logic");
            gl.readLoginAck();

            // Game should start automatically as two players are connected (--autostart)
            DoInitMessage doInit = gl.readDoInit();
            gl.sendDoInitAck(new JSONObject("{\"all_clients\": {\"gl\": \"Java\"}}"));

            player2.readGameStarts();

            // Player1 fails now.
            boolean player1Failed = false;
            try
            {
                player1.readLoginAck();
            }
            catch (RuntimeException ex)
            {
                player1Failed = true;
                assertEquals("Unexpected message received: GAME_STARTS", ex.getMessage());
            }
            assertEquals(true, player1Failed);

            for (int i = 1; i < doInit.nbTurnsMax; i++)
            {
                gl.readDoTurn();
                gl.sendDoTurnAck(new JSONObject("{\"all_clients\": {\"gl\": \"Java\"}}"), -1);

                TurnMessage turn = player2.readTurn();
                player2.sendTurnAck(turn.turnNumber, new JSONArray("[{\"player\": \"Java\"}]"));
            }

            System.out.println("hey");
            gl.readDoTurn();
            gl.sendDoTurnAck(new JSONObject("{\"all_clients\": {\"gl\": \"Java\"}}"), -1);

            // Player2 fails now.
            boolean player2Failed = false;
            try
            {
                player2.readTurn();
            }
            catch (RuntimeException ex)
            {
                player2Failed = true;
                assertEquals("Game over!", ex.getMessage());
            }
            assertEquals(true, player2Failed);
        }
        catch (IOException e) {}
        finally
        {
            if (n != null)
                n.process.destroy();
        }
    }
}

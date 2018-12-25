package org.netorcai.example;

import org.json.*;
import org.netorcai.Client;
import org.netorcai.message.*;

public class HelloPlayer
{
    public static void main(String [] args)
    {
        try
        {
            Client c = new Client();

            System.out.printf("Connecting to netorcai... ");
            System.out.flush();
            c.connect();
            System.out.println("done");

            System.out.printf("Logging in as a player... ");
            System.out.flush();
            c.sendLogin("java", "player");
            c.readLoginAck();
            System.out.println("done");

            System.out.printf("Waiting for GAME_STARTS... ");
            System.out.flush();
            GameStartsMessage gameStarts = c.readGameStarts();
            System.out.println("done");

            for (int i = 1; i < gameStarts.nbTurnsMax; i++)
            {
                System.out.printf("Waiting for TURN... ");
                System.out.flush();
                TurnMessage turn = c.readTurn();
                System.out.println("done");

                c.sendTurnAck(turn.turnNumber,  new JSONArray("[{\"player\": \"Java\"}]"));
            }
        }
        catch(Throwable t)
        {
            System.out.println(t);
        }
    }
}

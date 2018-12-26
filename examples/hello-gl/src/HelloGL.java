package org.netorcai.example;

import org.json.*;
import org.netorcai.Client;
import org.netorcai.message.*;

public class HelloGL
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

            System.out.printf("Logging in as a game logic... ");
            System.out.flush();
            c.sendLogin("java-GL", "game logic");
            c.readLoginAck();
            System.out.println("done");

            System.out.printf("Waiting for DO_INIT... ");
            System.out.flush();
            DoInitMessage doInit = c.readDoInit();
            System.out.println("done");

            c.sendDoInitAck(new JSONObject("{\"all_clients\": {\"gl\": \"Java\"}}"));

            for (int turn = 0; turn < doInit.nbTurnsMax; turn++)
            {
                System.out.printf("Waiting for DO_TURN... ");
                System.out.flush();
                DoTurnMessage doTurn = c.readDoTurn();
                System.out.println("done");

                c.sendDoTurnAck(new JSONObject("{\"all_clients\": {\"gl\": \"Java\"}}"), -1);
            }
        }
        catch(Throwable t)
        {
            System.out.println(t);
        }
    }
}

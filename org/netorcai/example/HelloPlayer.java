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
            c.connect();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}

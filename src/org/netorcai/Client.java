package org.netorcai;

import org.json.*;

public class Client
{
    public void connect(String hostname, int port)
    {

    }

    public void connect(String hostname)
    {
        connect(hostname, 4242);
    }

    public void connect()
    {
        connect("localhost", 4242);
    }

    public void close()
    {

    }

    public String recvString()
    {
        return new String();
    }

    public JSONObject recvJson()
    {
        return new JSONObject();
    }

    public void sendString()
    {

    }

    public void sendJson(JSONObject object)
    {

    }
}

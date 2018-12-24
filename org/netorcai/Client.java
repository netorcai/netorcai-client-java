package org.netorcai;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.*;
import org.json.*;

public class Client
{
    public void connect(String hostname, int port) throws UnknownHostException, IOException
    {
        _socket = new Socket(hostname, port);
        _in = new DataInputStream(new BufferedInputStream(_socket.getInputStream()));
        _out = new DataOutputStream(new BufferedOutputStream(_socket.getOutputStream()));
    }

    public void connect(String hostname) throws UnknownHostException, IOException
    {
        connect(hostname, 4242);
    }

    public void connect() throws UnknownHostException, IOException
    {
        connect("localhost", 4242);
    }

    public void close() throws IOException
    {
        _socket.close();
    }

    public String recvString() throws IOException
    {
        // Read string size (2 bytes) from the network.
        // These 2 bytes are an unsigned 16-bit integer in little-endian.
        // Java does not know unsigned types, so this code is a bit funny..
        int byte1 = _in.readUnsignedShort();
        int byte2 = _in.readUnsignedShort();
        int stringSize = (byte2 << 8) | byte1;

        // Read string content from the network.
        byte[] bytes = new byte[stringSize];
        _in.readFully(bytes);

        return new String(bytes, "UTF-8");
    }

    public JSONObject recvJson() throws IOException
    {
        return new JSONObject(recvString());
    }

    // (╯°□°）╯︵ ┻━┻)
    // Looks like Java does not know unsigned integers in 2018 (No, I did NOT meant 1958).
    // I'll consider using Java2K next time.
    // Hero of the day: https://stackoverflow.com/a/9883582
    public static void putUnsignedShort(ByteBuffer bb, int position, int value)
    {
        bb.putShort(position, (short) (value & 0xffff));
    }

    public void sendString(String s) throws IOException
    {
        String stringToSend = s + "\n";
        byte[] bytes = stringToSend.getBytes(Charset.forName("UTF-8"));

        // Send string size on the socket
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        putUnsignedShort(bb, 0, bytes.length);
        _out.write(bb.array());

        // Send bytes on the socket
        _out.write(bytes);
    }

    public void sendJson(JSONObject object) throws IOException
    {
        sendString(object.toString());
    }

    private Socket _socket;
    DataInputStream _in;
    DataOutputStream _out;
}

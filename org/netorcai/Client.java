package org.netorcai;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.*;
import org.json.*;

import org.netorcai.message.*;

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
        int byte1 = _in.readByte();
        int byte2 = _in.readByte();
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
        _out.flush();
    }

    public void sendJson(JSONObject object) throws IOException
    {
        sendString(object.toString());
    }

    public LoginAckMessage readLoginAck() throws IOException
    {
        JSONObject msg = recvJson();
        switch(msg.getString("message_type"))
        {
            case "LOGIN_ACK":
                return new LoginAckMessage();
            case "KICK":
                throw new RuntimeException("Kicked from netorcai. Reason: " + msg.getString("kick_reason"));
            default:
                throw new RuntimeException("Unexpected message received: " + msg.getString("message_type"));
        }
    }

    public GameStartsMessage readGameStarts() throws IOException
    {
        JSONObject msg = recvJson();
        switch(msg.getString("message_type"))
        {
            case "GAME_STARTS":
                return GameStartsMessage.parse(msg);
            case "KICK":
                throw new RuntimeException("Kicked from netorcai. Reason: " + msg.getString("kick_reason"));
            default:
                throw new RuntimeException("Unexpected message received: " + msg.getString("message_type"));
        }
    }

    public TurnMessage readTurn() throws IOException
    {
        JSONObject msg = recvJson();
        switch(msg.getString("message_type"))
        {
            case "TURN":
                return TurnMessage.parse(msg);
            case "GAME_ENDS":
                throw new RuntimeException("Game over!");
            case "KICK":
                throw new RuntimeException("Kicked from netorcai. Reason: " + msg.getString("kick_reason"));
            default:
                throw new RuntimeException("Unexpected message received: " + msg.getString("message_type"));
        }
    }

    public GameEndsMessage readGameEnds() throws IOException
    {
        JSONObject msg = recvJson();
        switch(msg.getString("message_type"))
        {
            case "GAME_ENDS":
                return GameEndsMessage.parse(msg);
            case "KICK":
                throw new RuntimeException("Kicked from netorcai. Reason: " + msg.getString("kick_reason"));
            default:
                throw new RuntimeException("Unexpected message received: " + msg.getString("message_type"));
        }
    }

    public DoInitMessage readDoInit() throws IOException
    {
        JSONObject msg = recvJson();
        switch(msg.getString("message_type"))
        {
            case "DO_INIT":
                return DoInitMessage.parse(msg);
            case "KICK":
                throw new RuntimeException("Kicked from netorcai. Reason: " + msg.getString("kick_reason"));
            default:
                throw new RuntimeException("Unexpected message received: " + msg.getString("message_type"));
        }
    }

    public DoTurnMessage readDoTurn() throws IOException
    {
        JSONObject msg = recvJson();
        switch(msg.getString("message_type"))
        {
            case "DO_TURN":
                return DoTurnMessage.parse(msg);
            case "KICK":
                throw new RuntimeException("Kicked from netorcai. Reason: " + msg.getString("kick_reason"));
            default:
                throw new RuntimeException("Unexpected message received: " + msg.getString("message_type"));
        }
    }

    public void sendLogin(String nickname, String role) throws IOException
    {
        JSONObject o = new JSONObject();
        o.put("message_type", "LOGIN");
        o.put("nickname", nickname);
        o.put("role", role);

        sendJson(o);
    }

    public void sendTurnAck(int turnNumber, JSONArray actions) throws IOException
    {
        JSONObject o = new JSONObject();
        o.put("message_type", "TURN_ACK");
        o.put("turn_number", turnNumber);
        o.put("actions", actions);

        sendJson(o);
    }

    public void sendDoInitAck(JSONObject initialGameState) throws IOException
    {
        JSONObject o = new JSONObject();
        o.put("message_type", "DO_INIT_ACK");
        o.put("initial_game_state", initialGameState);

        sendJson(o);
    }

    public void sendDoTurnAck(JSONObject gameState, int winnerPlayerID) throws IOException
    {
        JSONObject o = new JSONObject();
        o.put("message_type", "DO_TURN_ACK");
        o.put("game_state", gameState);
        o.put("winner_player_id", winnerPlayerID);

        sendJson(o);
    }

    private Socket _socket;
    private DataInputStream _in;
    private DataOutputStream _out;
}

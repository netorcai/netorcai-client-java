package org.netorcai.message;

import org.json.*;
import org.netorcai.Version;

public class LoginAckMessage
{
    public String metaprotocolVersion;

    public static LoginAckMessage parse(JSONObject o)
    {
        LoginAckMessage m = new LoginAckMessage();
        m.metaprotocolVersion = o.getString("metaprotocol_version");

        if (!m.metaprotocolVersion.equals(Version.metaprotocolVersion()))
        {
            System.out.format("Warning: netorcai uses version '%s' while netorcai-client-java uses '%s'\n",
                m.metaprotocolVersion, Version.metaprotocolVersion());
        }

        return m;
    }
}

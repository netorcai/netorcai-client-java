package org.netorcai;

public class Version
{
    public static int major = 2;
    public static int minor = 0;
    public static int patch = 0;

    public static String metaprotocolVersion()
    {
        return String.format("%d.%d.%d", major, minor, patch);
    }
}

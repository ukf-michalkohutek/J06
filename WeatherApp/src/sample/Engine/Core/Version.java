package sample.Engine.Core;

public final class Version {

    private final int major;
    private final int minor;
    private final int patch;

    public Version(int _major, int _minor, int _patch)
    {
        major = _major;
        minor = _minor;
        patch = _patch;
    }

    public Version(Version copy)
    {
        major = copy.major;
        minor = copy.minor;
        patch = copy.patch;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString()
    {
        return major + "." + minor + "." + patch;
    }

}

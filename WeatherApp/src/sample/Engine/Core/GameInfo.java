package sample.Engine.Core;

public class GameInfo {

    private final String gameName;
    private final Version gameVersion;
    private final Version engineVersion;

    public GameInfo(String _gameName, Version _gameVersion, Version _engineVersion)
    {
        gameName = _gameName;
        gameVersion = _gameVersion;
        engineVersion = _engineVersion;
    }

    public GameInfo(GameInfo copy)
    {
        gameName = copy.gameName;
        gameVersion = new Version(copy.gameVersion);
        engineVersion = new Version(copy.engineVersion);
    }

    public String getGameName()
    {
        return gameName;
    }

    public Version getGameVersion()
    {
        return gameVersion;
    }

    public Version getEngineVersion()
    {
        return engineVersion;
    }

}

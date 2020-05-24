package sample.Engine;

import sample.Engine.Core.GameScene;

public class GameSetup {

    public final String gameName = "Some Weird Weather App";
    public final int majorVersion = 1;
    public final int minorVersion = 0;
    public final int patchVersion = 0;
    public final double targetFramerate = 60;
    public final int defaultResolutionX = 500;
    public final int defaultResolutionY = 700;
    public final boolean resizableWindow = true;
    public final GameScene mainScene = new StartScene();

}

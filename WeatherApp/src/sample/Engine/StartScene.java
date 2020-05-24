package sample.Engine;

import sample.Engine.Core.GameScene;
import sample.Engine.Core.Main;
import sample.MainAppScene;

public class StartScene extends GameScene {

    @Override
    public void start() {
        Main.getSceneManager().setGameScene(new MainAppScene());
    }

}
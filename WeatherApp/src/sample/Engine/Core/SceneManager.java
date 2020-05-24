package sample.Engine.Core;

public class SceneManager {

    private GameScene gameScene;

    public SceneManager(GameScene _gameScene)
    {
        gameScene = _gameScene;
    }

    public void setGameScene(GameScene _gameScene)
    {
        gameScene = _gameScene;
    }

    public GameScene getGameScene() {
        return gameScene;
    }

}

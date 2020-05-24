// Game Engine by Ján Trenčanský

package sample.Engine.Core;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Engine.GameSetup;

import java.util.Iterator;
import java.util.List;

public class Main extends Application {

    private long currentTime;

    private Group root;
    private static Scene scene;
    private ObservableList<Node> children;

    private GameSettings gameSettings;
    private GameScene gameScene;

    private static GameInfo gameInfo;
    private static SceneManager sceneManager;

    @Override
    public void start(Stage primaryStage) {
        setupGame();
        primaryStage.setResizable(gameSettings.isResizableWindow());

        root = new Group();
        root.setOnMouseMoved(e -> { GameInput.mouseCoordinates.x = e.getSceneX(); GameInput.mouseCoordinates.y = e.getSceneY(); });
        root.setOnMouseDragged(e -> { GameInput.mouseCoordinates.x = e.getSceneX(); GameInput.mouseCoordinates.y = e.getSceneY(); });
        root.setOnMousePressed(e -> GameInput.mouseClicks.add(e.getButton()));
        children = root.getChildren();

        Vector2D<Integer> resolution = gameSettings.getDefaultResolution();
        scene = new Scene(root, resolution.x, resolution.y);

        children.add(GameInput.getInstance());

        currentTime = System.nanoTime();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000 / gameSettings.getTargetFramerate()), e -> update());
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Version version = gameInfo.getGameVersion();
        primaryStage.setTitle(gameInfo.getGameName() + " - " + version.getMajor() + "." + version.getMinor() + "." + version.getPatch());
        primaryStage.setScene(scene);
        primaryStage.show();

        loadSceneObjects();
    }

    private void setupGame()
    {
        int engineVersionMajor = 2;
        int engineVersionMinor = 2;
        int engineVersionPatch = 0;

        GameSetup gameSetup = new GameSetup();

        gameInfo = new GameInfo(
                gameSetup.gameName,
                new Version(gameSetup.majorVersion, gameSetup.minorVersion, gameSetup.patchVersion),
                new Version(engineVersionMajor, engineVersionMinor, engineVersionPatch)
        );

        gameSettings = new GameSettings(
                gameSetup.targetFramerate,
                new Vector2D<>(gameSetup.defaultResolutionX, gameSetup.defaultResolutionY),
                gameSetup.resizableWindow
        );

        sceneManager = new SceneManager(gameSetup.mainScene);
        gameScene = gameSetup.mainScene;

        GameInput.setup();
    }

    private void addAllGameObjects(List<GameObject> gameObjects)
    {
        for (GameObject obj : gameObjects)
        {
            if (obj.node != null)
            {
                children.add(obj.node);
            }
        }
    }

    private void removeAllGameObjects(List<GameObject> gameObjects)
    {
        for (GameObject obj : gameObjects)
        {
            if (obj.node != null)
            {
                children.remove(obj.node);
            }
        }
    }

    private void loadSceneObjects()
    {
        removeAllGameObjects(gameScene.objects);
        gameScene = sceneManager.getGameScene();
        addAllGameObjects(gameScene.objects);

        for (GameObject obj : gameScene.objects)
        {
            obj.start();
        }

        gameScene.start();
    }

    private void loadNewSceneObjects()
    {
        for (GameObject obj : gameScene.newObjects)
        {
            obj.start();
        }

        addAllGameObjects(gameScene.newObjects);
        gameScene.objects.addAll(gameScene.newObjects);
        gameScene.newObjects.clear();
    }

    private void update()
    {
        if (gameScene != sceneManager.getGameScene())
        {
            loadSceneObjects();
        }

        if (gameScene.newObjects.size() > 0)
        {
            loadNewSceneObjects();
        }

        long newTime = System.nanoTime();
        double deltaTime = (newTime - currentTime) / 1000000000d;
        currentTime = newTime;

        GameObject obj;

        Iterator<GameObject> iterator = gameScene.objects.iterator();
        while (iterator.hasNext())
        {
            obj = iterator.next();

            if (!obj.isAlive())
            {
                iterator.remove();
                children.remove(obj.node);
                gameScene.objects.remove(obj);
                continue;
            }
            obj.update(deltaTime);
            obj.componentUpdate(deltaTime);
        }

        gameScene.update(deltaTime);

        GameInput.mouseClicks.clear();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public static void setCursor(Cursor cursor)
    {
        scene.setCursor(cursor);
    }

    public static void setCursor(String path, Vector2D<Double> hotspot)
    {
        scene.setCursor(new ImageCursor(new Image(path), hotspot.x, hotspot.y));
    }

    public static GameInfo getGameInfo()
    {
        return new GameInfo(gameInfo);
    }

    public static SceneManager getSceneManager()
    {
        return sceneManager;
    }

    public static Vector2D<Double> getCurrentResolution()
    {
        return new Vector2D<>(scene.getWidth(), scene.getHeight());
    }

}

package sample.Engine.Core;

import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GameSubScene extends GameObject {

    private final Group root;
    private final List<GameObject> objects = new LinkedList<>();
    private final List<GameObject> newObjects = new LinkedList<>();

    public GameSubScene()
    {
        super();

        root = new Group();
        node = new SubScene(root, 0, 0, true, SceneAntialiasing.BALANCED);
    }

    public final SubScene getSubScene()
    {
        return ((SubScene)node);
    }

    public final Group getRoot()
    {
        return ((Group)getSubScene().getRoot());
    }

    public final void addObject(GameObject gameObject)
    {
        gameObject.currentSubScene = this;
        objects.add(gameObject);
        if (gameObject.node != null) getRoot().getChildren().add(gameObject.node);
    }

    public final void addObjectNextFrame(GameObject gameObject)
    {
        newObjects.add(gameObject);
    }

    public final List<GameObject> getObjects()
    {
        return objects;
    }

    public final void setCamera(Camera3D camera)
    {
        getSubScene().setCamera(camera.getCamera());
    }

    public final void setCamera(Camera2D camera)
    {
        getSubScene().setCamera(camera.getCamera());
    }

    @Override
    public final void start() {
        for (GameObject obj : objects)
        {
            obj.start();
        }
    }

    @Override
    public final void update(double deltaTime) {
        if (newObjects.size() > 0)
        {
            loadNewSceneObjects();
        }

        Vector2D<Double> resolution = Main.getCurrentResolution();
        getSubScene().setWidth(resolution.x);
        getSubScene().setHeight(resolution.y);

        GameObject obj;

        Iterator<GameObject> iterator = objects.iterator();
        while (iterator.hasNext())
        {
            obj = iterator.next();

            if (!obj.isAlive())
            {
                iterator.remove();
                getRoot().getChildren().remove(obj.node);
                objects.remove(obj);
                continue;
            }
            obj.update(deltaTime);
            obj.componentUpdate(deltaTime);
        }
    }

    private final void loadNewSceneObjects()
    {
        for (GameObject obj : newObjects)
        {
            obj.start();

            obj.currentSubScene = this;
            objects.add(obj);
            getRoot().getChildren().add(obj.node);
        }

        newObjects.clear();
    }

}

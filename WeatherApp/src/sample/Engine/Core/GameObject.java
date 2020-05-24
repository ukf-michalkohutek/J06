package sample.Engine.Core;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class GameObject {

    public String name;
    public Node node;
    public GameSubScene currentSubScene;

    private boolean alive;
    private final List<GameComponent> components = new LinkedList<>();

    public GameObject()
    {
        name = "";
        alive = true;
    }

    public void start() {}

    public void update(double deltaTime) {}

    public void componentUpdate(double deltaTime)
    {
        for (GameComponent component : components) {
            component.update(deltaTime);
        }
    }

    public final void destroy()
    {
        alive = false;
    }

    public final boolean isAlive()
    {
        return alive;
    }

    public final void setOpacity(double value)
    {
        node.setOpacity(value);
    }

    public final double getOpacity()
    {
        return node.getOpacity();
    }

    public final void addComponent(GameComponent component)
    {
        components.add(component);
    }

    public final <T> boolean hasComponent(Class<T> type)
    {
        for (GameComponent component : components) {
            if (type.isInstance(component)) {
                return true;
            }
        }

        return false;
    }

    public final <T> T getComponent(Class<T> type)
    {
        for (GameComponent component : components) {
            if (type.isInstance(component)) {
                return ((T) component);
            }
        }

        return null;
    }

    public final <T> List<T> getComponents(Class<T> type)
    {
        List<T> returnComponents = new LinkedList<>();

        for (GameComponent component : components) {
            if (type.isInstance(component)) {
                returnComponents.add(((T) component));
            }
        }

        return returnComponents;
    }

    public final GameScene getCurrentScene()
    {
        return Main.getSceneManager().getGameScene();
    }

    public final GameObject getGameObject(String _name)
    {
        return getGameObject(_name, false);
    }

    public final GameObject getGameObject(String _name, boolean ignoreSubscene)
    {
        List<GameObject> objects = currentSubScene == null || ignoreSubscene ? getCurrentScene().objects : currentSubScene.getObjects();

        for (GameObject obj : objects)
        {
            if (obj.name.equals(_name))
            {
                return obj;
            }
        }

        return null;
    }

    public final List<GameObject> getGameObjects(String _name)
    {
        return getGameObjects(_name, false);
    }

    public final List<GameObject> getGameObjects(String _name, boolean ignoreSubscene)
    {
        List<GameObject> objects = currentSubScene == null || ignoreSubscene ? getCurrentScene().objects : currentSubScene.getObjects();

        List<GameObject> gameObjects = new ArrayList<>();

        for (GameObject obj : objects)
        {
            if (obj.name.equals(_name))
            {
                gameObjects.add(obj);
            }
        }

        return gameObjects;
    }

    public final void addToScene(GameObject gameObject)
    {
        addToScene(gameObject, false);
    }

    public final void addToScene(GameObject gameObject, boolean ignoreSubscene)
    {
        if (currentSubScene == null || ignoreSubscene)
        {
            Main.getSceneManager().getGameScene().addObjectNextFrame(gameObject);
            return;
        }
        currentSubScene.addObjectNextFrame(gameObject);
    }

}

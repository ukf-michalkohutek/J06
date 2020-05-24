package sample.Engine.Core;
import java.util.LinkedList;
import java.util.List;

public abstract class GameScene {

    public Object args;

    public final List<GameObject> objects = new LinkedList<>();
    public final List<GameObject> newObjects = new LinkedList<>();

    public void start() {}

    public void update(double deltaTime) {}

    public final void addObjectNextFrame(GameObject gameObject)
    {
        newObjects.add(gameObject);
    }

}

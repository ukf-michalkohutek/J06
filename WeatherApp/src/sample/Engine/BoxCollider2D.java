package sample.Engine;

import sample.Engine.Core.*;

import java.util.List;

public class BoxCollider2D extends GameComponent {

    private double size;
    private boolean triggeredAutomatically;

    public BoxCollider2D(GameObject2D _parent, double _size, boolean _triggeredAutomatically)
    {
        parent = _parent;
        size = _size / 2;
        triggeredAutomatically = _triggeredAutomatically;
    }

    @Override
    public void update(double deltaTime) {
        if (triggeredAutomatically)
        {
            collides(true);
        }
    }

    public GameObject2D collides(boolean trigger)
    {
        return collidesAtPosition(((GameObject2D)parent).getPosition(), size, trigger);
    }

    public GameObject2D collides(double colliderSize, boolean trigger)
    {
        return collidesAtPosition(((GameObject2D)parent).getPosition(), colliderSize, trigger);
    }

    public GameObject2D collidesAtPosition(Vector2D<Double> thisPosition, boolean trigger)
    {
        return collidesAtPosition(thisPosition, size, trigger);
    }

    public GameObject2D collidesAtPosition(Vector2D<Double> thisPosition, double colliderSize, boolean trigger)
    {
        GameObject2D thisObj = ((GameObject2D)parent);
        double thisSideX1 = thisPosition.x - colliderSize;
        double thisSideX2 = thisPosition.x + colliderSize;
        double thisSideY1 = thisPosition.y - colliderSize;
        double thisSideY2 = thisPosition.y + colliderSize;

        GameObject2D otherObj;
        BoxCollider2D otherCollider;
        GameObject2D collided = null;

        List<GameObject> objects = parent.currentSubScene == null ? parent.getCurrentScene().objects : parent.currentSubScene.getObjects();
        for (GameObject obj : objects)
        {
            if (!(obj instanceof GameObject2D))
            {
                continue;
            }

            otherCollider = obj.getComponent(BoxCollider2D.class);
            if (otherCollider == null)
            {
                continue;
            }

            otherObj = ((GameObject2D)obj);
            if (otherObj.distanceTo(thisPosition) > size * 3)
            {
                continue;
            }

            if (thisObj == otherObj)
            {
                continue;
            }

            Vector2D<Double> otherPosition = otherObj.getPosition();
            double otherSideX1 = otherPosition.x - otherCollider.size;
            double otherSideX2 = otherPosition.x + otherCollider.size;
            double otherSideY1 = otherPosition.y - otherCollider.size;
            double otherSideY2 = otherPosition.y + otherCollider.size;

            if (thisSideX1 > otherSideX2 || thisSideX2 < otherSideX1)
            {
                continue;
            }

            if (thisSideY1 > otherSideY2 || thisSideY2 < otherSideY1)
            {
                continue;
            }

            collided = otherObj;
            if (trigger) thisObj.collision(otherObj);
        }

        return collided;
    }

    public double getSize() {
        return size;
    }

    public boolean isTriggeredAutomatically()
    {
        return triggeredAutomatically;
    }

}
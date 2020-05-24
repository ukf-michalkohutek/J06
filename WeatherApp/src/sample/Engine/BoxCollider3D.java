package sample.Engine;

import sample.Engine.Core.*;

import java.util.List;

public class BoxCollider3D extends GameComponent {

    private double size;
    private boolean triggeredAutomatically;

    public BoxCollider3D(GameObject3D _parent, double _size, boolean _triggeredAutomatically)
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

    public GameObject3D collides(boolean trigger)
    {
        return collidesAtPosition(((GameObject3D)parent).getPosition(), size, trigger);
    }

    public GameObject3D collides(double colliderSize, boolean trigger)
    {
        return collidesAtPosition(((GameObject3D)parent).getPosition(), colliderSize, trigger);
    }

    public GameObject3D collidesAtPosition(Vector3D<Double> thisPosition, boolean trigger)
    {
        return collidesAtPosition(thisPosition, size, trigger);
    }

    public GameObject3D collidesAtPosition(Vector3D<Double> thisPosition, double colliderSize, boolean trigger)
    {
        GameObject3D thisObj = ((GameObject3D)parent);
        double thisSideX1 = thisPosition.x - colliderSize;
        double thisSideX2 = thisPosition.x + colliderSize;
        double thisSideY1 = thisPosition.y - colliderSize;
        double thisSideY2 = thisPosition.y + colliderSize;
        double thisSideZ1 = thisPosition.z - colliderSize;
        double thisSideZ2 = thisPosition.z + colliderSize;

        GameObject3D otherObj;
        BoxCollider3D otherCollider;
        GameObject3D collided = null;

        List<GameObject> objects = parent.currentSubScene == null ? parent.getCurrentScene().objects : parent.currentSubScene.getObjects();
        for (GameObject obj : objects)
        {
            if (!(obj instanceof GameObject3D))
            {
                continue;
            }

            otherCollider = obj.getComponent(BoxCollider3D.class);
            if (otherCollider == null)
            {
                continue;
            }

            otherObj = ((GameObject3D)obj);
            if (otherObj.distanceTo(thisPosition) > size * 3)
            {
                continue;
            }

            if (thisObj == otherObj)
            {
                continue;
            }

            Vector3D<Double> otherPosition = otherObj.getPosition();
            double otherSideX1 = otherPosition.x - otherCollider.size;
            double otherSideX2 = otherPosition.x + otherCollider.size;
            double otherSideY1 = otherPosition.y - otherCollider.size;
            double otherSideY2 = otherPosition.y + otherCollider.size;
            double otherSideZ1 = otherPosition.z - otherCollider.size;
            double otherSideZ2 = otherPosition.z + otherCollider.size;

            if (thisSideX1 > otherSideX2 || thisSideX2 < otherSideX1)
            {
                continue;
            }

            if (thisSideY1 > otherSideY2 || thisSideY2 < otherSideY1)
            {
                continue;
            }

            if (thisSideZ1 > otherSideZ2 || thisSideZ2 < otherSideZ1)
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
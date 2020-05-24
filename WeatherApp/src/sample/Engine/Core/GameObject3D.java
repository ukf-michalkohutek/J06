package sample.Engine.Core;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public abstract class GameObject3D extends GameObject {

    public GameObject3D()
    {
        super();
    }

    public GameObject3D(String objFilePath)
    {
        super();

        ObjReader objReader = new ObjReader(objFilePath);

        setMesh(objReader.createMesh());
    }

    public GameObject3D(TriangleMesh mesh)
    {
        super();

        setMesh(mesh);
    }

    public MeshView getMesh()
    {
        return ((MeshView)node);
    }

    public final void setMesh(TriangleMesh mesh)
    {
        if (node == null)
        {
            node = new MeshView(mesh);
        } else {
            getMesh().setMesh(mesh);
        }

        getMesh().setCullFace(CullFace.NONE);

        setMeshColor(Color.WHITE);
    }

    public final void setMeshColor(Color color)
    {
        PhongMaterial material = new PhongMaterial(color);
        getMesh().setMaterial(material);
    }

    public void collision(GameObject3D other) {}

    public final void setX(double value)
    {
        node.setTranslateX(value);
    }

    public final void setY(double value)
    {
        node.setTranslateY(value);
    }

    public final void setZ(double value)
    {
        node.setTranslateZ(value);
    }

    public final void setPosition(double posX, double posY, double posZ)
    {
        setX(posX);
        setY(posY);
        setZ(posZ);
    }

    public final void setPosition(Vector3D<Double> value)
    {
        setPosition(value.x, value.y, value.z);
    }

    public final double getX()
    {
        return node.getTranslateX();
    }

    public final double getY()
    {
        return node.getTranslateY();
    }

    public final double getZ()
    {
        return node.getTranslateZ();
    }

    public final Vector3D<Double> getPosition()
    {
        return new Vector3D<>(getX(), getY(), getZ());
    }

    public final void moveX(double deltaX)
    {
        setX(getX() + deltaX);
    }

    public final void moveY(double deltaY)
    {
        setY(getY() + deltaY);
    }

    public final void moveZ(double deltaZ)
    {
        setZ(getZ() + deltaZ);
    }

    public final void move(double deltaX, double deltaY, double deltaZ)
    {
        moveX(deltaX);
        moveY(deltaY);
        moveZ(deltaZ);
    }

    public final void move(Vector3D<Double> delta)
    {
        move(delta.x, delta.y, delta.z);
    }

    public final void moveInVector(double deltaX, double deltaY, double deltaZ, double amount)
    {
        move(deltaX * amount, deltaY * amount, deltaZ * amount);
    }

    public final void moveInVector(Vector3D<Double> delta, double amount)
    {
        move(delta.x * amount, delta.y * amount, delta.z * amount);
    }

    public final void setRotationX(double value)
    {
        setRotation(value, Rotate.X_AXIS);
    }

    public final void setRotationY(double value)
    {
        setRotation(value, Rotate.Y_AXIS);
    }

    public final void setRotationZ(double value)
    {
        setRotation(value, Rotate.Z_AXIS);
    }

    public final void setRotation(double value, Point3D axis)
    {
        Rotate rotate = new Rotate(value, axis);
        node.getTransforms().clear();
        node.getTransforms().add(rotate);
    }

    public final void setRotation(double valueX, double valueY, double valueZ)
    {
        setRotationX(valueX);
        setRotationY(valueY);
        setRotationZ(valueZ);
    }

    public final void setRotation(Vector3D<Double> value)
    {
        setRotation(value.x, value.y, value.z);
    }

    public final void resetRotation()
    {
        setRotation(0, 0, 0);
    }

    public final void rotateX(double value)
    {
        rotate(value, Rotate.X_AXIS);
    }

    public final void rotateY(double value)
    {
        rotate(value, Rotate.Y_AXIS);
    }

    public final void rotateZ(double value)
    {
        rotate(value, Rotate.Z_AXIS);
    }

    public final void rotate(double value, Point3D axis)
    {
        if (node.getTransforms().size() == 0)
        {
            setRotation(0, 0, 0);
        }

        Rotate rotate = new Rotate(value, axis);
        Transform transform = node.getTransforms().get(0);
        transform = transform.createConcatenation(rotate);
        node.getTransforms().clear();
        node.getTransforms().add(transform);
    }

    public final void rotate(double valueX, double valueY, double valueZ)
    {
        rotateX(valueX);
        rotateY(valueY);
        rotateZ(valueZ);
    }

    public final void rotate(Vector3D<Double> value)
    {
        rotate(value.x, value.y, value.z);
    }

    public final void setScaleX(double value)
    {
        node.setScaleX(value);
    }

    public final void setScaleY(double value)
    {
        node.setScaleY(value);
    }

    public final void setScaleZ(double value)
    {
        node.setScaleZ(value);
    }

    public final void setScale(double scaleX, double scaleY, double scaleZ)
    {
        setScaleX(scaleX);
        setScaleY(scaleY);
        setScaleZ(scaleZ);
    }

    public final void setScale(Vector3D<Double> value)
    {
        setScale(value.x, value.y, value.z);
    }

    public final double getScaleX()
    {
        return node.getScaleX();
    }

    public final double getScaleY()
    {
        return node.getScaleY();
    }

    public final double getScaleZ()
    {
        return node.getScaleZ();
    }

    public final Vector3D<Double> getScale()
    {
        return new Vector3D<>(getScaleX(), getScaleY(), getScaleZ());
    }

    public final double distanceTo(double posX, double posY, double posZ)
    {
        double diffX = Math.pow(Math.abs(getX() - posX), 2);
        double diffY = Math.pow(Math.abs(getY() - posY), 2);
        double diffZ = Math.pow(Math.abs(getZ() - posZ), 2);

        return Math.sqrt(diffX + diffY + diffZ);
    }

    public final double distanceTo(Vector3D<Double> position)
    {
        return distanceTo(position.x, position.y, position.z);
    }

    public final double distanceTo(GameObject3D other)
    {
        return distanceTo(other.getPosition());
    }

    public final void setOnMouseClicked(EventHandler<? super MouseEvent> e)
    {
        node.setOnMouseClicked(e);
    }

}
package sample.Engine.Core;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public abstract class GameObject2D extends GameObject {

    protected final GraphicsContext context;

    public GameObject2D(double sizeX, double sizeY)
    {
        super();

        node = new Canvas(sizeX, sizeY);
        context = ((Canvas)node).getGraphicsContext2D();
    }

    public Canvas getCanvas()
    {
        return ((Canvas)node);
    }

    public void collision(GameObject2D other) {}

    public final void setWidth(double value)
    {
        getCanvas().setWidth(value);
    }

    public final void setHeight(double value)
    {
        getCanvas().setHeight(value);
    }

    public final void setSize(double sizeX, double sizeY)
    {
        setWidth(sizeX);
        setHeight(sizeY);
    }

    public final void setSize(Vector2D<Double> value)
    {
        setSize(value.x, value.y);
    }

    public final double getWidth()
    {
        return getCanvas().getWidth();
    }

    public final double getHeight()
    {
        return getCanvas().getHeight();
    }

    public final Vector2D<Double> getSize()
    {
        return new Vector2D<>(getWidth(), getHeight());
    }

    public final void setX(double value)
    {
        node.setLayoutX(value);
    }

    public final void setY(double value)
    {
        node.setLayoutY(value);
    }

    public final void setPosition(double posX, double posY)
    {
        setX(posX);
        setY(posY);
    }

    public final void setPosition(Vector2D<Double> value)
    {
        setPosition(value.x, value.y);
    }

    public final double getX()
    {
        return node.getLayoutX();
    }

    public final double getY()
    {
        return node.getLayoutY();
    }

    public final Vector2D<Double> getPosition()
    {
        return new Vector2D<>(getX(), getY());
    }

    public final void moveX(double deltaX)
    {
        setX(getX() + deltaX);
    }

    public final void moveY(double deltaY)
    {
        setY(getY() + deltaY);
    }

    public final void move(double deltaX, double deltaY)
    {
        moveX(deltaX);
        moveY(deltaY);
    }

    public final void move(Vector2D<Double> delta)
    {
        move(delta.x, delta.y);
    }

    public final void moveInVector(double deltaX, double deltaY, double amount)
    {
        move(deltaX * amount, deltaY * amount);
    }

    public final void moveInVector(Vector2D<Double> delta, double amount)
    {
        move(delta.x * amount, delta.y * amount);
    }

    public final void moveInDirection(double directionDeg, double amount)
    {
        directionDeg = directionDeg % 360;
        directionDeg = directionDeg < 0 ? 360 + directionDeg : directionDeg;
        directionDeg = Math.toRadians(directionDeg);

        setX(getX() + -Math.sin(directionDeg) * amount);
        setY(getY() + Math.cos(directionDeg) * amount);
    }

    public final void setRotation(double value)
    {
        getCanvas().setRotate(value);
    }

    public final void resetRotation()
    {
        setRotation(0);
    }

    public final double getRotation()
    {
        return getCanvas().getRotate();
    }

    public final void rotate(double value)
    {
        setRotation(getRotation() + value);
    }

    public final Vector2D<Double> getFacingDirection()
    {
        double rotationDeg = getRotation() % 360;
        rotationDeg = rotationDeg < 0 ? 360 + rotationDeg : rotationDeg;
        rotationDeg = Math.toRadians(rotationDeg);

        return new Vector2D<>(-Math.sin(rotationDeg), Math.cos(rotationDeg));
    }

    public final double distanceTo(double posX, double posY)
    {
        double diffX = Math.pow(Math.abs(getX() - posX), 2);
        double diffY = Math.pow(Math.abs(getY() - posY), 2);

        return Math.sqrt(diffX + diffY);
    }

    public final double distanceTo(Vector2D<Double> position)
    {
        return distanceTo(position.x, position.y);
    }

    public final double distanceTo(GameObject2D other)
    {
        return distanceTo(other.getPosition());
    }

    public final void setOnMouseClicked(EventHandler<? super MouseEvent> e)
    {
        getCanvas().setOnMouseClicked(e);
    }

}

package sample;

import javafx.scene.paint.Color;
import sample.Engine.Core.Main;
import sample.Engine.Core.Vector2D;

public class EffectSnow extends WeatherEffect {

    private Color color;
    private double size;
    private double speedX;
    private double speedY;
    private double rotationSpeed;

    public EffectSnow(Color _color, double _size)
    {
        super(_size, _size, new Vector2D<>(Math.random() * Main.getCurrentResolution().x, -50 - Math.random() * 150), new Vector2D<>(0d, 0d));

        color = _color;
        size = _size;
        speedX = -15 + Math.random() * 30;
        speedY = 50 + Math.random() * 100;
        rotationSpeed = 10 + Math.random() * 30;
    }

    @Override
    public void start()
    {
        super.start();

        context.setStroke(color);
        context.setLineWidth(0.08*size);

        context.strokeLine(0.5*size, 0.0*size, 0.5*size, 1.0*size);
        context.strokeLine(0.08*size, 0.25*size, 0.92*size, 0.75*size);
        context.strokeLine(0.08*size, 0.75*size, 0.92*size, 0.25*size);

        context.strokeLine(0.25*size, 0.17*size, 0.26*size, 0.36*size);
        context.strokeLine(0.07*size, 0.46*size, 0.26*size, 0.36*size);

        context.strokeLine(0.25*size, 0.83*size, 0.26*size, 0.64*size);
        context.strokeLine(0.07*size, 0.54*size, 0.26*size, 0.64*size);

        context.strokeLine(0.75*size, 0.17*size, 0.74*size, 0.36*size);
        context.strokeLine(0.93*size, 0.46*size, 0.74*size, 0.36*size);

        context.strokeLine(0.75*size, 0.83*size, 0.74*size, 0.64*size);
        context.strokeLine(0.93*size, 0.54*size, 0.74*size, 0.64*size);

        context.strokeLine(0.34*size, 0.11*size, 0.5*size, 0.22*size);
        context.strokeLine(0.66*size, 0.11*size, 0.5*size, 0.22*size);

        context.strokeLine(0.34*size, 0.89*size, 0.5*size, 0.78*size);
        context.strokeLine(0.66*size, 0.89*size, 0.5*size, 0.78*size);
    }

    @Override
    public void update(double deltaTime) {
        move(speedX * deltaTime, speedY * deltaTime);

        if (getY() > 800)
        {
            setPosition(pos);
        }

        rotate(rotationSpeed * deltaTime);
    }

}
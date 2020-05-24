package sample;

import javafx.scene.paint.Color;
import sample.Engine.Core.Vector2D;

public class EffectSun extends WeatherEffect {

    private Color color;
    private double size;
    private double rotationSpeed;

    public EffectSun(Color _color, double _size, double _rotationSpeed, Vector2D<Double> _position)
    {
        super(_size, _size, _position, new Vector2D<>(0d, 800d));

        color = _color;
        size = _size;
        rotationSpeed = _rotationSpeed;
    }

    @Override
    public void start()
    {
        super.start();

        context.setFill(color);
        context.fillPolygon(
                new double[] { 0.00*size, 0.25*size, 0.75*size, 1.00*size, 0.75*size, 0.25*size },
                new double[] { 0.50*size, 0.07*size, 0.07*size, 0.50*size, 0.93*size, 0.93*size },
                6
        );
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        rotate(rotationSpeed * deltaTime);
    }

}
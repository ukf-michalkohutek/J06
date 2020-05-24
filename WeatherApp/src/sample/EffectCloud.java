package sample;

import javafx.scene.paint.Color;
import sample.Engine.Core.Vector2D;

public class EffectCloud extends WeatherEffect {

    private Color color;

    public EffectCloud(Color _color, Vector2D<Double> _position)
    {
        super(140, 80, _position, new Vector2D<>(0d, -600d));

        color = _color;
    }

    @Override
    public void start()
    {
        super.start();

        context.setFill(color);

        context.fillOval(45, 10, 60, 60);
        context.fillRoundRect(17, 25, 60, 50, 50, 50);
        context.fillRoundRect(60, 40, 65, 40, 40, 40);
        context.fillRoundRect(0, 60, 140, 20, 20, 20);
    }

}
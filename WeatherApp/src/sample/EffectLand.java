package sample;

import javafx.scene.paint.Color;
import sample.Engine.Core.Main;
import sample.Engine.Core.Vector2D;

public class EffectLand extends WeatherEffect {

    private Color color;
    private double height;

    public EffectLand(Color _color, double _height)
    {
        super(Main.getCurrentResolution().x, Main.getCurrentResolution().y, new Vector2D<>(0d, 0d), new Vector2D<>(0d, 800d));

        color = _color;
        height = _height;
    }

    @Override
    public void start() {
        super.start();

        context.setFill(color);
        context.fillOval(-750, height, 2000, 2000);
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
    }

}
package sample;

import javafx.scene.paint.Color;
import sample.Engine.Core.Main;
import sample.Engine.Core.Vector2D;

public class EffectSky extends WeatherEffect {

    private Color color;

    public EffectSky(Color _color)
    {
        super(Main.getCurrentResolution().x, Main.getCurrentResolution().y, new Vector2D<>(0d, 0d), new Vector2D<>(0d, 0d));

        color = _color;
    }

    @Override
    public void start() {
        super.start();

        context.setFill(color);
        context.fillRect(0, 0, getWidth(), getHeight());
    }

}

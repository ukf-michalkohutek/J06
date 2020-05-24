package sample;

import javafx.scene.paint.Color;
import sample.Engine.Core.Main;
import sample.Engine.Core.Vector2D;

public class EffectRain extends WeatherEffect {

    private Color color;
    private double speedX;
    private double speedY;

    public EffectRain(Color _color)
    {
        super(60, 60, new Vector2D<>(Math.random() * Main.getCurrentResolution().x, -30 - Math.random() * 500), new Vector2D<>(0d, 0d));

        color = _color;
        speedX = -100;
        speedY = 450;
    }

    @Override
    public void start()
    {
        super.start();

        context.setFill(color);
        context.fillRoundRect(0, 0, 4, 25, 4, 4);

        rotate(200);
    }

    @Override
    public void update(double deltaTime) {
        move(speedX * deltaTime, speedY * deltaTime);

        if (getY() > 800)
        {
            setPosition(pos);
        }
    }

}
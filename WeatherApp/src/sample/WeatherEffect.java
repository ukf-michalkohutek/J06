package sample;

import sample.Engine.Core.GameObject2D;
import sample.Engine.Core.Vector2D;
import sample.Engine.GameAnimation;

public class WeatherEffect extends GameObject2D {

    private double animation;
    private double animationScale;
    protected Vector2D<Double> pos;
    protected Vector2D<Double> deltaStart;

    public WeatherEffect(double sizeX, double sizeY, Vector2D<Double> _pos, Vector2D<Double> _deltaStart)
    {
        super(sizeX, sizeY);

        animation = 1;
        animationScale = 0.5;
        pos = _pos;
        deltaStart = _deltaStart;
    }

    @Override
    public void start() {
        setPosition(pos.x + deltaStart.x, pos.y + deltaStart.y);
    }

    @Override
    public void update(double deltaTime) {
        if (animation <= 0)
        {
            setPosition(pos);
            return;
        }

        animation -= animationScale * deltaTime;
        double easeAnimation = GameAnimation.easeIn(animation);
        setPosition(pos.x + deltaStart.x * easeAnimation, pos.y + deltaStart.y * easeAnimation);

        if (animation <= 0) setPosition(pos);
    }

}

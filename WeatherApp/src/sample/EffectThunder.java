package sample;

import javafx.scene.paint.Color;
import sample.Engine.Core.Vector2D;

public class EffectThunder extends WeatherEffect {

    private Color color;
    private double size;

    public EffectThunder(Color _color, double _size, Vector2D<Double> _position)
    {
        super(_size, 2.3*_size, _position, new Vector2D<>((Math.random() > 0.5) ? -300d : 800d, 0d));

        color = _color;
        size = _size;
    }

    @Override
    public void start()
    {
        super.start();

        context.setFill(color);

        context.fillPolygon(
                new double[] { 0.12*size, 0.82*size, 0.52*size, 1.00*size, 0.70*size, 0.88*size, 0.06*size, 0.46*size, 0.02*size, 0.34*size },
                new double[] { 0.00*size, 0.50*size, 0.78*size, 1.32*size, 1.62*size, 2.30*size, 1.48*size, 1.30*size, 0.75*size, 0.54*size },
                10
        );
    }

}
package sample;

import javafx.scene.paint.Paint;
import sample.Engine.Core.GameObject2D;

public class ColorRectangle extends GameObject2D {

    private Paint primaryColor;
    private Paint secondaryColor;

    public ColorRectangle(double _width, double _height, Paint _primaryColor, Paint _secondaryColor)
    {
        super(_width, _height);

        primaryColor = _primaryColor;
        secondaryColor = _secondaryColor;
    }

    @Override
    public void start() {
        context.setFill(primaryColor);
        context.fillRect(0, 0, getWidth(), getHeight());

        context.setFill(secondaryColor);
        context.fillRect(0, getHeight() - 5, getWidth(), 5);
    }

}

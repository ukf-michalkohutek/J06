package sample.Engine;

import javafx.scene.paint.Color;
import sample.Engine.Core.GameObject2D;
import sample.Engine.Core.Main;

public class SolidBackground extends GameObject2D {

    private Color color;

    public SolidBackground(Color _color)
    {
        super(Main.getCurrentResolution().x, Main.getCurrentResolution().y);

        color = _color;

        getCanvas().setMouseTransparent(true);
    }

    @Override
    public void start()
    {
        context.setFill(color);
        context.fillRect(0, 0, getWidth(), getHeight());
    }

}
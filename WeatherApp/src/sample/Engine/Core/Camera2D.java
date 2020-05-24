package sample.Engine.Core;

import javafx.scene.Camera;
import javafx.scene.ParallelCamera;
import javafx.scene.canvas.Canvas;

public class Camera2D extends GameObject2D {

    public Camera2D()
    {
        super(0, 0);

        node = new ParallelCamera();
    }

    public final Camera getCamera()
    {
        return ((Camera)node);
    }

    @Override
    public final Canvas getCanvas()
    {
        return null;
    }

}

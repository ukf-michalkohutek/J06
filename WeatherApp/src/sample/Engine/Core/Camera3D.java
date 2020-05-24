package sample.Engine.Core;

import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.MeshView;

public class Camera3D extends GameObject3D {

    public Camera3D()
    {
        node = new PerspectiveCamera(true);
        getCamera().setNearClip(0.01);
        getCamera().setFarClip(10000);
    }

    public Camera3D(double nearClip, double farClip)
    {
        node = new PerspectiveCamera(true);
        getCamera().setNearClip(nearClip);
        getCamera().setFarClip(farClip);
    }

    public final Camera getCamera()
    {
        return ((Camera)node);
    }

    @Override
    public final MeshView getMesh()
    {
        return null;
    }

}

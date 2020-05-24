package sample.Engine.Core;

public class GameSettings {

    private final double targetFramerate;
    private final Vector2D<Integer> defaultResolution;
    private final boolean resizableWindow;

    public GameSettings(double _targetFramerate, Vector2D<Integer> _defaultResolution, boolean _resizableWindow)
    {
        targetFramerate = _targetFramerate;
        defaultResolution = _defaultResolution;
        resizableWindow = _resizableWindow;
    }

    public double getTargetFramerate() {
        return targetFramerate;
    }

    public Vector2D<Integer> getDefaultResolution()
    {
        return defaultResolution;
    }

    public boolean isResizableWindow() {
        return resizableWindow;
    }

}

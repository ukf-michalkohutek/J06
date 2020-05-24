package sample.Engine.Core;

public class Vector2D<T> {

    public T x;
    public T y;

    public Vector2D()
    {
        x = null;
        y = null;
    }

    public Vector2D(Vector2D<T> copy)
    {
        x = copy.x;
        y = copy.y;
    }

    public Vector2D(T _x, T _y)
    {
        x = _x;
        y = _y;
    }

}

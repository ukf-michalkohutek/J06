package sample.Engine.Core;

public class Vector3D<T> {

    public T x;
    public T y;
    public T z;

    public Vector3D()
    {
        x = null;
        y = null;
        z = null;
    }

    public Vector3D(Vector3D<T> copy)
    {
        x = copy.x;
        y = copy.y;
        z = copy.z;
    }

    public Vector3D(T _x, T _y, T _z)
    {
        x = _x;
        y = _y;
        z = _z;
    }

}

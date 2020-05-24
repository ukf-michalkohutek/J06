package sample.Engine;

public abstract class GameAnimation {

    public static double easeIn(double x)
    {
        return Math.pow(x, 3);
    }

    public static double easeOut(double x)
    {
        return 1 - Math.pow(1 - x, 3);
    }

    public static double easeInOut(double x, double alpha)
    {
        double xAlpha = Math.pow(x, alpha);

        return xAlpha / (xAlpha + Math.pow(1 - x, alpha));
    }

}

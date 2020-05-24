package sample.Engine;

import sample.Engine.Core.GameObject;

import java.util.ArrayList;
import java.util.List;

public class FramerateCounter extends GameObject {

    public interface FramerateUpdateEventListener
    {
        void onFramerateUpdate(double framerate);
    }

    private FramerateUpdateEventListener event;
    private List<Double> framerates;
    private int updateFrequency;
    private int updateCounter;
    private int smoothing;

    public FramerateCounter(int _updateFrequency, int _smoothing)
    {
        super();

        updateFrequency = _updateFrequency;
        updateCounter = 0;
        smoothing = _smoothing;

        framerates = new ArrayList<>(smoothing);
        for (int i = 0; i < smoothing; i++)
        {
            framerates.add(0d);
        }
    }

    public void setOnUpdate(FramerateUpdateEventListener _event)
    {
        event = _event;
    }

    @Override
    public void update(double deltaTime) {
        framerates.remove(0);
        framerates.add(1 / deltaTime);

        if (++updateCounter < updateFrequency)
        {
            return;
        }

        updateCounter = 0;

        double averageFramerate = 0;
        for (double framerate : framerates)
        {
            averageFramerate += framerate;
        }
        averageFramerate /= smoothing;

        event.onFramerateUpdate(averageFramerate);
    }

}

package core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vadym
 * @since 12.05.15 23:12
 */
public class Benchmark {
    private double time;
    private Map<String, Double> log = new HashMap<>();

    public void start() {
        time = System.nanoTime();
    }

    public double stop() {
        time = (System.nanoTime() - time) / 1000.0;
        return time;
    }

    public void stop(String tag) {
        log.put(tag, stop());
    }

    public double getTime() {
        return time;
    }

    public double getTime(String tag) {
        return log.get(tag);
    }
}

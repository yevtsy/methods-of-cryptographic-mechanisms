package core;

/**
 * @author vadym
 * @since 12.05.15 23:12
 */
public class Benchmark {
    private long time;

    public void start() {
        time = System.nanoTime();
    }

    public double stop() {
        time = System.nanoTime() - time;
        return time / 1000.0;
    }

    public double getTime() {
        return time / 1000.0;
    }
}

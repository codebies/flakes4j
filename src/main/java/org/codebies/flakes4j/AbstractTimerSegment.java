package org.codebies.flakes4j;

public abstract class AbstractTimerSegment implements TimerSegment {
    protected final Clock clock;
    private final Listenable listenable = new DefaultListenable();
    private long elapsed = 0;

    public AbstractTimerSegment(Clock clock) {
        this.clock = clock;
    }

    @Override
    public long next() {
        elapsed = onChange(extractTime(clock.millis()));
        return elapsed;
    }

    private long onChange(long current) {
        if (this.elapsed != current)
            notifyListeners();
        return current;
    }

    public void register(EventListener listener) {
        listenable.register(listener);
    }
    public void unregister(EventListener listener) {
        listenable.unregister(listener);
    }
    public void notifyListeners() {
        listenable.notifyListeners();
    }

    public abstract long extractTime(long mills);

}

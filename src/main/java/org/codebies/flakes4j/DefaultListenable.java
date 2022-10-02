package org.codebies.flakes4j;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class DefaultListenable implements Listenable {

    private final Set<EventListener> listeners = new HashSet<>();

    @Override
    public void register(EventListener listener) {
        if(Objects.nonNull(listener))
            listeners.add(listener);
    }

    @Override
    public void unregister(EventListener listener) {
        if(Objects.nonNull(listener))
            listeners.remove(listener);
    }

    @Override
    public void notifyListeners() {
        listeners.forEach(EventListener::onEvent);
    }
}

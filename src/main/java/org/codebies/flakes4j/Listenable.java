package org.codebies.flakes4j;

public interface Listenable {

    void register(EventListener listener);
    void unregister(EventListener listener);
    void notifyListeners();

}

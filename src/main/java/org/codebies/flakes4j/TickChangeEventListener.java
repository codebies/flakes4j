package org.codebies.flakes4j;

public interface TickChangeEventListener extends EventListener{

    @Override
    default void onEvent() {
        onTickChange();
    }
    void onTickChange();

}

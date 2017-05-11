package com.ktds.queuing_app.Util;

/**
 * Created by 206-013 on 2016-07-28.
 */
public class HandlerStack {

    private int index = -1;

    public HandlerStack setHandler(Task task, int delay) {
        synchronized(this) {
            nextIndex();
            task.run();
            //SystemClock.sleep(delay);
            return this;
        }
    }

    private void nextIndex() {
        index++;
    }


}

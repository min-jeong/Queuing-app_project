package com.ktds.queuing_app.Util;

/**
 * Created by 206-013 on 2016-07-28.
 */
public class Index implements Cloneable {

    private int index = 1;

    public void setIndex( int i ) {
        index = i;
    }

    public int getIndex() {
        return index;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Index getClone() {
        try {
            return (Index) this.clone();
        } catch (CloneNotSupportedException e) {
            return new Index();
        }
    }

}


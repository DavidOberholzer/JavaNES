package com.DavidOberholzer;

public class PC {
    private int current = 0x00;
    private int start;
    private int end;

    public void reset(int value) {
        current = value;
    }

    public void increment() {
        current += 0x01;
        if (current > end) {
            current = start;
        }
    }

    public void jumpBy(int value) {
        current += value;
        if (current > end) {
            current -= (end + 1);
            current += start;
        }
    }

    public void jumpTo(int value) {
        current = value;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}

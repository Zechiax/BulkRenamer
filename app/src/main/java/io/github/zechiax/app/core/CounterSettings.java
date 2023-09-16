package io.github.zechiax.app.core;

public class CounterSettings {
    private int start;
    private int step;
    private int padding;
    public CounterSettings(int start, int step, int padding) {
        this.start = start;
        this.step = step;
        this.padding = padding;
    }

    public int getStart() {
        return this.start;
    }

    public int getStep() {
        return this.step;
    }

    public int getPadding() {
        return this.padding;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public static CounterSettings getDefault() {
        return new CounterSettings(1, 1, 1);
    }
}
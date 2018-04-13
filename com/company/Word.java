package com.company;
import State.state;


public class Word {
    private String property;
    private int line;
    private String state;
    public Word(){
        property = "";
        line = 0;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

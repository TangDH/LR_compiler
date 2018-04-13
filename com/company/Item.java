package com.company;

import java.util.ArrayList;

public class Item {
    private ArrayList<String> production;
    private String signs;
    private int DotPosition;
    private String header;

    @Override
    public String toString() {
        String re="[";
        for(int i=0;i<production.size();i++){
            re +=production.get(i);
        }
        re+=String.valueOf(DotPosition);
        re+=signs;
        re+="]";
        return re;
    }
    public Item(){
        production = new ArrayList<>();
        signs = "";
        DotPosition = 0;
        header = "";
    }


    public ArrayList<String> getProduction() {
        return production;
    }

    public void setProduction(ArrayList<String> production) {
        this.production = production;
    }

    public String getSigns() {
        return signs;
    }

    public void setSigns(String signs) {
        this.signs = signs;
    }

    public int getDotPosition() {
        return DotPosition;
    }

    public void setDotPosition(int dotPosition) {
        DotPosition = dotPosition;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

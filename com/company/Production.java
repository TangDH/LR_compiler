package com.company;

import jdk.nashorn.internal.ir.Terminal;

import java.util.ArrayList;
import java.util.HashMap;

public class Production {

    public static ArrayList<String> PRODUCTION_RAW
            = new ArrayList<>();
    public static ArrayList<String> INTERMINAL_SET
            = new ArrayList<>();
    public static ArrayList<String> TERMINAL_SET
            = new ArrayList<>();
    public static HashMap<String,ArrayList<ArrayList<String>>> PRODUCTION_MAP
            = new HashMap<>();
    public static HashMap<String,ArrayList<String>> FirstSet=new HashMap<>();
    public static void addProduction(String production) {
        PRODUCTION_RAW.add(production);
    }

    public static void StoreInterminal() {
        for(String production:PRODUCTION_RAW){
            String interminal = production.split("->")[0];
            if(!INTERMINAL_SET.contains(interminal)&&!interminal.equals("S'"))
                INTERMINAL_SET.add(interminal);
        }
    }

    public static void Storeterminal(){
        for(String production:PRODUCTION_RAW){
            String production_right = production.split("->")[1];
            for(String terminal:production_right.split(" ")){
                if(!TERMINAL_SET.contains(terminal)&&!INTERMINAL_SET.contains(terminal)
                        &&terminal!="$"){
                    TERMINAL_SET.add(terminal);
                }
            }
        }
    }
    public static void StoreProduction(){
        for(String production:PRODUCTION_RAW){
            String production_right = production.split("->")[1];
            String production_left  = production.split("->")[0];
            ArrayList<String> right = new ArrayList<>();
            for(String item:production_right.split(" ")){
                right.add(item);
            }
            if(PRODUCTION_MAP.containsKey(production_left)){
                ArrayList<ArrayList<String>> temp = PRODUCTION_MAP.get(production_left);
                temp.add(right);
                PRODUCTION_MAP.put(production_left,temp);
            }else{
                ArrayList<ArrayList<String>> temp = new ArrayList<>();
                temp.add(right);
                PRODUCTION_MAP.put(production_left,temp);
            }
        }
    }

    public static void Show(){
        System.out.println(PRODUCTION_MAP);
        System.out.println(INTERMINAL_SET);
        System.out.println(TERMINAL_SET);
    }






}

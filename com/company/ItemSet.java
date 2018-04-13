package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemSet {
    private HashMap<Integer,ArrayList<Item>> Items;
    public ItemSet(){
        Items = new HashMap<>();
    }
    public void Add(Item item,int index){
        if(Items.containsKey(index)){
            ArrayList<Item> temp = Items.get(index);
            temp.add(item);
            Items.put(index,temp);
        }else{
            ArrayList<Item> temp = new ArrayList<>();
            temp.add(item);
            Items.put(index,temp);
        }
    }
    public void Add(int index,ArrayList<Item> items){
        Items.put(index,items);
    }

    public boolean hasKeys(int index){
        return Items.containsKey(index);
    }
    public ArrayList<Item> getItems(int index){
        return Items.get(index);
    }
    public void Put(int index,ArrayList<Item> items){
        Items.put(index,items);
    }
    public boolean Contains(ArrayList<Item> items){
        boolean flag;
        boolean flag1= false;
        for(int i=0;hasKeys(i);i++){
            flag = true;
            ArrayList<Item> Itemss = getItems(i);
            for(Item t:items){
                flag1=false;
                for(int j=0;j<Itemss.size();j++){
                    if(Itemss.get(j).toString().equals(t.toString())){
                        flag1=true;
                        break;
                    }
                }
                if(flag1==false){
                    flag =false;
                    break;

                }
            }
            if(flag)
                return true;
        }
        return false;
    }
    public int GetIndexOfItem(ArrayList<Item> items){
        boolean flag;
        boolean flag1= false;
        for(int i=0;hasKeys(i);i++){
            flag = true;
            ArrayList<Item> Itemss = getItems(i);
            for(Item t:items){
                flag1=false;
                for(int j=0;j<Itemss.size();j++){
                    if(Itemss.get(j).toString().equals(t.toString())){
                        flag1=true;
                        break;
                    }
                }
                if(flag1==false){
                    flag =false;
                    break;

                }
            }
            if(flag)
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String re="";
        for(int i=0;i<Items.size();i++){
            if(Items.containsKey(i)){
                re+=Items.get(i)+"\n";
            }
        }
        return re;
    }
}

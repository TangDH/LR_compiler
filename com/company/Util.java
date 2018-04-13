package com.company;

import java.util.ArrayList;
import java.util.Iterator;

public class Util {
    public static Item ConvertToItem(ArrayList<String> production, int position, String sign){
        Item item = new Item();
        item.setDotPosition(position);
        item.setProduction(production);
        item.setSigns(sign);

        return item;
    }
    public static String GetBFromItem(Item item){
        int index = item.getDotPosition();
        if(index>=item.getProduction().size()){
            return null;
        }else if(Production.INTERMINAL_SET.contains(item.getProduction().get(index))){
            return String.valueOf(item.getProduction().get(index));
        }
        else{
            return null;
        }
    }
    public static boolean ItemsEquals(ArrayList<Item> items, ArrayList<Item> itemSetItems){
        for(int i=0;i<items.size();i++){
            if(!items.get(i).toString().equals(itemSetItems.get(i).toString())){
                return false;
            }
        }
        return true;
    }
    public static String GetBetaFromItem(Item item){
        int index = item.getDotPosition();
        if(index+1>=item.getProduction().size()){
            return "$";
        }else{
            return String.valueOf(item.getProduction().get(index+1));
        }
    }
    public static ArrayList<String> GetFirst(String beta,String A){
        ArrayList<String> re = new ArrayList<>();
        //String beta = BetaA.substring(0,1);
        //String A = BetaA.substring(1);
        //System.out.println(BetaA);
        if(Production.TERMINAL_SET.contains(beta)&&beta!="$"){
            re.add(beta);
            return re;
        }else if(beta=="$"){
            re.add(A);
            return re;
        }else if(Production.INTERMINAL_SET.contains(beta)){
            ArrayList<String> temp = GetFirstByInterminal(beta);
            re.addAll(temp);
            return re;
        }

        if(!Production.TERMINAL_SET.contains(A)){
            //System.out.println(A);
            //System.out.println("Error at GetFirst");
        }
        re.add(A);
        return re;

    }
    private static ArrayList<String> GetFirstByInterminal(String interminal){ //Maybe some error
        ArrayList<String> re = new ArrayList<>();
        if(Production.FirstSet.containsKey(interminal)){
            return Production.FirstSet.get(interminal);
        }
        System.out.println(interminal);
        ArrayList<ArrayList<String>> productions = Production.PRODUCTION_MAP.get(interminal);
        for(ArrayList<String> production:productions ){
            for(String signs:production){
                if(Production.FirstSet.containsKey(interminal)){
                    re.addAll(Production.FirstSet.get(interminal));
                }
                if(Production.TERMINAL_SET.contains(signs)){
                    re.add(signs);
                    break;
                }else if(Production.INTERMINAL_SET.contains(signs)){
                    if(signs.equals(interminal)){
                        //Production.FirstSet.put(interminal,re);
                        break;
                    }
                    ArrayList<String> temp = GetFirstByInterminal(signs);
                    if(temp.contains("$")){
                        temp.remove("$");
                        re.addAll(temp);
                        continue;
                    }else{
                        re.addAll(temp);
                        break;
                    }
                }else{
                    System.out.println(signs+" At GetFirst By Intermianl");
                    return re;
                }
            }
        }
        re = getSingle(re);
        Production.FirstSet.put(interminal,re);
        return re;
    }
    public static ArrayList getSingle(ArrayList list) {
        ArrayList tempList = new ArrayList();          //1,创建新集合
        Iterator it = list.iterator();              //2,根据传入的集合(老集合)获取迭代器
        while(it.hasNext()) {                  //3,遍历老集合
            Object obj = it.next();                //记录住每一个元素
            if(!tempList.contains(obj)) {            //如果新集合中不包含老集合中的元素
                tempList.add(obj);                //将该元素添加
            }
        }
        return tempList;
    }
    public static void GenerateInterminalFirst(){

    }








}

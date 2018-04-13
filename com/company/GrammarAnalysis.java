package com.company;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.TreeMap;

public class GrammarAnalysis {
    private ItemSet m_ItemSet;
    private TreeMap<Tule,Integer> Goto;
    private HashMap<Tule,String> Action;
    private Stack<Integer> State;
    public GrammarAnalysis(String path){
        try{
            Files.readLines(new File(path),
                    Charsets.UTF_8).stream().
                    filter(line->line.length()>1).
                    forEach(Production::addProduction);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Production.StoreInterminal();
        Production.Storeterminal();
        Production.StoreProduction();
        Production.Show();
        ArrayList<String> GF = new ArrayList<>();
        ArrayList<String> EF = new ArrayList<>();
        GF.add("(");
        GF.add("id");
        GF.add("!");
        EF.add("(");
        EF.add("id");
        Production.FirstSet.put("E",EF);
        Production.FirstSet.put("G",GF);
        m_ItemSet = new ItemSet();
        Goto = new TreeMap<>();
        Action = new HashMap<>();
    }
    private ArrayList<Item> Closure(ArrayList<Item> items){
        //[[intmain(){A}5$], [CA0}], [C0}], [X0int], [X0bool], [X0}], [YZ;0int], [YZ;0bool], [YZ;0}], [int0id], [bool0id]]
        if(items.size()==0)
            return null;
        boolean flag = true;
        while(flag){
            flag = false;
            for(int i=0;i<items.size();i++){
                Item item = items.get(i);
                String B = Util.GetBFromItem(item);
                if(!Production.INTERMINAL_SET.contains(B)||B==null)
                    continue;
                String beta = Util.GetBetaFromItem(item);
                //System.out.println(B);
                for(ArrayList<String> production:Production.PRODUCTION_MAP.get(B)){
                    String CaculateFirst = beta+item.getSigns();
                    ArrayList<String> TerminalSet = Util.GetFirst(beta,item.getSigns());
                    for(String first:TerminalSet){
                        Item addItem = new Item();
                        addItem.setSigns(first);
                        addItem.setDotPosition(0);
                        addItem.setProduction(production);
                        addItem.setHeader(B);
                        boolean AddOrNot = true;
                        for(int j=0;j<items.size();j++){
                            if(items.get(j).toString().equals(addItem.toString())){
                                AddOrNot = false;
                                break;
                            }
                        }
                        if(AddOrNot){
                            items.add(addItem);
                        }
                    }
                }
            }
        }


        return items;
    }

    private ArrayList<Item> GOTO(ArrayList<Item> items, String X){
        ArrayList<Item> J  = new ArrayList<>();
        for(Item item:items){
            int index= item.getDotPosition();
            ArrayList<String> production = item.getProduction();
            if(index<production.size()&&production.get(index).equals(X)){
                Item temp = new Item();
                temp.setDotPosition(index+1);
                temp.setSigns(item.getSigns());
                temp.setProduction(item.getProduction());
                temp.setHeader(item.getHeader());
                J.add(temp);
            }
        }
        return Closure(J);
    }


    private void Items(Production grammar){
        ArrayList<String> StartProduction = Production.PRODUCTION_MAP.get("S'").get(0);
        Item item = Util.ConvertToItem(StartProduction,0,"$");
        item.setHeader("S'");
        ItemSet itemSet = new ItemSet();
        itemSet.Add(item,0);
        ArrayList<Item> temp = Closure(itemSet.getItems(0));
        itemSet.Put(0,temp);
        System.out.println(itemSet.getItems(0));
        boolean flag = true;
        int index =1;
        while(flag){
            flag =false;
            for(int i=0;itemSet.hasKeys(i);i++){
                for(String X:Production.TERMINAL_SET){
                    ArrayList<Item> items = GOTO(itemSet.getItems(i),X);
                    if(items!=null&&!itemSet.Contains(items)){
                        itemSet.Add(index,items);
                        System.out.println(itemSet.getItems(index));
                        index++;
                    }
                }
                for(String X:Production.INTERMINAL_SET){
                    ArrayList<Item> items = GOTO(itemSet.getItems(i),X);
                    if(items!=null&&itemSet.Contains(items)){
                        int k = itemSet.GetIndexOfItem(items);
                            Tule tule = new Tule();
                            tule.situaion = i;
                            tule.input = X;
                            System.out.println("2:"+tule.toString()+" "+k);
                            Goto.put(tule, k);
                    }
                    else if(items!=null&&!itemSet.Contains(items)){
                        itemSet.Add(index,items);
                        Tule tule = new Tule();
                        tule.situaion = i;
                        tule.input=X;
                        System.out.println("1:"+tule.toString()+" "+index);
                        Goto.put(tule,index);
                        System.out.println(itemSet.getItems(index));
                        index++;
                    }
                }
            }
        }
        m_ItemSet = itemSet;
    }
    public void GenerateItems(){
        Production production = new Production();
        Items(production);
        GenerateAction();
        System.out.println(Action);
        System.out.println("finish");
        System.out.println(Goto);
    }
    public void GenerateAction(){
        for(int i=0;m_ItemSet.hasKeys(i);i++){
            //System.out.println(m_ItemSet.hashCode());
            ArrayList<Item> items = m_ItemSet.getItems(i);
            for(int j=0;j<items.size();j++){
                Item item= items.get(j);
                if(!item.getHeader().equals("S'")){
                    ArrayList<String> production = item.getProduction();
                    int position = item.getDotPosition();
                    String sign = item.getSigns();
                    if(position<production.size()&&Production.TERMINAL_SET
                            .contains(production.get(position))){
                        Tule tule = new Tule();
                        tule.situaion=i;
                        tule.input=production.get(position);
                        int k = m_ItemSet.GetIndexOfItem(GOTO(items,tule.input));
                        Action.put(tule,"s"+String.valueOf(k));
                    }else if(position==production.size()){
                        Tule tule = new Tule();
                        tule.situaion=i;
                        tule.input=sign;
                        Item search = new Item();
                        search.setHeader(item.getHeader());
                        //System.out.println(item.getHeader());
                        search.setProduction(item.getProduction());
                        search.setDotPosition(0);
                        search.setSigns(item.getSigns());
                        ArrayList<ArrayList<String>> productions= Production.PRODUCTION_MAP.get(item.getHeader());
                        for(int k=0;k<productions.size();k++){
                            if(productions.get(k).equals(item.getProduction())){
                                Action.put(tule,"r"+item.getHeader()+item.getProduction());
                                break;
                            }
                        }
                    }
                }else{
                    int position = item.getDotPosition();
                    String sign = item.getSigns();
                    ArrayList<String> production = item.getProduction();
                    if(position==production.size()&&sign.equals("$")) {
                        Tule tule = new Tule();
                        tule.situaion = i;
                        tule.input = "$";
                        Action.put(tule, "Accept");
                    }

                    if(position<production.size()&&Production.TERMINAL_SET
                            .contains(production.get(position))){
                        Tule tule = new Tule();
                        tule.situaion=i;
                        tule.input=production.get(position);
                        int k = m_ItemSet.GetIndexOfItem(GOTO(items,tule.input));
                        Action.put(tule,"s"+String.valueOf(k));
                    }
                }
            }
        }
    }


    public void Initial() {
        State = new Stack<>();
        State.push(0);
    }

    public int analysis(Word word) {
        String property = word.getProperty();
        if(word.getState().equals("ID")&&!word.getProperty().equals("main")){
            property = "id";
        }else if(word.getState().equals("NUM")){
            property = "num";
        }
        Tule tule = new Tule();
        tule.input=property;
        tule.situaion=State.peek();
        Tule tule1 = new Tule();
        tule.input =property;
        tule.situaion = State.peek();
        if(Action.containsKey(tule)){
            String action = Action.get(tule);
            if(action==null)
                return -1;
            if(action.equals("Accept")){
                return 0;
            }
            while(action.charAt(0)!='s'){
                if(action.equals("Accept")){
                    return 0;
                }
                if(Production.INTERMINAL_SET.contains(tule1.input)){
                    if(!Goto.containsKey(tule1))
                        return -1;
                    int temp = Goto.get(tule1);
                    State.push(temp);
                    tule1.situaion=temp;
                    tule1.input=tule.input;
                    if(!Action.containsKey(tule1)){
                        return -1;
                    }
                    action = Action.get(tule1);
                }
                else {
                    String production = action.substring(1);
                    String header = production.split("\\[")[0];
                    production = production.substring(header.length());
                    ArrayList<ArrayList<String>> all  =Production.PRODUCTION_MAP.get(header);
                    int num=0;
                    for(int i=0;i<all.size();i++){
                        if(production.toString().equals(all.get(i).toString())){
                            num = all.get(i).size();
                            break;
                        }
                    }
                    for(int i=0;i<num;i++)
                        State.pop();
                    tule1.situaion = State.peek();
                    tule1.input = header;
                }
            }
            int state = Integer.valueOf(action.substring(1));
            State.push(state);
        }else{
            return -1;
        }
        return 1;
    }
}

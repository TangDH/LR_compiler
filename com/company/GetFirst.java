package com.company;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GetFirst {
    private BufferedReader reader;
    private static String signs="()[],+=-;";
    public HashMap<String,ArrayList<String>> First;
    private ArrayList<String> ExpressionList;
    public GetFirst(String filename) throws FileNotFoundException {
        File file = new File(filename);
        reader = new BufferedReader(new FileReader(file));
        First = new HashMap<String, ArrayList<String>>();
        ExpressionList = new ArrayList<String>();
    }

    public void  GenerateFirst() throws IOException {
        String expression  ="";
        while(true) {
            expression = reader.readLine();
            if(expression==null)
                break;
            ExpressionList.add(expression);
        }
        boolean flag = true;
        while(flag){
            flag = false;
            for(int i=0;i<ExpressionList.size();i++){
                String[] GenerateList = ExpressionList.get(i).split("->|\\|");   //得到生成式
                if(First.containsKey(GenerateList[0])){

                    for(int j=1;j<GenerateList.length;j++){
                        if((GenerateList[j].charAt(0)<='z'&&GenerateList[j].charAt(0)>='a')||signs.contains(String.valueOf(GenerateList[j].charAt(0)))){  //判断是否是终结符
                            String AddInFirst = GetTerminal(GenerateList[j]);
                            if(First.get(GenerateList[0]).contains(AddInFirst)){           //First集是否已经包含
                                continue;
                            }else{
                                ArrayList<String> temp = First.get(GenerateList[0]);
                                temp.add(AddInFirst);
                                First.put(GenerateList[0],temp);
                                flag = true;
                            }
                        }else{
                            ArrayList<String> Waiter  = GetNotTerminal(GenerateList[j]);//A->BTY  Waiter = [B,T,Y]
                            ArrayList<String> FirstToChange = First.get(GenerateList[0]);
                            boolean MeetDollar = true;
                            for(int k=0;k<Waiter.size()&&MeetDollar;k++){
                                MeetDollar = false;
                                String NTerminal = Waiter.get(k);

                                if(First.containsKey(NTerminal)){
                                    ArrayList<String> temp = First.get(NTerminal);
                                    if(temp.contains("$")){
                                        MeetDollar = true;
                                    }
                                    for(int l=0;l<temp.size();l++){
                                        if(FirstToChange.contains(temp.get(l))){
                                            continue;
                                        }else if(temp.get(l)!="$"){
                                            FirstToChange.add(temp.get(l));
                                            flag = true;
                                        }
                                    }
                                }

                                else{
                                    break;
                                }
                            }
                            if(MeetDollar){
                                if(!FirstToChange.contains("$"))
                                    FirstToChange.add("$");
                            }
                            First.put(GenerateList[0],FirstToChange);

                        }
                    }
                }
                else{
                    ArrayList<String> temp = new ArrayList<String>();
                    First.put(GenerateList[0],temp);
                    flag=true;
                }

            }
        }


    }

    private String GetTerminal(String generate){
        String re="";
        String signs = "()[]+=-,$";
        int i=0;
        while (signs.contains(String.valueOf(generate.charAt(i)))){
            re= re+generate.charAt(i);
            i++;
        }
        if(i>0){
            return re;
        }
        for(;i<generate.length();i++){
            if(generate.charAt(i)<='z'&&generate.charAt(i)>='a'){
                re  += generate.charAt(i);
            }else{
                break;
            }
        }
        return re;
    }
    private ArrayList<String> GetNotTerminal(String generate){ //T->AB'id     return [A,B']
        ArrayList<String> re = new ArrayList<String>();
        for(int i=0;i<generate.length();){
            int add = 1;
            if(i+1<generate.length()&&generate.charAt(i+1)=='\''){
                String temp = generate.substring(i,i+2);
                re.add(temp);
                i+=2;
            }else if(generate.charAt(i)>='A'&&generate.charAt(i)<='Z'){
                String temp = generate.substring(i,i+1);
                re.add(temp);
                i+=1;
            }else{
                break;
            }
        }
        return re;
    }

}

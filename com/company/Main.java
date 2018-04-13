package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static  void main(String[] args) throws IOException {
        WordScanner wc = new WordScanner("/home/tangdh/lala.txt");
        Word word;
        GrammarAnalysis grammarAnalysis = new GrammarAnalysis("/home/tangdh/hhkcg.txt");
        grammarAnalysis.GenerateItems();
        grammarAnalysis.Initial();
        System.out.println(Production.FirstSet);
        while((word=wc.GetNext())!=null){
            if(word.getProperty()!="") {
                if (word.getState() == "ID"&&!word.getState().equals("COMMENT")) {
                    Identifier.Id(word);
                    System.out.println(word.getState() + " " + word.getProperty() + " " + word.getLine());
                    int status = grammarAnalysis.analysis(word);
                    if(status==-1)
                        System.out.println("Error");
                    else if(status==0)
                        System.out.println("Accept");

                }

                else if(word.getState()=="ERROR"){
                    System.out.println("Could not know the identifier "+word.getProperty()+"  in line"+word.getLine());
                    //return 0;
                }else{
                    System.out.println(word.getState() + " " + word.getProperty() + " " + word.getLine());

                    int status = grammarAnalysis.analysis(word);

                    if(status==-1)
                        System.out.println("Error");
                    else if(status==0)
                        System.out.println("Accept");
                }
            }
        }
        Word temp = new Word();
        temp.setProperty("$");
        temp.setState("ff");
        temp.setLine(111);
        int status = grammarAnalysis.analysis(temp);
        if(status==-1)
            System.out.println("Error");
        else if(status==0)
            System.out.println("Accept");

    }

}

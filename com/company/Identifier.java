package com.company;

public class Identifier {
    static void Id(Word word){
        switch (word.getProperty()){
            case "for":
                word.setState("FOR");
                break;
            case "while":
                word.setState("WHILE");
                break;
             case "int":
                word.setState("INT");
                break;
             case "unsigned":
                word.setState("UNSIGNED");
                break;
              case "float":
                word.setState("FLOAT");
                break;
            case "else":
                word.setState("ELSE");
                break;
            case "return":
                word.setState("RETURN");
                break;
            case "void":
                word.setState("VOID");
                break;
            case "if":
                word.setState("IF");
                break;

            default:
                break;
        }
    }
}

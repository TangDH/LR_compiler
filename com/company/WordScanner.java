package com.company;

import java.io.*;

public class WordScanner {
    private File file;
    BufferedReader reader = null;
    private int BufferOffset;
    private int line;
    String Buffer1;
    Boolean BufferEnd;
    String[] Buffer;
    private int offset = 0;
       static String sign="[]{}()=+-/<>;*&|!";
    public WordScanner(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        reader = new BufferedReader(new FileReader(file));
        Buffer1 = "";
        BufferEnd = true;
        BufferOffset = 0;
    }
    public Word GetNext(){
        try{
            if(BufferEnd){
                Buffer1 = reader.readLine();
                if(Buffer1==null){
                    return null;
                }
                Buffer = Buffer1.split("\\s");
                line++;
                BufferEnd = false;
                BufferOffset = 0;
                offset = 0;
            }else{
                //Word token = new Word();
                if(BufferOffset<Buffer.length) {
                    String temp = Buffer[BufferOffset];
                    if(offset>=temp.length()){
                        BufferOffset+=1;
                        offset=0;
                        return new Word();
                    }

                    Word token = new Word();
                    int re =0;
                    if((re=ReadSign(temp,offset,token,line))!=-1){
                        offset+=re;
                        if(token.getState()=="COMMENT"){
                            BufferEnd = true;
                        }
                        if(offset>=temp.length()){
                            BufferOffset+=1;
                            offset=0;
                        }
                        return token;
                    }else if((re=ReadNum(temp,offset,token,line))!=-1){
                        offset+=re;
                        if(offset>=temp.length()){
                            BufferOffset+=1;
                            offset=0;
                        }
                        return token;
                    }else if((re=ReadStr(temp,offset,token,line))!=-1){
                        offset+=re;
                        if(offset>=temp.length()){
                            BufferOffset+=1;
                            offset=0;
                        }
                        return token;
                    }else {
                        System.out.println("GetNext Error");
                    }


                }else{
                    BufferOffset=0;
                    BufferEnd=true;
                    return new Word();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Word();
    }

    private int ReadNum(String str,int offset,Word token,int line){
        int start = offset;
        int end = offset;
        for(int i=offset;i<str.length();i++){
            char c = str.charAt(i);
            if((c>='0'&&c<='9')||c=='.'){
                end++;
            }else{
                break;
            }
        }
        if(end==start)
            return -1;
        token.setProperty(str.substring(start,end));
        token.setLine(line);
        token.setState("NUM");
        return end-start;

    }

    private int ReadStr(String str,int offset,Word token,int line){
        int start = offset;
        int end = offset;
        Boolean flag = false;
        for(int i=offset;i<str.length();i++){
            char c = str.charAt(i);
            if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c=='_')||(c>='0'&&c<='9')){
                end++;
            }
            else{
                if(sign.contains(String.valueOf(c))){
                    break;
                }else{
                    end++;
                    flag = true;
                }
            }
        }
        if(end==start){
            return -1;
        }
        if(flag){
            token.setProperty(str.substring(start,end));
            token.setLine(line);
            token.setState("ERROR");
            return end-start;
        }
        token.setProperty(str.substring(start,end));
        token.setLine(line);
        token.setState("ID");
        return end-start;

    }

    private int ReadSign(String str,int offset,Word token,int line){
        int re =-1;
        if(str==""){
            return -1;
        }
        switch (str.charAt(offset)){
            case ';':
                token.setProperty(";");
                token.setLine(line);
                token.setState("SELICOUM");
                re =1;
                break;

            case '{':
                 token.setProperty("{");
                 token.setLine(line);
                token.setState("LBB");
                 re =1;
                 break;

            case '}':
                 token.setProperty("}");
                 token.setLine(line);
                 token.setState("RBB");
                 re =1;
                 break;
            case '(':
                 token.setProperty("(");
                 token.setLine(line);
                 token.setState("LB");
                 re =1;
                 break;
            case ')':
                 token.setProperty(")");
                 token.setLine(line);
                 token.setState("RB");
                 re =1;
                 break;
            case '=':
                 if(offset+1<str.length()&&str.charAt(offset+1)=='='){
                     token.setProperty("==");
                     token.setLine(line);
                     token.setState("EQU");
                     re=2;
                 }else{
                     token.setProperty("=");
                     token.setLine(line);
                     token.setState("ASSIGN");
                     re =1;
                 }
                 break;
            case '-':
                if(offset+1<str.length()&&str.charAt(offset+1)=='='){
                    token.setProperty("-=");
                    token.setLine(line);
                    token.setState("MIEQU");
                    re=2;
                }else if(offset+1<str.length()&&str.charAt(offset+1)=='-'){
                    token.setState("MINO");
                    token.setLine(line);
                    token.setProperty("--");
                    re=2;
                }else {
                    token.setProperty("-");
                    token.setLine(line);
                    token.setState("MINUS");
                    re = 1;
                }
                break;
            case '+':
                if(offset+1<str.length()&&str.charAt(offset+1)=='='){
                    token.setProperty("+=");
                    token.setLine(line);
                    token.setState("ADEQU");
                    re =2;
                }else if(offset+1<str.length()&&str.charAt(offset+1)=='+'){
                    token.setState("ADDO");
                    token.setLine(line);
                    token.setProperty("++");
                    re=2;
                }else {
                    token.setProperty("+");
                    token.setLine(line);
                    token.setState("ADD");
                    re = 1;
                }
                break;
            case '/':
                if(offset+1<str.length()&&str.charAt(offset+1)=='/'){
                    token.setState("COMMENT");
                    token.setProperty("//");
                    token.setLine(line);
                    re =2;
                }else if(offset+1<str.length()&&str.charAt(offset+1)=='='){
                    token.setState("DIEQU");
                    token.setLine(line);
                    token.setProperty("/=");
                    re=2;
                }
                else {
                    token.setProperty("/");
                    token.setLine(line);
                    token.setState("DIVIDE");
                    re = 1;
                }
                break;
            case '*':
                if(offset+1<str.length()&&str.charAt(offset+1)=='='){
                    token.setProperty("*=");
                    token.setLine(line);
                    token.setState("MUEQU");
                    re =2;
                }else {
                    token.setProperty("*");
                    token.setLine(line);
                    token.setState("MULTI");
                    re = 1;
                }
                break;
            case '<':
                if(offset+1<str.length()&&str.charAt(offset+1)=='='){
                    token.setProperty("<=");
                    token.setLine(line);
                    token.setState("LET");
                    re =2;
                }else {
                    token.setProperty("<");
                    token.setLine(line);
                    token.setState("LT");
                    re = 1;
                }
                break;
            case '>':
                if(offset+1<str.length()&&str.charAt(offset+1)=='='){
                    token.setProperty(">=");
                    token.setLine(line);
                    token.setState("GET");
                    re =2;
                }else {
                    token.setProperty(">");
                    token.setLine(line);
                    token.setState("GT");
                    re = 1;
                }
                break;
            case '[':
                token.setProperty("[");
                token.setLine(line);
                token.setState("LSB");
                re= 1;
                break;
            case ']':
                token.setProperty("]");
                token.setLine(line);
                token.setState("RSB");
                re =1;
                break;
            case '&':
                if(offset+1<str.length()&&str.charAt(offset+1)=='&'){
                    token.setProperty("&&");
                    token.setLine(line);
                    token.setState("ANDAND");
                    re =2;
                }else {
                    token.setProperty("&");
                    token.setLine(line);
                    token.setState("AND");
                    re = 1;
                }
                break;
            case '|':
                if(offset+1<str.length()&&str.charAt(offset+1)=='|'){
                    token.setProperty("||");
                    token.setLine(line);
                    token.setState("OROR");
                    re =2;
                }else {
                    token.setProperty("|");
                    token.setLine(line);
                    token.setState("OR");
                    re = 1;
                }
                break;
            case '!':
                if(offset+1<str.length()&&str.charAt(offset+1)=='='){
                    token.setProperty("!=");
                    token.setLine(line);
                    token.setState("NOTEQUAL");
                    re =2;
                }else {
                    token.setProperty("!");
                    token.setLine(line);
                    token.setState("NOT");
                    re = 1;
                }
                break;

            default:
                return -1;

        }
        return re;


    }











}

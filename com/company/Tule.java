package com.company;

public class Tule implements Comparable<Tule>{
    public int situaion;
    public String input;

    @Override
    public int hashCode() {
        return input.hashCode()+situaion*9000;
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof Tule){
            Tule t = (Tule)o;
            return t.toString().equals(o.toString());
        }
        throw new ClassCastException("类型不匹配");
    }

    @Override
    public String toString() {
        String re="";
        re+=" "+String.valueOf(situaion)+" "+input;
        return re;
    }

    @Override
    public int compareTo(Tule o) {
//        int re=0;
//        if(this.equals(o)){
//            return 0;
//        }
//        else {
//
//            for (int i = 0; i < o.input.length() && i < this.input.length(); i++) {
//                re += o.input.charAt(i) - this.input.charAt(i);
//            }
//            re += (o.situaion - this.situaion);
//        }
//        return re;
        return o.hashCode()-this.hashCode();
    }
}


package com.example.indexablelist;

/**
 * Created by Lin.Hou on 2017/10/16.
 */

public class StringMatcher {
    //value item的文本
    //keyword：索引列表中的字符
    public static  boolean match(String value,String keyword){
        //value和keyword参数值都不能为null
        if (value==null||keyword==null){
            return false;
        }
        if (keyword.length()>value.length()){
            return false;
        }
        //value的指针
        int i=0;
        //keyword的指针
        int j=0;
        do{
            //
            if (keyword.charAt(j)==value.charAt(i)){
                i++;
                j++;
            }else  if (j>0){
                break;
            }else {
                i++;
            }

        }while (i<value.length()&&j<keyword.length());

        return (j==keyword.length())? true:false;

    }


}

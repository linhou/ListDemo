package com.example.indexablelist;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.example.indexablelist.view.IndexableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mItems;
    private IndexableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (IndexableListView) findViewById(R.id.list_indexable);
        /**
         * 1.初始化items
         * 2.根据section获取posotion
         * */
        mItems=new ArrayList<>();
        mItems.add("11111");
        mItems.add("A I am A");
        mItems.add("B  I am B");
        mItems.add("C  I am C");
        mItems.add("D  I am D");
        mItems.add("E   I am E");
        mItems.add("F  I am F");
        mItems.add("g  I am G");
        mItems.add("h I am H");
        mItems.add("I  I am I");
        mItems.add("J  I am J");
        mItems.add("K  I am K");
        mItems.add("l  I am L");
        mItems.add("M I am M");
        mItems.add("N  I am N");
        mItems.add("Y  I am Y");
        mItems.add("W  I am W");

        //进行排序
        Collections.sort(mItems);


        ContetAdapter adapter =new ContetAdapter(this,android.R.layout.simple_expandable_list_item_1,mItems);
        //书写一个控件

        mListView.setAdapter(adapter);
        mListView.setFastScrollEnabled(true);
    }
    //使用ArrayAdapter
    //实现SectionIndexer接口
    private  class ContetAdapter extends ArrayAdapter<String> implements SectionIndexer{

        private String mSection="#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //生成构造，使用三个参数的构造
        public ContetAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }
        /**
         * SectionIndexer有三个重写方法
         * 1.getSections数据元素存入的列表
         * 2.getPositionForSection根据数据元素获取搜索引
         * 3.getSectionForPosition 与上述相反获取postion来获取索引
         * */


        @Override
        public Object[] getSections() {
            String [] section=new String[mSection.length()];
            //将每个Section作为单个数组元素放入sections
            for (int i = 0; i < section.length; i++) {
                section[i]=String.valueOf(mSection.charAt(i));
            }
            return section;
        }

        @Override
        public int getPositionForSection(int i) {
            //从当前的section（也就是i）往前查,直到遇到第一个对应的item为止，否则不进行定位
            for (int j = i; j >=0 ; j--) {
                for (int k = 0; k < getCount(); k++) {

                    if (i==0){
                        //查询数字
                        for (int l = 0; l <=9; l++) {
                            //value是item首字母
                            if (StringMatcher.match(String.valueOf(getItem(l).charAt(0)),String.valueOf(l)));
                                return l;
                        }
                    }else {
                        //查询字母
                        if (StringMatcher.match(String.valueOf(getItem(k).charAt(0)),String.valueOf(mSection
                        .charAt(i)))){
                            return  k;
                        }
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int i) {
            return 0;
        }
    }

}

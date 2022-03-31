package com.urban.urbanreport.CustomClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.urban.urbanreport.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category_list_adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> category;
    private ArrayList<String> subcategory;
    private ArrayList<String> achievement;
    private ArrayList<String> sales;
    private ArrayList<String> exchange;

    public Category_list_adapter(Context context, ArrayList<String> arraycategory,ArrayList<String> arraysubcategory,
                               ArrayList<String> arrayachievement,ArrayList<String> arraysales,
                               ArrayList<String> arrayexchange) {
        super();
        mContext = context;
        category = arraycategory;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        subcategory = arraysubcategory;
        achievement=arrayachievement;
        sales=arraysales;
        exchange = arrayexchange;
    }

    public int getCount() {
        return category.size();
    }

    // getView method is called for each item of ListView
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_fcategory, parent, false);
        //
        TextView cat_list_category=(TextView)view.findViewById(R.id.cat_list_category);
        cat_list_category.setTag(position);

        TextView car_list_subcategory=(TextView)view.findViewById(R.id.car_list_subcategory);
        car_list_subcategory.setTag(position);

        TextView cat_list_achievement= (TextView)view.findViewById(R.id.cat_list_achievement);
        cat_list_achievement.setTag(position);

        TextView cat_list_sales= (TextView)view.findViewById(R.id.cat_list_sales);
        cat_list_sales.setTag(position);

        TextView cat_list_exchange= (TextView)view.findViewById(R.id.cat_list_exchange);
        cat_list_exchange.setTag(position);

        //
        cat_list_category.setText(category.get(position));

        car_list_subcategory.setText(subcategory.get(position));

        cat_list_achievement.setText(achievement.get(position));

        cat_list_sales.setText(sales.get(position));
        cat_list_exchange.setText(exchange.get(position));
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listview_card_adapter_anim);
        view.startAnimation(animation);
        return view;
    }


    public boolean isEmpty(){
        return category.isEmpty();
    }

//    public void delete(int i){
//        cabang.remove(i);
//        tanggal.remove(i);
//        achievement.remove(i);
//        target.remove(i);
//        sales.remove(i);
//        exchange.remove(i);
//        notifyDataSetChanged();
//    }


//    public String getName(int position){
//        return idscanlist.get(position);
//    }
//
//    public String getTanggal(int position){
//        return listtgl.get(position);
//    }
//
//    public String getCabang(int position){
//        return cabanglist.get(position);
//    }
//
//    public List<Boolean> getCheckList(){
//        return checked;
//    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return category.get(position);
//        return list.get(position);
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}


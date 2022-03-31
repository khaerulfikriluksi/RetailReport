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

public class Items_and_promotion_adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> name;
    private ArrayList<String> achievement;
    private ArrayList<String> sales;
    private ArrayList<String> exchange;

    public Items_and_promotion_adapter(Context context, ArrayList<String> arrayname,
                                ArrayList<String> arrayachievement,ArrayList<String> arraysales,
                                ArrayList<String> arrayexchange) {
        super();
        mContext = context;
        name = arrayname;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        achievement=arrayachievement;
        sales=arraysales;
        exchange = arrayexchange;
    }

    public int getCount() {
        return name.size();
    }

    // getView method is called for each item of ListView
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_promoitems, parent, false);
        //
        TextView itm_list_name=(TextView)view.findViewById(R.id.itm_list_name);
        itm_list_name.setTag(position);

        TextView itm_list_achievement= (TextView)view.findViewById(R.id.itm_list_achievement);
        itm_list_achievement.setTag(position);

        TextView itm_list_sales= (TextView)view.findViewById(R.id.itm_list_sales);
        itm_list_sales.setTag(position);

        TextView itm_list_exchange= (TextView)view.findViewById(R.id.itm_list_exchange);
        itm_list_exchange.setTag(position);

        //
        itm_list_name.setText(name.get(position));
        itm_list_achievement.setText(achievement.get(position));
        itm_list_sales.setText(sales.get(position));
        itm_list_exchange.setText(exchange.get(position));
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listview_card_adapter_anim);
        view.startAnimation(animation);
        return view;
    }


    public boolean isEmpty(){
        return name.isEmpty();
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
        return name.get(position);
//        return list.get(position);
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
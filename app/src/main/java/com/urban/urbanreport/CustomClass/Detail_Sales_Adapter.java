package com.urban.urbanreport.CustomClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.urban.urbanreport.R;
import java.util.ArrayList;

public class Detail_Sales_Adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> cabang;
    private ArrayList<String> tanggal;
    private ArrayList<String> achievement;
    private ArrayList<String> target;
    private ArrayList<String> sales;
    private ArrayList<String> exchange;

    public Detail_Sales_Adapter(Context context, ArrayList<String> arraycabang,ArrayList<String> arraytgl,
                               ArrayList<String> arrayachievement,ArrayList<String> arraytarget,ArrayList<String> arraysales,
                               ArrayList<String> arrayexchange) {
        super();
        mContext = context;
        cabang = arraycabang;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tanggal = arraytgl;
        achievement=arrayachievement;
        target=arraytarget;
        sales=arraysales;
        exchange = arrayexchange;
    }

    public int getCount() {
        return cabang.size();
    }

    // getView method is called for each item of ListView
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_foutlet, parent, false);
        //
        TextView sls_list_cabang=(TextView)view.findViewById(R.id.sls_list_cabang);
        sls_list_cabang.setTag(position);

        TextView sls_list_tgl=(TextView)view.findViewById(R.id.sls_list_tgl);
        sls_list_tgl.setTag(position);

        TextView sls_list_achievement= (TextView)view.findViewById(R.id.sls_list_achievement);
        sls_list_achievement.setTag(position);

        TextView sls_list_target= (TextView)view.findViewById(R.id.sls_list_target);
        sls_list_target.setTag(position);

        TextView sls_list_sales= (TextView)view.findViewById(R.id.sls_list_sales);
        sls_list_sales.setTag(position);

        TextView sls_list_exchange= (TextView)view.findViewById(R.id.sls_list_exchange);
        sls_list_exchange.setTag(position);

        //
        sls_list_cabang.setText(cabang.get(position));
        sls_list_tgl.setText(tanggal.get(position));
        sls_list_achievement.setText(achievement.get(position));
        sls_list_target.setText(target.get(position));
        sls_list_sales.setText(sales.get(position));
        sls_list_exchange.setText(exchange.get(position));
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listview_card_adapter_anim);
        view.startAnimation(animation);
        return view;
    }


    public boolean isEmpty(){
        return cabang.isEmpty();
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
        return cabang.get(position);
//        return list.get(position);
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}

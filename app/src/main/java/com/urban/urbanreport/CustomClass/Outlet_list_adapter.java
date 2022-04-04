package com.urban.urbanreport.CustomClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.urban.urbanreport.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Outlet_list_adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> kodecabang;
    private ArrayList<String> cabang;
    private ArrayList<String> tanggal;
    private ArrayList<String> achievement;
    private ArrayList<Integer> nilai;
    private ArrayList<String> target;
    private ArrayList<String> sales;
    private ArrayList<String> exchange;

    public Outlet_list_adapter(Context context, ArrayList<String> arraykodecabang,ArrayList<String> arraycabang,ArrayList<String> arraytgl,
                               ArrayList<String> arrayachievement, ArrayList<Integer> arraynilai,ArrayList<String> arraytarget,ArrayList<String> arraysales,
                               ArrayList<String> arrayexchange) {
        super();
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        kodecabang=arraykodecabang;
        cabang = arraycabang;
        tanggal = arraytgl;
        achievement=arrayachievement;
        nilai = arraynilai;
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

    public void sortCabang(Boolean isAscending){
        ArrayList<String> kodecabang1=new ArrayList<>();
        ArrayList<String> cabang1=new ArrayList<>();
        ArrayList<String> tanggal1=new ArrayList<>();
        ArrayList<String> achievement1=new ArrayList<>();
        ArrayList<String > log=new ArrayList<>(kodecabang);
        ArrayList<Integer> nilai1=new ArrayList<>();
        ArrayList<String> target1=new ArrayList<>();
        ArrayList<String> sales1=new ArrayList<>();
        ArrayList<String> exchange1=new ArrayList<>();
        if (isAscending==true) {
            Collections.sort(kodecabang);
        } else {
            Collections.sort(kodecabang, Collections.reverseOrder());
        }
        int[] indexes = new int[kodecabang.size()];
        for (int n = 0; n < kodecabang.size(); n++){
            indexes[n] = log.indexOf(kodecabang.get(n));
            kodecabang1.add(log.get(indexes[n]));
            cabang1.add(cabang.get(indexes[n]));
            tanggal1.add(tanggal.get(indexes[n]));
            achievement1.add(achievement.get(indexes[n]));
            nilai1.add(nilai.get(indexes[n]));
            target1.add(target.get(indexes[n]));
            sales1.add(sales.get(indexes[n]));
            exchange1.add(exchange.get(indexes[n]));
        }
        kodecabang=kodecabang1;
        cabang = cabang1;
        tanggal = tanggal1;
        achievement=achievement1;
        nilai=nilai1;
        target=target1;
        sales=sales1;
        exchange = exchange1;
        notifyDataSetChanged();
    }

    public void sortAchievement(Boolean isAscending){
        ArrayList<datanya> dta = new ArrayList<datanya>();
        for (int x = 0; x < kodecabang.size(); x++){
            dta.add(new datanya(kodecabang.get(x),cabang.get(x),tanggal.get(x),achievement.get(x),nilai.get(x),target.get(x),sales.get(x),exchange.get(x)));
        }
        Collections.sort(dta, new Comparator<datanya>() {
            @Override
            public int compare(datanya datanya, datanya t1) {
                if (isAscending==true) {
                    return Integer.valueOf(datanya.getAchievement()).compareTo(t1.getAchievement());
                } else {
                    return Integer.valueOf(t1.getAchievement()).compareTo(datanya.getAchievement());
                }
            }
        });
        kodecabang.clear();
        cabang.clear();
        tanggal.clear();
        achievement.clear();
        nilai.clear();
        target.clear();
        sales.clear();
        exchange.clear();
        for (int x = 0; x < dta.size(); x++){
            kodecabang.add(dta.get(x).kodecabangx);
            cabang.add(dta.get(x).cabangx);
            tanggal.add(dta.get(x).tanggalx);
            achievement.add(dta.get(x).achievementx);
            nilai.add(dta.get(x).nilaix);
            target.add(dta.get(x).targetx);
            sales.add(dta.get(x).salesx);
            exchange.add(dta.get(x).exchangex);
        }
        notifyDataSetChanged();
    }


    public boolean isEmpty(){
        return cabang.isEmpty();
    }

    public Object getItem(int position) {
        return cabang.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    private class datanya {
        private String kodecabangx;
        private String cabangx;
        private String tanggalx;
        private String achievementx;
        private Integer nilaix;
        private String targetx;
        private String salesx;
        private String exchangex;

        public datanya(String kodecabang, String cabang, String tanggal, String achievement, Integer nilai, String target, String sales, String exchange) {
            this.kodecabangx = kodecabang;
            this.cabangx = cabang;
            this.tanggalx = tanggal;
            this.achievementx = achievement;
            this.nilaix = nilai;
            this.targetx = target;
            this.salesx = sales;
            this.exchangex = exchange;
        }

        public int getAchievement(){
            return this.nilaix;
        }

    }

}


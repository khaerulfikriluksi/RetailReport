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

public class Artikel_list_adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> artikel;
    private ArrayList<String> season;
    private ArrayList<String> achievement;
    private ArrayList<String> sales;
    private ArrayList<String> exchange;
    private ArrayList<String> departement=new ArrayList<>();
    private ArrayList<String> kelompok=new ArrayList<>();
    private ArrayList<String> subkelompok=new ArrayList<>();

    public Artikel_list_adapter(Context context, ArrayList<String> arrayartikel, ArrayList<String> arrayseason,
                                 ArrayList<String> Arraydepartement, ArrayList<String> Arraykelompok, ArrayList<String> Arraysubkelompok,
                                 ArrayList<String> arrayachievement,ArrayList<String> arraysales,
                                 ArrayList<String> arrayexchange) {
        super();
        mContext = context;
        artikel = arrayartikel;
        season = arrayseason;
        departement = Arraydepartement;
        kelompok = Arraykelompok;
        subkelompok = Arraysubkelompok;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        achievement=arrayachievement;
        sales=arraysales;
        exchange = arrayexchange;
    }

    public int getCount() {
        return artikel.size();
    }

    // getView method is called for each item of ListView
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_fartikel, parent, false);
        //
        TextView art_list_artikel=(TextView)view.findViewById(R.id.art_list_artikel);
        art_list_artikel.setTag(position);

        TextView art_list_departement=(TextView) view.findViewById(R.id.art_list_departement);
        art_list_departement.setTag(position);

        TextView art_list_kelompok = (TextView) view.findViewById(R.id.art_list_kelompok);
        art_list_kelompok.setTag(position);

        TextView art_list_subkelompok = (TextView) view.findViewById(R.id.art_list_subkelompok);
        art_list_subkelompok.setTag(position);

        TextView art_list_achievement= (TextView)view.findViewById(R.id.art_list_achievement);
        art_list_achievement.setTag(position);

        TextView art_list_sales= (TextView)view.findViewById(R.id.art_list_sales);
        art_list_sales.setTag(position);

        TextView art_list_exchange= (TextView)view.findViewById(R.id.art_list_exchange);
        art_list_exchange.setTag(position);

        TextView art_list_season = (TextView) view.findViewById(R.id.art_list_season);
        art_list_season.setTag(position);

        //
        art_list_artikel.setText(artikel.get(position));
        art_list_departement.setText(departement.get(position));
        art_list_kelompok.setText(kelompok.get(position));
        art_list_subkelompok.setText(subkelompok.get(position));
        art_list_achievement.setText(achievement.get(position));
        art_list_sales.setText(sales.get(position));
        art_list_exchange.setText(exchange.get(position));
        art_list_season.setText(season.get(position));
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listview_card_adapter_anim);
        view.startAnimation(animation);
        return view;
    }


    public boolean isEmpty(){
        return artikel.isEmpty();
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
        return artikel.get(position);
//        return list.get(position);
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}

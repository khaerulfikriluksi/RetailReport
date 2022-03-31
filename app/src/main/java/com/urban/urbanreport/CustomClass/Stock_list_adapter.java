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

public class Stock_list_adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> nmbrg;
    private ArrayList<String> cabang;
    private ArrayList<String> barcode;
    private ArrayList<String> artikel;
    private ArrayList<String> hrgjual;
    private ArrayList<String> stock;

    public Stock_list_adapter(Context context,
                              ArrayList<String> arraynmbrg,
                              ArrayList<String> arraycabang,
                              ArrayList<String> arraybarcode,
                              ArrayList<String> arrayartikel,
                              ArrayList<String> arrayhrgjual,
                              ArrayList<String> arraystock) {
        super();
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nmbrg=arraynmbrg;
        cabang = arraycabang;
        barcode = arraybarcode;
        artikel=arrayartikel;
        hrgjual=arrayhrgjual;
        stock=arraystock;
    }

    public int getCount() {
        return nmbrg.size();
    }

    // getView method is called for each item of ListView
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_fstock, parent, false);
        //
        TextView stk_list_nmbrg=(TextView)view.findViewById(R.id.stk_list_nmbrg);
        stk_list_nmbrg.setTag(position);

        TextView stk_list_cabang=(TextView)view.findViewById(R.id.stk_list_cabang);
        stk_list_cabang.setTag(position);

        TextView stk_list_barcode= (TextView)view.findViewById(R.id.stk_list_barcode);
        stk_list_barcode.setTag(position);

        TextView stk_list_artikel= (TextView)view.findViewById(R.id.stk_list_artikel);
        stk_list_artikel.setTag(position);

        TextView stk_list_hrgjual= (TextView)view.findViewById(R.id.stk_list_hrgjual);
        stk_list_hrgjual.setTag(position);

        TextView stk_list_qty= (TextView)view.findViewById(R.id.stk_list_qty);
        stk_list_qty.setTag(position);

        //
        stk_list_nmbrg.setText(nmbrg.get(position));
        stk_list_cabang.setText(cabang.get(position));
        stk_list_barcode.setText(barcode.get(position));
        stk_list_artikel.setText(artikel.get(position));
        stk_list_hrgjual.setText(hrgjual.get(position));
        stk_list_qty.setText(stock.get(position));
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listview_card_adapter_anim);
        view.startAnimation(animation);
        return view;
    }


    public boolean isEmpty(){
        return nmbrg.isEmpty();
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
        return nmbrg.get(position);
//        return list.get(position);
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}

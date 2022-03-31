package com.urban.urbanreport.CustomClass;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clzola.glottie.GlottieView;
import com.clzola.glottie.GlottieViewTarget;
import com.urban.urbanreport.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BestSeller_list_adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> kodeart=new ArrayList<>();
    private ArrayList<String> artikel=new ArrayList<>();
    private ArrayList<String> departemen=new ArrayList<>();
    private ArrayList<String> kelompok=new ArrayList<>();
    private ArrayList<String> subkelompok=new ArrayList<>();
    private ArrayList<String> penjualan=new ArrayList<>();
    private ArrayList<String> stock=new ArrayList<>();
    private ArrayList<String> season=new ArrayList<>();
    private ViewClickListener mViewClickListener;

    public interface ViewClickListener {
        void onItemCLick(int position);
    }

//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }

    public BestSeller_list_adapter(Context mContext,ArrayList<String> kodeart, ArrayList<String> artikel, ArrayList<String> departemen, ArrayList<String> kelompok, ArrayList<String> subkelompok, ArrayList<String> penjualan, ArrayList<String> stock, ArrayList<String> season, ViewClickListener viewClickListener) {
        this.kodeart=kodeart;
        this.mContext = mContext;
        this.artikel = artikel;
        this.departemen = departemen;
        this.kelompok = kelompok;
        this.subkelompok = subkelompok;
        this.penjualan = penjualan;
        this.stock = stock;
        this.season = season;
        this.mViewClickListener=viewClickListener;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return artikel.size();
    }


    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_fbestseller, parent, false);
        //
        TextView bst_artikel = (TextView) view.findViewById(R.id.bst_artikel);
        TextView bst_dep = (TextView) view.findViewById(R.id.bst_dep);
        TextView bst_kelompok = (TextView) view.findViewById(R.id.bst_kelompok);
        TextView bst_subklp = (TextView) view.findViewById(R.id.bst_subklp);
        TextView bst_penjualan = (TextView) view.findViewById(R.id.bst_penjualan);
        TextView bst_stock = (TextView) view.findViewById(R.id.bst_stock);
        TextView bst_season = (TextView) view.findViewById(R.id.bst_season);
        TextView bst_bdetail = (TextView) view.findViewById(R.id.bst_bdetail);
        //
        bst_artikel.setTag(position);
        bst_dep.setTag(position);
        bst_kelompok.setTag(position);
        bst_subklp.setTag(position);
        bst_penjualan.setTag(position);
        bst_stock.setTag(position);
        bst_season.setTag(position);
        bst_bdetail.setTag(position);
        //
        bst_artikel.setText(artikel.get(position));
        bst_dep.setText(departemen.get(position));
        bst_kelompok.setText(kelompok.get(position));
        bst_subklp.setText(subkelompok.get(position));
        bst_penjualan.setText(penjualan.get(position));
        bst_stock.setText(stock.get(position));
        bst_season.setText(season.get(position));
        bst_bdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewClickListener.onItemCLick(position);
            }
        });
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listview_card_adapter_anim);
        view.startAnimation(animation);
        return view;
    }

    public boolean isEmpty(){
        return artikel.isEmpty();
    }

    public Object getItem(int position) {
        return artikel.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public String getKodeart(int pos) {
        return kodeart.get(pos);
    }

}
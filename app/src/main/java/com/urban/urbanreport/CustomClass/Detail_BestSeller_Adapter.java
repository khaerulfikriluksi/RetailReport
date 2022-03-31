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

public class Detail_BestSeller_Adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> nmbrg;
    private ArrayList<String> artikel;
    private ArrayList<String> season;
    private ArrayList<String> departemen;
    private ArrayList<String> kelompok;
    private ArrayList<String> subklp;
    private ArrayList<String> penjualan;
    private ArrayList<String> stock;
    private ArrayList<String> urlgambar;

    public Detail_BestSeller_Adapter(Context mContext, ArrayList<String> nmbrg, ArrayList<String> artikel, ArrayList<String> season, ArrayList<String> departemen, ArrayList<String> kelompok, ArrayList<String> subklp, ArrayList<String> penjualan, ArrayList<String> stock, ArrayList<String> urlgambar) {
        this.mContext = mContext;
        this.nmbrg = nmbrg;
        this.artikel = artikel;
        this.season = season;
        this.departemen = departemen;
        this.kelompok = kelompok;
        this.subklp = subklp;
        this.penjualan = penjualan;
        this.stock = stock;
        this.urlgambar = urlgambar;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return artikel.size();
    }


    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_detail_bestseller, parent, false);
        //
        TextView bst_det_nmbrg = (TextView) view.findViewById(R.id.bst_det_nmbrg);
        TextView bst_det_art = (TextView) view.findViewById(R.id.bst_det_art);
        TextView bst_det_season = (TextView) view.findViewById(R.id.bst_det_season);
        TextView bst_det_departemen = (TextView) view.findViewById(R.id.bst_det_departemen);
        TextView bst_det_kelompok = (TextView) view.findViewById(R.id.bst_det_kelompok);
        TextView bst_det_subkelompok = (TextView) view.findViewById(R.id.bst_det_subkelompok);
        TextView bst_det_penjualan = (TextView) view.findViewById(R.id.bst_det_penjualan);
        TextView bst_det_stock = (TextView) view.findViewById(R.id.bst_det_stock);
        GlottieView bst_det_gambar = (GlottieView) view.findViewById(R.id.bst_det_gambar);
        //
        bst_det_nmbrg.setTag(position);
        bst_det_art.setTag(position);
        bst_det_season.setTag(position);
        bst_det_departemen.setTag(position);
        bst_det_kelompok.setTag(position);
        bst_det_subkelompok.setTag(position);
        bst_det_penjualan.setTag(position);
        bst_det_stock.setTag(position);
        bst_det_gambar.setTag(position);
        //
        bst_det_nmbrg.setText(nmbrg.get(position));
        bst_det_art.setText(artikel.get(position));
        bst_det_season.setText(season.get(position));
        bst_det_departemen.setText(departemen.get(position));
        bst_det_kelompok.setText(kelompok.get(position));
        bst_det_subkelompok.setText(subklp.get(position));
        bst_det_penjualan.setText(penjualan.get(position));
        bst_det_stock.setText(stock.get(position));
        Glide.with(mContext)
                .load(urlgambar.get(position))
//                .load("http://103.112.139.155/api-retail/imgprodukwarna/aaa12.webp")
                .placeholder(R.drawable.ico_clear)
                .error(R.drawable.no_image)
                .into(new GlottieViewTarget(bst_det_gambar));
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listview_card_adapter_anim);
//        view.startAnimation(animation);
        return view;
    }

    public String getImageurl (int pos){
        return urlgambar.get(pos);
    }

    public boolean isEmpty(){
        return nmbrg.isEmpty();
    }

    public Object getItem(int position) {
        return nmbrg.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

}
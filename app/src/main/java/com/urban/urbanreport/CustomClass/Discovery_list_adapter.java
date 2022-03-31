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

public class Discovery_list_adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> artikel;
    private ArrayList<String> season;
    private ArrayList<String> kelompok;
    private ArrayList<String> subklp;
    private ArrayList<String> departemen;
    private ArrayList<String> promosi;
    private ArrayList<String> jual;
    private ArrayList<String> bersih;
    private ArrayList<String> urlgambar;

    public Discovery_list_adapter(Context mContext, ArrayList<String> artikel, ArrayList<String> season, ArrayList<String> kelompok, ArrayList<String> subklp, ArrayList<String> departemen, ArrayList<String> promosi, ArrayList<String> jual, ArrayList<String> bersih, ArrayList<String> urlgambar) {
        this.mContext = mContext;
        this.artikel = artikel;
        this.season = season;
        this.kelompok = kelompok;
        this.subklp = subklp;
        this.departemen = departemen;
        this.promosi = promosi;
        this.jual = jual;
        this.bersih = bersih;
        this.urlgambar = urlgambar;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return artikel.size();
    }


    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_fitems_discovery, parent, false);
        //
        TextView dsc_art = (TextView) view.findViewById(R.id.dsc_art);
        TextView dsc_season = (TextView) view.findViewById(R.id.dsc_season);
        TextView dsc_klp = (TextView) view.findViewById(R.id.dsc_klp);
        TextView dsc_subklp = (TextView) view.findViewById(R.id.dsc_subklp);
        TextView dsc_departemen = (TextView) view.findViewById(R.id.dsc_departemen);
        TextView dsc_promosi = (TextView) view.findViewById(R.id.dsc_promosi);
        TextView dsc_hrgjual = (TextView) view.findViewById(R.id.dsc_hrgjual);
        TextView dsc_hrgnet = (TextView) view.findViewById(R.id.dsc_hrgnet);
        GlottieView dsc_gambar = (GlottieView) view.findViewById(R.id.dsc_gambar);
        //
        dsc_art.setTag(position);
        dsc_season.setTag(position);
        dsc_klp.setTag(position);
        dsc_subklp.setTag(position);
        dsc_departemen.setTag(position);
        dsc_promosi.setTag(position);
        dsc_hrgjual.setTag(position);
        dsc_hrgnet.setTag(position);
        dsc_gambar.setTag(position);
        //
        dsc_art.setText(artikel.get(position));
        dsc_season.setText(season.get(position));
        dsc_klp.setText(kelompok.get(position));
        dsc_subklp.setText(subklp.get(position));
        dsc_departemen.setText(departemen.get(position));
        dsc_promosi.setText(promosi.get(position));
        dsc_hrgjual.setText(jual.get(position));
        dsc_hrgnet.setText(bersih.get(position));
        Glide.with(mContext)
                .load(urlgambar.get(position))
//                .load("http://103.112.139.155/api-retail/imgprodukwarna/aaa12.webp")
                .placeholder(R.drawable.ico_clear)
                .error(R.drawable.no_image)
                .into(new GlottieViewTarget(dsc_gambar));
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listview_card_adapter_anim);
        view.startAnimation(animation);
        return view;
    }

    public String getImageurl (int pos){
        return urlgambar.get(pos);
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

}

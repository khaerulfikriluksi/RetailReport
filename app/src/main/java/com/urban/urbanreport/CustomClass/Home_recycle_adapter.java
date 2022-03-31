package com.urban.urbanreport.CustomClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.clzola.glottie.GlottieView;
import com.clzola.glottie.GlottieViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.urban.urbanreport.FItemsDiscovery;
import com.urban.urbanreport.Fimage_popup;
import com.urban.urbanreport.R;

import java.io.InputStream;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class Home_recycle_adapter extends RecyclerView.Adapter<Home_recycle_adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> itemcode=new ArrayList<>();
    private ArrayList<String> itemname=new ArrayList<>();
    private ArrayList<String> itemqty=new ArrayList<>();
    private ArrayList<String> urlgambar=new ArrayList<>();
    private ArrayList<String> stock=new ArrayList<>();

    public Home_recycle_adapter(Context mContext, ArrayList<String> itemcode, ArrayList<String> itemname, ArrayList<String> itemqty, ArrayList<String> urlgambar, ArrayList<String> stock) {
        this.mContext = mContext;
        this.itemcode = itemcode;
        this.itemname = itemname;
        this.itemqty = itemqty;
        this.urlgambar = urlgambar;
        this.stock = stock;
    }

    public void replacedata(Context mContext, ArrayList<String> itemcode, ArrayList<String> itemname, ArrayList<String> itemqty, ArrayList<String> urlgambar, ArrayList<String> stock) {
        this.mContext = mContext;
        this.itemcode.clear();
        this.itemname.clear();
        this.itemqty.clear();
        this.urlgambar.clear();
        this.stock.clear();
        notifyDataSetChanged();
        this.itemcode = itemcode;
        this.itemname = itemname;
        this.itemqty = itemqty;
        this.urlgambar = urlgambar;
        this.stock = stock;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recycleview_fhome, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        Log.v(TAG,"onBindViewHolder Terpanggil");
        Glide.with(mContext)
                .load(urlgambar.get(position))
//                .load("http://103.112.139.155/api-retail/imgprodukwarna/aaa12.webp")
                .placeholder(R.drawable.ico_clear)
                .error(R.drawable.no_image)
                .into(new GlottieViewTarget(holder.hm_list_gambar));
        holder.hm_list_itemcode.setText(itemcode.get(position));
        holder.hm_list_itemname.setText(itemname.get(position));
        holder.hm_list_itemqty.setText(itemqty.get(position));
        holder.hm_list_stock.setText(stock.get(position));
        holder.CardRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImagePopup imagePopup = new ImagePopup(mContext);
                if (urlgambar.get(position).contains("amogos.webp")) {
                    Toast.makeText(mContext,"No image avaible", Toast.LENGTH_LONG).show();
                } else {
                    Intent formku = new Intent(mContext, Fimage_popup.class);
                    formku.putExtra("url",urlgambar.get(position));
                    mContext.startActivity(formku);
//                    imagePopup.initiatePopupWithGlide(urlgambar.get(position));
//                    imagePopup.setBackgroundColor(Color.TRANSPARENT);
////                                            imagePopup.setImageOnClickClose(true);
////                                            imagePopup.setFullScreen(true);
//                    imagePopup.setHideCloseIcon(true);
//                    imagePopup.viewPopup();
//                    imagePopup.bringToFront();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemcode.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        GlottieView hm_list_gambar;
        CardView CardRecycle;
        TextView hm_list_itemcode,hm_list_itemname,hm_list_itemqty,hm_list_stock;

        public ViewHolder (View view) {
            super(view);
            CardRecycle = (CardView) view.findViewById(R.id.CardRecycle);
            hm_list_gambar=(GlottieView) view.findViewById(R.id.hm_list_gambar);
            hm_list_itemcode=(TextView) view.findViewById(R.id.hm_list_itemcode);
            hm_list_itemname=(TextView) view.findViewById(R.id.hm_list_itemname);
            hm_list_itemqty=(TextView) view.findViewById(R.id.hm_list_itemqty);
            hm_list_stock=(TextView) view.findViewById(R.id.hm_list_stock);
        }
    }
}

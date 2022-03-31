package com.urban.urbanreport.CustomClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urban.urbanreport.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Detail_Sales_Adapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<String> kd_departemen;
    private ArrayList<String> departemen;
    private ArrayList<String> daily;
    private ArrayList<String> daily_sum;
    private ArrayList<String> daily_qty;
    private ArrayList<String> monthly;
    private ArrayList<String> monthly_sum;
    private ArrayList<String> monthly_qty;
    private SQLiteDatabase db;

    public Detail_Sales_Adapter(Context mContext, ArrayList<String> kd_departemen ,ArrayList<String> departemen, ArrayList<String> daily, ArrayList<String> daily_sum, ArrayList<String> daily_qty, ArrayList<String> monthly, ArrayList<String> monthly_sum, ArrayList<String> monthly_qty) {
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.kd_departemen = kd_departemen;
        this.departemen = departemen;
        this.daily = daily;
        this.daily_sum = daily_sum;
        this.daily_qty = daily_qty;
        this.monthly = monthly;
        this.monthly_sum = monthly_sum;
        this.monthly_qty = monthly_qty;
    }

    public int getCount() {
        return departemen.size();
    }

    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_sales_detail, parent, false);
        //
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        TextView detsls_departemen = (TextView) view.findViewById(R.id.detsls_departemen),
                detsls_Ldayly = (TextView) view.findViewById(R.id.detsls_Ldayly),
                detsls_Ldayly_sum = (TextView) view.findViewById(R.id.detsls_Ldayly_sum),
                detsls_Ldayly_qty = (TextView) view.findViewById(R.id.detsls_Ldayly_qty),
                detsls_Lmonthly = (TextView) view.findViewById(R.id.detsls_Lmonthly),
                detsls_Lmonthly_sum = (TextView) view.findViewById(R.id.detsls_Lmonthly_sum),
                detsls_Lmonthly_qty = (TextView) view.findViewById(R.id.detsls_Lmonthly_qty),
                detsls_bestlabel = (TextView) view.findViewById(R.id.detsls_bestlabel);
        RecyclerView detsls_recycle = (RecyclerView) view.findViewById(R.id.detsls_recycle);
        //
        detsls_departemen.setTag(position);
        detsls_Ldayly.setTag(position);
        detsls_Ldayly_sum.setTag(position);
        detsls_Ldayly_qty.setTag(position);
        detsls_Lmonthly.setTag(position);
        detsls_Lmonthly_sum.setTag(position);
        detsls_Lmonthly_qty.setTag(position);
        detsls_bestlabel.setTag(position);
        detsls_recycle.setTag(position);
        //
        detsls_departemen.setText(departemen.get(position));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(daily.get(position));
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM yyyy");
            detsls_Ldayly.setText(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        detsls_Ldayly_sum.setText("Rp. "+currency(daily_sum.get(position)));
        detsls_Ldayly_qty.setText(currency(daily_qty.get(position)));
        detsls_Lmonthly.setText(monthly.get(position));
        detsls_Lmonthly_sum.setText("Rp. "+currency(monthly_sum.get(position)));
        detsls_Lmonthly_qty.setText(currency(monthly_qty.get(position)));
        detsls_bestlabel.setText("Best Seller "+monthly.get(position));
        detsls_recycle.setLayoutManager(layoutManager);
        ArrayList<String> itemcode = new ArrayList<>();
        ArrayList<String> itemname = new ArrayList<>();
        ArrayList<String> itemqty = new ArrayList<>();
        ArrayList<String> urlgambar = new ArrayList<>();
        ArrayList<String> stock = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM tbl_detail_bestseller WHERE `Kode_Departemen`='"+kd_departemen.get(position)+"' AND `Tanggal`='"+daily.get(position)+"'",null);
        if (cr.getCount()>0){
            if (cr.moveToFirst()) {
                do {
                    itemcode.add(cr.getString(2));
                    itemname.add(cr.getString(3));
                    itemqty.add(cr.getString(4));
                    urlgambar.add(cr.getString(5));
                    stock.add("");
                } while (cr.moveToNext());
            }
            Home_recycle_adapter recycle_adapter = new Home_recycle_adapter(mContext,itemcode,itemname,itemqty,urlgambar,stock,false);
            detsls_recycle.setAdapter(recycle_adapter);
        }
        return view;
    }

    private String currency(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }


    public boolean isEmpty(){
        return departemen.isEmpty();
    }

    public Object getItem(int position) {
        return departemen.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

}

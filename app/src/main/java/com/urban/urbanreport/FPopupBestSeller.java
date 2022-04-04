package com.urban.urbanreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.urban.urbanreport.CustomClass.Cache;
import com.urban.urbanreport.CustomClass.Home_recycle_adapter;

import java.util.ArrayList;

public class FPopupBestSeller extends AppCompatActivity {

    private ArrayList<String> itemcode=new ArrayList<>();
    private ArrayList<String> itemname=new ArrayList<>();
    private ArrayList<String> itemqty=new ArrayList<>();
    private ArrayList<String> urlgambar=new ArrayList<>();
    private ArrayList<String> stock=new ArrayList<>();
    //
    private ArrayList<String> itemcode_daily=new ArrayList<>();
    private ArrayList<String> itemname_daily=new ArrayList<>();
    private ArrayList<String> itemqty_daily=new ArrayList<>();
    private ArrayList<String> urlgambar_daily=new ArrayList<>();
    private ArrayList<String> stock_daily=new ArrayList<>();
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fpopup_best_seller);
        RecyclerView hm_recycle = (RecyclerView) findViewById(R.id.hm_recycle),
                hm_recycle2 = (RecyclerView) findViewById(R.id.hm_recycle2);
        RadioGroup hm_checker = (RadioGroup) findViewById(R.id.hm_checker);
        RadioButton hm_checker_monthly = (RadioButton) findViewById(R.id.hm_checker_monthly),
                hm_checker_daily = (RadioButton) findViewById(R.id.hm_checker_daily);
        TextView close = (TextView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        hm_checker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.hm_checker_daily) {
                    hm_recycle2.setVisibility(View.VISIBLE);
                    hm_recycle.setVisibility(View.GONE);
                } else {
                    hm_recycle2.setVisibility(View.GONE);
                    hm_recycle.setVisibility(View.VISIBLE);
                }
            }
        });
        itemcode_daily = Cache.getInstance().getArrayItemCode_daily();
        itemname_daily = Cache.getInstance().getArrayItemName_daily();
        itemqty_daily = Cache.getInstance().getArrayItemQty_daily();
        urlgambar_daily = Cache.getInstance().getArrayurlgambar_daily();
        stock_daily = Cache.getInstance().getArraystock_daily();
        //
        itemcode = Cache.getInstance().getArrayItemCode();
        itemname = Cache.getInstance().getArrayItemName();
        itemqty = Cache.getInstance().getArrayItemQty();
        urlgambar = Cache.getInstance().getArrayurlgambar();
        stock = Cache.getInstance().getArraystock();
        LinearLayoutManager layoutManager = new LinearLayoutManager(FPopupBestSeller.this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(FPopupBestSeller.this,LinearLayoutManager.HORIZONTAL,false);
        Home_recycle_adapter recycle_adapter2 = new Home_recycle_adapter(FPopupBestSeller.this,itemcode_daily,itemname_daily,itemqty_daily,urlgambar_daily,stock_daily,true);
        hm_recycle2.setLayoutManager(layoutManager2);
        hm_recycle2.setAdapter(recycle_adapter2);
        Home_recycle_adapter recycle_adapter = new Home_recycle_adapter(FPopupBestSeller.this,itemcode,itemname,itemqty,urlgambar,stock,true);
        hm_recycle.setLayoutManager(layoutManager);
        hm_recycle.setAdapter(recycle_adapter);
    }
}
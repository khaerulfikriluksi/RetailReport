package com.urban.urbanreport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.urban.urbanreport.CustomClass.Cache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FDiscovery_Summary extends AppCompatActivity {

    private LinearLayout dscsls_utama,dscsls_loading, dscsls_loading1;
    private ShimmerFrameLayout dscsls_shimer1,dscsls_shimer2,dscsls_shimer3,
            dscsls_shimer4,dscsls_shimer5,dscsls_shimer6,
            dscsls_shimer7,dscsls_shimer8, dscsls_shimer11,
            dscsls_shimer22,dscsls_shimer33;
    private Boolean run=false;
    private String API,tanggal, bulan, tanggalfull, bulanfull;
    private SQLiteDatabase db;
    private Context context;
    private ListView dsc_list_departemen;
    private TextView dscsls_lastupdate, dscsls_Ldayly, dscsls_lsumdaily,
            dscsls_Lmonthly, dscsls_lsummonthly, dscsls_Ldayly_footwear,
            dscsls_lsumdaily_footwear, dscsls_Lmonthly_footwear,dscsls_lsummonthly_footwear,
            dscsls_Ldayly_clothing, dscsls_lsumdaily_clothing, dscsls_Lmonthly_clothing,
            dsc_lsummonthly_clothing, dscsls_Ldayly_online, dscsls_lsumdaily_online,
            dscsls_Lmonthly_online, dsc_lsummonthly_online, dscsls_lsumdaily_qty,
            dscsls_lsummonthly_qty, dscsls_lsumdaily_footwear_qty, dscsls_lsummonthly_footwear_qty,
            dscsls_lsumdaily_clothing_qty, dsc_lsummonthly_clothing_qty, dscsls_lsumdaily_online_qty,
            dsc_lsummonthly_online_qty, dscsls_deptext;
    private Map<String, String> dataCache = new HashMap<String, String>();
    private SharedPreferences storageCache;
    private SharedPreferences.Editor CacheEditor;
    private FloatingActionButton dscsls_date;
    private SwipeRefreshLayout dscsls_swiperefresh;
    private int count = 0;
    private Toolbar dscsls_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fdiscovery_summary);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
        }
        initialize();
    }

    private void clickListenerComp(){
        dscsls_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insertDate = tanggal;
                String[] items1 = insertDate.split("-");
                String d1=items1[2];
                String m1=items1[1];
                String y1=items1[0];
                int datenow = Integer.parseInt(d1);
                int monthnow = Integer.parseInt(m1)-1;
                int yearnow = Integer.parseInt(y1);
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog= new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (run==false) {
                            calendar.set(year, month, dayOfMonth);
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
                            final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MMMM yyyy");
                            final SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("MMMM_yyyy");
                            tanggal = simpleDateFormat.format(calendar.getTime());
                            tanggalfull = simpleDateFormat2.format(calendar.getTime());
                            bulanfull = simpleDateFormat3.format(calendar.getTime());
                            bulan = simpleDateFormat4.format(calendar.getTime());
                            tanggal = simpleDateFormat.format(calendar.getTime());
                            if (storageCache.contains("date_" + tanggal)) {
                                setValueComp(tanggal, bulan, tanggalfull, bulanfull);
                                Toast.makeText(context,"Menampilkan dari penyimpanan internal",Toast.LENGTH_SHORT).show();
                            } else {
                                get1();
                            }
                        } else {
                            Toast.makeText(context,"Mohon tunggu sampai proses selesai",Toast.LENGTH_SHORT).show();
                        }
                    }
                },yearnow,monthnow,datenow);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
        dscsls_swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get1();
            }
        });
        dscsls_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setValueComp(String tgl, String bln, String tglfull, String blnfull){
        dscsls_lastupdate.setText(storageCache.getString("last_update_"+tgl,"Not yet updated"));
        dscsls_Ldayly.setText(storageCache.getString("date_"+tgl,tglfull));
        dscsls_lsumdaily.setText(storageCache.getString("daily_"+tgl,""));
        dscsls_lsumdaily_qty.setText(storageCache.getString("daily_qty_"+tgl,""));
        dscsls_Lmonthly.setText(storageCache.getString("month_"+bln,blnfull));
        dscsls_lsummonthly.setText(storageCache.getString("monthly_"+tgl,""));
        dscsls_lsummonthly_qty.setText(storageCache.getString("monthly_qty_"+tgl,""));
        //
        dscsls_Ldayly_footwear.setText(dscsls_Ldayly.getText().toString());
        dscsls_lsumdaily_footwear.setText(storageCache.getString("daily_footwear"+tgl,""));
        dscsls_lsumdaily_footwear_qty.setText(storageCache.getString("daily_footwear_qty_"+tgl,""));
        dscsls_Lmonthly_footwear.setText(dscsls_Lmonthly.getText().toString());
        dscsls_lsummonthly_footwear.setText(storageCache.getString("monthly_footwear_"+tgl,""));
        dscsls_lsummonthly_footwear_qty.setText(storageCache.getString("monthly_footwear_qty_"+tgl,""));
        //
        dscsls_Ldayly_clothing.setText(dscsls_Ldayly.getText().toString());
        dscsls_lsumdaily_clothing.setText(storageCache.getString("daily_clothing"+tgl,""));
        dscsls_lsumdaily_clothing_qty.setText(storageCache.getString("daily_clothing_qty_"+tgl,""));
        dscsls_Lmonthly_clothing.setText(dscsls_Lmonthly.getText().toString());
        dsc_lsummonthly_clothing.setText(storageCache.getString("monthly_clothing_"+tgl,""));
        dsc_lsummonthly_clothing_qty.setText(storageCache.getString("monthly_clothing_qty_"+tgl,""));
        //
        dscsls_Ldayly_online.setText(dscsls_Ldayly.getText().toString());
        dscsls_lsumdaily_online.setText(storageCache.getString("daily_online"+tgl,""));
        dscsls_lsumdaily_online_qty.setText(storageCache.getString("daily_online_qty_"+tgl,""));
        dscsls_Lmonthly_online.setText(dscsls_Lmonthly.getText().toString());
        dsc_lsummonthly_online.setText(storageCache.getString("monthly_online_"+tgl,""));
        dsc_lsummonthly_online_qty.setText(storageCache.getString("monthly_online_qty_"+tgl,""));
    }

    private void setValuedetail(String tanggal){
        ArrayList<String> Kode_Departemen = new ArrayList<>();
        ArrayList<String> Departemen = new ArrayList<>();
        ArrayList<String> Qty_Daily = new ArrayList<>();
        ArrayList<String> Total_Daily = new ArrayList<>();
        ArrayList<String> Qty_Bulan = new ArrayList<>();
        ArrayList<String> Total_Bulan = new ArrayList<>();
        ArrayList<String> Bulan = new ArrayList<>();
        ArrayList<String> Tanggal = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM tbl_detail_sales WHERE Tanggal='"+tanggal+"'",null);
        if (cr.getCount()>0){
            if (cr.moveToFirst()) {
                do {
                    Kode_Departemen.add(cr.getString(1));
                    Departemen.add(cr.getString(1));
                    Qty_Daily.add(cr.getString(1));
                    Total_Daily.add(cr.getString(1));
                    Qty_Bulan.add(cr.getString(1));
                    Total_Bulan.add(cr.getString(1));
                    Bulan.add(cr.getString(1));
                    Tanggal.add(cr.getString(1));
                } while (cr.moveToNext());
            }
        }
    }

    private void initializeComp(){
        dscsls_utama = (LinearLayout) findViewById(R.id.dscsls_utama);
        dscsls_loading = (LinearLayout) findViewById(R.id.dscsls_loading);
        dscsls_loading1 = (LinearLayout) findViewById(R.id.dscsls_loading1);
        dscsls_shimer1 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer1);
        dscsls_shimer2 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer2);
        dscsls_shimer3 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer3);
        dscsls_shimer4 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer4);
        dscsls_shimer5 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer5);
        dscsls_shimer6 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer6);
        dscsls_shimer7 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer7);
        dscsls_shimer8 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer8);
        dscsls_shimer11 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer11);
        dscsls_shimer22 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer22);
        dscsls_shimer33 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer33);
        dscsls_deptext = (TextView) findViewById(R.id.dscsls_deptext);
        dscsls_lastupdate = (TextView) findViewById(R.id.dscsls_lastupdate);
        dscsls_Ldayly = (TextView) findViewById(R.id.dscsls_Ldayly);
        dscsls_lsumdaily = (TextView) findViewById(R.id.dscsls_lsumdaily);
        dscsls_Lmonthly = (TextView) findViewById(R.id.dscsls_Lmonthly);
        dscsls_lsummonthly = (TextView) findViewById(R.id.dscsls_lsummonthly);
        dscsls_Ldayly_footwear = (TextView) findViewById(R.id.dscsls_Ldayly_footwear);
        dscsls_lsumdaily_footwear = (TextView) findViewById(R.id.dscsls_lsumdaily_footwear);
        dscsls_Lmonthly_footwear = (TextView) findViewById(R.id.dscsls_Lmonthly_footwear);
        dscsls_lsummonthly_footwear = (TextView) findViewById(R.id.dscsls_lsummonthly_footwear);
        dscsls_Ldayly_clothing = (TextView) findViewById(R.id.dscsls_Ldayly_clothing);
        dscsls_lsumdaily_clothing = (TextView) findViewById(R.id.dscsls_lsumdaily_clothing);
        dscsls_Lmonthly_clothing = (TextView) findViewById(R.id.dscsls_Lmonthly_clothing);
        dsc_lsummonthly_clothing = (TextView) findViewById(R.id.dsc_lsummonthly_clothing);
        dscsls_Ldayly_online = (TextView) findViewById(R.id.dscsls_Ldayly_online);
        dscsls_lsumdaily_online = (TextView) findViewById(R.id.dscsls_lsumdaily_online);
        dscsls_Lmonthly_online = (TextView) findViewById(R.id.dscsls_Lmonthly_online);
        dsc_lsummonthly_online = (TextView) findViewById(R.id.dsc_lsummonthly_online);
        dscsls_date = (FloatingActionButton) findViewById(R.id.dscsls_date);
        dscsls_swiperefresh = (SwipeRefreshLayout) findViewById(R.id.dscsls_swiperefresh);
        dscsls_toolbar = (Toolbar) findViewById(R.id.dscsls_toolbar);
        setSupportActionBar(dscsls_toolbar);
        dscsls_lsumdaily_qty = (TextView) findViewById(R.id.dscsls_lsumdaily_qty);
        dscsls_lsummonthly_qty = (TextView) findViewById(R.id.dscsls_lsummonthly_qty);
        dscsls_lsumdaily_footwear_qty = (TextView) findViewById(R.id.dscsls_lsumdaily_footwear_qty);
        dscsls_lsummonthly_footwear_qty = (TextView) findViewById(R.id.dscsls_lsummonthly_footwear_qty);
        dscsls_lsumdaily_clothing_qty = (TextView) findViewById(R.id.dscsls_lsumdaily_clothing_qty);
        dsc_lsummonthly_clothing_qty = (TextView) findViewById(R.id.dsc_lsummonthly_clothing_qty);
        dscsls_lsumdaily_online_qty = (TextView) findViewById(R.id.dscsls_lsumdaily_online_qty);
        dsc_lsummonthly_online_qty = (TextView) findViewById(R.id.dsc_lsummonthly_online_qty);
    }

    private void shimerdetail(Boolean toogle) {
        if (toogle==true) {
            dsc_list_departemen.setVisibility(View.GONE);
            dscsls_loading1.setVisibility(View.VISIBLE);
            dscsls_shimer11.startShimmerAnimation();
            dscsls_shimer22.startShimmerAnimation();
            dscsls_shimer33.startShimmerAnimation();
        } else {
            dsc_list_departemen.setVisibility(View.VISIBLE);
            dscsls_loading1.setVisibility(View.GONE);
            dscsls_shimer11.stopShimmerAnimation();
            dscsls_shimer22.stopShimmerAnimation();
            dscsls_shimer33.stopShimmerAnimation();
        }
    }

    private void startShimer(Boolean toogle){
        if (toogle==true){
            dscsls_utama.setVisibility(View.GONE);
            dscsls_loading.setVisibility(View.VISIBLE);
            dscsls_shimer1.startShimmerAnimation();
            dscsls_shimer2.startShimmerAnimation();
            dscsls_shimer3.startShimmerAnimation();
            dscsls_shimer4.startShimmerAnimation();
            dscsls_shimer5.startShimmerAnimation();
            dscsls_shimer6.startShimmerAnimation();
            dscsls_shimer7.startShimmerAnimation();
            dscsls_shimer8.startShimmerAnimation();
        } else {
            dscsls_shimer1.stopShimmerAnimation();
            dscsls_shimer2.stopShimmerAnimation();
            dscsls_shimer3.stopShimmerAnimation();
            dscsls_shimer4.stopShimmerAnimation();
            dscsls_shimer5.stopShimmerAnimation();
            dscsls_shimer6.stopShimmerAnimation();
            dscsls_shimer7.stopShimmerAnimation();
            dscsls_shimer8.stopShimmerAnimation();
            dscsls_loading.setVisibility(View.GONE);
            dscsls_utama.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        count++;
        if (count ==1) {
            CoordinatorLayout bg;
            bg=(CoordinatorLayout) findViewById(R.id.dscsls_parent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int cx = bg.getWidth();
                int cy = 0;
                float finalRadius = Math.max(bg.getWidth(), bg.getHeight());
                Animator circularReveal = ViewAnimationUtils.createCircularReveal(bg, cx, cy, finalRadius, 0);
                circularReveal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        bg.setVisibility(View.INVISIBLE);
//                        Intent formku = new Intent(FDiscovery_Summary.this, FDashboardMenu.class);
//                        startActivity(formku);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                circularReveal.setDuration(500);
                circularReveal.start();
            } else {
//                Intent formku = new Intent(FDiscovery_Summary.this, FDashboardMenu.class);
//                startActivity(formku);
                finish();
            }
        } else {
            //            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count = 0;
                }
            }, 2000);
        }
    }

    private void initialize(){
        storageCache = getSharedPreferences("UrbanReport", MODE_PRIVATE);
        CacheEditor = storageCache.edit();
        context=FDiscovery_Summary.this;
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
        final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MMMM yyyy");
        final SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("MMMM_yyyy");
        tanggal=simpleDateFormat.format(calendar.getTime());
        tanggalfull=simpleDateFormat2.format(calendar.getTime());
        bulanfull=simpleDateFormat3.format(calendar.getTime());
        bulan=simpleDateFormat4.format(calendar.getTime());
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_api", null);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            API=cursor.getString(1)+"/api/sales/summary-filter?";
            initializeComp();
            setValueComp(tanggal,bulan,tanggalfull,bulanfull);
            clickListenerComp();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Application Error");
            builder.setCancelable(false);
            builder.setMessage("API tidak ada, silahkan kontak developer...");
            builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    System.exit(0);
                }
            });
            builder.show();
        }
    }

    private void remoceCacheStorage(String tgl, String bln){
        CacheEditor.remove("last_update_"+tgl);
        CacheEditor.remove("date_"+tgl);
        CacheEditor.remove("daily_"+tgl);
        CacheEditor.remove("month_"+bln);
        CacheEditor.remove("monthly_"+tgl);
        CacheEditor.remove("daily_footwear"+tgl);
        CacheEditor.remove("monthly_footwear_"+tgl);
        CacheEditor.remove("daily_clothing"+tgl);
        CacheEditor.remove("monthly_clothing_"+tgl);
        CacheEditor.remove("daily_online"+tgl);
        CacheEditor.remove("monthly_online_"+tgl);
        CacheEditor.remove("daily_qty_"+tgl);
        CacheEditor.remove("monthly_qty_"+tgl);
        CacheEditor.remove("daily_footwear_qty_"+tgl);
        CacheEditor.remove("monthly_footwear_qty_"+tgl);
        CacheEditor.remove("daily_clothing_qty_"+tgl);
        CacheEditor.remove("monthly_clothing_qty_"+tgl);
        CacheEditor.remove("daily_online_qty_"+tgl);
        CacheEditor.remove("monthly_online_qty_"+tgl);
        CacheEditor.commit();
    }

    private void saveCacheStorage(String tgl,String bln,int key, String value){
        if (key==1) {
            CacheEditor.putString("last_update_"+tgl,"Last Update "+value);
        } else if (key==2) {
            CacheEditor.putString("date_"+tgl,value);
        } else if (key==3) {
            CacheEditor.putString("daily_"+tgl,value);
        } else if (key==4) {
            CacheEditor.putString("month_"+bln,value);
        } else if (key==5) {
            CacheEditor.putString("monthly_"+tgl,value);
        } else if (key==6) {
            CacheEditor.putString("daily_footwear"+tgl,value);
        } else if (key==7) {
            CacheEditor.putString("monthly_footwear_"+tgl,value);
        } else if (key==8) {
            CacheEditor.putString("daily_clothing"+tgl,value);
        } else if (key==9) {
            CacheEditor.putString("monthly_clothing_"+tgl,value);
        } else if (key==10) {
            CacheEditor.putString("daily_online"+tgl,value);
        } else if (key==11) {
            CacheEditor.putString("monthly_online_"+tgl,value);
        } else if (key==12) {
            CacheEditor.putString("daily_qty_"+tgl,value);
        } else if (key==13) {
            CacheEditor.putString("monthly_qty_"+tgl,value);
        } else if (key==14) {
            CacheEditor.putString("daily_footwear_qty_"+tgl,value);
        } else if (key==15) {
            CacheEditor.putString("monthly_footwear_qty_"+tgl,value);
        } else if (key==16) {
            CacheEditor.putString("daily_clothing_qty_"+tgl,value);
        } else if (key==17) {
            CacheEditor.putString("monthly_clothing_qty_"+tgl,value);
        } else if (key==18) {
            CacheEditor.putString("daily_online_qty_"+tgl,value);
        } else if (key==19) {
            CacheEditor.putString("monthly_online_qty_"+tgl,value);
        }
        CacheEditor.commit();
    }

    private void get1() {
        if (run==false) {
            final Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
            String waktu = simpleDateFormat.format(calendar.getTime());
            dscsls_lastupdate.setText("Updating");
            startShimer(true);
            dscsls_swiperefresh.setRefreshing(true);
            run = true;
            String Filter = API+"date="+tanggal;
            Log.v("API", Filter);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Filter,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dscsls_swiperefresh.setRefreshing(false);
                            startShimer(false);
                            run = false;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int stat = jsonObject.getInt("status");
                                if (stat == 200) {
                                    remoceCacheStorage(tanggal,bulan);
                                    JSONObject object = jsonObject.getJSONObject("data");
                                    JSONObject objsummary = object.getJSONObject("result");
                                    saveCacheStorage(tanggal,bulan,1,waktu);
                                    saveCacheStorage(tanggal,bulan,2,objsummary.getString("tanggal"));
                                    saveCacheStorage(tanggal,bulan,3,"Rp. " + currency(objsummary.getString("daily")));
                                    saveCacheStorage(tanggal,bulan,12,currency(objsummary.getString("daily_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,4,objsummary.getString("bulan"));
                                    saveCacheStorage(tanggal,bulan,5,"Rp. " + currency(objsummary.getString("monthly")));
                                    saveCacheStorage(tanggal,bulan,13,currency(objsummary.getString("monthly_qty"))+" Pcs");
                                    //
                                    JSONObject row3 = object.getJSONObject("result_det_month");
                                    saveCacheStorage(tanggal,bulan,7,"Rp. " + currency(row3.getString("footwear_month")));
                                    saveCacheStorage(tanggal,bulan,15,currency(row3.getString("footwear_month_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,9,"Rp. " + currency(row3.getString("clothing_month")));
                                    saveCacheStorage(tanggal,bulan,17,currency(row3.getString("clothing_month_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,11,"Rp. " + currency(row3.getString("onlinestore_month")));
                                    saveCacheStorage(tanggal,bulan,19,currency(row3.getString("onlinestore_month_qty"))+" Pcs");
                                    //
                                    JSONObject row4 = object.getJSONObject("result_det_dayly");
                                    saveCacheStorage(tanggal,bulan,6,"Rp. " + currency(row4.getString("footwear_day")));
                                    saveCacheStorage(tanggal,bulan,14,currency(row4.getString("footwear_day_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,8,"Rp. " + currency(row4.getString("clothing_day")));
                                    saveCacheStorage(tanggal,bulan,16,currency(row4.getString("clothing_day_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,10,"Rp. " + currency(row4.getString("onlinestore_day")));
                                    saveCacheStorage(tanggal,bulan,18,currency(row4.getString("onlinestore_day_qty"))+" Pcs");
                                    setValueComp(tanggal,bulan,tanggalfull,bulanfull);
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(context,"Server not responding Error 400", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            run = false;
                            startShimer(false);
                            dscsls_swiperefresh.setRefreshing(false);
                            if (error.getMessage()!=null) {
                                Log.v("error", error.getMessage());
                            }
                            if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                                Toast.makeText(context,"Lost connect from server", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(context,"Server not responding", Toast.LENGTH_LONG).show();
                            }  else if (error instanceof ParseError) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Application error");
                                builder.setCancelable(false);
                                builder.setMessage("Data Server error, Mohon kontak developer...");
                                builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        System.exit(0);
                                    }
                                });
                                builder.show();
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("API_KEY", "53713");
                    return headers;
                }

                @Override
                public Response<String> parseNetworkResponse(NetworkResponse response) {
                    String statusCode = String.valueOf(response.statusCode);
                    return super.parseNetworkResponse(response);
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(context,"Mohon tunggu sampai proses selesai",Toast.LENGTH_SHORT).show();
            dscsls_swiperefresh.setRefreshing(false);
        }
    }

    private String currency(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }
}
package com.urban.urbanreport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
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
import com.urban.urbanreport.CustomClass.Detail_Sales_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FSubDetailSummary extends AppCompatActivity {

    private ListView subdet_listview;
    private TextView subdet_Ldayly,subdet_lsumdaily,subdet_lsumdaily_qty,
            subdet_Lmonthly, subdet_lsummonthly, subdet_lsummonthly_qty;
    private Toolbar subdet_toolbar;
    private SQLiteDatabase db;
    private String Tanggal="2022-04-01",API,APIGAMBAR,title;
    private int count = 0;
    private LinearLayout subdet_layutama, subdet_layloading;
    private ShimmerFrameLayout dscsls_shimer1, dscsls_shimer2, dscsls_shimer3,
            dscsls_shimer4, dscsls_shimer5;
    private SwipeRefreshLayout subdet_swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fsub_detail_summary);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initialize();
        } else {
            Toast.makeText(FSubDetailSummary.this, "Failed to open activity, contact developer...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void onclick(){
        subdet_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        subdet_swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get2();
            }
        });
    }

    private void initialize(){
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        subdet_listview = (ListView) findViewById(R.id.subdet_listview);
        subdet_Ldayly = (TextView) findViewById(R.id.subdet_Ldayly);
        subdet_lsumdaily = (TextView) findViewById(R.id.subdet_lsumdaily);
        subdet_lsumdaily_qty =(TextView) findViewById(R.id.subdet_lsumdaily_qty);
        subdet_Lmonthly = (TextView) findViewById(R.id.subdet_Lmonthly);
        subdet_lsummonthly = (TextView) findViewById(R.id.subdet_lsummonthly);
        subdet_lsummonthly_qty = (TextView) findViewById(R.id.subdet_lsummonthly_qty);
        subdet_toolbar = (Toolbar) findViewById(R.id.subdet_toolbar);
        setSupportActionBar(subdet_toolbar);
        subdet_layutama = (LinearLayout) findViewById(R.id.subdet_layutama);
        subdet_layloading = (LinearLayout) findViewById(R.id.subdet_layloading);
        dscsls_shimer1 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer1);
        dscsls_shimer2 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer2);
        dscsls_shimer3 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer3);
        dscsls_shimer4 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer4);
        dscsls_shimer5 = (ShimmerFrameLayout) findViewById(R.id.dscsls_shimer5);
        subdet_swiperefresh = (SwipeRefreshLayout) findViewById(R.id.subdet_swiperefresh);
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_api", null);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            API=cursor.getString(1)+"/api/sales/sub-detail?";
            APIGAMBAR=cursor.getString(1)+"/product/";
            String daily = getIntent().getExtras().getString("daily");
            String sum_daily = getIntent().getExtras().getString("sum_daily");
            String daily_qty = getIntent().getExtras().getString("daily_qty");
            String monthly = getIntent().getExtras().getString("monthly");
            String sum_monthly = getIntent().getExtras().getString("sum_monthly");
            String monthly_qty = getIntent().getExtras().getString("monthly_qty");
            Tanggal = getIntent().getExtras().getString("tanggal");
            if (getIntent().getExtras().getString("title").contains("CLOTHING")) {
                setText(daily,sum_daily,daily_qty,monthly,sum_monthly,monthly_qty,"CLOTHING");
                title="CONCEPT STORE";
            } else {
                title = getIntent().getExtras().getString("title");
                setText(daily,sum_daily,daily_qty,monthly,sum_monthly,monthly_qty,title);
            }
            onclick();
            Cursor cr = db.rawQuery("SELECT * FROM tbl_subdetail_sales WHERE Tanggal='"+Tanggal+"' AND Jenis_Store='"+title+"'",null);
            if (cr.getCount()>0){
                setValuedetail(Tanggal,title);
            } else {
                Cursor crx = db.rawQuery("SELECT * FROM tbl_subdetail_sales_month WHERE Tanggal='"+Tanggal+"' AND Jenis_Store='"+title+"'",null);
                if (crx.getCount()>0) {
                    setValuedetail(Tanggal,title);
                } else {
                    get2();
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(FSubDetailSummary.this);
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

    private void setText(String daily,String sumdaily, String dailyqty,String monthly, String summonthly, String monthlyqty,String title){
        subdet_Ldayly.setText(daily);
        subdet_lsumdaily.setText(sumdaily);
        subdet_lsumdaily_qty.setText(dailyqty);
        subdet_Lmonthly.setText(monthly);
        subdet_lsummonthly.setText(summonthly);
        subdet_lsummonthly_qty.setText(monthlyqty);
        subdet_toolbar.setTitle(title);
    }

    private void setValuedetail(String tanggal,String store){
        ArrayList<String> Kode_Departemen = new ArrayList<>();
        ArrayList<String> Departemen = new ArrayList<>();
        ArrayList<String> Qty_Daily = new ArrayList<>();
        ArrayList<String> Total_Daily = new ArrayList<>();
        ArrayList<String> Qty_Bulan = new ArrayList<>();
        ArrayList<String> Total_Bulan = new ArrayList<>();
        ArrayList<String> Bulan = new ArrayList<>();
        ArrayList<String> Tanggal = new ArrayList<>();
        Log.v("OOKKK", store);
        Cursor cr = db.rawQuery("SELECT * FROM tbl_subdetail_sales WHERE Tanggal='"+tanggal+"' AND Jenis_Store='"+store+"'",null);
        if (cr.getCount()>0){
            if (cr.moveToFirst()) {
                do {
                    Kode_Departemen.add(cr.getString(2));
                    Departemen.add(cr.getString(3));
                    Qty_Daily.add(cr.getString(4));
                    Total_Daily.add(cr.getString(5));
                    Qty_Bulan.add(cr.getString(6));
                    Total_Bulan.add(cr.getString(7));
                    Bulan.add(cr.getString(8));
                    Tanggal.add(cr.getString(9));
                } while (cr.moveToNext());
            }
            Detail_Sales_Adapter adapter = new Detail_Sales_Adapter(FSubDetailSummary.this, Kode_Departemen,Departemen,Tanggal,Total_Daily,Qty_Daily,Bulan,Total_Bulan,Qty_Bulan,title,true);
            subdet_listview.setAdapter(adapter);
        } else {
            Cursor crx = db.rawQuery("SELECT * FROM tbl_subdetail_sales_month WHERE Tanggal='"+tanggal+"' AND Jenis_Store='"+store+"'",null);
            if (crx.getCount()>0) {
                if (crx.moveToFirst()) {
                    do {
                        Kode_Departemen.add("Kosong");
                        Departemen.add(crx.getString(2));
                        Qty_Daily.add("0 Pcs");
                        Total_Daily.add("Rp. 0");
                        Qty_Bulan.add(crx.getString(3));
                        Total_Bulan.add(crx.getString(4));
                        Bulan.add(crx.getString(5));
                        Tanggal.add(crx.getString(6));
                    } while (crx.moveToNext());
                }
                Detail_Sales_Adapter adapter = new Detail_Sales_Adapter(FSubDetailSummary.this, Kode_Departemen,Departemen,Tanggal,Total_Daily,Qty_Daily,Bulan,Total_Bulan,Qty_Bulan,title,true);
                subdet_listview.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onBackPressed() {
        count++;
        if (count ==1) {
            LinearLayout bg;
            bg=(LinearLayout) findViewById(R.id.subdet_parent);
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
                finish();
            }
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count = 0;
                }
            }, 2000);
        }
    }

    private void startshimer(Boolean a){
        if (a==true) {
            subdet_layloading.setVisibility(View.VISIBLE);
            subdet_layutama.setVisibility(View.GONE);
            dscsls_shimer1.startShimmerAnimation();
            dscsls_shimer2.startShimmerAnimation();
            dscsls_shimer3.startShimmerAnimation();
            dscsls_shimer4.startShimmerAnimation();
            dscsls_shimer5.startShimmerAnimation();
        } else {
            subdet_layloading.setVisibility(View.GONE);
            subdet_layutama.setVisibility(View.VISIBLE);
            dscsls_shimer1.stopShimmerAnimation();
            dscsls_shimer2.stopShimmerAnimation();
            dscsls_shimer3.stopShimmerAnimation();
            dscsls_shimer4.stopShimmerAnimation();
            dscsls_shimer5.stopShimmerAnimation();
        }
    }

    private void get2() {
        startshimer(true);
        subdet_swiperefresh.setRefreshing(true);
        String Filter = API+"date="+Tanggal;
        Log.v("API", Filter);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Filter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        startshimer(false);
                        subdet_swiperefresh.setRefreshing(false);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int stat = jsonObject.getInt("status");
                            if (stat==200) {
                                JSONObject object = jsonObject.getJSONObject("data");
                                JSONArray arr1 = object.getJSONArray("result_subdetail");
                                db.execSQL("DELETE FROM `tbl_subdetail_sales` WHERE `Tanggal`='"+Tanggal+"'");
                                db.execSQL("DELETE FROM `tbl_subdetail_sales_month` WHERE `Tanggal`='"+Tanggal+"'");
                                db.execSQL("DELETE FROM `tbl_subdetail_bestseller` WHERE `Tanggal`='"+Tanggal+"'");
                                for (int position = 0; position < arr1.length(); position++) {
                                    JSONObject row = arr1.getJSONObject(position);
                                    String jenis_store = row.getString("jenis_store");
                                    String Kode_Departemen = row.getString("kode_departemen");
                                    String Departemen = row.getString("departemen");
                                    String Qty_Daily = currency(row.getString("qty_daily"))+" Pcs";
                                    String Total_Daily = "Rp. "+currency(row.getString("Total_daily"));
                                    String Qty_Bulan = currency(row.getString("qty_bln"))+" Pcs";
                                    String Total_Bulan = "Rp. "+currency(row.getString("Total_bulan"));
                                    String Bulan = row.getString("bulan");
                                    db.execSQL("INSERT INTO `tbl_subdetail_sales` (`Jenis_Store`,`Kode_Departemen`,`Departemen`,`Qty_Daily`,`Total_Daily`,`Qty_Bulan`,`Total_Bulan`,`Bulan`,`Tanggal`) VALUES " +
                                            "('"+jenis_store+"','"+Kode_Departemen+"','"+Departemen+"','"+Qty_Daily+"','"+Total_Daily+"','"+Qty_Bulan+"','"+Total_Bulan+"','"+Bulan+"','"+Tanggal+"')");
                                }
                                //
                                JSONArray arr2 = object.getJSONArray("result_bestseller");
                                for (int position = 0; position < arr2.length(); position++) {
                                    JSONObject row2 = arr2.getJSONObject(position);
                                    String jenis_store = row2.getString("jenis_store");
                                    String Kode_Departemen = row2.getString("Kode_Departemen");
                                    String Kode_Barang = row2.getString("Kode_Barang");
                                    String Nama_Barang = row2.getString("Nama_Barang");
                                    String qty = row2.getString("qty");
                                    String Foto = APIGAMBAR+row2.getString("foto");
                                    db.execSQL("INSERT INTO `tbl_subdetail_bestseller` (`Jenis_Store`,`Kode_Departemen`,`Kode_Barang`,`Nama_Barang`,`qty`,`Foto`,`Tanggal`) VALUES " +
                                            "('"+jenis_store+"','"+Kode_Departemen+"','"+Kode_Barang+"','"+Nama_Barang+"','"+qty+"','"+Foto+"','"+Tanggal+"')");
                                }
                                //
                                JSONArray arr3 = object.getJSONArray("result_subdetail_monthly");
                                for (int position = 0; position < arr3.length(); position++) {
                                    JSONObject row3 = arr3.getJSONObject(position);
                                    String jenis_store = row3.getString("jenis_store");
                                    String departemen = row3.getString("departemen");
                                    String Qty_Bulan = currency(row3.getString("qty_bln"))+" Pcs";
                                    String Total_Bulan = "Rp. "+currency(row3.getString("Total_bulan"));
                                    String Bulan = row3.getString("bulan");
                                    db.execSQL("INSERT INTO `tbl_subdetail_sales_month` (`Jenis_Store`,`Departemen`,`Qty_Bulan`,`Total_Bulan`,`Bulan`,`Tanggal`) VALUES " +
                                            "('"+jenis_store+"','"+departemen+"','"+Qty_Bulan+"','"+Total_Bulan+"','"+Bulan+"','"+Tanggal+"')");
                                }
                                setValuedetail(Tanggal, title);
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(FSubDetailSummary.this,message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(FSubDetailSummary.this,"Server not responding Error 400 : "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        startshimer(false);
                        subdet_swiperefresh.setRefreshing(false);
                        if (error.getMessage()!=null) {
                            Log.v("error", error.getMessage());
                        }
                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                            Toast.makeText(FSubDetailSummary.this,"Lost connect from server", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(FSubDetailSummary.this,"Server not responding", Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ParseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FSubDetailSummary.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(FSubDetailSummary.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private String currency(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }
}
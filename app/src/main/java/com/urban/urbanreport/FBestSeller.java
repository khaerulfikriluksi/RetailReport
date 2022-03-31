package com.urban.urbanreport;

import androidx.annotation.ArrayRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.urban.urbanreport.CustomClass.Artikel_list_adapter;
import com.urban.urbanreport.CustomClass.BestSeller_list_adapter;
import com.urban.urbanreport.CustomClass.Cache;
import com.urban.urbanreport.CustomClass.Discovery_list_adapter;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FBestSeller extends AppCompatActivity {

    private ArrayList<String> kodeart=new ArrayList<>();
    private ArrayList<String> artikel=new ArrayList<>();
    private ArrayList<String> departemen=new ArrayList<>();
    private ArrayList<String> kelompok=new ArrayList<>();
    private ArrayList<String> subkelompok=new ArrayList<>();
    private ArrayList<String> penjualan=new ArrayList<>();
    private ArrayList<String> stock=new ArrayList<>();
    private ArrayList<String> season=new ArrayList<>();
    //
    private ArrayList<String> store = new ArrayList<>();
    private ArrayList<String> brand = new ArrayList<>();
    private ArrayList<String> art = new ArrayList<>();
    private ArrayList<String> depart = new ArrayList<>();
    private ArrayList<String> cbalias=new ArrayList<>();
    private ArrayList<String> cbkode=new ArrayList<>();
    //
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private BestSeller_list_adapter adapter;
    private String storeselected="ALL";
    private String brandselected="ALL";
    private String artselected="";
    private String depselected="ALL";
    private String kodecabangselected="";
    private String DateFrom,DateTo;
    private Boolean methode=false;
    private String apiurl,api,apigambar;
    private SQLiteDatabase db;
    private Boolean running=false;
    private ListView bst_listview;
    private EditText bst_search;
    private LottieAnimationView bst_animsearch,bst_animnoresult, bst_loading_detail;
    private int count = 0, count2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbest_seller);
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursss = db.rawQuery("SELECT * FROM tbl_api", null);
        cursss.moveToFirst();
        if (cursss.getCount()>0) {
            api = cursss.getString(1);
            apigambar=cursss.getString(1)+"/product/";
            final Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-01");
            final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            DateFrom=simpleDateFormat.format(calendar.getTime());
            DateTo=simpleDateFormat2.format(calendar.getTime());

            bst_search=(EditText) findViewById(R.id.bst_search);
            bst_listview=(ListView) findViewById(R.id.bst_listview);
            bst_animnoresult=(LottieAnimationView) findViewById(R.id.bst_animnoresult);
            bst_animsearch=(LottieAnimationView) findViewById(R.id.bst_animsearch);
            bst_loading_detail = (LottieAnimationView) findViewById(R.id.bst_loading_detail);

            if(Cache.getInstance().isExistMap("kodeart_bst")) {
                Toast.makeText(FBestSeller.this, "Menampilkan data pencarian sebelumnya", Toast.LENGTH_LONG).show();
                bst_animnoresult.setVisibility(View.INVISIBLE);
                cbalias=Cache.getInstance().getMapArray("cbalias_bst");
                cbkode=Cache.getInstance().getMapArray("cbkode_bst");
                kodecabangselected=Cache.getInstance().getDataList("kodecabangselected_bst");
                DateFrom=Cache.getInstance().getDataList("DateFrom_bst");
                DateTo=Cache.getInstance().getDataList("DateTo_bst");
                storeselected=Cache.getInstance().getDataList("storeselected_bst");
                brandselected=Cache.getInstance().getDataList("brandselected_bst");
                artselected=Cache.getInstance().getDataList("artselected_bst");
                depselected=Cache.getInstance().getDataList("depselected_bst");
                kodeart=Cache.getInstance().getMapArray("kodeart_bst");
                artikel=Cache.getInstance().getMapArray("artikel_bst");
                departemen=Cache.getInstance().getMapArray("departemen_bst");
                kelompok=Cache.getInstance().getMapArray("kelompok_bst");
                subkelompok=Cache.getInstance().getMapArray("subkelompok_bst");
                penjualan=Cache.getInstance().getMapArray("penjualan_bst");
                stock=Cache.getInstance().getMapArray("stock_bst");
                season=Cache.getInstance().getMapArray("season_bst");
                adapter = new BestSeller_list_adapter(FBestSeller.this,kodeart,artikel,departemen,kelompok,subkelompok,penjualan,stock,season, new BestSeller_list_adapter.ViewClickListener() {
                    @Override
                    public void onItemCLick(int position) {
                        onClickItems(position);
                    }
                });
                bst_listview.setAdapter(adapter);
            }

            Cursor cursor1 = db.rawQuery("SELECT `jenis_store` from tbl_cabang GROUP BY `jenis_store`",null);
            store.add("ALL");
            if (cursor1.moveToFirst()) {
                do {
                    store.add(cursor1.getString(0));
                } while (cursor1.moveToNext());
            }
            //
            Cursor cursor2 = db.rawQuery("SELECT `brand_cabang` from tbl_cabang WHERE `jenis_cabang`='CABANG' GROUP BY `brand_cabang`",null);
            brand.add("ALL");
            if (cursor2.moveToFirst()) {
                do {
                    brand.add(cursor2.getString(0));
                } while (cursor2.moveToNext());
            }

            Cursor cur = db.rawQuery("select * from tbl_artikel",null);
            if (cur.getCount()>0){
                if (cur.moveToFirst()) {
                    do {
                        art.add(cur.getString(1));
                    } while (cur.moveToNext());
                }
            }

            Cursor curs = db.rawQuery("select * from tbl_departemen",null);
            depart.add("ALL");
            if (curs.getCount()>0){
                if (curs.moveToFirst()) {
                    do {
                        depart.add(curs.getString(1));
                    } while (curs.moveToNext());
                }
            }

            toolbar = findViewById(R.id.bst_toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(this.getResources().getColor(R.color.sls_header1));
                window.setNavigationBarColor(this.getResources().getColor(R.color.white));
            }

            bst_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()>0 && s.toString()!=null) {
                        ArrayList<String> kodeart1=new ArrayList<>();
                        ArrayList<String> artikel1=new ArrayList<>();
                        ArrayList<String> departemen1=new ArrayList<>();
                        ArrayList<String> kelompok1=new ArrayList<>();
                        ArrayList<String> subkelompok1=new ArrayList<>();
                        ArrayList<String> penjualan1=new ArrayList<>();
                        ArrayList<String> stock1=new ArrayList<>();
                        ArrayList<String> season1=new ArrayList<>();
                        for (int i=0;i < artikel.size();i++) {
                            if (artikel.get(i).toLowerCase().contains(s.toString().toLowerCase()) || departemen.get(i).toLowerCase().contains(s.toString().toLowerCase())) {
                                kodeart1.add(kodeart.get(i));
                                artikel1.add(artikel.get(i));
                                departemen1.add(departemen.get(i));
                                kelompok1.add(kelompok.get(i));
                                subkelompok1.add(subkelompok.get(i));
                                penjualan1.add(penjualan.get(i));
                                stock1.add(stock.get(i));
                                season1.add(season.get(i));
                            }
                        }
                        adapter = new BestSeller_list_adapter(FBestSeller.this,kodeart1,artikel1,departemen1,kelompok1,subkelompok1,penjualan1,stock1,season1, new BestSeller_list_adapter.ViewClickListener() {
                            @Override
                            public void onItemCLick(int position) {
                                onClickItems(position);
                            }
                        });
                        bst_listview.setAdapter(adapter);
                    } else {
                        adapter = new BestSeller_list_adapter(FBestSeller.this,kodeart,artikel,departemen,kelompok,subkelompok,penjualan,stock,season, new BestSeller_list_adapter.ViewClickListener() {
                            @Override
                            public void onItemCLick(int position) {
                                onClickItems(position);
                            }
                        });
                        bst_listview.setAdapter(adapter);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            floatingActionButton=(FloatingActionButton) findViewById(R.id.bst_get);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FBestSeller.this,R.style.AppBottomSheetDialogTheme);
                    View bottomsheetview = LayoutInflater.from(FBestSeller.this).inflate(
                            R.layout.cus_bottom_sheet_oulet,(LinearLayout) findViewById(R.id.cus_sls_bottomsheet));

                    RadioGroup sls_toggle = (RadioGroup) bottomsheetview.findViewById(R.id.sls_toggle);
                    TextInputLayout sls_spin_store_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_store_head);
                    TextInputLayout sls_spin_brand_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_brand_head);
                    TextInputLayout sls_spin_cabang_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_cabang_head);
                    TextInputLayout sls_spin_departemen_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_departemen_head);

                    AutoCompleteTextView sls_spin_store = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_store);
                    AutoCompleteTextView sls_spin_brand = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_brand);
                    AutoCompleteTextView sls_datefrom = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_datefrom);
                    AutoCompleteTextView sls_dateto = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_dateto);
                    AutoCompleteTextView sls_spin_departemen = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_departemen);
                    TextInputLayout sls_datefrom_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_datefrom_head);
                    TextInputLayout sls_dateto_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_dateto_head);
                    TextInputLayout sls_spin_artikel_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_artikel_head);
                    AutoCompleteTextView sls_spin_artikel = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_artikel);
                    RadioButton sls_daily = (RadioButton) bottomsheetview.findViewById(R.id.sls_daily);
                    RadioButton sls_monthly = (RadioButton) bottomsheetview.findViewById(R.id.sls_monthly);
                    Button sls_bsearch = (Button) bottomsheetview.findViewById(R.id.sls_bsearch);
                    sls_datefrom.setFocusable(true);
                    //
                    sls_spin_store.setText(storeselected);
                    sls_spin_brand.setText(brandselected);
                    sls_spin_artikel.setText(artselected);
                    sls_spin_departemen.setText(depselected);
                    //
                    sls_datefrom_head.setEndIconDrawable(R.drawable.ico_calendar_text);
                    sls_dateto_head.setEndIconDrawable(R.drawable.ico_calendar_text);
                    sls_datefrom_head.setHint("From Date");
                    //
                    sls_dateto_head.setVisibility(View.VISIBLE);
                    sls_spin_brand_head.setVisibility(View.VISIBLE);
                    sls_toggle.setVisibility(View.VISIBLE);
                    sls_spin_store_head.setVisibility(View.VISIBLE);
                    sls_spin_artikel_head.setVisibility(View.VISIBLE);
                    sls_spin_cabang_head.setVisibility(View.GONE);
                    sls_spin_departemen_head.setVisibility(View.VISIBLE);

                    //Adapter
                    ArrayAdapter<String> storeadapter = new ArrayAdapter<String>(FBestSeller.this,
                            R.layout.cus_dropdown_spiner, store);
                    sls_spin_store.setAdapter(storeadapter);
                    ArrayAdapter<String> brandadapter = new ArrayAdapter<String>(FBestSeller.this,
                            R.layout.cus_dropdown_spiner, brand);
                    sls_spin_brand.setAdapter(brandadapter);
                    ArrayAdapter<String> artadapter = new ArrayAdapter<>(FBestSeller.this,
                            R.layout.cus_dropdown_spiner,art);
                    sls_spin_artikel.setAdapter(artadapter);
                    ArrayAdapter<String> depadapter = new ArrayAdapter<>(FBestSeller.this,
                            R.layout.cus_dropdown_spiner,depart);
                    sls_spin_departemen.setAdapter(depadapter);
                    //ActionEvent
                    sls_datefrom_head.setEndIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sls_datefrom.callOnClick();
                        }
                    });
                    sls_dateto_head.setEndIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sls_dateto.callOnClick();
                        }
                    });
                    sls_spin_artikel.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length()>0 && s.toString()!=null) {
                                sls_spin_artikel_head.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                                sls_spin_artikel_head.setEndIconDrawable(R.drawable.ico_clear);
                            } else {
                                sls_spin_artikel_head.setEndIconMode(TextInputLayout.END_ICON_NONE);
                                sls_spin_artikel_head.setEndIconDrawable(R.drawable.ico_clear);
                            }
                            sls_spin_artikel_head.setEndIconOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sls_spin_artikel.setText("");
                                }
                            });
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    sls_daily.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar calendar = Calendar.getInstance();
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-01");
                            final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                            sls_datefrom.setText(simpleDateFormat.format(calendar.getTime()));
                            sls_dateto.setText(simpleDateFormat2.format(calendar.getTime()));
                        }
                    });
                    sls_monthly.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar calendar = Calendar.getInstance();
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-01-01");
                            final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-12-31");
                            int lastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            sls_datefrom.setText(simpleDateFormat.format(calendar.getTime()));
                            calendar.set(calendar.DATE, lastDate);
                            sls_dateto.setText(simpleDateFormat2.format(calendar.getTime()));
                        }
                    });
                    sls_dateto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sls_daily.isChecked()) {
                                String insertDate = sls_dateto.getText().toString();
                                String[] items1 = insertDate.split("-");
                                String d1=items1[2];
                                String m1=items1[1];
                                String y1=items1[0];
                                int datenow = Integer.parseInt(d1);
                                int monthnow = Integer.parseInt(m1)-1;
                                int yearnow = Integer.parseInt(y1);

                                final Calendar calendar = Calendar.getInstance();
                                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DatePickerDialog datePickerDialog= new DatePickerDialog(FBestSeller.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        calendar.set(year,month,dayOfMonth);
                                        sls_dateto.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                },yearnow,monthnow,datenow);
                                datePickerDialog.setTitle("To Date");
                                datePickerDialog.show();
                            } else
                            if (sls_monthly.isChecked()) {
                                String insertDate = sls_dateto.getText().toString();
                                String[] items1 = insertDate.split("-");
                                String d1=items1[2];
                                String m1=items1[1];
                                String y1=items1[0];
                                int datenow = Integer.parseInt(d1);
                                int monthnow = Integer.parseInt(m1)-1;
                                int yearnow = Integer.parseInt(y1);
                                final Calendar today = Calendar.getInstance();
                                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(FBestSeller.this,
                                        new MonthPickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(int selectedMonth, int selectedYear) { // on date set
                                                today.set(selectedYear,selectedMonth,01);
                                                Calendar to = Calendar.getInstance();
                                                to.set(selectedYear,selectedMonth,today.getActualMaximum(Calendar.DAY_OF_MONTH));
                                                sls_dateto.setText(simpleDateFormat.format(to.getTime()));
                                            }
                                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
                                builder
                                        .setMinYear(1990)
                                        .setActivatedMonth(monthnow)
                                        .setActivatedYear(yearnow)
                                        .setMaxYear(2070)
                                        .setTitle("To Month")
                                        .build().show();
                            }
                            DateTo=sls_dateto.getText().toString();
                        }
                    });

                    sls_datefrom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sls_daily.isChecked()) {
                                String insertDate = sls_datefrom.getText().toString();
                                String[] items1 = insertDate.split("-");
                                String d1=items1[2];
                                String m1=items1[1];
                                String y1=items1[0];
                                int datenow = Integer.parseInt(d1);
                                int monthnow = Integer.parseInt(m1)-1;
                                int yearnow = Integer.parseInt(y1);
                                final Calendar calendar = Calendar.getInstance();
                                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DatePickerDialog datePickerDialog= new DatePickerDialog(FBestSeller.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        calendar.set(year,month,dayOfMonth);
                                        sls_datefrom.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                },yearnow,monthnow,datenow);
                                datePickerDialog.setTitle("From Date");
                                datePickerDialog.show();
                            } else
                            if (sls_monthly.isChecked()) {
                                String insertDate = sls_datefrom.getText().toString();
                                String[] items1 = insertDate.split("-");
                                String d1=items1[2];
                                String m1=items1[1];
                                String y1=items1[0];
                                int datenow = Integer.parseInt(d1);
                                int monthnow = Integer.parseInt(m1)-1;
                                int yearnow = Integer.parseInt(y1);
                                final Calendar today = Calendar.getInstance();
                                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(FBestSeller.this,
                                        new MonthPickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(int selectedMonth, int selectedYear) { // on date set
                                                today.set(selectedYear,selectedMonth,01);
                                                sls_datefrom.setText(simpleDateFormat.format(today.getTime()));
                                            }
                                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
                                builder
                                        .setMinYear(1990)
                                        .setActivatedMonth(monthnow)
                                        .setActivatedYear(yearnow)
                                        .setMaxYear(2070)
                                        .setTitle("From Month")
                                        .build().show();
                            }
                            DateFrom=sls_datefrom.getText().toString();
                        }
                    });

                    sls_datefrom.setText(DateFrom);
                    sls_dateto.setText(DateTo);
                    if (methode==false) {
                        sls_daily.setChecked(true);
                        sls_monthly.setChecked(false);
                    } else {
                        sls_daily.setChecked(false);
                        sls_monthly.setChecked(true);
                    }

                    sls_bsearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> cb1 = new ArrayList<>();
                            String store, brand, artikelnama, departemennama;
                            if (!sls_spin_store.getText().toString().equals("ALL")) {
                                store = " = '" + sls_spin_store.getText().toString() + "'";
                            } else {
                                store = " like '%%'";
                            }
                            //
                            if (!sls_spin_brand.getText().toString().equals("ALL")) {
                                brand = " = '" + sls_spin_brand.getText().toString() + "'";
                            } else {
                                brand = " like '%%'";
                            }
                            //
                            cbkode.clear();
                            cbkode.add("All");
                            cbalias.clear();
                            cbalias.add("All");
                            Cursor cursor = db.rawQuery("SELECT * FROM tbl_cabang where `jenis_store` " + store + " and `brand_cabang` " + brand + " and `jenis_cabang` <> 'GUDANG';", null);
                            if (cursor.getCount() > 0) {
                                int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), sls_datefrom.getText().toString(), sls_dateto.getText().toString());
                                if (cursor.getCount()>=10 || dateDifference>=17) {
                                    Toast.makeText(FBestSeller.this, "Data besar, ini akan memakan waktu beberapa menit..", Toast.LENGTH_LONG).show();
                                }
                                if (cursor.moveToFirst()) {
                                    do {
                                        cb1.add(cursor.getString(0));
                                        cbalias.add(cursor.getString(2));
                                        cbkode.add(cursor.getString(0));
                                    } while (cursor.moveToNext());
                                }
                                String cb2 = cb1.toString();
                                String cb4 = cb2.replace("[", "");
                                String cb5 = cb4.replace("]", "");
                                String cb6 = cb5.replace(" ", "");
                                kodecabangselected = cb6;
                                if (sls_daily.isChecked()) {
                                    methode=false;
                                } else if (sls_monthly.isChecked()) {
                                    methode=true;
                                }
                                if ((sls_spin_artikel.getText().toString()!=null) && (sls_spin_artikel.getText().length()>0)){
                                    if (!sls_spin_artikel.getText().toString().equals("ALL")) {
                                        Cursor cur = db.rawQuery("select * from tbl_artikel where `nama`='"+sls_spin_artikel.getText().toString()+"'",null);
                                        if (cur.getCount()>0) {
                                            cur.moveToFirst();
                                            artikelnama=cur.getString(0);
                                        } else {
                                            artikelnama ="";
                                        }
                                    } else {
                                        artikelnama ="";
                                    }
                                } else {
                                    artikelnama ="";
                                }
                                if ((sls_spin_departemen.getText().toString()!=null) && (sls_spin_departemen.getText().length()>0)){
                                    Cursor curs = db.rawQuery("select * from tbl_departemen where `nama`='"+sls_spin_departemen.getText().toString()+"'",null);
                                    if (curs.getCount()>0) {
                                        curs.moveToFirst();
                                        departemennama=curs.getString(0);
                                    } else {
                                        departemennama ="";
                                    }
                                } else {
                                    departemennama ="";
                                }
                                apiurl = api + "/api/sales/best-seller?" +
                                        "cabang=" + cb6 + "&" +
                                        "datefrom=" + sls_datefrom.getText().toString() + "&" +
                                        "dateto=" + sls_dateto.getText().toString() + "&" +
                                        "kodeDepartement=" + departemennama + "&" +
                                        "merk="+artikelnama;
                                Log.v("API", apiurl);
                                DateTo=sls_dateto.getText().toString();
                                DateFrom=sls_datefrom.getText().toString();
                                storeselected=sls_spin_store.getText().toString();
                                brandselected=sls_spin_brand.getText().toString();
                                artselected=sls_spin_artikel.getText().toString();
                                depselected=sls_spin_departemen.getText().toString();
                                Cache.getInstance().putMapData("cbalias_bst",cbalias);
                                Cache.getInstance().putMapData("cbkode_bst",cbkode);
                                Cache.getInstance().setDataList("kodecabangselected_bst",kodecabangselected);
                                Cache.getInstance().setDataList("DateFrom_bst",DateFrom);
                                Cache.getInstance().setDataList("DateTo_bst",DateTo);
                                Cache.getInstance().setDataList("storeselected_bst",storeselected);
                                Cache.getInstance().setDataList("brandselected_bst",brandselected);
                                Cache.getInstance().setDataList("artselected_bst",artselected);
                                Cache.getInstance().setDataList("depselected_bst",depselected);
                                bottomSheetDialog.dismiss();
                                bst_search.setText("");
                                get();
                            } else {
                                Toast.makeText(FBestSeller.this, "Tidak ada cabang yang cocok", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    bottomSheetDialog.setContentView(bottomsheetview);
                    bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            BottomSheetDialog d = (BottomSheetDialog) dialog;
                            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
                            BottomSheetBehavior.from(bottomSheet)
                                    .setState(BottomSheetBehavior.STATE_EXPANDED);
                            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        }
                    });
                    bottomSheetDialog.show();
                }
            });
            if(kodeart.size()<=0) {
                floatingActionButton.callOnClick();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(FBestSeller.this);
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

    private static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void get() {
        kodeart.clear();
        artikel.clear();
        departemen.clear();
        kelompok.clear();
        subkelompok.clear();
        penjualan.clear();
        stock.clear();
        season.clear();
        bst_listview.setAdapter(null);
        bst_animnoresult.setVisibility(View.INVISIBLE);
        bst_animsearch.setVisibility(View.VISIBLE);
        Log.v("API",apiurl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiurl,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        bst_animsearch.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status==200) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int position = 0; position < jsonArray.length(); position++) {
                                    JSONObject row = jsonArray.getJSONObject(position);
                                    kodeart.add(row.getString("kode_merk"));
                                    artikel.add(row.getString("artikel"));
                                    departemen.add(row.getString("departemen"));
                                    kelompok.add(row.getString("kelompok"));
                                    subkelompok.add(row.getString("subkelompok"));
                                    penjualan.add("Rp. "+currency(row.getString("value"))+" ("+row.getString("total")+" Pcs)");
                                    stock.add(row.getString("stock")+" Pcs");
                                    season.add(row.getString("season").toUpperCase());
                                }
                                Cache.getInstance().putMapData("kodeart_bst",kodeart);
                                Cache.getInstance().putMapData("artikel_bst",artikel);
                                Cache.getInstance().putMapData("departemen_bst",departemen);
                                Cache.getInstance().putMapData("kelompok_bst",kelompok);
                                Cache.getInstance().putMapData("subkelompok_bst",subkelompok);
                                Cache.getInstance().putMapData("penjualan_bst",penjualan);
                                Cache.getInstance().putMapData("stock_bst",stock);
                                Cache.getInstance().putMapData("season_bst",season);
                                adapter = new BestSeller_list_adapter(FBestSeller.this, kodeart, artikel, departemen, kelompok, subkelompok, penjualan, stock, season, new BestSeller_list_adapter.ViewClickListener() {
                                    @Override
                                    public void onItemCLick(int position) {
                                        onClickItems(position);
                                    }
                                });
                                bst_listview.setAdapter(adapter);
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(FBestSeller.this,message, Toast.LENGTH_LONG).show();
                                bst_animnoresult.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.v("error",e.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(FBestSeller.this);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        bst_animsearch.setVisibility(View.INVISIBLE);
                        if (error.getMessage()!=null) {
                            Log.v("error", error.getMessage());
                        }
                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                            bst_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(FBestSeller.this,"Lost connect from server", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            bst_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(FBestSeller.this,"Server not responding", Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ParseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FBestSeller.this);
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
                })
        {
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
        RequestQueue requestQueue = Volley.newRequestQueue(FBestSeller.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDetail() {
        bst_loading_detail.setVisibility(View.VISIBLE);
        bst_listview.setEnabled(false);
        Log.v("API-DETAIL", apiurl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiurl,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        bst_loading_detail.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status == 200) {
                                db.execSQL("DROP TABLE IF EXISTS `tbl_bestseller_dump`;");
                                db.execSQL("CREATE TABLE `tbl_bestseller_dump` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `Kode_Cabang` varchar(255),`Nama_Barang` varchar(255),`artikel` varchar(255),`season` varchar(255),`departemen` varchar(255),`kelompok` varchar(255),`subkelompok` varchar(255),`qty` varchar(255),`value` varchar(255),`Stock` varchar(255),`gambar` varchar(255));");
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int position = 0; position < jsonArray.length(); position++) {
                                    JSONObject row = jsonArray.getJSONObject(position);
                                    db.execSQL("INSERT INTO `tbl_bestseller_dump` (`Kode_Cabang`,`Nama_Barang`,`artikel`,`season`,`departemen`,`kelompok`,`subkelompok`,`qty`,`value`,`Stock`,`gambar`) VALUES" +
                                            "('" + row.getString("Kode_Cabang") + "'," +
                                            "'[" + row.getString("Kode_Barang") + "] " + row.getString("Nama_Barang") + "'," +
                                            "'" + row.getString("artikel") + "'," +
                                            "'" + row.getString("season") + "'," +
                                            "'" + row.getString("departemen") + "'," +
                                            "'" + row.getString("kelompok") + "'," +
                                            "'" + row.getString("subkelompok") + "'," +
                                            "'" + row.getString("total") + "'," +
                                            "'" + row.getString("value") + "'," +
                                            "'" + row.getString("stock") + "'," +
                                            "'" + apigambar + row.getString("gambar") + "')");
                                }
                                Intent formku = new Intent(FBestSeller.this, FDetail_ViewPager.class);
                                formku.putStringArrayListExtra("arr", cbalias);
                                formku.putStringArrayListExtra("kode", cbkode);
                                formku.putExtra("menu", "BestSeller");
                                startActivity(formku);
                                bst_listview.setEnabled(true);
                                count2 = 0;
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(FBestSeller.this, message, Toast.LENGTH_LONG).show();
                                bst_listview.setEnabled(true);
                                count2 = 0;
                            }
                        } catch (JSONException e) {
                            Log.v("error", e.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(FBestSeller.this);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        count2 = 0;
                        bst_listview.setEnabled(true);
                        bst_loading_detail.setVisibility(View.INVISIBLE);
                        if (error.getMessage() != null) {
                            Log.v("error", error.getMessage());
                        }
                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                            Toast.makeText(FBestSeller.this, "Lost connect from server", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(FBestSeller.this, "Server not responding", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FBestSeller.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(FBestSeller.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void onClickItems(int pos) {
        if (bst_listview.isEnabled()) {
            bst_listview.setEnabled(false);
            count2++;
            if (count2 ==1) {
                Toast.makeText(FBestSeller.this, "Mohon menuggu, ini memakan waktu beberapa menit...", Toast.LENGTH_LONG).show();
                apiurl = api + "/api/sales/best-seller/detail?" +
                        "cabang=" + kodecabangselected + "&" +
                        "datefrom=" + DateFrom + "&" +
                        "dateto=" + DateTo + "&" +
                        "merk=" + adapter.getKodeart(pos);
                getDetail();
            }
        } else {
            Toast.makeText(FBestSeller.this, "Sedang mengambil data, mohon tunggu...", Toast.LENGTH_LONG).show();
        }
    }

    public String currency(String num) {
        double m = 0;
        try {
            m = Double.parseDouble(num);
        } catch (Exception e) {
            m=0;
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }

    @Override
    public void onBackPressed() {
        count++;
        if (count ==1) {
            CoordinatorLayout bg;
            bg=(CoordinatorLayout) findViewById(R.id.bst_parent);
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
                        Intent formku = new Intent(FBestSeller.this, FDashboardMenu.class);
                        startActivity(formku);
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
                Intent formku = new Intent(FBestSeller.this, FDashboardMenu.class);
                startActivity(formku);
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
}
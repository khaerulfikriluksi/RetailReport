package com.urban.urbanreport;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.animation.Animator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.SimpleAdapter;
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
import com.google.android.material.textfield.TextInputLayout;
import com.urban.urbanreport.CustomClass.Cache;
import com.urban.urbanreport.CustomClass.Home_recycle_adapter;
import com.urban.urbanreport.CustomClass.HttpHandler;
import com.urban.urbanreport.CustomClass.Outlet_list_adapter;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import toan.android.floatingactionmenu.FloatingActionButton;
import toan.android.floatingactionmenu.FloatingActionsMenu;

public class Foutlet extends AppCompatActivity {
    ArrayList<String> kodecabang= new ArrayList<>();
    ArrayList<String> cabang= new ArrayList<>();
    ArrayList<String> tanggal= new ArrayList<>();
    ArrayList<String> achievement= new ArrayList<>();
    ArrayList<Integer> nilai= new ArrayList<>();
    ArrayList<String> target= new ArrayList<>();
    ArrayList<String> sales= new ArrayList<>();
    ArrayList<String> exchange= new ArrayList<>();
    FloatingActionButton floatingActionButton, floatingfilter;
    FloatingActionsMenu floatingmenu;
    private boolean[] selectedfilter;
    private String[] filters = {"Kode Cabang A-Z","Kode Cabang Z-A","Achievement 9-0","Achievement 0-9"};
    LottieAnimationView sls_animsearch,sls_animnoresult;
    ListView sls_listview;
    EditText sls_search;
    Outlet_list_adapter adapter;
    String DateFrom,DateTo;
    String storeselected="ALL",brandselected="ALL",limitselected="Unlimited";
    Boolean methode=false;
    SQLiteDatabase db;
    String apiurl,api;
    ArrayList<String> store = new ArrayList<>();
    ArrayList<String> brand = new ArrayList<>();
    ArrayList<String> limitvalue = new ArrayList<>();
    private Toolbar toolbar;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foutlet);
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-01");
        final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        DateFrom=simpleDateFormat.format(calendar.getTime());
        DateTo=simpleDateFormat2.format(calendar.getTime());
        sls_search=(EditText) findViewById(R.id.sls_search);
        sls_listview=(ListView) findViewById(R.id.sls_listview);
        sls_animnoresult=(LottieAnimationView) findViewById(R.id.sls_animnoresult);
        sls_animsearch=(LottieAnimationView) findViewById(R.id.sls_animsearch);
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        selectedfilter = new boolean[filters.length];
        selectedfilter[2]=true;
        Cursor cursss = db.rawQuery("SELECT * FROM tbl_api", null);
        cursss.moveToFirst();
        if (cursss.getCount()>0) {
            api = cursss.getString(1);
            Cursor cursor1 = db.rawQuery("SELECT `jenis_store` from tbl_cabang GROUP BY `jenis_store`",null);
            store.add("ALL");
            if (cursor1.moveToFirst()) {
                do {
                    store.add(cursor1.getString(0));
                } while (cursor1.moveToNext());
            }
            //
            Cursor cursor2 = db.rawQuery("SELECT `brand_cabang` from tbl_cabang GROUP BY `brand_cabang`",null);
            brand.add("ALL");
            if (cursor2.moveToFirst()) {
                do {
                    brand.add(cursor2.getString(0));
                } while (cursor2.moveToNext());
            }

            toolbar = findViewById(R.id.sls_toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            limitvalue.add("Unlimited");
            limitvalue.add("5");
            limitvalue.add("10");
            limitvalue.add("20");
            limitvalue.add("50");
            limitvalue.add("100");
            limitvalue.add("500");
            limitvalue.add("1000");

            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(this.getResources().getColor(R.color.sls_header1));
                window.setNavigationBarColor(this.getResources().getColor(R.color.white));
            }

            sls_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()>0 && s.toString()!=null) {
                        ArrayList<String> kodecabang1= new ArrayList<>();
                        ArrayList<String> cabang1= new ArrayList<>();
                        ArrayList<String> tanggal1= new ArrayList<>();
                        ArrayList<String> achievement1= new ArrayList<>();
                        ArrayList<Integer> nilai1= new ArrayList<>();
                        ArrayList<String> target1= new ArrayList<>();
                        ArrayList<String> sales1= new ArrayList<>();
                        ArrayList<String> exchange1= new ArrayList<>();
                        for (int i=0;i < cabang.size();i++) {
                            if (cabang.get(i).toLowerCase().contains(s.toString().toLowerCase())) {
                                cabang1.add(cabang.get(i));
                                tanggal1.add(tanggal.get(i));
                                achievement1.add(achievement.get(i));
                                nilai1.add(nilai.get(i));
                                target1.add(target.get(i));
                                sales1.add(sales.get(i));
                                exchange1.add(exchange.get(i));
                            }
                        }
                        adapter = new Outlet_list_adapter(Foutlet.this,kodecabang1,cabang1,tanggal1,achievement1, nilai1,target1,sales1,exchange1);
                        sls_listview.setAdapter(adapter);
                    } else {
                        adapter = new Outlet_list_adapter(Foutlet.this,kodecabang,cabang,tanggal,achievement, nilai,target,sales,exchange);
                        sls_listview.setAdapter(adapter);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            floatingfilter=(FloatingActionButton) findViewById(R.id.sls_filter);
//            if (cabang.size()>0) {
//                floatingfilter.setVisibility(View.VISIBLE);
//            } else {
//                floatingfilter.setVisibility(View.GONE);
//            }
            floatingActionButton=(FloatingActionButton) findViewById(R.id.sls_get);
            floatingmenu = (FloatingActionsMenu) findViewById(R.id.sls_menu);
            floatingmenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
                @Override
                public void onMenuExpanded() {
//                    if (cabang.size()>0) {
//                        floatingfilter.setVisibility(View.VISIBLE);
//                    } else {
//                        floatingfilter.setVisibility(View.GONE);
//                    }
                }

                @Override
                public void onMenuCollapsed() {

                }
            });
            floatingfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingmenu.collapse();
                    if (achievement.size()>0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Foutlet.this);
                        builder.setTitle("Select Filter");
                        builder.setMultiChoiceItems(filters, selectedfilter, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                for (int n = 0; n < selectedfilter.length; n++) {
                                    if (n == i) {
                                        selectedfilter[n]=true;
                                        ((AlertDialog) dialogInterface).getListView().setItemChecked(i, true);
                                    }
                                    else {
                                        selectedfilter[n]=false;
                                        ((AlertDialog) dialogInterface).getListView().setItemChecked(i, false);
                                    }
                                }
                                if (i==0) {
                                    adapter.sortCabang(true);
                                } else if (i==1){
                                    adapter.sortCabang(false);
                                } else if (i==2){
                                    adapter.sortAchievement(false);
                                } else if (i==3){
                                    adapter.sortAchievement(true);
                                }
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(Foutlet.this, "No data to filter", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    floatingmenu.collapse();
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Foutlet.this,R.style.AppBottomSheetDialogTheme);
                    View bottomsheetview = LayoutInflater.from(Foutlet.this).inflate(
                            R.layout.cus_bottom_sheet_oulet,(LinearLayout) findViewById(R.id.cus_sls_bottomsheet));

                    RadioGroup sls_toggle = (RadioGroup) bottomsheetview.findViewById(R.id.sls_toggle);
                    TextInputLayout sls_spin_store_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_store_head);
                    TextInputLayout sls_spin_brand_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_brand_head);
                    TextInputLayout sls_spin_cabang_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_cabang_head);
                    TextInputLayout sls_spin_limit_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_limit_head);

                    AutoCompleteTextView sls_spin_store = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_store);
                    AutoCompleteTextView sls_spin_brand = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_brand);
                    AutoCompleteTextView sls_datefrom = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_datefrom);
                    AutoCompleteTextView sls_dateto = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_dateto);
                    AutoCompleteTextView sls_spin_limit = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_limit);
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
                    sls_spin_limit.setText(limitselected);
                    //
                    sls_datefrom_head.setEndIconDrawable(R.drawable.ico_calendar_text);
                    sls_dateto_head.setEndIconDrawable(R.drawable.ico_calendar_text);
                    sls_datefrom_head.setHint("From Date");
                    //
                    sls_dateto_head.setVisibility(View.VISIBLE);
                    sls_spin_brand_head.setVisibility(View.VISIBLE);
                    sls_toggle.setVisibility(View.VISIBLE);
                    sls_spin_store_head.setVisibility(View.VISIBLE);
                    sls_spin_artikel_head.setVisibility(View.GONE);
                    sls_spin_cabang_head.setVisibility(View.GONE);
                    sls_spin_limit_head.setVisibility(View.VISIBLE);
                    //Adapter
                    ArrayAdapter<String> storeadapter = new ArrayAdapter<String>(Foutlet.this,
                            R.layout.cus_dropdown_spiner, store);
                    sls_spin_store.setAdapter(storeadapter);
                    ArrayAdapter<String> brandadapter = new ArrayAdapter<String>(Foutlet.this,
                            R.layout.cus_dropdown_spiner, brand);
                    sls_spin_brand.setAdapter(brandadapter);
                    ArrayAdapter<String> limitadapter = new ArrayAdapter<String>(Foutlet.this,
                            R.layout.cus_dropdown_spiner, limitvalue);
                    sls_spin_limit.setAdapter(limitadapter);
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
                                DatePickerDialog datePickerDialog= new DatePickerDialog(Foutlet.this, new DatePickerDialog.OnDateSetListener() {
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
                                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(Foutlet.this,
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
//                .setMinMonth(Calendar.FEBRUARY)
                                        .setTitle("To Month")
//                .setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
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
                                DatePickerDialog datePickerDialog= new DatePickerDialog(Foutlet.this, new DatePickerDialog.OnDateSetListener() {
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
                                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(Foutlet.this,
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
//                .setMinMonth(Calendar.FEBRUARY)
                                        .setTitle("From Month")
//                .setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
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
                            String store, brand;
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
                            Cursor cursor = db.rawQuery("SELECT * FROM tbl_cabang where `jenis_store` " + store + " and `brand_cabang` " + brand + ";", null);
                            cursor.moveToFirst();
                            if (cursor.getCount() > 0) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        cb1.add(cursor.getString(0));
                                    } while (cursor.moveToNext());
                                }
                                String cb2 = cb1.toString();
                                String cb4 = cb2.replace("[", "");
                                String cb5 = cb4.replace("]", "");
                                String cb6 = cb5.replace(" ", "");
//                                String cabang = "in ('" + cb6 + "')";
                                String data = "";
                                if (sls_daily.isChecked()) {
                                    data = "daily";
                                    methode=false;
                                } else if (sls_monthly.isChecked()) {
                                    data = "monthly";
                                    methode=true;
                                }
                                String limitnya="";
                                if (sls_spin_limit.getText().toString().contains("Unlimited")) {
                                    limitnya="";
                                } else {
                                    limitnya="&limit="+sls_spin_limit.getText().toString().trim();
                                }
                                apiurl = api + "/api/sales/outlet?" +
                                        "cabang=" + cb6 + "&" +
                                        "datefrom=" + sls_datefrom.getText().toString() + "&" +
                                        "dateto=" + sls_dateto.getText().toString() + "&" +
                                        "data=" + data + limitnya;
                                Log.v("API", apiurl);
                                DateTo=sls_dateto.getText().toString();
                                DateFrom=sls_datefrom.getText().toString();
                                storeselected=sls_spin_store.getText().toString();
                                brandselected=sls_spin_brand.getText().toString();
                                limitselected=sls_spin_limit.getText().toString();
                                bottomSheetDialog.dismiss();
                                sls_search.setText("");
                                get();
                            } else {
                                Toast.makeText(Foutlet.this, "Tidak ada cabang yang cocok", Toast.LENGTH_LONG).show();
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
            floatingActionButton.callOnClick();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Foutlet.this);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void get() {
        kodecabang.clear();
        cabang.clear();
        tanggal.clear();
        achievement.clear();
        nilai.clear();
        target.clear();
        sales.clear();
        exchange.clear();
        sls_animnoresult.setVisibility(View.INVISIBLE);
        sls_listview.setAdapter(null);
        sls_animsearch.setVisibility(View.VISIBLE);
        Log.v("API",apiurl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiurl,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        sls_animsearch.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status==200) {
                                selectedfilter = new boolean[filters.length];
                                selectedfilter[2]=true;
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int position = 0; position < jsonArray.length(); position++) {
                                    JSONObject row = jsonArray.getJSONObject(position);
                                    kodecabang.add(row.getString("Kode_Cabang"));
                                    cabang.add(row.getString("cabang"));
                                    tanggal.add(row.getString("Tanggal"));
                                    achievement.add("Rp. " + currency(row.getString("achievement")) + " (" + row.getString("persen") + "%)");
                                    nilai.add(row.getInt("achievement"));
                                    target.add("Rp. " + currency(row.getString("target")));
                                    sales.add("Rp. " + currency(row.getString("harga_net_sales")) + " (" + row.getString("qty_sales") + " Pcs)");
                                    exchange.add("Rp. " + currency(row.getString("harga_net_exchange")) + " (" + row.getString("qty_exchange") + " Pcs)");
                                }
                                adapter = new Outlet_list_adapter(Foutlet.this,kodecabang,cabang,tanggal,achievement, nilai,target,sales,exchange);
                                sls_listview.setAdapter(adapter);
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(Foutlet.this,message, Toast.LENGTH_LONG).show();
                                sls_animnoresult.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.v("error",e.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(Foutlet.this);
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
                        if (error.getMessage()!=null) {
                            Log.v("error", error.getMessage());
                        }
                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                            sls_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(Foutlet.this,"Lost connection from server", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            sls_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(Foutlet.this,"Server not responding", Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ParseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Foutlet.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Foutlet.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


//    private class getdata extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            cabang.clear();
//            tanggal.clear();
//            achievement.clear();
//            target.clear();
//            sales.clear();
//            exchange.clear();
//            sls_animnoresult.setVisibility(View.INVISIBLE);
//            sls_listview.setAdapter(null);
//            sls_animsearch.setVisibility(View.VISIBLE);
//            running=true;
//            err=0;
//            nodata=0;
//        }
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            HttpHandler httpHandler = new HttpHandler();
//            // JSON data url
//            String jsonurl = apiurl;
//            try {
//                String jsonString = httpHandler.makeServiceCall(jsonurl);
//                if (httpHandler.GetResponseCode()==200) {
//                    if (jsonString != null) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(jsonString);
//                            int status = jsonObject.getInt("status");
//                            if (status==200) {
//                                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                                for (int position = 0; position < jsonArray.length(); position++) {
//                                    JSONObject row = jsonArray.getJSONObject(position);
//                                    cabang.add(row.getString("cabang"));
//                                    tanggal.add(row.getString("Tanggal"));
//                                    achievement.add("Rp. " + currency(row.getString("achievement")) + " (" + row.getString("persen") + "%)");
//                                    target.add("Rp. " + currency(row.getString("target")));
//                                    sales.add("Rp. " + currency(row.getString("harga_net_sales")) + " (" + row.getString("qty_sales") + " Pcs)");
//                                    exchange.add("Rp. " + currency(row.getString("harga_net_exchange")) + " (" + row.getString("qty_exchange") + " Pcs)");
//                                }
//                            } else {
//                                nodata=1;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("Error",e.getMessage());
//                            err=1;
//                        }
//                    } else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                nodata=1;
//                            }
//                        });
//                    }
//                } else {
//                    nodata=1;
//                }
//            } catch (Exception e){
//                err=1;
//                Log.v("Error",e.getMessage());
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            running=false;
//            sls_animsearch.setVisibility(View.INVISIBLE);
//            if (err==1) {
//                sls_animnoresult.setVisibility(View.VISIBLE);
//                Toast.makeText(Foutlet.this,"Error", Toast.LENGTH_LONG).show();
//            }
//            if (nodata==1) {
//                Toast.makeText(Foutlet.this,"No Data", Toast.LENGTH_LONG).show();
//                sls_animnoresult.setVisibility(View.VISIBLE);
//            } else {
//                adapter = new Outlet_list_adapter(Foutlet.this,cabang,tanggal,achievement,target,sales,exchange);
//                sls_listview.setAdapter(adapter);
//            }
//        }
//    }

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
            bg = (CoordinatorLayout) findViewById(R.id.sls_parent);
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
                        Intent formku = new Intent(Foutlet.this, FDashboardMenu.class);
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
                Intent formku = new Intent(Foutlet.this, FDashboardMenu.class);
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
package com.urban.urbanreport;

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
import android.os.AsyncTask;
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
import com.urban.urbanreport.CustomClass.HttpHandler;
import com.urban.urbanreport.CustomClass.Items_and_promotion_adapter;
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

public class Fitems extends AppCompatActivity {

    private ArrayList<String> items=new ArrayList<>();
    private ArrayList<String> achievement=new ArrayList<>();
    private ArrayList<String> sales=new ArrayList<>();
    private ArrayList<String> exchange=new ArrayList<>();
    ArrayList<String> store = new ArrayList<>();
    ArrayList<String> brand = new ArrayList<>();
    ArrayList<String> art = new ArrayList<>();
    private Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    Items_and_promotion_adapter adapter;
    String storeselected="ALL";
    String brandselected="ALL";
    String artselected="";
    String DateFrom,DateTo;
    Boolean methode=false;
    String apiurl,api;
    SQLiteDatabase db;
    ListView itm_listview;
    EditText itm_search;
    LottieAnimationView itm_animsearch,itm_animnoresult;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitems);
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursss = db.rawQuery("SELECT * FROM tbl_api", null);
        cursss.moveToFirst();
        if (cursss.getCount()>0) {
            api = cursss.getString(1);
            final Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-01");
            final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            DateFrom=simpleDateFormat.format(calendar.getTime());
            DateTo=simpleDateFormat2.format(calendar.getTime());
            itm_search=(EditText) findViewById(R.id.itm_search);
            itm_listview=(ListView) findViewById(R.id.itm_listview);
            itm_animnoresult=(LottieAnimationView) findViewById(R.id.itm_animnoresult);
            itm_animsearch=(LottieAnimationView) findViewById(R.id.itm_animsearch);
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
            Cursor cur = db.rawQuery("select * from tbl_artikel",null);
            if (cur.getCount()>0){
                if (cur.moveToFirst()) {
                    do {
                        art.add(cur.getString(1));
                    } while (cur.moveToNext());
                }
            }

            toolbar = findViewById(R.id.itm_toolbar);
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

            itm_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()>0 && s.toString()!=null) {
                        ArrayList<String> items1=new ArrayList<>();
                        ArrayList<String> achievement1=new ArrayList<>();
                        ArrayList<String> sales1=new ArrayList<>();
                        ArrayList<String> exchange1=new ArrayList<>();
                        for (int i=0;i < items.size();i++) {
                            if (items.get(i).toLowerCase().contains(s.toString().toLowerCase())) {
                                items1.add(items.get(i));
                                achievement1.add(achievement.get(i));
                                sales1.add(sales.get(i));
                                exchange1.add(exchange.get(i));
                            }
                        }
                        adapter = new Items_and_promotion_adapter(Fitems.this,items1,achievement1,sales1,exchange1);
                        itm_listview.setAdapter(adapter);
                    } else {
                        adapter = new Items_and_promotion_adapter(Fitems.this,items,achievement,sales,exchange);
                        itm_listview.setAdapter(adapter);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            floatingActionButton=(FloatingActionButton) findViewById(R.id.itm_get);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Fitems.this,R.style.AppBottomSheetDialogTheme);
                    View bottomsheetview = LayoutInflater.from(Fitems.this).inflate(
                            R.layout.cus_bottom_sheet_oulet,(LinearLayout) findViewById(R.id.cus_sls_bottomsheet));

                    RadioGroup sls_toggle = (RadioGroup) bottomsheetview.findViewById(R.id.sls_toggle);
                    TextInputLayout sls_spin_store_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_store_head);
                    TextInputLayout sls_spin_brand_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_brand_head);
                    TextInputLayout sls_spin_cabang_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_cabang_head);

                    AutoCompleteTextView sls_spin_store = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_store);
                    AutoCompleteTextView sls_spin_brand = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_brand);
                    AutoCompleteTextView sls_datefrom = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_datefrom);
                    AutoCompleteTextView sls_dateto = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_dateto);
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

                    //Adapter
                    ArrayAdapter<String> storeadapter = new ArrayAdapter<String>(Fitems.this,
                            R.layout.cus_dropdown_spiner, store);
                    sls_spin_store.setAdapter(storeadapter);
                    ArrayAdapter<String> brandadapter = new ArrayAdapter<String>(Fitems.this,
                            R.layout.cus_dropdown_spiner, brand);
                    sls_spin_brand.setAdapter(brandadapter);
                    ArrayAdapter<String> artadapter = new ArrayAdapter<>(Fitems.this,
                            R.layout.cus_dropdown_spiner,art);
                    sls_spin_artikel.setAdapter(artadapter);
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
                                DatePickerDialog datePickerDialog= new DatePickerDialog(Fitems.this, new DatePickerDialog.OnDateSetListener() {
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
                                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(Fitems.this,
                                        new MonthPickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(int selectedMonth, int selectedYear) { // on date set
                                                today.set(selectedYear,selectedMonth,01);
                                                sls_dateto.setText(simpleDateFormat.format(today.getTime()));
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
                                DatePickerDialog datePickerDialog= new DatePickerDialog(Fitems.this, new DatePickerDialog.OnDateSetListener() {
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
                                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(Fitems.this,
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
                            String store, brand, artikelnama;
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
                                if (sls_daily.isChecked()) {
                                    methode=false;
                                } else if (sls_monthly.isChecked()) {
                                    methode=true;
                                }
                                if ((sls_spin_artikel.getText().toString()!=null) && (sls_spin_artikel.getText().length()>0)){
                                    artikelnama=sls_spin_artikel.getText().toString();
                                } else {
                                    artikelnama = "";
                                }
                                apiurl = api + "/api/sales/items?" +
                                        "cabang=" + cb6 + "&" +
                                        "datefrom=" + sls_datefrom.getText().toString() + "&" +
                                        "dateto=" + sls_dateto.getText().toString() + "&" +
                                        "artname="+artikelnama;
                                Log.v("API", apiurl);
                                DateTo=sls_dateto.getText().toString();
                                DateFrom=sls_datefrom.getText().toString();
                                storeselected=sls_spin_store.getText().toString();
                                brandselected=sls_spin_brand.getText().toString();
                                artselected=sls_spin_artikel.getText().toString();
                                bottomSheetDialog.dismiss();
                                itm_search.setText("");
                                get();
                            } else {
                                Toast.makeText(Fitems.this, "Tidak ada cabang yang cocok", Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Fitems.this);
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
        items.clear();
        achievement.clear();
        sales.clear();
        exchange.clear();
        itm_listview.setAdapter(null);
        itm_animnoresult.setVisibility(View.INVISIBLE);
        itm_animsearch.setVisibility(View.VISIBLE);
        Log.v("API",apiurl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiurl,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        itm_animsearch.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status==200) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int position = 0; position < jsonArray.length(); position++) {
                                    JSONObject row = jsonArray.getJSONObject(position);
                                    items.add(row.getString("nama_barang"));
                                    achievement.add("Rp. " + currency(row.getString("achievement")) + " (" + row.getString("qty_bersih") + "Pcs)");
                                    sales.add("Rp. " + currency(row.getString("harga_net_sales")) + " (" + row.getString("SALES") + " Pcs)");
                                    exchange.add("Rp. " + currency(row.getString("harga_net_exchange")) + " (" + row.getString("EXCHANGE") + " Pcs)");
                                }
                                adapter = new Items_and_promotion_adapter(Fitems.this,items,achievement,sales,exchange);
                                itm_listview.setAdapter(adapter);
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(Fitems.this,message, Toast.LENGTH_LONG).show();
                                itm_animnoresult.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.v("error",e.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(Fitems.this);
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
                        itm_animsearch.setVisibility(View.INVISIBLE);
                        if (error.getMessage()!=null) {
                            Log.v("error", error.getMessage());
                        }
                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                            itm_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(Fitems.this,"Lost connect from server", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            itm_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(Fitems.this,"Server not responding", Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ParseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Fitems.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Fitems.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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
            bg=(CoordinatorLayout) findViewById(R.id.itm_parent);
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
                        Intent formku = new Intent(Fitems.this, FDashboardMenu.class);
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
                Intent formku = new Intent(Fitems.this, FDashboardMenu.class);
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
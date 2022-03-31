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
import com.urban.urbanreport.CustomClass.Discovery_list_adapter;
import com.urban.urbanreport.CustomClass.HttpHandler;
import com.urban.urbanreport.CustomClass.Stock_list_adapter;
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

public class Fstock extends AppCompatActivity {

    private ArrayList<String> nmbrg=new ArrayList<>();
    private ArrayList<String> cabang=new ArrayList<>();
    private ArrayList<String> barcode=new ArrayList<>();
    private ArrayList<String> artikel=new ArrayList<>();
    private ArrayList<String> hrgjual=new ArrayList<>();
    private ArrayList<String> stock=new ArrayList<>();
    //
    ArrayList<String> cabangname = new ArrayList<>();
    private Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    Stock_list_adapter adapter;
    String cabangselected="",idcabangselected="";
    String DateSelected;
    String apiurl,api;
    SQLiteDatabase db;
    ListView stk_listview;
    EditText stk_search;
    LottieAnimationView stk_animsearch,stk_animnoresult;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fstock);
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursss = db.rawQuery("SELECT * FROM tbl_api", null);
        cursss.moveToFirst();
        if (cursss.getCount()>0) {
            api = cursss.getString(1);
            final Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            DateSelected=simpleDateFormat2.format(calendar.getTime());

            stk_search=(EditText) findViewById(R.id.stk_search);
            stk_listview=(ListView) findViewById(R.id.stk_listview);
            stk_animnoresult=(LottieAnimationView) findViewById(R.id.stk_animnoresult);
            stk_animsearch=(LottieAnimationView) findViewById(R.id.stk_animsearch);

            Cursor cursor1 = db.rawQuery("SELECT * from tbl_cabang",null);
            if (cursor1.moveToFirst()) {
                do {
                    cabangname.add("["+cursor1.getString(0)+"] "+cursor1.getString(1));
                } while (cursor1.moveToNext());
            }
            cursor1.moveToFirst();
            idcabangselected=cursor1.getString(0);
            cabangselected="["+cursor1.getString(0)+"] "+cursor1.getString(1);

            toolbar = findViewById(R.id.stk_toolbar);
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

            stk_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()>0 && s.toString()!=null) {
                        ArrayList<String> nmbrg1=new ArrayList<>();
                        ArrayList<String> cabang1=new ArrayList<>();
                        ArrayList<String> barcode1=new ArrayList<>();
                        ArrayList<String> artikel1=new ArrayList<>();
                        ArrayList<String> hrgjual1=new ArrayList<>();
                        ArrayList<String> stock1=new ArrayList<>();
                        for (int i=0;i < nmbrg.size();i++) {
                            if (nmbrg.get(i).toLowerCase().contains(s.toString().toLowerCase())) {
                                nmbrg1.add(nmbrg.get(i));
                                cabang1.add(cabang.get(i));
                                barcode1.add(barcode.get(i));
                                artikel1.add(artikel.get(i));
                                hrgjual1.add(hrgjual.get(i));
                                stock1.add(stock.get(i));
                            }
                        }
                        adapter = new Stock_list_adapter(Fstock.this,nmbrg1,cabang1,barcode1,artikel1,hrgjual1,stock1);
                        stk_listview.setAdapter(adapter);
                    } else {
                        adapter = new Stock_list_adapter(Fstock.this,nmbrg,cabang,barcode,artikel,hrgjual,stock);
                        stk_listview.setAdapter(adapter);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            floatingActionButton=(FloatingActionButton) findViewById(R.id.stk_get);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Fstock.this,R.style.AppBottomSheetDialogTheme);
                    View bottomsheetview = LayoutInflater.from(Fstock.this).inflate(
                            R.layout.cus_bottom_sheet_oulet,(LinearLayout) findViewById(R.id.cus_sls_bottomsheet));

                    RadioGroup sls_toggle = (RadioGroup) bottomsheetview.findViewById(R.id.sls_toggle);
                    TextInputLayout sls_spin_store_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_store_head);
                    TextInputLayout sls_spin_brand_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_brand_head);
                    TextInputLayout sls_spin_cabang_head = (TextInputLayout) bottomsheetview.findViewById(R.id.sls_spin_cabang_head);

                    AutoCompleteTextView sls_spin_cabang = (AutoCompleteTextView) bottomsheetview.findViewById(R.id.sls_spin_cabang);
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
                    sls_spin_cabang.setText(cabangselected);
                    //
                    sls_datefrom_head.setEndIconDrawable(R.drawable.ico_calendar_text);
                    sls_dateto_head.setEndIconDrawable(R.drawable.ico_calendar_text);
                    sls_datefrom_head.setHint("Stock Date");
                    //
                    sls_dateto_head.setVisibility(View.GONE);
                    sls_spin_brand_head.setVisibility(View.GONE);
                    sls_toggle.setVisibility(View.GONE);
                    sls_spin_store_head.setVisibility(View.GONE);
                    sls_spin_artikel_head.setVisibility(View.GONE);
                    sls_spin_cabang_head.setVisibility(View.VISIBLE);

                    //Adapter
                    ArrayAdapter<String> idcabangdapater = new ArrayAdapter<String>(Fstock.this,
                            R.layout.cus_dropdown_spiner, cabangname);
                    sls_spin_cabang.setAdapter(idcabangdapater);
                    //ActionEvent
                    sls_datefrom_head.setEndIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sls_datefrom.callOnClick();
                        }
                    });
                    sls_datefrom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                            DatePickerDialog datePickerDialog= new DatePickerDialog(Fstock.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    calendar.set(year,month,dayOfMonth);
                                    sls_datefrom.setText(simpleDateFormat.format(calendar.getTime()));
                                }
                            },yearnow,monthnow,datenow);
                            datePickerDialog.setTitle("Stock Date");
                            datePickerDialog.show();
                            DateSelected=sls_datefrom.getText().toString();
                        }
                    });
                    sls_datefrom.setText(DateSelected);
                    sls_bsearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Cursor cur22 = db.rawQuery("SELECT * FROM `tbl_cabang` where '['||kode||'] '||nama='" + sls_spin_cabang.getText().toString() + "'", null);
                            cur22.moveToFirst();
                            if (cur22.getCount()>=1) {
                                idcabangselected=cur22.getString(0);
                                cabangselected = "["+cur22.getString(0)+"] "+cur22.getString(1);
                                apiurl = api + "/api/sales/stock?" +
                                        "cabang=" + idcabangselected + "&" +
                                        "date=" + sls_datefrom.getText().toString();
                                Log.v("API", apiurl);
                                DateSelected=sls_datefrom.getText().toString();
                                bottomSheetDialog.dismiss();
                                stk_search.setText("");
                                get();
                            } else {
                                Toast.makeText(Fstock.this,"No Data", Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Fstock.this);
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
        nmbrg.clear();
        cabang.clear();
        barcode.clear();
        artikel.clear();
        hrgjual.clear();
        stock.clear();
        stk_listview.setAdapter(null);
        stk_animnoresult.setVisibility(View.INVISIBLE);
        stk_animsearch.setVisibility(View.VISIBLE);
        Log.v("API",apiurl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiurl,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stk_animsearch.setVisibility(View.INVISIBLE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int status = jsonObject.getInt("status");
                                    if (status==200) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int position = 0; position < jsonArray.length(); position++) {
                                            JSONObject row = jsonArray.getJSONObject(position);
                                            row = jsonArray.getJSONObject(position);
                                            nmbrg.add(row.getString("NAMA_BARANG"));
                                            cabang.add(row.getString("CABANG"));
                                            barcode.add(row.getString("BARCODE"));
                                            artikel.add(row.getString("ARTIKEL"));
                                            hrgjual.add("Rp. " + currency(row.getString("harga_jual")));
                                            stock.add("Rp. " + currency(row.getString("stock_value")) + " (" + row.getString("SISA") + " Pcs)");
                                        }
                                        adapter = new Stock_list_adapter(Fstock.this,nmbrg,cabang,barcode,artikel,hrgjual,stock);
                                        stk_listview.setAdapter(adapter);
                                    } else {
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(Fstock.this,message, Toast.LENGTH_LONG).show();
                                        stk_animnoresult.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    Log.v("error",e.getMessage());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Fstock.this);
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
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stk_animsearch.setVisibility(View.INVISIBLE);
                        if (error.getMessage()!=null) {
                            Log.v("error", error.getMessage());
                        }
                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                            stk_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(Fstock.this,"Lost connect from server", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            stk_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(Fstock.this,"Server not responding", Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ParseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Fstock.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Fstock.this);
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
            bg=(CoordinatorLayout) findViewById(R.id.stk_parent);
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
                        Intent formku = new Intent(Fstock.this, FDashboardMenu.class);
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
                Intent formku = new Intent(Fstock.this, FDashboardMenu.class);
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
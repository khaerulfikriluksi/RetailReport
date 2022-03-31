package com.urban.urbanreport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
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
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.Result;
import com.urban.urbanreport.CustomClass.Artikel_list_adapter;
import com.urban.urbanreport.CustomClass.Discovery_list_adapter;
import com.urban.urbanreport.CustomClass.HttpHandler;
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

public class FItemsDiscovery extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private Discovery_list_adapter adapter;
    private ArrayList<String> artikel=new ArrayList<>();
    private ArrayList<String> season=new ArrayList<>();
    private ArrayList<String> kelompok=new ArrayList<>();
    private ArrayList<String> subklp=new ArrayList<>();
    private ArrayList<String> departemen=new ArrayList<>();
    private ArrayList<String> promosi=new ArrayList<>();
    private ArrayList<String> jual=new ArrayList<>();
    private ArrayList<String> bersih=new ArrayList<>();
    private ArrayList<String> urlgambar=new ArrayList<>();
    //
    private SQLiteDatabase db;
    private AlertDialog show,show2;
    private AlertDialog.Builder builderreset;
    private String filter="";
    private String apiurl,api,apigambar;
    private ListView dsc_listview;
    private LottieAnimationView dsc_animsearch,dsc_animnoresult;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitems_discovery);
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursss = db.rawQuery("SELECT * FROM tbl_api", null);
        cursss.moveToFirst();
        if (cursss.getCount()>0) {
            api = cursss.getString(1);
            apigambar=cursss.getString(1)+"/product/";
            dsc_listview=(ListView) findViewById(R.id.dsc_listview);
            dsc_animnoresult=(LottieAnimationView) findViewById(R.id.dsc_animnoresult);
            dsc_animsearch=(LottieAnimationView) findViewById(R.id.dsc_animsearch);
            //
            toolbar = findViewById(R.id.dsc_toolbar);
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

            floatingActionButton=(FloatingActionButton) findViewById(R.id.dsc_get);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builderreset = new AlertDialog.Builder(FItemsDiscovery.this);
                    LayoutInflater inflater = FItemsDiscovery.this.getLayoutInflater();
                    View view = inflater.inflate(R.layout.cus_popup_scan, null);
                    AutoCompleteTextView e_search = (AutoCompleteTextView) view.findViewById(R.id.e_search);
                    TextInputLayout e_search_head = (TextInputLayout) view.findViewById(R.id.e_search_head);
                    e_search_head.setEndIconDrawable(R.drawable.ico_scan);
                    e_search_head.setEndIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            AlertDialog.Builder builderscan = new AlertDialog.Builder(FItemsDiscovery.this);
                            LayoutInflater inflater2 = FItemsDiscovery.this.getLayoutInflater();
                            View view2 = inflater2.inflate(R.layout.cust_popup_scan, null);
                            ZXingScannerView camera = (ZXingScannerView) view2.findViewById(R.id.camera);
                            camera.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    camera.toggleFlash();
                                }
                            });
                            camera.setResultHandler(new ZXingScannerView.ResultHandler() {
                                @Override
                                public void handleResult(Result result) {
                                    e_search_head.setHelperText(null);
                                    e_search.setText(result.getText());
                                    camera.stopCamera();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            show2.dismiss();
                                        }
                                    }, 500);
                                }
                            });
                            builderscan.setCancelable(false);
                            camera.startCamera();
                            builderscan.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    show2.dismiss();
                                }
                            });
                            builderscan.setView(view2);
                            show2=builderscan.show();
                            show2.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                    });
                    e_search.setText(filter);
                    e_search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.toString().trim().length()>0) {
                                Button positiveButton = ((AlertDialog) show)
                                        .getButton(AlertDialog.BUTTON_POSITIVE);
                                positiveButton.setEnabled(true);
                                e_search_head.setHelperText(null);
                            } else {
                                Button positiveButton = ((AlertDialog) show)
                                        .getButton(AlertDialog.BUTTON_POSITIVE);
                                positiveButton.setEnabled(false);
                                e_search_head.setHelperText("*Required");
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    builderreset.setPositiveButton("Get Data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (e_search_head.getHelperText()==null) {
                                apiurl = api + "/api/sales/discovery?" +
                                        "search=" + e_search.getText().toString().trim();
                                filter=e_search.getText().toString().trim();
                                get();
                            } else {
                                Button positiveButton = ((AlertDialog) show)
                                        .getButton(AlertDialog.BUTTON_POSITIVE);
                                positiveButton.setEnabled(false);
                                e_search_head.setHelperText("*Required");
                            }
                        }
                    });
                    builderreset.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            show.dismiss();
                        }
                    });
                    builderreset.setCancelable(false);
                    builderreset.setView(view);
                    show=builderreset.show();
                    Button positiveButton = ((AlertDialog) show)
                            .getButton(AlertDialog.BUTTON_POSITIVE);
                    if (e_search.getText().toString().trim().length()>0) {
                        positiveButton.setEnabled(true);
                    } else {
                        positiveButton.setEnabled(false);
                        e_search_head.setHelperText("*Required");
                    }
                }
            });
            floatingActionButton.callOnClick();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(FItemsDiscovery.this);
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

    @Override
    public void onBackPressed() {
        count++;
        if (count ==1) {
            CoordinatorLayout bg;
            bg=(CoordinatorLayout) findViewById(R.id.dsc_parent);
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
                        Intent formku = new Intent(FItemsDiscovery.this, FDashboardMenu.class);
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
                Intent formku = new Intent(FItemsDiscovery.this, FDashboardMenu.class);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void get() {
        artikel.clear();
        season.clear();
        kelompok.clear();
        subklp.clear();
        departemen.clear();
        promosi.clear();
        jual.clear();
        bersih.clear();
        urlgambar.clear();
        dsc_listview.setAdapter(null);
        dsc_animnoresult.setVisibility(View.INVISIBLE);
        dsc_animsearch.setVisibility(View.VISIBLE);
        Log.v("API",apiurl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiurl,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        dsc_animsearch.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status==200) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int position = 0; position < jsonArray.length(); position++) {
                                    JSONObject row = jsonArray.getJSONObject(position);
                                    artikel.add(row.getString("merk") + " ["+row.getString("warna")+"]");
                                    season.add(row.getString("season"));
                                    kelompok.add(row.getString("kelompok"));
                                    subklp.add(row.getString("subkelompok"));
                                    departemen.add(row.getString("departemen"));
                                    promosi.add(row.getString("Promosi"));
                                    jual.add("Rp. "+currency(row.getString("Harga_jual")));
                                    bersih.add("Rp. "+currency(row.getString("harga_net")));
                                    urlgambar.add(apigambar+row.getString("foto"));
                                }
                                adapter = new Discovery_list_adapter(FItemsDiscovery.this,artikel,season,kelompok,subklp,departemen,promosi,jual,bersih,urlgambar);
                                dsc_listview.setAdapter(adapter);
                                dsc_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if (adapter.getImageurl(position).contains("amogos.webp")) {
                                            Toast.makeText(FItemsDiscovery.this,"No image avaible", Toast.LENGTH_LONG).show();
                                        } else {
                                            Intent formku = new Intent(FItemsDiscovery.this, Fimage_popup.class);
                                            formku.putExtra("url",adapter.getImageurl(position));
                                            startActivity(formku);
//                                            imagePopup.initiatePopupWithGlide(adapter.getImageurl(position));
//                                            imagePopup.setBackgroundColor(Color.TRANSPARENT);
////                                            imagePopup.setImageOnClickClose(true);
////                                            imagePopup.setFullScreen(true);
//                                            imagePopup.setHideCloseIcon(true);
//                                            imagePopup.viewPopup();
                                        }
                                    }
                                });
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(FItemsDiscovery.this,message, Toast.LENGTH_LONG).show();
                                dsc_animnoresult.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.v("error",e.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(FItemsDiscovery.this);
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
                        dsc_animsearch.setVisibility(View.INVISIBLE);
                        if (error.getMessage()!=null) {
                            Log.v("error", error.getMessage());
                        }
                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                            dsc_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(FItemsDiscovery.this,"Lost connect from server", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            dsc_animnoresult.setVisibility(View.VISIBLE);
                            Toast.makeText(FItemsDiscovery.this,"Server not responding", Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ParseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FItemsDiscovery.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(FItemsDiscovery.this);
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
}
package com.urban.urbanreport;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.urban.urbanreport.CustomClass.Cache;
import com.urban.urbanreport.CustomClass.Home_recycle_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragmentDashboard extends Fragment {

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
    private TextView LGreetings,lsumdaily,lsummonthly,
            Ldayly,Lmonthly,lkoneksi,Ldayly_footwear
            ,lsumdaily_footwear,Lmonthly_footwear,
            lsummonthly_footwear,Ldayly_clothing,Lmonthly_clothing
            ,Ldayly_online,lsumdaily_online,Lmonthly_online,lsummonthly_online,
            lsumdaily_clothing,lsummonthly_clothing, hm_showmore, lsumdaily_qty, lsummonthly_qty,
            lsumdaily_footwear_qty,lsummonthly_footwear_qty, lsumdaily_clothing_qty, lsummonthly_clothing_qty,
            lsumdaily_online_qty,lsummonthly_online_qty;
    private LottieAnimationView animdashboard;
    private Integer sleeptime=180000;
    private LinearLayout Tkonkeksi,hm_layMenu,hm_cardbg;
    private SQLiteDatabase db;
    private ImageButton hm_brefresh;
    private Animation in,out,in1,out1;
    private TextView hm_bexpand;
    private Boolean run=true;
    private CardView hm_card_expand;
//    private RadioGroup hm_checker;
//    private RadioButton hm_checker_monthly,hm_checker_daily;
    private AlertDialog show;
    private AlertDialog.Builder builder;
    private Boolean run2=true;
    private Boolean run3=false,firstrun=true;
    private String apiDashboard,apigambar;
    private Handler mHandler = new Handler();
    private Context context;
    private int count = 0;
    private SharedPreferences storageCache;
    private SharedPreferences.Editor CacheEditor;
    private int count1 = 0;
//    private Home_recycle_adapter recycle_adapter,recycle_adapter2;

    public FragmentDashboard() {

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        context= getContext();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_api", null);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            storageCache = context.getSharedPreferences("UrbanReport", Context.MODE_PRIVATE);
            CacheEditor = storageCache.edit();
            apiDashboard=cursor.getString(1)+"/api/sales/summary";
            apigambar=cursor.getString(1)+"/product/";
            //
            hm_cardbg=(LinearLayout) view.findViewById(R.id.hm_cardbg);
            hm_layMenu=(LinearLayout) view.findViewById(R.id.hm_layMenu);
            hm_card_expand=(CardView) view.findViewById(R.id.hm_card_expand);
            hm_bexpand=(TextView) view.findViewById(R.id.hm_bexpand);
            hm_brefresh=(ImageButton) view.findViewById(R.id.hm_brefresh);
            lkoneksi=(TextView) view.findViewById(R.id.lkoneksi);
            Tkonkeksi = (LinearLayout) view.findViewById(R.id.Tkonkeksi);
            hm_showmore = (TextView) view.findViewById(R.id.hm_showmore);
            animdashboard=(LottieAnimationView) view.findViewById(R.id.animdashboard);
            animdashboard.setVisibility(View.INVISIBLE);
            //
            Animation ii = new AlphaAnimation(0.0f, 1.0f);
            ii.setDuration(2000);
            hm_card_expand.setAnimation(ii);
            ii.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    hm_card_expand.setAlpha(1.0f);
                    firstrun=false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            hm_bexpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(R.layout.cus_popup_bestseller, null);
                    RadioGroup hm_checker = (RadioGroup) view.findViewById(R.id.hm_checker);
                    RadioButton hm_checker_monthly = (RadioButton) view.findViewById(R.id.hm_checker_monthly),
                            hm_checker_daily = (RadioButton) view.findViewById(R.id.hm_checker_daily);
                    RecyclerView hm_recycle = (RecyclerView) view.findViewById(R.id.hm_recycle),
                            hm_recycle2 = (RecyclerView) view.findViewById(R.id.hm_recycle2);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);

                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.R)
                        @Override
                        public void run() {
                            Home_recycle_adapter recycle_adapter2 = new Home_recycle_adapter(getContext(),itemcode_daily,itemname_daily,itemqty_daily,urlgambar_daily,stock_daily);
                            hm_recycle2.setLayoutManager(layoutManager2);
                            hm_recycle2.setAdapter(recycle_adapter2);
                            Home_recycle_adapter recycle_adapter = new Home_recycle_adapter(getContext(),itemcode,itemname,itemqty,urlgambar,stock);
                            hm_recycle.setLayoutManager(layoutManager);
                            hm_recycle.setAdapter(recycle_adapter);
                        }
                    }).start();
                    if (Cache.getInstance().isDailyrecycle()==true) {
                        hm_recycle.setVisibility(View.GONE);
                        hm_recycle2.setVisibility(View.VISIBLE);
                        hm_checker_daily.setChecked(true);
                        hm_checker_monthly.setChecked(false);
                    } else {
                        hm_recycle.setVisibility(View.VISIBLE);
                        hm_recycle2.setVisibility(View.GONE);
                        hm_checker_daily.setChecked(false);
                        hm_checker_monthly.setChecked(true);
                    }
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
                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            show.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.setView(view);
                    show=builder.show();
                }
            });

            hm_showmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count1++;
                    if (count1==1) {
                        RelativeLayout bg;
                        bg = (RelativeLayout) getActivity().findViewById(R.id.parent_dashboard);
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
                                    Intent formku = new Intent(getContext(), FDiscovery_Summary.class);
                                    startActivity(formku);
//                                    getActivity().finish();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            count1=0;
                                            bg.setVisibility(View.VISIBLE);
                                        }
                                    }, 2000);
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
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    count1=0;
                                }
                            }, 2000);
                            Intent formku = new Intent(getContext(), FDiscovery_Summary.class);
                            startActivity(formku);
//                            getActivity().finish();
                        }
                    } else {
                        Toast.makeText(context,"Please wait, to may tap...",Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                count1=0;
                            }
                        }, 2000);
                    }
                }
            });

            in1 = new AlphaAnimation(0.0f, 1.0f);
            in1.setDuration(1000);
            out1 = new AlphaAnimation(1.0f, 0.0f);
            out1.setDuration(1000);
            in1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Tkonkeksi.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Tkonkeksi.startAnimation(out1);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            out1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (lkoneksi.getText()=="Connected") {
                        Tkonkeksi.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            //
            in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(1000);
            out = new AlphaAnimation(1.0f, 0.0f);
            out.setDuration(1000);
            in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animdashboard.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animdashboard.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            LGreetings=(TextView) view.findViewById(R.id.LGreetings);
            Ldayly=(TextView) view.findViewById(R.id.Ldayly);
            Lmonthly=(TextView) view.findViewById(R.id.Lmonthly);
            lsumdaily=(TextView) view.findViewById(R.id.lsumdaily);
            lsumdaily_qty = (TextView) view.findViewById(R.id.lsumdaily_qty);
            lsummonthly=(TextView) view.findViewById(R.id.lsummonthly);
            lsummonthly_qty = (TextView) view.findViewById(R.id.lsummonthly_qty);
            Ldayly_footwear=(TextView) view.findViewById(R.id.Ldayly_footwear);
            lsumdaily_footwear=(TextView) view.findViewById(R.id.lsumdaily_footwear);
            lsumdaily_footwear_qty = (TextView) view.findViewById(R.id.lsumdaily_footwear_qty);
            Lmonthly_footwear=(TextView) view.findViewById(R.id.Lmonthly_footwear);
            lsummonthly_footwear=(TextView) view.findViewById(R.id.lsummonthly_footwear);
            lsummonthly_footwear_qty=(TextView) view.findViewById(R.id.lsummonthly_footwear_qty);
            Ldayly_clothing=(TextView) view.findViewById(R.id.Ldayly_clothing);
            lsumdaily_clothing=(TextView) view.findViewById(R.id.lsumdaily_clothing);
            lsumdaily_clothing_qty=(TextView) view.findViewById(R.id.lsumdaily_clothing_qty);
            Lmonthly_clothing=(TextView) view.findViewById(R.id.Lmonthly_clothing);
            lsummonthly_clothing=(TextView) view.findViewById(R.id.lsummonthly_clothing);
            lsummonthly_clothing_qty=(TextView) view.findViewById(R.id.lsummonthly_clothing_qty);
            Ldayly_online=(TextView) view.findViewById(R.id.Ldayly_online);
            lsumdaily_online=(TextView) view.findViewById(R.id.lsumdaily_online);
            lsumdaily_online_qty=(TextView) view.findViewById(R.id.lsumdaily_online_qty);
            Lmonthly_online=(TextView) view.findViewById(R.id.Lmonthly_online);
            lsummonthly_online=(TextView) view.findViewById(R.id.lsummonthly_online);
            lsummonthly_online_qty=(TextView) view.findViewById(R.id.lsummonthly_online_qty);
            //
            timer();
            if (Cache.getInstance().isExist()) {
                run2 = true;
//                autorefresh();
                get1();
            } else {
                if (Cache.getInstance().getDataList("daily").trim().length()>0) {
                    //
                    run2 = false;
                    get1();
                    Ldayly.setText(Cache.getInstance().getDataList("daily"));
                    Lmonthly.setText(Cache.getInstance().getDataList("monthly"));
                    Ldayly_footwear.setText(Cache.getInstance().getDataList("daily"));
                    Ldayly_clothing.setText(Cache.getInstance().getDataList("daily"));
                    Ldayly_online.setText(Cache.getInstance().getDataList("daily"));
                    Lmonthly_footwear.setText(Cache.getInstance().getDataList("monthly"));
                    Lmonthly_clothing.setText(Cache.getInstance().getDataList("monthly"));
                    Lmonthly_online.setText(Cache.getInstance().getDataList("monthly"));
                    //
                    lsumdaily_footwear.setText(Cache.getInstance().getDataList("dailyfootwear"));
                    lsumdaily_footwear_qty.setText(Cache.getInstance().getDataList("dailyfootwear_qty"));
                    lsummonthly_footwear.setText(Cache.getInstance().getDataList("monthlyfootwear"));
                    lsummonthly_footwear_qty.setText(Cache.getInstance().getDataList("monthlyfootwear_qty"));
                    lsumdaily_clothing.setText(Cache.getInstance().getDataList("dailyclothing"));
                    lsumdaily_clothing_qty.setText(Cache.getInstance().getDataList("dailyclothing_qty"));
                    lsummonthly_clothing.setText(Cache.getInstance().getDataList("monthlyclothing"));
                    lsummonthly_clothing_qty.setText(Cache.getInstance().getDataList("monthlyclothing_qty"));
                    lsumdaily_online.setText(Cache.getInstance().getDataList("dailyonline"));
                    lsumdaily_online_qty.setText(Cache.getInstance().getDataList("dailyonline_qty"));
                    lsummonthly_online.setText(Cache.getInstance().getDataList("monthlyonline"));
                    lsummonthly_online_qty.setText(Cache.getInstance().getDataList("monthlyonline_qty"));
                    lsumdaily.setText(Cache.getInstance().getDataList("sumdaily"));
                    lsumdaily_qty.setText(Cache.getInstance().getDataList("sumdaily_qty"));
                    lsummonthly.setText(Cache.getInstance().getDataList("summonthly"));
                    lsummonthly_qty.setText(Cache.getInstance().getDataList("summonthly_qty"));
                    //
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
//                    recycle_adapter = new Home_recycle_adapter(context,itemcode,itemname,itemqty,urlgambar,stock);
//                    recycle_adapter2 = new Home_recycle_adapter(context,itemcode_daily,itemname_daily,itemqty_daily,urlgambar_daily,stock_daily);
                } else {
                    run2 = true;
                    get1();
                }
            }
            hm_brefresh.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    run2 = false;
                    get1();
                }
            });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        return view;
    }

    public void timer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    try {
                        Thread.sleep(100);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Calendar c = Calendar.getInstance();
                                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                                if(timeOfDay >= 0 && timeOfDay < 12){
                                    LGreetings.setText("Good Morning");
                                }else if(timeOfDay >= 12 && timeOfDay < 16){
                                    LGreetings.setText("Good Afternoon");
                                }else if(timeOfDay >= 16 && timeOfDay < 21){
                                    LGreetings.setText("Good Evening");
                                }else if(timeOfDay >= 21 && timeOfDay < 24){
                                    LGreetings.setText("Good Night");
                                }
                            }
                        });
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }).start();
    }

    public String currency(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void get1() {
        count++;
        if (count ==1) {
            final Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
            final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MMMM yyyy");
            final SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("MMMM_yyyy");
            final SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
            String tanggal=simpleDateFormat.format(calendar.getTime());
            String bulan=simpleDateFormat4.format(calendar.getTime());
            String waktu = simpleDateFormat5.format(calendar.getTime());
            if (urlgambar.size()>0 || urlgambar_daily.size()>0) {
                hm_bexpand.setVisibility(View.VISIBLE);
            } else {
                hm_bexpand.setVisibility(View.INVISIBLE);
            }
            Cache.getInstance().replace("FIRSTRUN", "false");
            hm_brefresh.setVisibility(View.INVISIBLE);
            if (animdashboard.getVisibility() == View.INVISIBLE) {
                animdashboard.startAnimation(in);
            }
            run2 = false;
            Log.v("API", apiDashboard);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, apiDashboard,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                count = 0;
                                if (lkoneksi.getText() == "Reconnecting") {
                                    Tkonkeksi.setBackgroundResource(R.drawable.bg_sneakebar_con);
                                    lkoneksi.setText("Connected");
                                    Tkonkeksi.startAnimation(out1);
                                }
                                Cache.getInstance().ClearCache();
                                JSONObject jsonObject = new JSONObject(response);
                                int stat = jsonObject.getInt("status");
                                if (stat == 200) {
                                    JSONObject object = jsonObject.getJSONObject("data");
                                    JSONObject objsummary = object.getJSONObject("result");
                                    Cache.getInstance().setDataList("daily", objsummary.getString("tanggal"));
                                    Cache.getInstance().setDataList("monthly", objsummary.getString("bulan"));
                                    Cache.getInstance().setDataList("sumdaily", "Rp. " + currency(objsummary.getString("daily")));
                                    Cache.getInstance().setDataList("sumdaily_qty", currency(objsummary.getString("daily_qty"))+" Pcs");
                                    Cache.getInstance().setDataList("summonthly", "Rp. " + currency(objsummary.getString("monthly")));
                                    Cache.getInstance().setDataList("summonthly_qty", currency(objsummary.getString("monthly_qty"))+" Pcs");
                                    //
                                    JSONArray jsonArray3 = object.getJSONArray("result_det_month");
                                    JSONObject row3 = jsonArray3.getJSONObject(0);
                                    Cache.getInstance().setDataList("monthlyfootwear", "Rp. " + currency(row3.getString("footwear_month")));
                                    Cache.getInstance().setDataList("monthlyfootwear_qty", currency(row3.getString("footwear_month_qty"))+" Pcs");
                                    Cache.getInstance().setDataList("monthlyonline", "Rp. " + currency(row3.getString("onlinestore_month")));
                                    Cache.getInstance().setDataList("monthlyonline_qty", currency(row3.getString("onlinestore_month_qty"))+" Pcs");
                                    Cache.getInstance().setDataList("monthlyclothing", "Rp. " + currency(row3.getString("clothing_month")));
                                    Cache.getInstance().setDataList("monthlyclothing_qty", currency(row3.getString("clothing_month_qty"))+" Pcs");
                                    //
                                    JSONArray jsonArray4 = object.getJSONArray("result_det_dayly");
                                    JSONObject row4 = jsonArray4.getJSONObject(0);
                                    Cache.getInstance().setDataList("dailyfootwear", "Rp. " + currency(row4.getString("footwear_day")));
                                    Cache.getInstance().setDataList("dailyfootwear_qty", currency(row4.getString("footwear_day_qty"))+" Pcs");
                                    Cache.getInstance().setDataList("dailyonline", "Rp. " + currency(row4.getString("onlinestore_day")));
                                    Cache.getInstance().setDataList("dailyonline_qty", currency(row4.getString("onlinestore_day_qty"))+" Pcs");
                                    Cache.getInstance().setDataList("dailyclothing", "Rp. " + currency(row4.getString("clothing_day")));
                                    Cache.getInstance().setDataList("dailyclothing_qty", currency(row4.getString("clothing_day_qty"))+" Pcs");
                                    //
                                    remoceCacheStorage(tanggal,bulan);
                                    saveCacheStorage(tanggal,bulan,1,waktu);
                                    saveCacheStorage(tanggal,bulan,2,objsummary.getString("tanggal"));
                                    saveCacheStorage(tanggal,bulan,3,"Rp. " + currency(objsummary.getString("daily")));
                                    saveCacheStorage(tanggal,bulan,12,currency(objsummary.getString("daily_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,4,objsummary.getString("bulan"));
                                    saveCacheStorage(tanggal,bulan,5,"Rp. " + currency(objsummary.getString("monthly")));
                                    saveCacheStorage(tanggal,bulan,13,currency(objsummary.getString("monthly_qty"))+" Pcs");
                                    //
                                    saveCacheStorage(tanggal,bulan,7,"Rp. " + currency(row3.getString("footwear_month")));
                                    saveCacheStorage(tanggal,bulan,15,currency(row3.getString("footwear_month_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,9,"Rp. " + currency(row3.getString("clothing_month")));
                                    saveCacheStorage(tanggal,bulan,17,currency(row3.getString("clothing_month_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,11,"Rp. " + currency(row3.getString("onlinestore_month")));
                                    saveCacheStorage(tanggal,bulan,19,currency(row3.getString("onlinestore_month_qty"))+" Pcs");
                                    //
                                    saveCacheStorage(tanggal,bulan,6,"Rp. " + currency(row4.getString("footwear_day")));
                                    saveCacheStorage(tanggal,bulan,14,currency(row4.getString("footwear_day_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,8,"Rp. " + currency(row4.getString("clothing_day")));
                                    saveCacheStorage(tanggal,bulan,16,currency(row4.getString("clothing_day_qty"))+" Pcs");
                                    saveCacheStorage(tanggal,bulan,10,"Rp. " + currency(row4.getString("onlinestore_day")));
                                    saveCacheStorage(tanggal,bulan,18,currency(row4.getString("onlinestore_day_qty"))+" Pcs");

                                    int status = object.getInt("status_top");
                                    if (status == 200) {
                                        ArrayList<String> kd1 = new ArrayList<>();
                                        ArrayList<String> nm1 = new ArrayList<>();
                                        ArrayList<String> qty1 = new ArrayList<>();
                                        ArrayList<String> url1 = new ArrayList<>();
                                        ArrayList<String> stk1 = new ArrayList<>();
                                        //
                                        ArrayList<String> kd2 = new ArrayList<>();
                                        ArrayList<String> nm2 = new ArrayList<>();
                                        ArrayList<String> qty2 = new ArrayList<>();
                                        ArrayList<String> url2 = new ArrayList<>();
                                        ArrayList<String> stk2 = new ArrayList<>();
                                        //
                                        JSONArray jsonArray2 = object.getJSONArray("result_top");
                                        for (int position = 0; position < jsonArray2.length(); position++) {
                                            JSONObject row2 = jsonArray2.getJSONObject(position);
                                            kd1.add(row2.getString("Kode_Barang"));
                                            nm1.add(row2.getString("Nama_Barang"));
                                            qty1.add(row2.getString("total"));
                                            url1.add(apigambar + row2.getString("gambar"));
                                            stk1.add(row2.getString("Stock")+" Pcs");
                                        }
                                        JSONArray jsonArrayxx = object.getJSONArray("result_top_daily");
                                        for (int position = 0; position < jsonArrayxx.length(); position++) {
                                            JSONObject row33 = jsonArrayxx.getJSONObject(position);
                                            kd2.add(row33.getString("Kode_Barang"));
                                            nm2.add(row33.getString("Nama_Barang"));
                                            qty2.add(row33.getString("total"));
                                            url2.add(apigambar + row33.getString("gambar"));
                                            stk2.add(row33.getString("Stock")+" Pcs");
                                        }
                                        Cache.getInstance().putArrayItemCode(kd1);
                                        Cache.getInstance().putArrayItemName(nm1);
                                        Cache.getInstance().putArrayItemQty(qty1);
                                        Cache.getInstance().putArrayUrlGambar(url1);
                                        Cache.getInstance().putArrayStock(stk1);
                                        //
                                        Cache.getInstance().putArrayItemCode_daily(kd2);
                                        Cache.getInstance().putArrayItemName_daily(nm2);
                                        Cache.getInstance().putArrayItemQty_daily(qty2);
                                        Cache.getInstance().putArrayUrlGambar_daily(url2);
                                        Cache.getInstance().putArrayStock_daily(stk2);
                                        //
                                        itemcode_daily=kd2;
                                        itemname_daily=nm2;
                                        itemqty_daily=qty2;
                                        urlgambar_daily=url2;
                                        stock_daily=stk2;
                                        //
                                        itemcode=kd1;
                                        itemname=nm1;
                                        itemqty=qty1;
                                        urlgambar=url1;
                                        stock=stk1;
//                                        recycle_adapter = new Home_recycle_adapter(context,itemcode,itemname,itemqty,urlgambar,stock);
//                                        recycle_adapter2 = new Home_recycle_adapter(context,itemcode_daily,itemname_daily,itemqty_daily,urlgambar_daily,stock_daily);
                                        hm_bexpand.setVisibility(View.VISIBLE);
                                        if (show != null && show.isShowing()) {
                                            show.dismiss();
                                            hm_bexpand.callOnClick();
                                        }
                                    }
                                    //
                                    Ldayly.setText(Cache.getInstance().getDataList("daily"));
                                    Lmonthly.setText(Cache.getInstance().getDataList("monthly"));
                                    Ldayly_footwear.setText(Cache.getInstance().getDataList("daily"));
                                    Ldayly_clothing.setText(Cache.getInstance().getDataList("daily"));
                                    Ldayly_online.setText(Cache.getInstance().getDataList("daily"));
                                    Lmonthly_footwear.setText(Cache.getInstance().getDataList("monthly"));
                                    Lmonthly_clothing.setText(Cache.getInstance().getDataList("monthly"));
                                    Lmonthly_online.setText(Cache.getInstance().getDataList("monthly"));
                                    //
                                    lsumdaily_footwear.setText(Cache.getInstance().getDataList("dailyfootwear"));
                                    lsumdaily_footwear_qty.setText(Cache.getInstance().getDataList("dailyfootwear_qty"));
                                    lsummonthly_footwear.setText(Cache.getInstance().getDataList("monthlyfootwear"));
                                    lsummonthly_footwear_qty.setText(Cache.getInstance().getDataList("monthlyfootwear_qty"));
                                    lsumdaily_clothing.setText(Cache.getInstance().getDataList("dailyclothing"));
                                    lsumdaily_clothing_qty.setText(Cache.getInstance().getDataList("dailyclothing_qty"));
                                    lsummonthly_clothing.setText(Cache.getInstance().getDataList("monthlyclothing"));
                                    lsummonthly_clothing_qty.setText(Cache.getInstance().getDataList("monthlyclothing_qty"));
                                    lsumdaily_online.setText(Cache.getInstance().getDataList("dailyonline"));
                                    lsumdaily_online_qty.setText(Cache.getInstance().getDataList("dailyonline_qty"));
                                    lsummonthly_online.setText(Cache.getInstance().getDataList("monthlyonline"));
                                    lsummonthly_online_qty.setText(Cache.getInstance().getDataList("monthlyonline_qty"));
                                    lsumdaily.setText(Cache.getInstance().getDataList("sumdaily"));
                                    lsumdaily_qty.setText(Cache.getInstance().getDataList("sumdaily_qty"));
                                    lsummonthly.setText(Cache.getInstance().getDataList("summonthly"));
                                    lsummonthly_qty.setText(Cache.getInstance().getDataList("summonthly_qty"));
                                    //
                                    animdashboard.startAnimation(out);
                                    hm_brefresh.setVisibility(View.VISIBLE);
                                    run2 = true;
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("No Result");
                                    builder.setMessage("Server respond 400, mohon kontak developer");
                                    builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            System.exit(0);
                                        }
                                    });
                                    builder.show();
                                }
                            } catch (JSONException e) {
//                                Log.v("Parsing-Error", e.getMessage());
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("API Error");
                                builder.setCancelable(false);
                                builder.setMessage("Server tidak merespon, Mohon kontak developer...");
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
                            count = 0;
                            run2 = false;
//                        String message = null; // error message, show it in toast or dialog, whatever you want
                            if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                                if (Tkonkeksi.getVisibility() == View.INVISIBLE) {
                                    Tkonkeksi.startAnimation(in1);
                                    Tkonkeksi.setBackgroundResource(R.drawable.bg_sneakebar_rec);
                                    lkoneksi.setText("Reconnecting");
                                }
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        get1();
                                    }
                                }, 3000);
                            } else if (error instanceof ServerError) {
                                if (Tkonkeksi.getVisibility() == View.INVISIBLE) {
                                    Tkonkeksi.startAnimation(in1);
                                    Tkonkeksi.setBackgroundResource(R.drawable.bg_sneakebar_rec);
                                    lkoneksi.setText("Reconnecting");
                                }
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        get1();
                                    }
                                }, 3000);
                            } else if (error instanceof ParseError) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count = 0;
                }
            }, 500);
        }
    }

//    public void autorefresh() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (run2) {
//                    try {
//                        mHandler.post(new Runnable() {
//                            @RequiresApi(api = Build.VERSION_CODES.N)
//                            @Override
//                            public void run() {
//                                get1();
//                            }
//                        });
//                        Thread.sleep(sleeptime);
//                    }
//                    catch (Exception e) {
//                    }
//                }
//            }
//        }).start();
//    }
}
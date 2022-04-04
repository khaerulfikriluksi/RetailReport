package com.urban.urbanreport;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.urban.urbanreport.CustomClass.Cache;
import com.urban.urbanreport.CustomClass.DatabaseHelper;
import com.urban.urbanreport.CustomClass.DownloadService;
import com.urban.urbanreport.CustomClass.HttpHandler;
import com.urban.urbanreport.CustomClass.Outlet_list_adapter;
import com.google.firebase.messaging.FirebaseMessaging;
import com.urban.urbanreport.CustomClass.UpdateHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fsplash extends AppCompatActivity implements UpdateHelper.onUpdateCheckListener {
    Animation TextAnim;
    TextView LConnect;
    Animation in,out;
    SQLiteDatabase db;
//    String PublicIP="http://103.112.139.155/RetailReport/dashboard.php";
//    String PublicIP="http://192.168.1.11/RetailReport/dashboard.php";
//    String PublicIP="http://5a98-103-112-139-153.ngrok.io";
    String APIget;
    DatabaseHelper module;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ProgressDialog mProgressDialog;
    private int count = 0;
    private Boolean bypassMaintenance=false;
    Boolean isMaintenance = true;
    int appVersion = 0, percobaan=0;
    String ipConfig = "";
    String urlUpdate = "";
//    private FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fsplash);
//        remoteConfig = FirebaseRemoteConfig.getInstance();
//        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(3600)
//                .build();
//        remoteConfig.setConfigSettingsAsync(firebaseRemoteConfigSettings);
        database = FirebaseDatabase.getInstance("https://stockapp-4abf1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("settings");

        TextView LVersion = (TextView) findViewById(R.id.LVersion);
        try{
            String versionName = fsplash.this.getPackageManager()
                    .getPackageInfo(fsplash.this.getPackageName(), 0).versionName;
            int versioncode= fsplash.this.getPackageManager()
                    .getPackageInfo(fsplash.this.getPackageName(),0).versionCode;
            LVersion.setText("--Versi "+versionName+"."+String.valueOf(versioncode)+"--");
        } catch (PackageManager.NameNotFoundException e) {
            LVersion.setText("");
            e.printStackTrace();
        }

        Gettoken();
        GoogleRestAPI();
        Cache.getInstance().setDataList("FIRSTRUN","true");
        Cache.getInstance().setDataList("UPDATE","false");
        Cache.getInstance().putBooldata("rest_mode",false);
        module = new DatabaseHelper(this);
        db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        db.execSQL("delete from tbl_api");
        db.execSQL("DROP TABLE IF EXISTS `tbl_departemen`");
        db.execSQL("CREATE TABLE tbl_departemen (kode varchar(255) PRIMARY KEY, nama varchar(255))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tbl_detail_sales` (id INTEGER PRIMARY KEY, " +
                "Kode_Departemen varchar(255)," +
                "Departemen varchar(255)," +
                "Qty_Daily varchar(255)," +
                "Total_Daily varchar(255)," +
                "Qty_Bulan varchar(255)," +
                "Total_Bulan varchar(255)," +
                "Bulan varchar(255)," +
                "Tanggal DATE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tbl_subdetail_sales` (id INTEGER PRIMARY KEY, " +
                "Jenis_Store varchar(255)," +
                "Kode_Departemen varchar(255)," +
                "Departemen varchar(255)," +
                "Qty_Daily varchar(255)," +
                "Total_Daily varchar(255)," +
                "Qty_Bulan varchar(255)," +
                "Total_Bulan varchar(255)," +
                "Bulan varchar(255)," +
                "Tanggal DATE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tbl_detail_bestseller` (id INTEGER PRIMARY KEY, " +
                "Kode_Departemen varchar(255)," +
                "Kode_Barang varchar(255)," +
                "Nama_Barang varchar(255)," +
                "qty varchar(255)," +
                "Foto TEXT," +
                "Tanggal DATE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tbl_subdetail_bestseller` (id INTEGER PRIMARY KEY, " +
                "Jenis_Store varchar(255)," +
                "Kode_Departemen varchar(255)," +
                "Kode_Barang varchar(255)," +
                "Nama_Barang varchar(255)," +
                "qty varchar(255)," +
                "Foto TEXT," +
                "Tanggal DATE);");
        //
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pink));
            window.setNavigationBarColor(this.getResources().getColor(R.color.pink));
        }
        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(800);
        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(800);
        //
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LConnect.startAnimation(out);
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
                LConnect.startAnimation(in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //
        LConnect=(TextView) findViewById(R.id.LConnect);
        TextAnim= AnimationUtils.loadAnimation(fsplash.this,R.anim.splash_text_anim);
        LConnect.startAnimation(TextAnim);
        TextAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LConnect.startAnimation(out);
                requestPermissions();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //
            }
        });
    }

    private void Gettoken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FAIL-GET-TOKEN", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        Log.w("TOKEN", task.getResult());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TOKEN-error", "No token avaible");
            }
        });
    }

    private void getValueMaintenance() {
        myRef.child("app_sales_isMaintenance").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    if (urlUpdate.trim().length()>0) {
                        bypassfirebase();
                    } else {
                        percobaan++;
                        if (percobaan>=10) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                            builder.setTitle("Google Service");
                            builder.setCancelable(false);
                            builder.setMessage("Google services tidak merespon, silahkan kontak developer...");
                            builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    System.exit(0);
                                }
                            });
                            builder.show();
                        } else {
                            getValueMaintenance();
                        }
                    }
                }
                else {
                    Boolean maintenance = (Boolean) task.getResult().getValue();
                    if (maintenance==true) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                        builder.setTitle("Maintenance");
                        builder.setCancelable(false);
                        builder.setMessage("Sedang ada perbaikan sistem, mohon coba beberapa saat lagi...");
                        builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                System.exit(0);
                            }
                        });
                        builder.show();
                    } else {
//                        bypassfirebase();//disini
                        getValueFirebase();
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (urlUpdate.trim().length()>0) {
                            bypassfirebase();
                        } else {
                            percobaan++;
                            if (percobaan>=10) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                                builder.setTitle("Google Service");
                                builder.setCancelable(false);
                                builder.setMessage("Google services tidak merespon, silahkan kontak developer...");
                                builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        System.exit(0);
                                    }
                                });
                                builder.show();
                            } else {
                                getValueMaintenance();
                            }
                        }
                    }
                });
    }

    private void bypassfirebase(){
        if (ipConfig.trim().length() > 0) {
            Cache.getInstance().putBooldata("rest_mode",true);
            Cache.getInstance().setDataList("AppVersion",String.valueOf(appVersion));
            Log.v("FirebaseConfigResponse", "Value fetch : " + ipConfig);
            db.execSQL("INSERT INTO tbl_api (`nama`) values ('" + ipConfig + "')");
            APIget = ipConfig + "/api/sales/start";
            LConnect.setText("Checking for update...");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    UpdateHelper.with(fsplash.this)
                            .onUpdateCheck(fsplash.this)
                            .check();
                }
            }, 1000);
        } else {
            Log.v("FirebaseConfigResponse", "Task not successfully");
            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
            builder.setTitle("Server not found");
            builder.setCancelable(false);
            builder.setMessage("Server tidak ditemukan, silahkan kontak developer...");
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

    private void getValueFirebase() {
        myRef.child("ipconfig").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                     Log.e("firebase", "Error getting data", task.getException());
                     AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                     builder.setTitle("Application Error");
                     builder.setCancelable(false);
                     builder.setMessage("Google services tidak merespon, silahkan kontak developer...");
                     builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                             System.exit(0);
                         }
                     });
                     builder.show();
                }
                else {
                    String ipnya = String.valueOf(task.getResult().getValue());
                    if (ipnya.trim().length() > 0) {
                        Log.v("FirebaseConfigResponse", "Value fetch : " + ipnya);
                        db.execSQL("INSERT INTO tbl_api (`nama`) values ('" + ipnya + "')");
                        APIget = ipnya + "/api/sales/start";
                        LConnect.setText("Checking for update...");
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UpdateHelper.with(fsplash.this)
                                        .onUpdateCheck(fsplash.this)
                                        .check();
                            }
                        }, 1000);
                    } else {
                        Log.v("FirebaseConfigResponse", "Task not successfully");
                        AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                        builder.setTitle("Server not found");
                        builder.setCancelable(false);
                        builder.setMessage("Server tidak ditemukan, silahkan kontak developer...");
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
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 Log.v("FirebaseConfigResponse", "Connection lost");
                 AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                 builder.setTitle("Lost Connection");
                 builder.setCancelable(false);
                 builder.setMessage("Koneksi ke google gagal, silahkan kontak developer...");
                 builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.cancel();
                         System.exit(0);
                     }
                 });
                 builder.show();
            }
        });
    }

    private void requestPermissions() {
        Dexter.withActivity(fsplash.this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager()) {
                                    if (bypassMaintenance==true) {
                                        getValueFirebase();
                                    } else {
                                        getValueMaintenance();
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                                    builder.setTitle("Need Permissions");
                                    builder.setCancelable(false);
                                    builder.setMessage("Android 9 ke atas butuh akses tertentu, mohon izinkan");
                                    builder.setPositiveButton("Buka Pengaturan", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent permissionIntent = new Intent();
                                            permissionIntent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                            Uri uri = Uri.fromParts("package", fsplash.this.getPackageName(), null);
                                            permissionIntent.setData(uri);
                                            startActivity(permissionIntent);
                                            aWait();
                                        }
                                    });
                                    builder.show();
                                }
                            } else {
                                if (bypassMaintenance==true) {
                                    getValueFirebase();
                                } else {
                                    getValueMaintenance();
                                }
                            }
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
//                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
    }

    public void aWait() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void run() {
                Log.v("Running Check Update :","Waiting");
                if (Environment.isExternalStorageManager()) {
                    Log.v("Running Check Update :","True");
                    if (bypassMaintenance==true) {
                        getValueFirebase();
                    } else {
                        getValueMaintenance();
                    }
                    Thread.currentThread().interrupt();
                } else {
                    aWait();
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
        builder.setTitle("Need Permissions");
        builder.setCancelable(false);
        builder.setMessage("Aplikasi ini butuh akses. Anda dapat mengubah kembali akses di pengaturan");
        builder.setPositiveButton("Buka Pengaturan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                System.exit(0);
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void get() {
        LConnect.setText("Mengunduh data...");
        db.execSQL("DELETE FROM tbl_cabang");
        db.execSQL("DELETE FROM tbl_artikel");
        db.execSQL("DELETE FROM tbl_promosi");
        Log.v("API",APIget);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIget,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int stat = jsonObject.getInt("status");
                            if (stat==200) {
                                JSONObject obj = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = obj.getJSONArray("result_cabang");
                                for (int position = 0; position < jsonArray.length(); position++) {
                                    JSONObject row = jsonArray.getJSONObject(position);
                                    String kodecb = row.getString("kode");
                                    String namacb = row.getString("nama");
                                    String aliascb = row.getString("alias");
                                    String jenisstorecb = row.getString("jenis_store");
                                    String jeniscb = row.getString("jenis_cabang");
                                    String brandcb = row.getString("brand_cabang");
                                    String QryCabang = "INSERT INTO tbl_cabang ('kode','nama','alias','jenis_store','jenis_cabang','brand_cabang') " +
                                            "VALUES('" + kodecb + "','" + namacb + "','" + aliascb + "','" + jenisstorecb + "','" + jeniscb + "','" + brandcb + "');";
                                    db.execSQL(QryCabang);
                                }
                                JSONArray jsonArray1 = obj.getJSONArray("result_merk");
                                for (int pos1 = 0; pos1 < jsonArray1.length(); pos1++) {
                                    JSONObject row = jsonArray1.getJSONObject(pos1);
                                    String kodeart = row.getString("kode");
                                    String namart = row.getString("nama");
                                    String desart = row.getString("deskripsi");
                                    String qryart = "INSERT INTO tbl_artikel ('kode','nama','deskripsi') " +
                                            "VALUES('" + kodeart + "','" + namart + "','" + desart + "');";
                                    db.execSQL(qryart);
                                }
                                JSONArray jsonArray2 = obj.getJSONArray("result_departemen");
                                for (int pos2 = 0; pos2 < jsonArray2.length(); pos2++) {
                                    JSONObject row = jsonArray2.getJSONObject(pos2);
                                    String kodepro = row.getString("kode");
                                    String namapro = row.getString("nama");
                                    String qrypro = "INSERT INTO tbl_departemen ('kode','nama') " +
                                            "VALUES('" + kodepro + "','" + namapro + "');";
                                    db.execSQL(qrypro);
                                }
                                Cursor cr1 = db.rawQuery("SELECT * FROM tbl_cabang", null);
                                cr1.moveToFirst();
                                Cursor cr2 = db.rawQuery("SELECT * FROM tbl_artikel", null);
                                cr2.moveToFirst();
                                Cursor cr3 = db.rawQuery("SELECT * FROM tbl_departemen", null);
                                cr3.moveToFirst();
                                if (cr1.getCount()>0 && cr2.getCount()>0 && cr3.getCount()>0) {
                                    ConstraintLayout bg;
                                    bg = (ConstraintLayout) findViewById(R.id.splash_parent);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        int cx = bg.getWidth();
                                        int cy = 0;
                                        float finalRadius = Math.max(bg.getWidth(), bg.getHeight());
                                        Animator circularReveal = ViewAnimationUtils.createCircularReveal(bg, cx, cy, finalRadius, 0);

                                        circularReveal.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {
                                                Intent formku = new Intent(fsplash.this, FDashboardMenu.class);
                                                startActivity(formku);
                                                finish();
                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                bg.setVisibility(View.INVISIBLE);
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
                                        Intent formku = new Intent(fsplash.this, FDashboardMenu.class);
                                        startActivity(formku);
                                        finish();
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                                    builder.setTitle("No data avaible");
                                    builder.setCancelable(false);
                                    builder.setMessage("Tidak ada data di database...");
                                    builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                System.exit(0);
                                            }
                                        });
                                    builder.show();
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                            builder.setTitle("Application Error");
                            builder.setMessage("API System error, silahkan kontak developer...");
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
                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                            builder.setTitle("Connection problem");
                            builder.setMessage("Koneksi bermasalah, Silahkan cek kembali koneksi anda dan mulai kembali...");
                            builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    System.exit(0);
                                }
                            });
                            builder.show();
                        } else if (error instanceof ServerError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                            builder.setTitle("Connection problem");
                            builder.setMessage("Koneksi bermasalah, Silahkan cek kembali koneksi anda dan mulai kembali...");
                            builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    System.exit(0);
                                }
                            });
                            builder.show();
                        }  else if (error instanceof ParseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
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
                Map<String, String> headers = new HashMap<>();
                headers.put("API_KEY", "53713");
                return headers;
            }

            @Override
            public Response<String> parseNetworkResponse(NetworkResponse response) {
                String statusCode = String.valueOf(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(fsplash.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void GoogleRestAPI() {
        String url = "https://stockapp-4abf1-default-rtdb.asia-southeast1.firebasedatabase.app/settings.json?auth=uDm7ck9Iab8xVTri9DzmZ5QfRvYJIFPb0EqYrfvs";
        Log.v("API",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            isMaintenance = obj.getBoolean("app_sales_isMaintenance");
                            appVersion = obj.getInt("app_sales_version");
                            ipConfig = obj.getString("ipconfig");
                            urlUpdate = obj.getString("url_sales_app");
                            Log.v("Update",urlUpdate);
                        } catch (JSONException e) {
                            Log.v("Error",e.getMessage());
//                            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
//                            builder.setTitle("Google Cloud");
//                            builder.setMessage("Data cloud error, mohon kontak developer....");
//                            builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    System.exit(0);
//                                }
//                            });
//                            builder.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
//
//                        } else if (error instanceof ServerError) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
//                            builder.setTitle("Connection problem");
//                            builder.setMessage("Koneksi bermasalah, Silahkan cek kembali koneksi anda dan mulai kembali...");
//                            builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    System.exit(0);
//                                }
//                            });
//                            builder.show();
//                        }  else if (error instanceof ParseError) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
//                            builder.setTitle("Application error");
//                            builder.setCancelable(false);
//                            builder.setMessage("Google cloud tidak merespon, Mohon kontak developer...");
//                            builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    System.exit(0);
//                                }
//                            });
//                            builder.show();
//                        }
                    }
                })
        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("API_KEY", "53713");
//                return headers;
//            }

            @Override
            public Response<String> parseNetworkResponse(NetworkResponse response) {
                String statusCode = String.valueOf(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(fsplash.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onUpdateCheckListener(String url) {
        if (url.trim().contains("true")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
            builder.setTitle("Update avaible");
            builder.setCancelable(false);
            builder.setMessage("Versi terbaru tersedia, mohon update untuk melanjutkan...");
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (urlUpdate.trim().length()>0) {
                        mProgressDialog = new ProgressDialog(fsplash.this);
                        mProgressDialog.setMessage("Downloading update");
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show();
                        Intent intent = new Intent(fsplash.this, DownloadService.class);
                        intent.putExtra("url", urlUpdate);
                        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                        startService(intent);
                    } else {
                        myRef.child("url_sales_app").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (Cache.getInstance().isUpdate()) {
                                        Cache.getInstance().replace("UPDATE", "true");
                                    } else {
                                        Cache.getInstance().setDataList("UPDATE", "true");
                                    }
                                    String ur = String.valueOf(task.getResult().getValue());
                                    if (ur.trim().length() > 0) {
                                        mProgressDialog = new ProgressDialog(fsplash.this);
                                        mProgressDialog.setMessage("Downloading update");
                                        mProgressDialog.setIndeterminate(true);
                                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        mProgressDialog.setCancelable(false);
                                        mProgressDialog.show();
                                        Intent intent = new Intent(fsplash.this, DownloadService.class);
                                        intent.putExtra("url", ur);
                                        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                                        startService(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                                        builder.setTitle("Application error");
                                        builder.setCancelable(false);
                                        builder.setMessage("Tidak ada URL update, Mohon kontak developer...");
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
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(fsplash.this);
                                        builder.setTitle("Application error");
                                        builder.setCancelable(false);
                                        builder.setMessage("Gagal mengambil URL Update, Mohon kontak developer...");
                                        builder.setPositiveButton("Tutup aplikasi", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                System.exit(0);
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                    }
                }
            });
            builder.show();
        } else {
            get();
        }
    }

    private class DownloadReceiver extends ResultReceiver{

        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                mProgressDialog.setIndeterminate(false);
                int progress = resultData.getInt("progress"); //get the progress
                Log.v("DownloadProgress",String.valueOf(progress));
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                    File toInstall = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "RetailReport" + ".apk");
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        count++;
                        if (count ==1) {
                            Log.v("Update", "Installing : " + toInstall);
                            Uri apkUri = FileProvider.getUriForFile(fsplash.this, BuildConfig.APPLICATION_ID + ".fileprovider", toInstall);
                            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.setData(apkUri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
//                            finish();
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    count = 0;
                                }
                            }, 2000);
                        }
                    } else {
                        count++;
                        if (count ==1) {
                            Log.v("Update", "Installing 2 : "+toInstall);
                            Uri apkUri = Uri.fromFile(toInstall);
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
//                            finish();
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
                }
            }
        }
    }

}
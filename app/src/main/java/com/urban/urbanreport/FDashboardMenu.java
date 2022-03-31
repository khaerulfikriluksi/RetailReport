package com.urban.urbanreport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.urban.urbanreport.CustomClass.Cache;
import com.urban.urbanreport.CustomClass.Home_recycle_adapter;

import java.util.Calendar;

public class FDashboardMenu extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private Fragment fragment1 = new FragmentDashboard();
    private Fragment fragment2 = new FragmentMenu();
    final FragmentManager fm = getSupportFragmentManager();
    private ChipNavigationBar bottomBar;
    private Fragment active = fragment1;
    private AlertDialog show;
    private int versekarang,versibaru;
    private String ver;
    private AlertDialog.Builder builder;
    private SharedPreferences prefs;
    private SharedPreferences.Editor CacheEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fdashboard_menu);
        prefs = getSharedPreferences("versi_app", MODE_PRIVATE);
        versekarang=prefs.getInt("ver", 0);
        CacheEditor = prefs.edit();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
            window.setNavigationBarColor(this.getResources().getColor(R.color.ungu_muda));
        }
        try{
            String versionName = FDashboardMenu.this.getPackageManager()
                    .getPackageInfo(FDashboardMenu.this.getPackageName(), 0).versionName;
            int versioncode= FDashboardMenu.this.getPackageManager()
                    .getPackageInfo(FDashboardMenu.this.getPackageName(),0).versionCode;
            ver=versionName+"."+String.valueOf(versioncode);
            String vv=ver.replace(".","");
            versibaru=Integer.valueOf(vv);
        } catch (PackageManager.NameNotFoundException e) {
            ver="";
            e.printStackTrace();
        }
        bottomBar = (ChipNavigationBar) findViewById(R.id.bottomBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentDashboard()).commit();
        bottomBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.item0:
                        setFragment(fragment1, "1", 0);
                        break;
                    case R.id.item1:
                        setFragment(fragment2, "2", 1);
                        break;
                }
            }
        });
//        bottomBar.setItemSelected(R.id.item1, true);
        bottomBar.setItemSelected(R.id.item0, true);
        if (versekarang<versibaru) {
            CacheEditor.remove("ver");
            CacheEditor.commit();
            CacheEditor.putInt("ver",versibaru);
            CacheEditor.commit();
            builder = new AlertDialog.Builder(FDashboardMenu.this);
            LayoutInflater inflater = FDashboardMenu.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.cus_popup_newupdate, null);
            TextView updt_notes = (TextView) view.findViewById(R.id.updt_notes),
                    updt_version = (TextView) view.findViewById(R.id.updt_version),
                    updt_date = (TextView) view.findViewById(R.id.updt_date);
            updt_version.setText("New Update "+ver);
            updt_notes.setText("1. Memperbaiki masalah google service pada device tertentu.\n" +
                    "2. Device HUAWEI tanpa google sekarang bisa di akses.");
            updt_notes.setMaxLines(7);
            updt_notes.setLines(7);
            updt_date.setText("2022-03-17");
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
    }

    private void setFragment(Fragment fragment, String tag, int position) {
        if (fragment.isAdded()) {
            fm
                    .beginTransaction()
                    .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                    .hide(active)
                    .show(fragment)
                    .commit();
        } else {
            fm
                    .beginTransaction()
                    .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                    .add(R.id.fragment_container, fragment, tag)
                    .commit();
        }
        switch (position){
            case R.id.item0:
                bottomBar.setItemSelected(R.id.item0, true);
                break;
            case R.id.item1:
                bottomBar.setItemSelected(R.id.item1, true);
                break;
        }
        active = fragment;
    }

    @Override
    public void onBackPressed() {
        if (active == fragment1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        } else {
//            setFragment(fragment1, "1", 0);
            bottomBar.setItemSelected(R.id.item0, true);
        }
    }

    public static boolean isFirstInstall(Context context) {
        try {
            long firstInstallTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
            return firstInstallTime == lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return true;
        }
    }



    public static boolean isInstallFromUpdate(Context context) {
        try {
            long firstInstallTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
            return firstInstallTime != lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
package com.urban.urbanreport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.urban.urbanreport.CustomClass.ViewPager_Adapter;

import java.util.ArrayList;
import java.util.Arrays;

public class FDetail_ViewPager extends AppCompatActivity {

    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> kodecb = new ArrayList<>();
    private TabLayout pgr_tab;
    private ViewPager pgr_viewpager;
    private ViewPager_Adapter adapter;
    private ImageButton pgr_bmore;
    private AlertDialog show;
    private String menu;
    private androidx.appcompat.widget.Toolbar pgr_toolbar;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fdetail_viewpager);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.sls_header1));
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
        }
        pgr_tab = (TabLayout) findViewById(R.id.pgr_tab);
        pgr_viewpager = (ViewPager) findViewById(R.id.pgr_viewpager);
        pgr_bmore = (ImageButton) findViewById(R.id.pgr_bmore);
        pgr_toolbar = (Toolbar) findViewById(R.id.pgr_toolbar);
        setSupportActionBar(pgr_toolbar);
        pgr_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menu = getIntent().getExtras().getString("menu");
        title = getIntent().getExtras().getStringArrayList("arr");
        kodecb = getIntent().getExtras().getStringArrayList("kode");

        prepareadapter(pgr_viewpager,title);
        pgr_tab.setupWithViewPager(pgr_viewpager);
        pgr_bmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(FDetail_ViewPager.this);
                LayoutInflater inflater = FDetail_ViewPager.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.cus_popup_scan, null);
                AutoCompleteTextView e_spin = (AutoCompleteTextView) view.findViewById(R.id.e_spin);
                TextInputLayout e_spin_head = (TextInputLayout) view.findViewById(R.id.e_spin_head);
                TextInputLayout e_search_head = (TextInputLayout) view.findViewById(R.id.e_search_head);
                e_search_head.setVisibility(View.GONE);
                e_spin.setText(adapter.getPageTitle(pgr_viewpager.getCurrentItem()));
                e_spin_head.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adap = new ArrayAdapter<String>(FDetail_ViewPager.this,
                        R.layout.cus_dropdown_spiner, title);
                e_spin.setAdapter(adap);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int index = -1;
                        for (int i=0;i < title.size();i++) {
                            if (title.get(i).equals(e_spin.getText().toString())) {
                                index=i;
                                break;
                            }
                        }
                        pgr_viewpager.setCurrentItem(index,true);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
//        pgr_viewpager.setCurrentItem();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void prepareadapter(ViewPager pager, ArrayList<String> titlenya) {
        adapter = new ViewPager_Adapter(getSupportFragmentManager());
        Fragment_adapter fragment = new Fragment_adapter();
        for (int i=0;i < titlenya.size(); i++) {
            if (menu.equals("BestSeller")) {
                Bundle bundle = new Bundle();
                bundle.putString("kode",kodecb.get(i));
                bundle.putString("menu",menu);
                fragment.setArguments(bundle);
                adapter.addFragment(fragment,titlenya.get(i));
                fragment = new Fragment_adapter();
            }
        }
        pager.setAdapter(adapter);
    }
}
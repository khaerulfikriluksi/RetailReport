package com.urban.urbanreport;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.urban.urbanreport.CustomClass.BestSeller_list_adapter;
import com.urban.urbanreport.CustomClass.Detail_BestSeller_Adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Fragment_adapter extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fragment_adapter() {

    }

    public static Fragment_adapter newInstance(String param1, String param2) {
        Fragment_adapter fragment = new Fragment_adapter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adapter, container, false);
        SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/com.urban.urbanreport/databases/report.db", null,
                SQLiteDatabase.OPEN_READWRITE);
        EditText frg_search = (EditText) v.findViewById(R.id.frg_search);
        ListView frg_listview = (ListView) v.findViewById(R.id.frg_listview);
        String menu = getArguments().getString("menu").toUpperCase();
        String kode = getArguments().getString("kode").toUpperCase();
        if (menu.equals("BESTSELLER")) {
            ArrayList<String> nama_barang = new ArrayList<>();
            ArrayList<String> artikel = new ArrayList<>();
            ArrayList<String> season = new ArrayList<>();
            ArrayList<String> departemen = new ArrayList<>();
            ArrayList<String> kelompok = new ArrayList<>();
            ArrayList<String> subkelompok = new ArrayList<>();
            ArrayList<String> penjualan = new ArrayList<>();
            ArrayList<String> Stock = new ArrayList<>();
            ArrayList<String> gambar = new ArrayList<>();
            Cursor cur;
            if (kode.equals("ALL")) {
                cur = db.rawQuery("SELECT `Nama_Barang`, `artikel`, `season`, `departemen`,`kelompok`,`subkelompok`, sum(`qty`) as `qty`, sum(`value`) as `value`, sum(`Stock`) as `Stock`, `gambar` FROM `tbl_bestseller_dump` GROUP BY `Nama_Barang`",null);
            } else {
                cur = db.rawQuery("SELECT `Nama_Barang`, `artikel`, `season`, `departemen`,`kelompok`,`subkelompok`, sum(`qty`) as `qty`, sum(`value`) as `value`, sum(`Stock`) as `Stock`, `gambar` FROM `tbl_bestseller_dump` WHERE `Kode_Cabang`='"+kode+"' GROUP BY `Nama_Barang`",null);
            }
            cur.moveToFirst();
            if (cur.getCount() > 0) {
                if (cur.moveToFirst()) {
                    do {
                        nama_barang.add(cur.getString(0));
                        artikel.add(cur.getString(1));
                        season.add(cur.getString(2));
                        departemen.add(cur.getString(3));
                        kelompok.add(cur.getString(4));
                        subkelompok.add(cur.getString(5));
                        penjualan.add("Rp. "+currency(cur.getString(7))+" ("+cur.getString(6)+" Pcs)");
                        Stock.add(cur.getString(8)+" Pcs");
                        gambar.add(cur.getString(9));
                    } while (cur.moveToNext());
                }
                frg_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length()>0 && s.toString()!=null) {
                            ArrayList<String> nama_barang1 = new ArrayList<>();
                            ArrayList<String> artikel1 = new ArrayList<>();
                            ArrayList<String> season1 = new ArrayList<>();
                            ArrayList<String> departemen1 = new ArrayList<>();
                            ArrayList<String> kelompok1 = new ArrayList<>();
                            ArrayList<String> subkelompok1 = new ArrayList<>();
                            ArrayList<String> penjualan1 = new ArrayList<>();
                            ArrayList<String> Stock1 = new ArrayList<>();
                            ArrayList<String> gambar1 = new ArrayList<>();
                            for (int i=0;i < nama_barang.size();i++) {
                                if (nama_barang.get(i).toLowerCase().contains(s.toString().toLowerCase())) {
                                    nama_barang1.add(nama_barang.get(i));
                                    artikel1.add(artikel.get(i));
                                    season1.add(season.get(i));
                                    departemen1.add(departemen.get(i));
                                    kelompok1.add(kelompok.get(i));
                                    subkelompok1.add(subkelompok.get(i));
                                    penjualan1.add(penjualan.get(i));
                                    Stock1.add(Stock.get(i));
                                    gambar1.add(gambar.get(i));
                                }
                            }
                            Detail_BestSeller_Adapter myAdapter = new Detail_BestSeller_Adapter(getContext(),nama_barang1,artikel1,season1,departemen1,kelompok1,subkelompok1,penjualan1,Stock1,gambar1);
                            frg_listview.setAdapter(myAdapter);
                            frg_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    previewphoto(myAdapter.getImageurl(position));
                                }
                            });
                        } else {
                            Detail_BestSeller_Adapter myAdapter = new Detail_BestSeller_Adapter(getContext(),nama_barang,artikel,season,departemen,kelompok,subkelompok,penjualan,Stock,gambar);
                            frg_listview.setAdapter(myAdapter);
                            frg_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    previewphoto(myAdapter.getImageurl(position));
                                }
                            });
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                Detail_BestSeller_Adapter myAdapter = new Detail_BestSeller_Adapter(getContext(),nama_barang,artikel,season,departemen,kelompok,subkelompok,penjualan,Stock,gambar);
                frg_listview.setAdapter(myAdapter);
                frg_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        previewphoto(myAdapter.getImageurl(position));
                    }
                });
            }
        }
        return v;
    }

    private void previewphoto(String photourl) {
        if (photourl.contains("amogos.webp")) {
            Toast.makeText(getContext(),"No image avaible", Toast.LENGTH_LONG).show();
        } else {
            Intent formku = new Intent(getContext(), Fimage_popup.class);
            formku.putExtra("url",photourl);
            startActivity(formku);
        }
    }

    private String currency(String num) {
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
package com.urban.urbanreport;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMenu extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CardView MenuOutlet,MenuCategory,MenuArtikel,
            MenuPromosi,MenuItems,MenuStock,MenuDiscovery,
            MenuBestSeller;
    private int count1 = 0;

    private String mParam1;
    private String mParam2;

    public FragmentMenu() {
    }

    public static FragmentMenu newInstance(String param1, String param2) {
        FragmentMenu fragment = new FragmentMenu();
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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_menu, container, false);
        MenuOutlet = (CardView) v.findViewById(R.id.MenuOutlet);
        MenuOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuOutlet();
            }
        });
        MenuCategory = (CardView) v.findViewById(R.id.MenuCategory);
        MenuCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuCategory();
            }
        });
        MenuArtikel= (CardView) v.findViewById(R.id.MenuArtikel);
        MenuArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuArtikel();
            }
        });
        MenuPromosi = (CardView) v.findViewById(R.id.MenuPromosi);
        MenuPromosi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuPromosi();
            }
        });
        MenuItems = (CardView) v.findViewById(R.id.MenuItems);
        MenuItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItems();
            }
        });
        MenuStock = (CardView) v.findViewById(R.id.MenuStock);
        MenuStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuStock();
            }
        });
        MenuDiscovery = (CardView) v.findViewById(R.id.MenuDiscovery);
        MenuDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDiscovery();
            }
        });
        MenuBestSeller = (CardView) v.findViewById(R.id.MenuBestSeller);
        MenuBestSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuBestSeller();
            }
        });
        return v;
    }

    public void MenuBestSeller() {
        count1++;
        if (count1 == 1) {
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
                        Intent formku = new Intent(getContext(), FBestSeller.class);
                        startActivity(formku);
                        getActivity().finish();
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
                Intent formku = new Intent(getContext(), FBestSeller.class);
                startActivity(formku);
                getActivity().finish();
            }
        } else {
//            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count1 = 0;
                }
            }, 2000);
        }
    }

    public void MenuDiscovery() {
        count1++;
        if (count1 == 1) {
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
                        Intent formku = new Intent(getContext(), FItemsDiscovery.class);
                        startActivity(formku);
                        getActivity().finish();
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
                Intent formku = new Intent(getContext(), FItemsDiscovery.class);
                startActivity(formku);
                getActivity().finish();
            }
        } else {
//            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count1 = 0;
                }
            }, 2000);
        }
    }

    //OpenMenuOulet
    public void MenuStock () {
        count1++;
        if (count1 == 1) {
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
                        Intent formku = new Intent(getContext(), Fstock.class);
                        startActivity(formku);
                        getActivity().finish();
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
                Intent formku = new Intent(getContext(), Fstock.class);
                startActivity(formku);
                getActivity().finish();
            }
        } else {
//            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count1 = 0;
                }
            }, 2000);
        }
    }

    //OpenMenuOulet
    public void MenuItems () {
        count1++;
        if (count1 == 1) {
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
                        Intent formku = new Intent(getContext(), Fitems.class);
                        startActivity(formku);
                        getActivity().finish();
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
                Intent formku = new Intent(getContext(), Fitems.class);
                startActivity(formku);
                getActivity().finish();
            }
        } else {
//            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count1 = 0;
                }
            }, 2000);
        }
    }

    //OpenMenuOulet
    public void MenuPromosi () {
        count1++;
        if (count1 == 1) {
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
                        Intent formku = new Intent(getContext(), Fpromosi.class);
                        startActivity(formku);
                        getActivity().finish();
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
                Intent formku = new Intent(getContext(), Fpromosi.class);
                startActivity(formku);
                getActivity().finish();
            }
        } else {
//            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count1 = 0;
                }
            }, 2000);
        }
    }

    //OpenMenuOulet
    public void MenuArtikel () {
        count1++;
        if (count1 == 1) {
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
                        Intent formku = new Intent(getContext(), Fartikel.class);
                        startActivity(formku);
                        getActivity().finish();
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
                Intent formku = new Intent(getContext(), Fartikel.class);
                startActivity(formku);
                getActivity().finish();
            }
        } else {
//            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count1 = 0;
                }
            }, 2000);
        }
    }

    //OpenMenuOulet
    public void MenuCategory () {
        count1++;
        if (count1 == 1) {
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
                        Intent formku = new Intent(getContext(), Fcategory.class);
                        startActivity(formku);
                        getActivity().finish();
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
                Intent formku = new Intent(getContext(), Fcategory.class);
                startActivity(formku);
                getActivity().finish();
            }
        } else {
//            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count1 = 0;
                }
            }, 2000);
        }
    }

    //OpenMenuOulet
    public void MenuOutlet (){
        count1++;
        if (count1 ==1) {
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
                        Intent formku = new Intent(getContext(), Foutlet.class);
                        startActivity(formku);
                        getActivity().finish();
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
                Intent formku = new Intent(getContext(), Foutlet.class);
                startActivity(formku);
                getActivity().finish();
            }
        } else {
//            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count1 = 0;
                }
            }, 2000);
        }
    }
}
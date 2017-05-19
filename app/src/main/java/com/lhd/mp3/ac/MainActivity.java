package com.lhd.mp3.ac;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lhd.mp3.R;
import com.lhd.mp3.fm.FmListTop100;
import com.lhd.mp3.fm.FmTimVaTaiBai;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private AdView mAdView;
    private TabLayout tabLayout;
    private FmTimVaTaiBai fmTimVaTaiBai;
    private FmListTop100 fmListTop100;
    private ViewPager viewPager;
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }catch (Exception e) {return false;}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        if (!isOnline(this)){
            Toast.makeText(this, "Please, make your phone connect to internet.", Toast.LENGTH_SHORT).show();
            finish();
        }
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setOnTabSelectedListener(this);
        fmTimVaTaiBai = new FmTimVaTaiBai();
        fmListTop100 = new FmListTop100();
        viewPager = (ViewPager) findViewById(R.id.frame_layout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return fmTimVaTaiBai;
                } else return fmListTop100;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "Search Music";
                } else return "Album Top 100";
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        int i = tab.getPosition();
        if (i == 0) {
        } else {
            fmListTop100.getListTopMp3(fmListTop100.getHandlerTop());
        }
    }
}

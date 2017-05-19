package com.lhd.mp3.adaptor;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lhd.mp3.R;
import com.lhd.mp3.item.ItemTop;

import java.util.ArrayList;

/**
 * Created by D on 5/17/2017.
 */

public class ListTop100Adaptor extends ArrayAdapter<ItemTop> {
    ProgressDialog progressDialog;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ItemTop> objects;
    private AdView mAdView;

    public ListTop100Adaptor(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ItemTop> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            View view = inflater.inflate(R.layout.item_ads, null);
            mAdView = (AdView) view.findViewById(R.id.adView_nho);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            return view;
        }
        View view = inflater.inflate(R.layout.item_song, null);
        TextView tvTitle, tvLuotNghe, tv_xem;
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tv_xem = (TextView) view.findViewById(R.id.tv_xem);
        tvTitle.setText(objects.get(position - 1).getTitle());
        tv_xem.setText("Click to view all");
        return view;
    }
}

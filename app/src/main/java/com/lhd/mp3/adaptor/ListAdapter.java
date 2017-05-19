package com.lhd.mp3.adaptor;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lhd.mp3.R;
import com.lhd.mp3.ac.MainActivity;
import com.lhd.mp3.fm.FmTimVaTaiBai;
import com.lhd.mp3.item.ItemSong;
import com.lhd.mp3.parser.ParserHTML;

import java.util.ArrayList;

/**
 * Created by D on 5/16/2017.
 */

public class ListAdapter extends ArrayAdapter<ItemSong> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ItemSong> objects;
    private AdView mAdView;

    public ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ItemSong> objects) {
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
        final View view = inflater.inflate(R.layout.item_song, null);
        TextView tvTitle, tvLuotNghe, tv_xem;
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tv_xem = (TextView) view.findViewById(R.id.tv_xem);
        tvLuotNghe = (TextView) view.findViewById(R.id.tvLuotNghe);
        tvTitle.setText(objects.get(position - 1).getTitle());
        final MainActivity mainActivity = (MainActivity) context;
        if (FmTimVaTaiBai.isVideo())
            tv_xem.setText("Click to download...");
        else tv_xem.setText("Click to download...");
        tvLuotNghe.setText(objects.get(position).getLuotnghe() + " listen");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(view, "Ready to download " + objects.get(position - 1).getTitle(), Snackbar.LENGTH_SHORT).show();
                if (FmTimVaTaiBai.isVideo())
                    ParserHTML.downloadMp4ByLinkPlay(objects.get(position - 1).getLink(), context);
                else
                    ParserHTML.downloadMp3ByLinkPlay(objects.get(position - 1).getLink(), context);
            }
        });
        return view;
    }
}

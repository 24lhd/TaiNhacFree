package com.lhd.mp3.fm;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lhd.mp3.R;
import com.lhd.mp3.ac.MainActivity;
import com.lhd.mp3.adaptor.ListAdapter;
import com.lhd.mp3.item.ItemSong;
import com.lhd.mp3.parser.ParserHTML;

import java.util.ArrayList;

/**
 * Created by D on 5/17/2017.
 */

public class FmTimVaTaiBai extends Fragment {
    private ArrayList<ItemSong> itemSongs;
    private ListView lv;
    EditText etSreach;
    ImageButton imageButton;
    ProgressDialog progressDialog;
    SwitchCompat switchCompat;
    TextView tvVideo;
    TextView tvMp3;
    static boolean isVideo;
    private View viewCon;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCon = inflater.inflate(R.layout.activity_main, null);
        mainActivity = (MainActivity) getActivity();
        return viewCon;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lv = (ListView) view.findViewById(R.id.lvItems);
        etSreach = (EditText) view.findViewById(R.id.txtSreach);
        imageButton = (ImageButton) view.findViewById(R.id.btSreach);
        progressDialog = new ProgressDialog(mainActivity);
        tvVideo = (TextView) view.findViewById(R.id.tvVideo);
        tvMp3 = (TextView) view.findViewById(R.id.tvMp3);
        switchCompat = (SwitchCompat) view.findViewById(R.id.scpSelect);
        isVideo = false;
        tvVideo.setTextColor(ColorStateList.valueOf(Color.BLACK));
        tvMp3.setTextColor(ColorStateList.valueOf(Color.RED));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isVideo = isChecked;
                if (isChecked) {
                    tvVideo.setTextColor(Color.RED);
                    tvMp3.setTextColor(Color.BLACK);
                } else {
                    tvVideo.setTextColor(Color.BLACK);
                    tvMp3.setTextColor(Color.RED);
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etSreach.getText().toString();
                if (text.isEmpty())
                    Snackbar.make(viewCon,"Empty",Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(mainActivity, "Empty", Toast.LENGTH_SHORT).show();
                else {
                    text = text.replace(" ", "+");
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    if (isVideo)
                        ParserHTML.searchMp4(text, handler);
                    else
                        ParserHTML.searchMp3(text, handler);
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                itemSongs= (ArrayList<ItemSong>) msg.obj;
                if (itemSongs.size() == 0)
                    Snackbar.make(viewCon,"Not found",Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(mainActivity, "Not found", Toast.LENGTH_SHORT).show();
                else
                    lv.setAdapter(new ListAdapter(mainActivity, android.R.layout.simple_list_item_2, itemSongs));
                progressDialog.dismiss();
            }catch (NullPointerException e){
                Snackbar.make(viewCon,"Not found",Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(mainActivity, "Not found", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public static boolean isVideo() {
        return isVideo;
    }
}

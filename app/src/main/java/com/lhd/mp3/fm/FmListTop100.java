package com.lhd.mp3.fm;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lhd.mp3.R;
import com.lhd.mp3.ac.MainActivity;
import com.lhd.mp3.adaptor.ListAdapter;
import com.lhd.mp3.adaptor.ListTop100Adaptor;
import com.lhd.mp3.item.ItemSong;
import com.lhd.mp3.item.ItemTop;
import com.lhd.mp3.parser.ParserHTML;

import org.jsoup.select.Elements;

import java.util.ArrayList;


/**
 * Created by D on 5/17/2017.
 */

public class FmListTop100 extends Fragment {
    private View viewCon;
    private MainActivity mainActivity;
    private ListView listView;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCon = inflater.inflate(R.layout.list_top_100_layout, null);
        mainActivity = (MainActivity) getActivity();
        progressDialog = new ProgressDialog(mainActivity);
        listView = (ListView) viewCon.findViewById(R.id.lvTop100Album);
        getListTopMp3(handlerTop);
        return viewCon;
    }

    public Handler getHandlerTop() {
        return handlerTop;
    }

    private ArrayList<ItemTop> itemTops;
    Handler handlerTop = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                itemTops = (ArrayList<ItemTop>) msg.obj;
//                listView.removeAllViews();
                listView.setAdapter(new ListTop100Adaptor(mainActivity, android.R.layout.simple_list_item_2, itemTops));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        getTop100Mp3(itemTops.get(position-1).getLink(), handlerSong);
                    }
                });
                progressDialog.dismiss();
            } catch (Exception e) {
                Snackbar.make(viewCon,"Error! Check back", Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(mainActivity, "Error! Check back", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private ArrayList<ItemSong> itemSongs;
    Handler handlerSong = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                itemSongs = (ArrayList<ItemSong>) msg.obj;
                listView.setAdapter(new ListAdapter(mainActivity, android.R.layout.simple_list_item_2, itemSongs));
                progressDialog.dismiss();
            } catch (Exception e) {
                Snackbar.make(viewCon,"Error! Check back",Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(mainActivity, "Error! Check back", Toast.LENGTH_SHORT).show();
            }
        }
    };
    public void getListTopMp3(final Handler handler) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Elements elements = ParserHTML.getDocumentHTMLByURL("http://mp3.zing.vn/chu-de/Top-100-Hay-Nhat/IWZ9ZI68.html").select(".album-item");
                String str = elements.html();
                ArrayList<ItemTop> itemTops = new ArrayList<ItemTop>();
                for (int i = 0; i < elements.size(); i++) {
                    String link = "http://mp3.zing.vn" + elements.get(i).select("a").get(0).attr("href");
                    String title = elements.get(i).select("a").get(0).attr("title");
                    title = title.replace("-", "");
                    itemTops.add(new ItemTop(link, title));
                }
                Message message = new Message();
                message.obj = itemTops;
                handler.sendMessage(message);
                Log.e("faker", "end");
            }
        }).start();
    }

    private void getTop100Mp3(final String url, final Handler handler) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Elements newsHeadlines = ParserHTML.getDocumentHTMLByURL(url).select(".item-song");
                ArrayList<ItemSong> itemSongs = new ArrayList<ItemSong>();
                for (int i = 0; i < newsHeadlines.size(); i++) {
                    String link = newsHeadlines.get(i).select("a").get(0).attr("href");
                    String title = newsHeadlines.get(i).select("a").get(0).attr("title");
                    itemSongs.add(new ItemSong(link.replace("http://mp3.zing.vn",""), title, "unknow"));
                }
                Message message = new Message();
                message.obj = itemSongs;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

}

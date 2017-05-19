package com.lhd.mp3.parser;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lhd.mp3.item.ItemSong;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by D on 5/17/2017.
 */

public class ParserHTML {

    /**
     * tìm kiếm một video qua tên
     *
     * @param noiDung
     * @param handler
     */
    public static void searchMp4(final String noiDung, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Document doc = null;
                ArrayList<Object> itemSongs;
                itemSongs = new ArrayList<>();
                try {
                    doc = Jsoup.connect("http://mp3.zing.vn/tim-kiem/video.html?q=" + noiDung).get();
                    Elements newsHeadlines = doc.select("ul.video-list").select("li");
                    for (int i = 0; i < newsHeadlines.size(); i++) {
                        String link = newsHeadlines.select(".item").get(i).select("a").get(0).attr("href");
                        String title = newsHeadlines.select(".item").get(i).select("a").get(0).attr("title");
                        String luotnghe = newsHeadlines.get(i).select(".fn-number").get(0).text();
                        itemSongs.add(new ItemSong(link, title, luotnghe));
                    }
                } catch (Exception e) {
                    Log.e("faker", "Exception");
                    handler.sendEmptyMessage(0);
                }
                message.obj = itemSongs;
                handler.sendMessage(message);

            }
        }).start();
    }

    /**
     * tìm kiếm một list bài hát qua tên
     *
     * @param noiDung
     * @param handler
     */
    public static void searchMp3(final String noiDung, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Document doc = null;
                ArrayList<Object> itemSongs;
                itemSongs = new ArrayList<>();
                for (int j = 1; j < 3; j++) {
                    try {
                        doc = Jsoup.connect("http://mp3.zing.vn/tim-kiem/bai-hat.html?q=" + noiDung + "&page=" + j).get();
                        Elements newsHeadlines = doc.select(".item-song");
                        for (int i = 0; i < newsHeadlines.size(); i++) {
                            String link = newsHeadlines.select(".title-song").get(i).select("a").get(0).attr("href");
                            String title = newsHeadlines.select(".title-song").get(i).select("a").get(0).attr("title");
                            String luotnghe = newsHeadlines.get(i).select(".fn-number").get(0).text();
                            itemSongs.add(new ItemSong(link, title, luotnghe));
                        }
                    } catch (Exception e) {
                        handler.sendEmptyMessage(0);
                    }
                }
                message.obj = itemSongs;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * tải nhạc về qua link play nhạc
     *
     * @param linkPlayMp3
     * @param context
     */
    public static void downloadMp3ByLinkPlay(final String linkPlayMp3, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(linkPlayMp3).get();
                    Element newsHeadlines = doc.getElementById("zplayerjs-wrapper");
                    String json = readContentUrl("http://mp3.zing.vn" + newsHeadlines.attr("data-xml"));
                    JSONObject jsonObject = new JSONObject(json.trim());
                    String tenBaiHat = jsonObject.getJSONArray("data").getJSONObject(0).getString("name");
                    String tacGia = jsonObject.getJSONArray("data").getJSONObject(0).getString("artist");
                    String linkFile = jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("source_list").get(0).toString();
                    downloadToFoderDownload(tenBaiHat + " - " + tacGia + ".mp3", linkFile, context);
                } catch (Exception e) {
                    Log.e("faker", "Exception");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * đọc nội dung của url truyền tới và trả về
     *
     * @param urlString
     * @return
     * @throws Exception
     */
    private static String readContentUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    /**
     * tải về 1 mot file qua đối tuong Download của android
     *
     * @param fileName
     * @param url
     * @param context
     */
    public static void downloadToFoderDownload(final String fileName, String url, final Context context) {
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);
        File file = new File(destination);
        if (file.exists())
            file.delete();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("File comming to folder Download");
        request.setTitle("Downloading " + fileName);
        request.setDestinationUri(uri);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = manager.enqueue(request);
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
//                Snackbar.make(view,"Already downloaded " + fileName + " to folder  Download",Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(ctxt, "Already downloaded " + fileName + " to folder  Download", Toast.LENGTH_SHORT).show();
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * @param link
     * @param context
     */
    public static void downloadMp4ByLinkPlay(final String link, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(link).get();
                    Log.e("faker", link);
                    Element newsHeadlines = doc.getElementById("zplayerjs-wrapper");
                    String json = readContentUrl("http://mp3.zing.vn" + newsHeadlines.attr("data-xml"));
                    JSONObject jsonObject = new JSONObject(json.trim());
                    String tenVideo = jsonObject.getJSONObject("data").getJSONArray("item").getJSONObject(0).getString("title");
                    String tenCasi = jsonObject.getJSONObject("data").getJSONArray("item").getJSONObject(0).getString("artist");
                    String linkVideo;
                    try {
                        linkVideo = jsonObject.getJSONObject("data").getJSONArray("item").getJSONObject(0).getJSONArray("source_list").getString(1);
                    } catch (Exception e) {
                        linkVideo = jsonObject.getJSONObject("data").getJSONArray("item").getJSONObject(0).getJSONArray("source_list").getString(0);
                    }
                    downloadToFoderDownload(tenVideo + " - " + tenCasi + ".mp4", linkVideo, context);
                } catch (Exception e) {
                    Log.e("faker", "Exception");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static Document getDocumentHTMLByURL(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
        }
        return doc;
    }
}

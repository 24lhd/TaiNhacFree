package com.lhd.mp3.item;

/**
 * Created by D on 5/16/2017.
 */

public class ItemSong {
    String link;
    String title;
    String luotnghe;

    public String getLink() {
        return "http://mp3.zing.vn"+link;
    }

    public String getTitle() {
        return title;
    }

    public String getLuotnghe() {
        return luotnghe;
    }

    public ItemSong(String link, String title, String luotnghe) {

        this.link = link;
        this.title = title;
        this.luotnghe = luotnghe;
    }

    @Override
    public String toString() {
        return "ItemSong{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", luotnghe='" + luotnghe + '\'' +
                '}';
    }
}

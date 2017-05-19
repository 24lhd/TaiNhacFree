package com.lhd.mp3.item;

/**
 * Created by D on 5/17/2017.
 */

public class ItemTop {
    String link;
    String title;

    @Override
    public String toString() {
        return "ItemTop{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public ItemTop(String link, String title) {

        this.link = link;
        this.title = title;
    }
}

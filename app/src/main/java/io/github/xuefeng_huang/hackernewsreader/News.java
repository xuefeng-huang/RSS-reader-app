package io.github.xuefeng_huang.hackernewsreader;

/**
 * Created by xuefeng on 4/1/2016.
 */
public class News {
    private String title;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return this.title;
    }
}

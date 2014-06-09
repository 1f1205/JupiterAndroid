package com.Ichif1205.jupiter.item;

import android.graphics.Bitmap;

/**
 * ItemData.
 *
 * @author wkodate
 *
 */
public class ItemData {

    /**
     * タイトル.
     */
    private String title;

    /**
     * リンク.
     */
    private String link;

    /**
     * 概要.
     */
    private String description;

    /**
     * 画像.
     */
    private Bitmap image;

    /**
     * RSS元のタイトル.
     */
    private String rssTitle;

    /**
     * 日付.
     */
    private String date;

    /**
     * タイトルをセット.
     *
     * @param t
     *            タイトル.
     */
    public final void setTitle(final String t) {
        title = t;
    }

    /**
     * タイトルを取得.
     *
     * @return title.
     */
    public final String getTitle() {
        return title;
    }

    /**
     * リンクをセット.
     *
     * @param l
     *            タイトル.
     */
    public final void setLink(final String l) {
        link = l;
    }

    /**
     * リンクを取得.
     *
     * @return link.
     */
    public final String getLink() {
        return link;
    }

    /**
     * 概要をセット.
     *
     * @param desc
     *            概要.
     */
    public final void setDescription(final String desc) {
        description = desc;
    }

    /**
     * 概要を取得.
     *
     * @return description.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * 画像をセット.
     *
     * @param i
     *            画像.
     */
    public final void setImage(final Bitmap i) {
        image = i;
    }

    /**
     * 画像を取得.
     *
     * @return image.
     */
    public final Bitmap getImage() {
        return image;
    }

    /**
     * RSSのタイトルをセット.
     *
     * @param rt
     *            RSSのタイトル.
     */
    public final void setRssTitle(final String rt) {
        rssTitle = rt;
    }

    /**
     * RSSのタイトルを取得.
     *
     * @return rssTitle.
     */
    public final String getRssTitle() {
        return rssTitle;
    }

    /**
     * 日付をセット.
     *
     * @param d
     *            日付.
     *
     */
    public final void setDate(final String d) {
        date = d;
    }

    /**
     * 日付を取得.
     *
     * @return date.
     */
    public final String getDate() {
        return date;
    }

}
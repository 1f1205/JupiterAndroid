package com.Ichif1205.jupiter;

/**
 * Constant.
 *
 * @author wkodate
 *
 */
public final class Constant {

    /**
     * タイトルフィールド.
     */
    public static final String TITLE_FIELD = "title";

    /**
     * リンクフィールド.
     */
    public static final String LINK_FIELD = "link";

    /**
     * ディスクリプションフィールド.
     */
    public static final String DESC_FIELD = "description";

    /**
     * RSSタイトルフィールド.
     */
    public static final String RSS_TITLE_FIELD = "rss_title";

    /**
     * 日付フィールド.
     */
    public static final String DATE_FIELD = "date";

    /**
     * 日付フィールド.
     */
    public static final String IMAGE_FIELD = "image";

    /**
     * Viewで表示するアイテム数.
     */
    public static final int ITEM_VIEW_COUNT = 20;

    /**
     * 最大読み込み数.
     */
    public static final int MAX_READING_COUNT = 100;

    /**
     * API取得サーバのURL.
     */
    public static final String API_URL = "http://www6178uo.sakura.ne.jp/"
            + "jupiter/db2json/db2json.php";

    /**
     * インスタンスを生成しない.
     */
    private Constant() {
    }

}
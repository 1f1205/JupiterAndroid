package com.Ichif1205.jupiter;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ShareActionProvider;

import com.Ichif1205.jupiter.http.AsyncFetcher;
import com.Ichif1205.jupiter.item.ItemAdapter;
import com.Ichif1205.jupiter.item.ItemData;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * MainActivity.
 *
 * @author wkodate
 *
 */
public class MainActivity extends FragmentActivity implements
        OnScrollListener, LoaderCallbacks<List<ItemData>> {

    /**
     * ログ.
     */
    private static final String TAG = "MainActivity";

    /**
     * Tracker.
     */
    private Tracker tracker;

    /**
     * ListViewのfooter.
     */
    private View listViewFooter;

    /**
     * intentで渡すURLの配列.
     */
    private String[] urls;

    /**
     * intentで渡すtitleの配列.
     */
    private String[] titles;

    /**
     * rssTitleの配列.
     */
    private String[] rssTitles;

    /**
     * ListView.
     */
    private ListView listView;

    /**
     * ItemAdapter.
     */
    private ItemAdapter itemAdapter;

    /**
     * Fetcher.
     */
    private AsyncFetcher asyncFetcher;

    /**
     * コンストラクタ.
     */
    public MainActivity() {
        Log.d(TAG, "Call Constructor.");
        this.listView = null;
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Call onCreate.");

        // loaderの初期化
        getSupportLoaderManager().initLoader(0, null, this);
        // プログレスバー
        setContentView(R.layout.listview_progress_bar);

        // Get tracker.
        tracker = ((AnalyticsApplication) getApplication()).getTracker(
                AnalyticsApplication.TrackerName.APP_TRACKER);
        // Set screen name.
        tracker.setScreenName("MainActivity");
        // Send a screen view.
        tracker.send(new HitBuilders.AppViewBuilder().build());

    }

    @Override
    protected final void onStart() {
        super.onStart();
        // トラッキング開始
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected final void onStop() {
        super.onStop();
        // トラッキング終了
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public final Loader<List<ItemData>> onCreateLoader(final int itemCount,
            final Bundle arg1) {
        // 新しいLoaderが作成された時に呼ばれる
        Log.d(TAG, "Call onCreateLoader.");
        asyncFetcher = new AsyncFetcher(this, itemCount);
        asyncFetcher.forceLoad();
        return asyncFetcher;
    }

    @Override
    public final void onLoadFinished(final Loader<List<ItemData>> arg0,
            final List<ItemData> itemDataList) {
        // 前に作成したloaderがloadを完了した時に呼ばれる
        Log.d(TAG, "Call onLoadFinished.");
        // インテントで送るためのデータ作成
        createIntentData(itemDataList);

        // Adapterを指定
        if (null == listView) {
            // リストビューに入れるアイテムのAdapterを生成
            setContentView(R.layout.activity_main);
            itemAdapter = new ItemAdapter(this, 0, itemDataList);
            listView = (ListView) findViewById(R.id.listView);
            listView.addFooterView(getFooter());
            listView.setAdapter(itemAdapter);
            listView.setOnScrollListener(this);
        } else {
            // 2回目以降の処理
            itemAdapter.addAll(itemDataList);
            listView.invalidate();
        }
        asyncFetcher.stopLoading();

        // クリックされた時の処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view,
                    final int position, final long id) {
                Log.d(TAG, "Call onItemClick.");
                tracker.send(new HitBuilders.EventBuilder()
                .setCategory("setOnItemClickListener")
                .setAction(urls[position])
                .setLabel(rssTitles[position])
                .build());

                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", urls[position]);
                intent.putExtra("title", titles[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    public final void onLoaderReset(final Loader<List<ItemData>> arg0) {
        // 前に作成したloaderがリセットされた時に呼ばれる
        Log.d(TAG, "Call onLoadReset.");
    }

    /**
     * インテントのためのデータ生成.
     *
     * @param itemDataList
     *            ItemDataのリスト.
     */
    private void createIntentData(final List<ItemData> itemDataList) {
        List<String> linkList = new ArrayList<String>();
        List<String> titleList = new ArrayList<String>();
        List<String> rssTitleList = new ArrayList<String>();
        for (int i = 0; i < itemDataList.size(); i++) {
            linkList.add(itemDataList.get(i).getLink());
            titleList.add(itemDataList.get(i).getTitle());
            rssTitleList.add(itemDataList.get(i).getRssTitle());
        }
        urls = linkList.toArray(new String[linkList.size()]);
        titles = titleList.toArray(new String[titleList.size()]);
        rssTitles = rssTitleList.toArray(new String[rssTitleList.size()]);
    }

    /**
     * footerを取得.
     *
     * @return ListViewのfooter.
     */
    private View getFooter() {
        if (listViewFooter == null) {
            listViewFooter = getLayoutInflater().inflate(R.layout.listview_progress_bar, null);
        }
        return listViewFooter;
    }

    @Override
    public final void onScroll(final AbsListView view, final int firstVisibleItem,
            final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "Call onScroll.");
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            // 読み込み回数が最大値ならスキップ
            if (totalItemCount >= Constant.MAX_READING_COUNT) {
                invisibleFooter();
                return;
            }
            // 読み込み中ならスキップ
            if (asyncFetcher.isStarted()) {
                return;
            }
            getSupportLoaderManager().initLoader(totalItemCount, null, this);
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {

    }

    /**
     * Footerを非表示にする.
     */
    private void invisibleFooter() {
        listView.removeFooterView(getFooter());
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        Log.d(TAG, "Call onCreateOptionsMenu.");
        // menuファイルの読み込み
        getMenuInflater().inflate(R.menu.main, menu);
        // プロバイダの取得と共有インテントのセット
        MenuItem actionItem = menu.findItem(R.id.share);
        ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
        // アクションビュー取得前にデフォルトの履歴をセット
        actionProvider.setShareIntent(getDefaultShareIntent());

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 共有用のインテントを返す.
     *
     * @return Intent.
     */
    private Intent getDefaultShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "#jupiter");
        return shareIntent;
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        Log.d(TAG, "Call onOptionsItemSelected.");
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * コンストラクタ.
         */
        public PlaceholderFragment() {
        }

        @Override
        public final View onCreateView(final LayoutInflater inflater,
                final ViewGroup container,
                final Bundle savedInstanceState) {
            Log.d(TAG, "Call onCreateView.");
            View rootView = inflater.inflate(R.layout.fragment_main,
                    container, false);
            return rootView;
        }
    }

}

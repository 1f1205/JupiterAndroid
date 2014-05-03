package com.Ichif1205.jupiter;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.Ichif1205.jupiter.http.AsyncFetcher;

/**
 * MainActivity.
 *
 * @author wkodate
 *
 */
public class MainActivity extends FragmentActivity implements LoaderCallbacks<String> {

    /**
     * ログ.
     */
    private static final String TAG = "MainActivity";

    /**
     * アイテム.
     */
    private final List<String> items;

    /**
     * ListView.
     */
    private ListView listView;

    /**
     * コンストラクタ.
     */
    public MainActivity() {
        Log.d(TAG, "Call Constructor.");
        // item取得
        items = new ArrayList<String>();
        items.add("article1");
        items.add("article2");
        items.add("article3");
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Call onCreate.");

        listView = new ListView(this);
        setContentView(listView);

        // リストビューに入れるアイテムのAdapterを生成
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_row, items);

        // Adapterを指定
        listView.setAdapter(adapter);

        // loaderの初期化
        getSupportLoaderManager().initLoader(0, null, this);

        // クリックされた時の処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view,
                    final int position, final long id) {
                ListView lView = (ListView) parent;
                String item = (String) lView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item + " clicked ",
                        Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    public final Loader<String> onCreateLoader(final int arg0, final Bundle arg1) {
        Log.d(TAG, "Call onCreateLoader.");
        AsyncFetcher asyncFetcher = new AsyncFetcher(this);
        asyncFetcher.forceLoad();
        return asyncFetcher;
    }

    @Override
    public final void onLoadFinished(final Loader<String> arg0, final String arg1) {
        Log.d(TAG, "Call onLoadFinished.");

    }

    @Override
    public void onLoaderReset(final Loader<String> arg0) {
        Log.d(TAG, "Call onLoadReset.");

    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
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
            View rootView = inflater.inflate(R.layout.fragment_main,
                    container, false);
            return rootView;
        }
    }
}

package com.com.teamupapps.searchsms;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.com.teamupapps.searchsms.adapters.ImageMessageAdapter;
import com.com.teamupapps.searchsms.adapters.LinkListAdapter;
import com.com.teamupapps.searchsms.adapters.MessageListAdapter;
import com.com.teamupapps.searchsms.models.MMS;
import com.com.teamupapps.searchsms.models.SMS;
import com.com.teamupapps.searchsms.utils.SearchUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.teamupapps.searchsms.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchLinkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_search_links);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_fragment_search_links, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @InjectView(R.id.listView)
        ListView listview;
        @InjectView(R.id.text_count)
        TextView textCount;

        public PlaceholderFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            AdView mAdView = (AdView) getView().findViewById(R.id.ads);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fragment_search_links, container, false);
            ButterKnife.inject(this, rootView);

            performSearch();

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            return rootView;
        }

        private void performSearch() {
            List<SMS> found = SearchUtils.getHyperlinks(getActivity());
            LinkListAdapter adapter = new LinkListAdapter(getActivity(), found);
            listview.setAdapter(adapter);
            textCount.setText("Found " + found.size() );
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }
}

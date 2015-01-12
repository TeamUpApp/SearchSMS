package com.com.teamupapps.searchsms.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.com.teamupapps.searchsms.models.Contact;
import com.com.teamupapps.searchsms.models.MMS;
import com.com.teamupapps.searchsms.adapters.MessageListAdapter;
import com.com.teamupapps.searchsms.models.SMS;
import com.com.teamupapps.searchsms.utils.SearchUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.teamupapps.searchsms.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnEditorAction;

/**
 * Created by nrobatmeily on 28/10/2014.
 */
public class SearchFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    @InjectView(R.id.listView)
    ListView listview;
    @InjectView(R.id.editText)
    EditText editSearch;
    @InjectView(R.id.text_count)
    TextView textCount;

    public SearchFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_main_activty, container, false);
        ButterKnife.inject(this, rootView);



        /*editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(editSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });*/

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return rootView;
    }

    @OnEditorAction(R.id.editText)
    public boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            performSearch(editSearch.getText().toString());
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    private void setUpContactsList() {
        final Map<String, String> countMap;
        countMap = SearchUtils.getContactList(getActivity());
        final String names[];
        List<Contact> cnt;
        List<String> cmtct = new LinkedList<String>();

        for (Map.Entry<String, String> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            cmtct.add(entry.getValue());

        }

        if (cmtct != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, cmtct);

            //listView.setAdapter(adapter);
        }
    }


    private void findHyperLinks() {
        List<SMS> smsLinksFound = SearchUtils.getHyperlinks(getActivity());
        Log.i("Link LIST", smsLinksFound.size() + "");
    }

    private void performSearch(String text) {
        List<SMS> found = SearchUtils.readMessages(getActivity(), text);
        MessageListAdapter adapter = new MessageListAdapter(getActivity(), found);
        listview.setAdapter(adapter);
        textCount.setText("Found " + found.size() + " messages containing \"" + text + "\"");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
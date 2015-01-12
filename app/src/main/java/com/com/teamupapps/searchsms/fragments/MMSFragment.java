package com.com.teamupapps.searchsms.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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
import com.com.teamupapps.searchsms.utils.SearchUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.teamupapps.searchsms.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by clazell on 12/01/2015.
 */
public class MMSFragment  extends Fragment {

    private ListView listview;
    private TextView textCount;

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

        final ListView listView = (ListView) rootView.findViewById(R.id.listView);

        final Map<String, String> countMap;
        countMap = SearchUtils.getContactList(getActivity());
        final String names[];
        List<Contact> cnt;
        List<String> cmtct = new LinkedList<String>();

        for (Map.Entry<String, String> entry : countMap.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            cmtct.add(entry.getValue());

        }

        if(cmtct != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, cmtct);

            listView.setAdapter(adapter);
        }


        listview = (ListView) rootView.findViewById(R.id.listView);
        textCount = (TextView) rootView.findViewById(R.id.text_count);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
}

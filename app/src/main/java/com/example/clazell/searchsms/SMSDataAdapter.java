package com.example.clazell.searchsms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by clazell on 22/10/2014.
 */


    public class SMSDataAdapter extends ArrayAdapter<SMSData> {
        public SMSDataAdapter(Context context, ArrayList<SMSData> SMS) {
            super(context, 0, SMS);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            SMSData MSG = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
            }
            // Lookup view for data population
            TextView SMSName = (TextView) convertView.findViewById(R.id.textView);
            TextView SMSBody = (TextView) convertView.findViewById(R.id.textView2);
            // Populate the data into the template view using the data object
            SMSName.setText(MSG.name);
            SMSBody.setText(MSG.Body);
            // Return the completed view to render on screen
            return convertView;
        }
    }



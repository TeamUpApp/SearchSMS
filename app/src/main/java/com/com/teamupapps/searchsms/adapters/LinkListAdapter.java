package com.com.teamupapps.searchsms.adapters;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.com.teamupapps.searchsms.models.SMS;
import com.com.teamupapps.searchsms.utils.SearchUtils;
import com.teamupapps.searchsms.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by nrobatmeily on 27/10/2014.
 */
public class LinkListAdapter extends ArrayAdapter<SMS> {

    private final List<SMS> list;
    private final Activity context;
    private LayoutInflater inflater;

    public LinkListAdapter(Activity context, List<SMS> list) {
        super(context, R.layout.link_list_row, list);
        this.context = context;
        this.list = list;
    }


    static class ViewHolder {
        @InjectView(R.id.txt_message)
        TextView txtMessage;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_link)
        TextView txtLink;

        @InjectView((R.id.btn_share))
        ImageButton btnShare;
        @InjectView((R.id.btn_link))
        ImageButton btnLink;
        @InjectView((R.id.btn_copy))
        ImageButton btnCopy;


        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            if (inflater == null)
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.link_list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        final SMS sms = list.get(position);

        holder.txtMessage.setText(sms.getContent());
        holder.txtName.setText(sms.getName());
        holder.txtLink.setText(sms.getUrl());

        holder.btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = sms.getUrl();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });

        holder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager _clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                _clipboard.setText(sms.getUrl());
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sms.getUrl());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
        return convertView;

    }

}
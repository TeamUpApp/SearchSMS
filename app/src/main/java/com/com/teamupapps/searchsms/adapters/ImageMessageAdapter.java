package com.com.teamupapps.searchsms.adapters;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.com.teamupapps.searchsms.models.MMS;
import com.com.teamupapps.searchsms.models.SMS;
import com.com.teamupapps.searchsms.utils.SearchUtils;
import com.teamupapps.searchsms.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnEditorAction;

/**
 * Created by clazell on 12/01/2015.
 */
public class ImageMessageAdapter extends
        ArrayAdapter<MMS> {

    private final List<MMS> list;
    private final Activity context;
    private LayoutInflater inflater;

    public ImageMessageAdapter(Activity context, List<MMS> list) {
        super(context, R.layout.list_row, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {

        @InjectView(R.id.image_profile) ImageView image;
        @InjectView(R.id.image_message) ImageView imageMessage;
        @InjectView(R.id.txt_contact_name) TextView name;
        @InjectView(R.id.txt_message) TextView message;
        @InjectView(R.id.btn_settings) ImageButton buttonMore;

        public ViewHolder(View view){
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

            convertView = inflater.inflate(R.layout.image_list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        final MMS mms = list.get(position);

        holder.name.setText(list.get(position).getName());
        holder.message.setText(list.get(position).getContent());
        holder.imageMessage.setImageBitmap(list.get(position).getBitmap());

        holder.buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.buttonMore);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.drop_down_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.share) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, mms.getContent());
                            sendIntent.setType("text/plain");
                            context.startActivity(sendIntent);
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        if (list.get(position).getImageURI() != null) {
            try {
                Bitmap bitmap = SearchUtils.getRoundedShape(MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(list.get(position).getImageURI())));
                holder.image.setImageBitmap(bitmap);
                System.out.println(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                holder.image.setImageResource(R.drawable.contact);
            } catch (IOException e) {
                e.printStackTrace();
                holder.image.setImageResource(R.drawable.contact);
            }


        } else {
            Log.w("tag", "os null");
            holder.image.setImageResource(R.drawable.contact);
        }
        return convertView;

    }


}


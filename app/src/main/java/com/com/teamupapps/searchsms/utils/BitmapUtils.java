package com.com.teamupapps.searchsms.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by nedarobatmeily on 19/01/15.
 */
public class BitmapUtils {

    public static class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String data = "";
        private Context context;

        public BitmapWorkerTask(ImageView imageView, Context context) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            imageViewReference.get().setImageBitmap(null);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            data = params[0];
            return getMmsImage(context, data);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public static Bitmap getMmsImage(Context context, String _id) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = context.getContentResolver().openInputStream(partURI);
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}

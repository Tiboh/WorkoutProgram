package com.erikbuto.workoutprogram.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Utilisateur on 16/07/2015.
 */
public abstract class MyUtils {

    public static final String IMAGE_FOLDER_URL = "exerciseImages/";

    public static String stringifySet(Set set, Context context) {
        String rep = Integer.toString(set.getNbRep()); // + " " + context.getString(R.string.label_rep);

        if (set.getWeight() == 0) {
            return rep;
        } else {
            String X = context.getString(R.string.set_summary_divider);
            String weight = Integer.toString(set.getWeight()) + context.getString(R.string.weight_unit);
            return rep + X + weight;
        }
    }

    public static String stringifyPositionEnglish(int position) {
        String positionStr;
        if (position == 11 || position == 12 || position == 13) {
            positionStr = Integer.toString(position) + "TH";
        } else {
            switch (position % 10) {
                case 1:
                    positionStr = Integer.toString(position) + "ST";
                    break;
                case 2:
                    positionStr = Integer.toString(position) + "ND";
                    break;
                case 3:
                    positionStr = Integer.toString(position) + "RD";
                    break;
                default:
                    positionStr = Integer.toString(position) + "TH";
                    break;
            }
        }
        return positionStr;
    }

    public static String stringifyRestTime(int restMinute, int restSeconds, String minuteUnit, String secondUnit) {
        return restMinute + minuteUnit + restSeconds + secondUnit;
    }

    public static boolean saveImageToInternalStorage(Bitmap image, String fileUrl, Context context) {
        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = context.openFileOutput(fileUrl, Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    public static String saveImageToCache(Context context, Bitmap bitmap, String url){
        File cacheDir = context.getCacheDir();
        File f = new File(cacheDir, url);
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static Bitmap getImageFromInternalStorage(String url, Context context) {
        Bitmap result;
        try {
            File filePath = context.getFileStreamPath(url);
            FileInputStream fi = new FileInputStream(filePath);
            result = BitmapFactory.decodeStream(fi);
        } catch (Exception ex) {
            Log.e("getImageInternalStor()", ex.getMessage());
            result = null;
        }
        return result;
    }

    public static File getFileFromInternalStorage(String url, Context context) {
        File filePath = context.getFileStreamPath(url);
        return filePath;
    }

    public static Bitmap convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return viewBmp;
    }

    public static String generateImageUrl(){
        return Long.toString(System.currentTimeMillis());
    }
}

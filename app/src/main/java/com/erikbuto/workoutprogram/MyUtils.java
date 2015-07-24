package com.erikbuto.workoutprogram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.erikbuto.workoutprogram.DB.Set;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Utilisateur on 16/07/2015.
 */
public abstract class MyUtils {

    public static final String IMAGE_FOLDER_URL = "exerciseImages";

    public static String stringifySet(Set set, String summaryDivider, String weightUnit, String labelRep) {
        String rep = Integer.toString(set.getNbRep()); // + " " + labelRep;

        if (set.getWeight() == 0) {
            return rep;
        } else {
            String X = summaryDivider;
            String weight = Integer.toString(set.getWeight()) + weightUnit;
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
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
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
}

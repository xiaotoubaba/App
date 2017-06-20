package com.water.roll.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

/**
 * Created by Yale on 2017/3/24.
 */

public class FilesUtils {

    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getFileName(Context context, Uri uri) {
        String path = getPath(context, uri);

        String name = path.substring(path.lastIndexOf("/") + 1, path.length());

        return name;
    }

    public static String getFileName(String path) {

        String name = path.substring(path.lastIndexOf("/") + 1, path.length());

        return name;

    }

    public static void createFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

package com.jovision.xunwei.junior.lib.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {

    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    
    @SuppressLint("NewApi")
	public static int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();               //earlier version
    }

    public static Bitmap decodeBitmap(String path) {
        return decodeBitmapForSize(path, 0, 0);
    }

    public static Bitmap decodeBitmapForSize(String path, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        if (width != 0 && height != 0) {
            // decode with inJustDecodeBounds=true to check size
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            // calculate inSampleSize according to the requested size
            options.inSampleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;
        }

        // decode bitmap with the calculated inSampleSize
        options.inPreferredConfig = Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据原图尺寸和目标区域的尺寸计算出合适的Bitmap尺寸
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 将图片按照某个角度进行旋转
     * 
     * @param bm 需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}

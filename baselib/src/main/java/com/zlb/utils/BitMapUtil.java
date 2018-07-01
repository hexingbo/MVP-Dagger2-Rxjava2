package com.zlb.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 *
 */
public class BitMapUtil {

    /**
     *zlb
     */
    public static Bitmap getSimpleByBelowLine(Context context,Uri file, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只是去读取图片的信息，不要实际的分配空间
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(file),null,options);
        }catch (Exception e){

        }
        int originWidth= options.outWidth;
        int originHeight = options.outHeight;
        int simpleSize=1;
        if(originWidth>maxWidth||originHeight>maxHeight){
            simpleSize = calculateInSampleSize(options,maxWidth,maxHeight);   //最大的图片的size 限制在1920*1080
        }
        options.inJustDecodeBounds = false;
        if(simpleSize>1){
            options.inSampleSize = simpleSize;
        }
        options.inPreferredConfig= Bitmap.Config.RGB_565;   //降低编码成像质量
        Bitmap bitmap=null;
        try {
            bitmap=BitmapFactory.decodeStream(context.getContentResolver().openInputStream(file),null,options);
        }catch (Exception e){

        }
        return bitmap;
    }

    /**
     *zlb
     */
    public static Bitmap getSimpleByBelowLine(String file, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只是去读取图片的信息，不要实际的分配空间
        BitmapFactory.decodeFile(file, options);
        int originWidth= options.outWidth;
        int originHeight = options.outHeight;
        int simpleSize=1;
        if(originWidth>maxWidth||originHeight>maxHeight){
            simpleSize = calculateInSampleSize(options,maxWidth,maxHeight);   //最大的图片的size 限制在1920*1080
        }

        options.inJustDecodeBounds = false;
        if(simpleSize>1){
            options.inSampleSize = simpleSize;
        }
        options.inPreferredConfig= Bitmap.Config.RGB_565;   //降低编码成像质量

        return BitmapFactory.decodeFile(file, options);
    }


    /**
     * 计算缩放比例的方法    zlb
     *
     * @param op
     * @param reqWidth
     * @param reqheight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options op, int reqWidth, int reqheight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqheight) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;

            while ((halfWidth / inSampleSize > reqWidth)||(halfHeight / inSampleSize > reqheight)) {
                inSampleSize *= 2;
            }
            if(originalWidth/(2*inSampleSize)>reqWidth||originalHeight/(2*inSampleSize)>reqheight){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}

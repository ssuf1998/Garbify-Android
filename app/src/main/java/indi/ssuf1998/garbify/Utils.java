package indi.ssuf1998.garbify;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class Utils {
  public static float dp2px(Context context, float dp) {
    return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }

  public static float dp2px(float dp) {
    return dp2px(MainActivity.getWeakRef().get(), dp);
  }

  public static File getImgCacheFile(Context context, String subDir) {
    File cacheDir = context.getExternalCacheDir();
    String fn = "garbify_" + UUID.randomUUID().toString() + ".jpg";
    String sub = "";
    if (!subDir.isEmpty()) {
      sub = File.separator + subDir;
      new File(cacheDir + sub).mkdir();
    }
    return new File(cacheDir + sub + File.separator + fn);
  }

  public static File getImgCacheFile(Context context) {
    return getImgCacheFile(context, "");
  }

  public static String getRandomDigitStr(int length) {
    String CHARS = "1234567890";
    StringBuilder sb = new StringBuilder();
    Random rnd = new Random();
    while (sb.length() < length) {
      int index = (int) (rnd.nextFloat() * CHARS.length());
      sb.append(CHARS.charAt(index));
    }
    return sb.toString();
  }
}

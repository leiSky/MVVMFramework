package cn.com.gnnt.workingdaily.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class WDUtils {

    public static Bitmap stringToBitmap(String string) {
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive() && context.getCurrentFocus() != null ) {
            if (context.getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}

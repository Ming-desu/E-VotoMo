package com.qqdota.evotomo.utils;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.qqdota.evotomo.activities.MainActivity;
import com.qqdota.evotomo.activities.LoginActivity;

public class Redirect {
    private static Redirect instance;

    public static synchronized Redirect getInstance() {
        if (instance == null)
            instance = new Redirect();
        return instance;
    }

    public <T extends Activity> void FromTo(Activity activity, Class<T> redirectTo, String error) {
        Intent intent = new Intent(activity, redirectTo);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        if (error != null)
            intent.putExtra("error", error);

        activity.finish();
        activity.startActivity(intent);
    }
}

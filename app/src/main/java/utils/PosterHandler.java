package utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by lenovo on 2016/12/15.
 */

public class PosterHandler extends Handler {
    private static PosterHandler instance;

    public static PosterHandler getInstance() {
        if (instance == null)
            synchronized (PosterHandler.class) {
                if (instance == null)
                    instance = new PosterHandler();
            }
        return instance;
    }

    private PosterHandler() {
        super(Looper.getMainLooper());
    }
}

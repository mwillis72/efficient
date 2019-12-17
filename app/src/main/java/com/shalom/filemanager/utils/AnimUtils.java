

package com.shalom.filemanager.utils;

import android.content.Context;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.shalom.filemanager.ui.views.ThemedTextView;

/**
 * Utility methods for working with animations.
 */
public class AnimUtils {

    private static Interpolator fastOutSlowIn;

    public static Interpolator getFastOutSlowInInterpolator(Context context) {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_slow_in);
        }
        return fastOutSlowIn;
    }

    /**
     * Animates filenames textview to marquee after a delay.
     * Make sure to set {@link TextView#setSelected(boolean)} to false in order to stop the marquee later
     */
    public static void marqueeAfterDelay(int delayInMillis, ThemedTextView marqueeView) {
        new Handler().postDelayed(() -> {
            // marquee works only when text view has focus
            marqueeView.setSelected(true);
        }, delayInMillis);
    }
}
package com.example.googlemap;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

public class utils
{
    public void SlideUP(View view, Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slide_up));
    }

    public void SlideDown(View view,Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slide_down));
    }

}

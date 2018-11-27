package com.caltyfarm.caltyfarm.utils;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Locale;

public class LoggingListener<T> implements RequestListener<T> {

    @Override
    public boolean onLoadFailed(GlideException e, Object model, Target<T> target, boolean isFirstResource) {
        android.util.Log.d("GLIDE", String.format(Locale.ROOT, "on Exception(%s, %s, %s, %s)", e, model, target, isFirstResource), e);
        return false;
    }

    @Override
    public boolean onResourceReady(T resource, Object model, Target<T> target, DataSource dataSource, boolean isFirstResource) {
        android.util.Log.d("GLIDE", String.format(Locale.ROOT, "on Exception(%s, %s, %s, %s)", resource, model, target, isFirstResource));
        return false;
    }
}
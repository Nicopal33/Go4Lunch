package com.example.goforlunch.utils;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent <T> extends MutableLiveData{

    private static final String TAG = "singleLiveEvent";
    private final AtomicBoolean mPending = new AtomicBoolean(false);


    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer observer) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registred but only one will be notified of changes");
        }
        super.observe(owner, t-> {
            if(mPending.compareAndSet(true,false)) {
                observer.onChanged(t);
            }
        });
    }

    @MainThread
    public void setValue(@Nullable Object t) {
        mPending.set(true);
        super.setValue(t);
    }



}

package com.bluemango.bokjipang;

import android.app.Application;
import android.content.Context;

public class ApplicationClass extends Application{
    private static Context mContext;

    public ApplicationClass() {
        super();

        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}

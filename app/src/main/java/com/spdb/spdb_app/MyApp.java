package com.spdb.spdb_app;

import android.app.Application;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/rreg.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

    }
}

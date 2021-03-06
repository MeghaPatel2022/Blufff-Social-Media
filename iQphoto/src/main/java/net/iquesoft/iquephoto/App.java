package net.iquesoft.iquephoto;

import android.app.Application;

import androidx.multidex.MultiDex;

import net.iquesoft.iquephoto.di.AppComponent;
import net.iquesoft.iquephoto.di.DaggerAppComponent;
import net.iquesoft.iquephoto.di.modules.AppModule;

public class App extends Application {
    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
}
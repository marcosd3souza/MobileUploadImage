package android.mso.com.uploadimagesapp;

import android.app.Application;

/**
 * Created by msoliveira2 on 12/12/2016.
 */
public class ServiceApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Service.getInstance().init(this);
    }
}
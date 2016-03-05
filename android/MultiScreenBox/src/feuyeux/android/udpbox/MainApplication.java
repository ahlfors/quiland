package feuyeux.android.udpbox;

import android.app.Application;
import android.content.Intent;
 
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, ServerService.class));
    }
}

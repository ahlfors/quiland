package feuyeux.android.udpclient;

import com.technicolor.multiscreen.communication.ClientSideSocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ClientService extends Service {
    ClientSideSocket client;
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDestroy() {}
}

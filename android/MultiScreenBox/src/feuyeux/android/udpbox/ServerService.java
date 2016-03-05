package feuyeux.android.udpbox;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.technicolor.multiscreen.common.ENV;
import com.technicolor.multiscreen.communication.BioServerSideSocket;
import com.technicolor.multiscreen.communication.ServerSideSocket;

import feuyeux.android.udpbox.MainActivity.UIHandler;

public class ServerService extends Service {
    ServerSideSocket udpServer;
    private static UIHandler handler;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startUdpService();
        Log.i(ENV.TAG, "BroadCastServer created.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUdpService();
        Log.d(ENV.TAG, "BroadCastServer destroyed.");
    }

    private void startUdpService() {
        while(handler==null){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Thread(new ServerSideSocket(handler)) {
        }.start();
        new Thread(new BioServerSideSocket()) {
        }.start();
    }

    private void stopUdpService() {

    }

    public static void setUIHandler(UIHandler uiHandler) {
        handler = uiHandler;
    }
}

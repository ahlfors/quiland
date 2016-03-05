package feuyeux.android.udpclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.technicolor.multiscreen.common.ENV;
import com.technicolor.multiscreen.communication.ClientSideSocket;

public class MainActivity extends Activity {
    private static final int BROADCAST_TIMEOUT_SECOND = 3;
    protected static final String EXTRA_MESSAGE = "server.host";
    ListView serverListView;
    private Handler handler = new UIHander();
    final int broadCastWaitingTime = 10000;//10 seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverListView = (ListView) findViewById(R.id.serverListView);
        initListView(serverListView);
    }

    private void initListView(final ListView serverListView) {
        new Thread() {
            public void run() {
                try {
                    Message msg = new Message();
                    msg.what = 1;
                    String[] servers = ClientSideSocket.broadCast(BROADCAST_TIMEOUT_SECOND);
                    msg.getData().putStringArray("serverList", servers);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Log.e(ENV.TAG, "broadcast error", e);
                }
            };
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private final class UIHander extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String[] serverList = msg.getData().getStringArray("serverList");
                    if (serverList.length > 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, android.R.id.text1,
                                serverList);
                        serverListView.setAdapter(adapter);
                        //serverListView.setF
                        serverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemValue = (String) serverListView.getItemAtPosition(position);
                                Intent intent = new Intent(getApplication(), CmdActivity.class);
                                intent.putExtra(EXTRA_MESSAGE, itemValue);
                                startActivity(intent);
                            }
                        });
                    }
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

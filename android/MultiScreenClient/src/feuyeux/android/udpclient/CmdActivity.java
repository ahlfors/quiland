package feuyeux.android.udpclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.technicolor.multiscreen.communication.ClientSideSocket;
import com.technicolor.multiscreen.protocol.BaseMessage;
import com.technicolor.multiscreen.protocol.StringInputMessage;

public class CmdActivity extends Activity {
    String serverIp;
    TextView receiveText;
    ClientSideSocket client;

    private Handler handler = new UIHander();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmd);

        Intent intent = getIntent();
        serverIp = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView serverHost = (TextView) findViewById(R.id.serverHost);
        serverHost.setText(serverIp);
        client = new ClientSideSocket(serverIp, handler);
        new Thread("Multi-Screen-Client-Thread") {
            public void run() {
                client.run();
            };
        }.start();

        receiveText = (TextView) findViewById(R.id.receiveText);
        Button sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText sendText = (EditText) findViewById(R.id.sendText);
                StringInputMessage cmd = new StringInputMessage(sendText.getText().toString());
                client.sendCommand(cmd);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cmd, menu);
        return true;
    }

    @SuppressLint("HandlerLeak")
    private final class UIHander extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseMessage.CMD_TYPE_STRING_INPUT:
                    StringInputMessage cmdMessage = (StringInputMessage)msg.obj;
                    receiveText.setText(cmdMessage.getInputStr());
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

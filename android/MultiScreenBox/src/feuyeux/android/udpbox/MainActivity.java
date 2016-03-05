package feuyeux.android.udpbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView receiveText;

    public MainActivity() {
        super();
        ServerService.setUIHandler(new UIHandler());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiveText = (TextView) findViewById(R.id.receiveText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("HandlerLeak")
    public final class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String receiveMessage = msg.getData().getString("receiveMessage");
                    receiveText.setText(receiveMessage);
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

package lu.uni.mics.meetingregistration;

import android.content.Intent;
import android.os.Bundle;
//import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//import com.google.zxing.integration.android.IntentIntegrator;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String message = "Welcome to the Meeting Registration app!\n\nWhich meeting are you attending today?";

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);

        Button scanButton = findViewById(R.id.scan);

        scanButton.setOnClickListener(v -> scanButtonHandler());
    }

    public void scanButtonHandler() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
}
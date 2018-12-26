package com.apps.newsviews.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newsviews.R;
import com.apps.newsviews.utility.BackAsyncTask;
import com.apps.newsviews.utility.ConstantKey;

public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";
    private Activity context;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        context = this;

        textView = (TextView) findViewById(R.id.number_api_result);

        String result = getIntent().getStringExtra(ConstantKey.NUMBER_API_RESULT_KEY);

        if (result != null) {
            new BackAsyncTask(context, new BackAsyncTask.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    if (output != null && !output.isEmpty() && !output.equals("null")) {
                        Log.d(TAG, "Number API: "+output);
                        textView.setText(output);
                    }
                }
            }).execute("number_api", result);
        }
    }

}

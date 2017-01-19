package com.loadingview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoadingView pathLoadingView = (LoadingView) findViewById(R.id.loading_view);
        pathLoadingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pathLoadingView.startAnim();
            }
        }, 1000);

        Button successBtn = (Button) findViewById(R.id.btn_success);
        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathLoadingView.success();
            }
        });
        Button failBtn = (Button) findViewById(R.id.btn_fail);
        failBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathLoadingView.fail();
            }
        });
        Button resetBtn = (Button) findViewById(R.id.btn_reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathLoadingView.reset();
            }
        });
    }

}

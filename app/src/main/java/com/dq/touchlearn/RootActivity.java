package com.dq.touchlearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.dq.touchlearn.viewpager.inside.InsideRootActivity;
import com.dq.touchlearn.viewpager.outside.OutsideRootActivity;

import androidx.appcompat.app.AppCompatActivity;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        findViewById(R.id.to_log_btn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(RootActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.inside_vp_btn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(RootActivity.this, InsideRootActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.outside_vp_btn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(RootActivity.this, OutsideRootActivity.class);
                startActivity(i);
            }
        });

    }
}
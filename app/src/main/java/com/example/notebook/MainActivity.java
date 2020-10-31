package com.example.notebook;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private LinearLayout linearLayout;
    private final int USERID = 6000;
    private int countID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newRecord(View view){
            setContentView(R.layout.record);
        }

    public void save(View view) {

        setContentView(R.layout.activity_main);
    }
}
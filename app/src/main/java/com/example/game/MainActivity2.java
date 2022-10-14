package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    Button btn_back;
    EditText editName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn_back = (Button) findViewById(R.id.btn_back);
        editName = (EditText) findViewById(R.id.edit_name);
        btn_back.setOnClickListener(this::Back);
        Intent intent = this.getIntent();
        editName.setText(intent.getExtras().getString(Intent.EXTRA_TEXT));
    }
    public void Back(View view)  {
        Intent data = new Intent();
        data.putExtra("feedback", editName.getText().toString());
        this.setResult(Activity.RESULT_OK, data);
        this.onBackPressed();
    }
}

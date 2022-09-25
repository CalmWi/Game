package com.example.game;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int comp_num=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comp_num=GuessNum.rnd_comp_num();
        Button btn_guess = (Button) findViewById(R.id.btn_guess);
        View.OnClickListener clck = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editNum = (EditText) findViewById(R.id.edit_num);
                int num = Integer.parseInt(editNum.getText().toString());
                TextView show_msg = (TextView) findViewById(R.id.show_msg);
                TextView show_hint = (TextView) findViewById(R.id.show_hint);
                TextView show_attempts = (TextView) findViewById(R.id.show_attempts);
                Context context = getApplicationContext();
                int attemps = Integer.parseInt(show_attempts.getText().toString());
                    if (num == comp_num) {
                        show_msg.setText(R.string.show_msg_finish_label);
                        Toast.makeText(context, R.string.wishes, Toast.LENGTH_LONG).show();
                        btn_guess.setEnabled(false);
                    } else if (num < comp_num) {
                        show_hint.setText(R.string.show_msg_bigger_label);
                    } else {
                        show_hint.setText(R.string.show_msg_smaller_label);
                    }
                    attemps--;
                    show_attempts.setText(Integer.toString(attemps));
                    if(attemps == 0 && num != comp_num)
                    {
                        Toast.makeText(context,R.string.loss,Toast.LENGTH_LONG).show();
                        btn_guess.setEnabled(false);
                    }
            }
        };
        btn_guess.setOnClickListener(clck);
    }

    public void restart(View view) {
        TextView show_attempts = (TextView) findViewById(R.id.show_attempts);
        show_attempts.setText(R.string.show_attempts_label);
        TextView show_msg = (TextView) findViewById(R.id.show_msg);
        show_msg.setText((R.string.show_msg_label));
        Button btn_guess = (Button) findViewById(R.id.btn_guess);
        btn_guess.setEnabled(true);
        EditText editNum = (EditText) findViewById(R.id.edit_num);
        editNum.getText().clear();
        comp_num=GuessNum.rnd_comp_num();
    }
}
package com.example.game;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int comp_num=0;
    int currMode = 0;
    int attempts;
    AlertDialog.Builder builder;
    Button btn_guess;
    Button btn_mode;
    Button btn_send;
    Button btn_save;
    Button btn_change_name;
    EditText editNum;
    TextView show_msg;
    TextView show_hint;
    TextView show_attempts;
    TextView show_name;
    Context context ;
    Intent sendIntent;
    Intent shareIntent;
    Intent saveIntent;
    Intent intent;
    boolean success = false;
    public static final int MY_REQUEST_CODE = 100;
    InputFilter[] filterArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder = new AlertDialog.Builder(this);
        btn_guess = (Button) findViewById(R.id.btn_guess);
        btn_mode = (Button) findViewById(R.id.btn_choose_mode);
        btn_send = (Button) findViewById(R.id.btn_send_result);
        btn_save = (Button) findViewById(R.id.btn_save_result);
        btn_change_name = (Button) findViewById(R.id.btn_change_name);
        editNum = (EditText) findViewById(R.id.edit_num);
        show_msg = (TextView) findViewById(R.id.show_msg);
        show_hint = (TextView) findViewById(R.id.show_hint);
        show_attempts = (TextView) findViewById(R.id.show_attempts);
        show_name = (TextView) findViewById(R.id.show_player);
        context = getApplicationContext();
        editNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        editNum.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editNum.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
        btn_guess.setOnClickListener(this::guess);
        btn_mode.setOnClickListener(this::ChooseMode);
        btn_send.setOnClickListener(this::SendResult);
        btn_send.setEnabled(false);
        btn_save.setOnClickListener(this::SaveResult);
        btn_save.setEnabled(false);
        btn_change_name.setOnClickListener(this::ChangeName);
        setMode(currMode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            String feedback = data.getStringExtra("feedback");
            show_name.setText(feedback);
        }
    }
    public void restart(View view) {
        setMode(currMode);
        show_attempts.setText(Integer.toString(attempts));
        show_msg.setText((R.string.show_msg_label));
        btn_guess.setEnabled(true);
        btn_send.setEnabled(false);
        btn_save.setEnabled(false);
        editNum.getText().clear();
    }
    public void ChooseMode(View view) {
        int mode = currMode;
        builder.setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setMode(currMode);
            }
        });
        builder.setSingleChoiceItems(R.array.diaps_array, mode, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                currMode=which;
            }
        });
        builder.create().show();
    }
    public void SendResult(View view) {
        String result;
        result = (success)? "Победа! Загаданное число: " + comp_num:"Проигрыш! Загаданное число: "+ comp_num;
        sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, result);
        sendIntent.setType("text/plain");
        shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
    public void SaveResult(View view) {
        String result;
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy  hh:mm ");
        result = (success)? formatForDateNow.format(date) + "\nПобеда! Загаданное число: " + comp_num: formatForDateNow.format(date)+ "\nПроигрыш! Загаданное число: "+ comp_num;
        saveIntent = new Intent(Intent.ACTION_SEND);
        String packageName = "com.miui.notes";
        String clsName = packageName + ".ui.NotesListActivity";
        ComponentName component = new ComponentName(packageName, clsName);
        saveIntent.setComponent(component);
        saveIntent.putExtra(Intent.EXTRA_TEXT, result);
        saveIntent.setType("text/plain");
        startActivity(saveIntent);
    }
    public void ChangeName(View view) {
        intent = new Intent(this,MainActivity2.class);
        intent.putExtra(Intent.EXTRA_TEXT, show_name.getText().toString());
        startActivityForResult(intent, MY_REQUEST_CODE);
    }
    public void setMode(int mode)
    {
        int min,max;
        editNum.getText().clear();
        switch (mode){
            default:
            case 0:
            {
                attempts = 5;
                min = 10;
                max = 99;
                editNum.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
                break;
            }
            case 1:
            {
                attempts = 7;
                min = 100;
                max = 999;
                editNum.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
                break;
            }
            case 2:
            {
                attempts = 10;
                min = 1000;
                max = 9999;
                editNum.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
                break;
            }
        }
        show_attempts.setText(Integer.toString(attempts));
        comp_num = GuessNum.rnd_comp_num(min,max);
    }
    public void guess(View view) {
        if(editNum.getText().toString().length()!=0) {
            int num = Integer.parseInt(editNum.getText().toString());
            if (num == comp_num) {
                success = true;
                show_msg.setText(R.string.show_msg_finish_label);
                Toast.makeText(context, R.string.wishes, Toast.LENGTH_LONG).show();
                btn_guess.setEnabled(false);
                btn_send.setEnabled(true);
                btn_save.setEnabled(true);
            } else if (num < comp_num) {
                show_hint.setText(R.string.show_msg_bigger_label);
            } else {
                show_hint.setText(R.string.show_msg_smaller_label);
            }
            attempts--;
            show_attempts.setText(Integer.toString(attempts));
            if (attempts == 0 && num != comp_num) {
                Toast.makeText(context, R.string.loss, Toast.LENGTH_LONG).show();
                btn_guess.setEnabled(false);
                btn_send.setEnabled(true);
                btn_save.setEnabled(true);
            }
        }
    }
}
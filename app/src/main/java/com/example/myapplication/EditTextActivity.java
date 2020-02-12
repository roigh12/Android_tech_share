package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditTextActivity extends AppCompatActivity {

    EditText edit_Name;
    EditText edit_Text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        edit_Name = findViewById(R.id.edit_Name);
        edit_Text = findViewById(R.id.edit_Text);

        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        switch (from) {
            case "file_add":
                sendTextToMain();
                break;
            case "file_edit":
                editFile();
                break;
        }
        Log.d("Naver", intent.getStringExtra("from"));


    }

    // 버튼을 눌렀을 때 작성한 내용으로 intent에 실어서 메인에 보낸다.
    private void sendTextToMain() {
        findViewById(R.id.edit_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                // 작성한 내용으로 파일 저장하기
                intent.putExtra("Name", edit_Name.getText().toString());
                intent.putExtra("Content", edit_Text.getText().toString());

                finish();
            }
        });
    }

    // 지정된 문자열로 edit을 시작한다.
    private void editFile() {
        Intent intent = getIntent();
        String Name = intent.getStringExtra("file_name");
        String Content = intent.getStringExtra("file_content");

        Log.d("Naver", "NAme = " + Name);
        edit_Name.setText(Name);
        edit_Text.setText(Content);
        sendTextToMain();
    }
}

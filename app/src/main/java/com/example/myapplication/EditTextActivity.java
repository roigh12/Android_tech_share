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


        clickButton();


    }

    private void clickButton() {
        findViewById(R.id.edit_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                // 작성한 내용으로 파일 저장하기
                edit_Name = findViewById(R.id.edit_Name);
                edit_Text = findViewById(R.id.edit_Text);
                intent.putExtra("Name", edit_Name.getText().toString());
                intent.putExtra("Content", edit_Text.getText().toString());

                finish();
            }
        });
    }
}

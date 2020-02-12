package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager linearLayoutManager, gridLayoutManager, layoutManager;
    private File here;
    private TextView pointer;
    private ImageView toggle;
    private static final int REQUEST_CODE_ADD_FILE = 1000;


    private final String TAG = "Naver";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // FIle[]을 통으로 넘겨주는게 좋다.
        // 처리하여 제한된 정보만 넘겨주는 것보단

        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        layoutManager = linearLayoutManager;

        initView();

        String rootPath = getFilesDir().getAbsolutePath();
        print();

        setRecyclerData(getFilesDir().getAbsolutePath());
//      setRecyclerData(Environment.getRootDirectory().toString());


    }

    private void print() {
        for(File temp : getFilesDir().listFiles()) {
            Log.d(TAG, temp.getName());
        }
    }


    // 해당 파일 명으로 만든다.
    public void makeTextFile(String fileName, String fileContent) {
        try {

            OutputStream output = new FileOutputStream(here + "/" + fileName);
            byte[] by= fileContent.getBytes();
            output.write(by);
            Log.d(TAG, "작성됨" + fileContent);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // 해당 파일 명으로 읽어옴
    public void readTextFile(String fileName) {
        try {
            //파일 객체 생성
            File file = new File(here + "/" + fileName);
            //입력 스트림 생성
            FileReader file_reader = new FileReader(file);
            int cur = 0;
            String tt = "";
            while ((cur = file_reader.read()) != -1) {
                tt += (char) cur;
            }
            Log.d(TAG, tt);
            file_reader.close();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }


    private void initView() {
        here = getFilesDir();

        toggle = findViewById(R.id.main_layout_change);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutManager == linearLayoutManager) {
                    toggle.setActivated(true);
                    layoutManager = gridLayoutManager;
                    mAdapter.setLayoutType(ItemType.LAYOUT_GRID);

                } else {
                    toggle.setActivated(false);
                    layoutManager = linearLayoutManager;
                    mAdapter.setLayoutType(ItemType.LAYOUT_LINEAR);
                }
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        // 좌측 상단 백버튼 클릭
        findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        // 파일 생성 버튼 클릭
        findViewById(R.id.add_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditTextActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_FILE);
            }
        });

        pointer = findViewById(R.id.pointer);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }



    private void goBack() {
        if(here.getAbsoluteFile().equals(Environment.getDataDirectory().toString())) {
            finish();
        }
        else {
            File parentFile = here.getParentFile();
            String pointerTextNow = pointer.getText().toString();
            pointer.setText(pointerTextNow.substring(0, pointerTextNow.length() - (here.getName().length() + 3)));
            here = parentFile;
            Log.d(TAG, "here = " + here.getAbsolutePath());

            mAdapter.setDataList(parentFile.listFiles());
        }
    }

    private void setRecyclerData(String filePath) {
        File[] files = new File(filePath).listFiles();
        mAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setDataList(files);
        mAdapter.setOnItemClickListener(itemClickListener);

    }

    private RecyclerAdapter.OnItemClickListener itemClickListener
            = new RecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(File data) {
            if(data.isDirectory() && data.listFiles() != null) {
                File[] files = data.listFiles();
                here = data;
                pointer.append(" > " + here.getName());
                Log.d(TAG, "here = " + here.getAbsolutePath());

                mAdapter.setDataList(files);
            }
        }
    };

    @Override
    public void onBackPressed() { goBack(); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD_FILE && resultCode == RESULT_OK) {
            String fileName = data.getStringExtra("Name");
            String fileContent = data.getStringExtra("Content");
            makeTextFile(fileName, fileContent);
            setRecyclerData(getFilesDir().getAbsolutePath());
        }

    }

}

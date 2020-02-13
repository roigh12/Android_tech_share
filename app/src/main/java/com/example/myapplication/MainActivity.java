package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    public static Context mcontext;
    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager linearLayoutManager, gridLayoutManager, layoutManager;
    private File here;
    private TextView pointer;
    private ImageView toggle;
    private static final int REQUEST_CODE_ADD_FILE = 1000;
    private static final int REQUEST_CODE_EDIT_FILE = 2000;
    public int position;


    private final String TAG = "Naver";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
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
        Log.d(TAG, "file리스트 시작");
        for(File temp : getFilesDir().listFiles()) {
            Log.d(TAG, temp.getName());
        }
        Log.d(TAG, "file리스트 끝");
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

    // 해당 파일 명으로 읽어와서 반
    public String readTextFile(String fileName) {
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
            file_reader.close();
            return tt;
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return "";
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
                intent.putExtra("from", "file_add");
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


    // 파일 클릭하면
    private RecyclerAdapter.OnItemClickListener itemClickListener
            = new RecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view) {
            File data = mAdapter.mDataset[(int) view.getTag(mAdapter.POSITION_TAG)];

            // ContextMenu 등록하기 (view 받아와서 처리)
            //registerForContextMenu(view);
            // 폴더 클릭했을 때
            if(data.isDirectory() && data.listFiles() != null) {
                File[] files = data.listFiles();
                here = data;
                pointer.append(" > " + here.getName());
                Log.d(TAG, "here = " + here.getAbsolutePath());

                mAdapter.setDataList(files);
            }
            // 파일 클릭했을 때
            else{
                // 파일 내용 읽어서 intent에 실어서 editActivity에 보낸다.
                Intent intent = new Intent(MainActivity.this, EditTextActivity.class);
                intent.putExtra("from", "file_edit");
                String Name = data.getName();
                String Content = readTextFile(Name);
                intent.putExtra("file_name", Name);
                intent.putExtra("file_content", Content);
                startActivityForResult(intent, REQUEST_CODE_EDIT_FILE);

            }
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        position = (int) v.getTag(mAdapter.POSITION_TAG);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        String a = mAdapter.mDataset[position].getAbsolutePath();
        int b = position;
        String c = String.valueOf(position);
        switch (item.getItemId()) {
            case R.id.delete:
                setDirEmpty(a);
                mAdapter.setDataList(here.listFiles());
                Toast.makeText(MainActivity.this, c, Toast.LENGTH_SHORT).show();
                break;


        }

        return super.onContextItemSelected(item);
    }

    // 해당 디렉토리 통째로 비우기
    public void setDirEmpty(String dirPath){
        Log.d(TAG, "오");
        String path = dirPath;

        File dir = new File(path);

        if(dir.exists()){
            // 폴더일 때
            if(dir.isDirectory() && dir.listFiles() != null){
                File[] childFileList = dir.listFiles();


                for (File childFile : childFileList) {
                    if (childFile.isDirectory()) {
                        setDirEmpty(childFile.getAbsolutePath());    //하위 디렉토리
                    } else {
                        childFile.delete();    //하위 파일
                    }
                }
                dir.delete();
            }
            // 파일일 때
            else{
                dir.delete();
            }
        }



    }

    @Override
    public void onBackPressed() { goBack(); }

    // 액티비티에서 데이터 받아옴.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD_FILE || requestCode == REQUEST_CODE_EDIT_FILE) {
            if(resultCode == RESULT_OK) {
                String fileName = data.getStringExtra("Name");
                String fileContent = data.getStringExtra("Content");
                makeTextFile(fileName, fileContent);
                mAdapter.setDataList(here.listFiles());


            }
        }

    }



    //


}

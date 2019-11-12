package com.github.rkhusainov.externalstoragepermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements FilesAdapter.OnFileClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int READ_REQUEST_CODE = 1;

    private RecyclerView mRecyclerView;
    private FilesAdapter mFilesAdapter;

    private Stack<File> mFilesStack = new Stack<>();
    private File[] mFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        requestExternalReadPermission();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isExternalStorageReadable()) {
                getFilesList();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private void getFilesList() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        mFilesStack.push(externalStorageDirectory);
        Log.d(TAG, "first: " + mFilesStack.size());
        mFiles = externalStorageDirectory.listFiles();
        mFilesAdapter = new FilesAdapter(Arrays.asList(externalStorageDirectory.list()), this);
        mRecyclerView.setAdapter(mFilesAdapter);
    }

    private void requestExternalReadPermission() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
        } else {
            getFilesList();
        }
    }

    @Override
    public void onFileClick(int position) {
        if (mFiles[position].isDirectory()) {
            mFilesStack.push(mFiles[position]);
            Log.d(TAG, "onClick: " + mFilesStack.size());
            mFilesAdapter.setFiles(Arrays.asList(mFiles[position].list()));
        }
    }

    @Override
    public void onBackPressed() {
        if (mFilesStack.size() == 1)
            super.onBackPressed();
        else {
            mFilesAdapter.setFiles(Arrays.asList(mFilesStack.pop().list()));
            Log.d(TAG, "onBackPressed: " + mFilesStack.size());
        }
    }
}

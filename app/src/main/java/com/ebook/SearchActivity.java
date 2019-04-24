package com.ebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ebook.model.BookLab;

import java.io.File;
import java.util.Scanner;

public class SearchActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Context context;
    private String title;
    private Bitmap bitmap;
    private String host = "129.204.93.61";
    private String localhost = "127.0.0.1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        context = MyApplication.getContext();
        Button searchButton = (Button) findViewById(R.id.search_button);
        final Button downloadButton = (Button) findViewById(R.id.download_button);
        final TextView textView = (TextView) findViewById(R.id.title);
        final ImageView imageView = (ImageView) findViewById(R.id.icon);
        final LinearLayout downloadView = (LinearLayout) findViewById(R.id.download_view);
        final EditText editText = (EditText) findViewById(R.id.search_input);
        final Client client = new Client(host,1234);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("信息已接受");
                Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
                downloadButton.setText("下载完成");
            }
        };
        final Handler handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("信息已接受");
                Toast.makeText(context,"查找完成",Toast.LENGTH_SHORT).show();
                final Intent stopIntent = new Intent(context,Client.class);

                String path = context.getExternalCacheDir().getAbsolutePath()+"/books/title.txt";
                File file = new File(path);
                try{
                    Scanner scanner = new Scanner(file,"utf-8");
                    title = scanner.nextLine();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (title == "")
                {
                    Toast.makeText(context,"找不到该书籍",Toast.LENGTH_SHORT).show();
                    client.stopService(stopIntent);
                    return;
                }
                downloadView.setVisibility(View.VISIBLE);
                path = context.getExternalCacheDir().getAbsolutePath()+"/books/abc.jpg";
                Bitmap bitmap = BookLab.getImage(path);
                imageView.setImageBitmap(bitmap);
                textView.setText(title);

                downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadButton.setText("下载中");
                        downloadButton.setEnabled(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                client.getText(title,context);
                                Message message=new Message();
                                message.what=123;
                                message.obj="通过Handler机制";
                                handler.sendMessage(message);

                            }
                        }).start();
                    }
                });
            }
        };
        searchButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                final String name = editText.getText().toString();

               new Thread(new Runnable() {
                   @Override
                   public void run() {

                       final String title = client.getTitle(name,context);
                       Message message=new Message();
                       message.what=123;
                       message.obj="通过Handler机制";
                       handler2.sendMessage(message);
                   }
               }).start();


            }
        });

//        mToolbar = (Toolbar) findViewById(R.id.toolbar3);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ali_feedback_common_back_btn_normal);
//        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//        }
//        return true;
//    }
}

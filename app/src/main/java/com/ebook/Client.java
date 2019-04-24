package com.ebook;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client extends Service {

    Socket client;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    public Client(){

    }
    public Client(String serverName,int port){
        try {

            client = new Socket(serverName, port);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    void sendline(String bookName){
        try {
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(bookName);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    String getTitle(String name,Context context){
        System.out.println("getTitle");
        String temp = "";
        String title = null;
        sendline(name);
        try {
            String dirPath = context.getExternalCacheDir().getAbsolutePath()+"/books";
            File file2 = new File(dirPath);
            if (!file2.exists()) file2.mkdirs();
            temp = getContent(10);
            int titleLength = Integer.parseInt(temp);
            System.out.println(titleLength);
            if (titleLength == 0)
                return  null;
            title = getContent(titleLength);
            String titlePath =  context.getExternalCacheDir().getAbsolutePath()+"/books/title.txt";

            File file = new File(titlePath);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(title);
            fileWriter.close();

            //获取图片
            temp = getContent(10);
            String path = context.getExternalCacheDir().getAbsolutePath()+"/books/abc.jpg";
            System.out.println("img path1 :  "+path);
            int img_size = Integer.parseInt(temp);
            byte[] bytes = getImg(img_size);
            File img = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(img);
            fileOutputStream.write(bytes);
            fileOutputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return title;
    }
    void getText(final String title, Context context){
        final Context context1 = context;
        final String title1 = title;

                String temp = "";
                try {
                    //获取文件数
                    temp = getContent(10);
                    int fileNum = Integer.parseInt(temp);
                    System.out.println(fileNum);

                    String dirPath = context1.getExternalCacheDir().getAbsolutePath()+"/books/"+title1;
                    File file = new File(dirPath);
                    if (!file.exists()) file.mkdirs();
                    System.out.println("download path:"+dirPath);
                    //获取图片
                    temp = getContent(10);
                    int img_size = Integer.parseInt(temp);
                    byte[] bytes = getImg(img_size);
                    String imgPath = dirPath+"/"+title+".jpg";
                    File img = new File(imgPath);
                    FileOutputStream fileOutputStream = new FileOutputStream(img);
                    fileOutputStream.write(bytes);
                    fileOutputStream.close();


                    //获取内容
                    int get = 1;
                    String path = dirPath+"/"+title+".txt";
                    File file2 = new File(path);

                    FileWriter fileWriter = new FileWriter(file2,true);
                    while (get <fileNum){

                        temp = getContent(10);
                        int textSize = Integer.parseInt(temp);
                        temp = getContent(textSize);
                        fileWriter.write(temp);
                        get++;
                    }
                    fileWriter.close();
                stopSelf();



                } catch (Exception e) {
                    e.printStackTrace();
                    stopSelf();
                }



    }
    String getContent(int length){
        String s="";
        try{
            InputStream inputStream = client.getInputStream();
            byte[] bytes = new byte[length];
            int getlen = 0;
            while (getlen <length)
            {
                getlen = inputStream.read(bytes,getlen,length-getlen)+getlen;
            }
            s = new String(bytes,"utf-8");

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return s;
    }
    byte[] getImg(int length){
        byte[] bytes = new byte[length];
        try{
            InputStream inputStream = client.getInputStream();
            int getlen = 0;
            while (getlen <length)
            {
                getlen = inputStream.read(bytes,getlen,length-getlen)+getlen;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return bytes;

    }

}

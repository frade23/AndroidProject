package com.ebook.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mum on 2017/1/24.
 */

public class BookLab {
    public static final String TEXT = "text";
    public static final String IMAGE = "image";
    private static BookLab sBookLab;

    private AssetManager mAssetManager;
    private List<Book> mBookList;
    //assets中的文件名清单
    private String[] mAssetsImageList;
    private String[] mAssetsTextList;


    private BookLab(Context context) {
        mAssetManager = context.getAssets();
        loadAssetsFiles(context);
    }


    public static BookLab newInstance(Context context) {
        sBookLab = new BookLab(context);
        return sBookLab;
    }

    //加载assets中的文件
    private void loadAssetsFiles(Context context) {
        System.out.println("loadAssetsFiles is called ");
        mBookList = new ArrayList<>();
        String path = context.getExternalCacheDir().getAbsolutePath()+"/books";
        System.out.println("loadpath:"+path);
        File booksDir = new File(path);
        if (!booksDir.exists())
            booksDir.mkdirs();

        File[] books = booksDir.listFiles();
        if (books == null)
            return;
        for (int i = 0 ;i< books.length;i++){
            System.out.println(books[i].getName());
            if (books[i].isDirectory()){
                //获取标题
                String bookTitle = books[i].getName();
                Bitmap bookCover = null;
                String bodyText = null;
                File[] files = books[i].listFiles();
                for (int j = 0;j<files.length;j++){
                    //获取文本
                    if (files[j].getName().compareTo(bookTitle+".txt")==0){
                         bodyText =getText(files[j].getPath());
                    }
                    //获取图片
                    if (files[j].getName().compareTo(bookTitle+".jpg")==0){
                         bookCover = getImage(files[j].getPath());
                    }
                }
                Book book = new Book(bookTitle,bookCover,bodyText);
                mBookList.add(book);
            }
        }
        //获取image、text中的文件名清单

//        try {
//            mAssetsImageList = mAssetManager.list(IMAGE);
//            mAssetsTextList = mAssetManager.list(TEXT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        for (int i = 0; i < mAssetsTextList.length; i++) {
//            //获取书名
//            String[] nameSplit = mAssetsTextList[i].split("_");
//            String nameSecond = nameSplit[nameSplit.length - 1];
//            String bookTitle = nameSecond.replace(".txt", "");
//
//            //获取封面
//            String imagePath = IMAGE + "/" + mAssetsImageList[i];
//            Bitmap bookCover = loadImage(imagePath);
//
//            //获取文本
//            String textPath = TEXT + "/" + mAssetsTextList[i];
//            String bodyText = loadText(textPath);
//
//
//            Book book = new Book(bookTitle, bookCover, bodyText);
//            mBookList.add(book);
//
//        }

    }

    private String getText(String path){
        InputStream in = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            reader = new BufferedReader(new InputStreamReader(in));

            String line = "";
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return stringBuilder.toString();
    }
    public static Bitmap getImage(String path){
        System.out.println("img path2 :  "+path);
        Bitmap image = null;
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            image = BitmapFactory.decodeStream(in);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return image;
    }




    public List<Book> getBookList() {
        return mBookList;
    }
}

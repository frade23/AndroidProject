package com.ebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v7.recyclerview.*;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ebook.model.Book;
import com.ebook.model.BookLab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyy on 2017/3/31.
 */

public class shelfActivity extends AppCompatActivity {


    /**
     * @function 返回托管的fragment
     */
    protected  Fragment createFragment(){
        return new Fragment();
    }
    private DrawerLayout mDrawerLayout;
    private Icon icon;
    private Context mContext;
    private List<Book> mBookList;
    @Override
    protected void onResume() {
        super.onResume();
        initEvents();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreen();
        setContentView(R.layout.shelf);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home_head_setting);
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.model:
                        break;
                    case R.id.auto_download:
                        break;
                    case R.id.agreement:
                        Intent intent = new Intent(shelfActivity.this, AgreementActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.button_setting:
                        Intent intent2 = new Intent(shelfActivity.this, SettingsActivity.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shelfActivity.this, DownloadManageActivity.class);
                startActivity(intent);
            }
        });
        initEvents();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.search_icon:
                Intent intent = new Intent(shelfActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.menu:
                Intent intent2 = new Intent(shelfActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * @function 设置屏幕显示状态
     */
    protected void setScreen() {
    }
    private void initEvents() {
        System.out.println("initevent is called ");
        mContext = MyApplication.getContext();
        mBookList = BookLab.newInstance(mContext).getBookList();
        View cv = getWindow().getDecorView();

        RecyclerView recyclerView = (RecyclerView) cv.findViewById(R.id.fragment_book_shelf_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setAdapter(new shelfActivity.BookAdapter(mBookList));

    }



    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mBookCover;
        private Book mBook;

        public BookHolder(View itemView) {
            super(itemView);
            mBookCover = (ImageView) itemView.findViewById(R.id.item_recycler_view_image_view);
            itemView.setOnClickListener(this);
        }

        public void bind(Book book) {
            mBook = book;
            mBookCover.setImageBitmap(mBook.getBookCover());
        }

        @Override
        public void onClick(View v) {
            mContext = MyApplication.getContext();

            Intent intent = ReadingActivity.newIntent(mContext, mBookList.indexOf(mBook));
            startActivity(intent);
        }


    }

    private class BookAdapter extends RecyclerView.Adapter<shelfActivity.BookHolder> {
        private List<Book> bookList = new ArrayList<>();

        public BookAdapter(List<Book> bookList) {
            this.bookList = bookList;
        }

        @Override
        public shelfActivity.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_recycler_view_shelf, parent, false);

            return new shelfActivity.BookHolder(view);
        }

        @Override
        public void onBindViewHolder(shelfActivity.BookHolder holder, int position) {
            holder.bind(bookList.get(position));
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }
    }
}

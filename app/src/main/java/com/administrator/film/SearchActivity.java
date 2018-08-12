package com.administrator.film;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.administrator.adapter.FilmAdapter;
import com.administrator.bean.Film;
import com.administrator.util.ActivityCollector;
import com.administrator.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Film> films = new ArrayList<>();

    private AppCompatButton search;
    private AppCompatEditText name;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                //设置Adapter
                FilmAdapter filmAdapter = new FilmAdapter(films);
                recyclerView.setAdapter(filmAdapter);
                ToastUtil.normalShow(SearchActivity.this, "查询结束!", true);
            } else if (msg.what == 1) {
                ToastUtil.normalShow(SearchActivity.this, "输入框为空!", true);
            } else if (msg.what == 2) {
                ToastUtil.normalShow(SearchActivity.this, "查询失败!", true);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bmob.initialize(this, "97b23ad6570940e818fc1f748192d3cc");
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        //设置Adapter
        FilmAdapter filmAdapter = new FilmAdapter(films);
        recyclerView.setAdapter(filmAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        search = findViewById(R.id.search);
        name = findViewById(R.id.name);
        search.setOnClickListener(this);
    }

    private void initFilms(final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<Film> query = new BmobQuery<>();
                //查询playerName叫“比目”的数据
                query.addWhereEqualTo("name", name);
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(50);
                //执行查询方法
                query.findObjects(new FindListener<Film>() {
                    @Override
                    public void done(List<Film> object, BmobException e) {
                        if(e == null) {
                            for (Film film : object) films.add(film);
                            handler.sendEmptyMessage(0);
                            Log.d("TAG", object.size() + "=========");
                        } else {
                            handler.sendEmptyMessage(2);
                            Log.d("TAG","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search: {
                Editable n = name.getText();
                if (n == null) return;
                String a = n.toString();
                if (TextUtils.isEmpty(a)) {
                    handler.sendEmptyMessage(1);
                } else {
                    initFilms(a);
                }
                break;
            }
        }
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.show(this, R.string.exit_app, true);
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.sm:startActivity(new Intent(this, PushActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

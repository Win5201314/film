package com.administrator.film;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<Film> films = new ArrayList<>();
    private FilmAdapter filmAdapter;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //设置Adapter
                    filmAdapter = new FilmAdapter(films);
                    recyclerView.setAdapter(filmAdapter);
                    break;
                case 1:
                    if (fs != null && fs.size() > 0) {
                        Log.d("TAG", "进来了====");
                        filmAdapter.AddHeaderItem(fs);
                    }
                    //刷新完成
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.normalShow(MainActivity.this, "更新完成!", true);
                    break;
                case 2:
                    if (fs != null && fs.size() > 0) {
                        Log.d("TAG", "进来了");
                        filmAdapter.AddFooterItem(fs);
                    }
                    ToastUtil.normalShow(MainActivity.this, "更新完成!", true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "97b23ad6570940e818fc1f748192d3cc");
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        //initFilms();
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE ,Color.GREEN);
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        //设置Adapter
        filmAdapter = new FilmAdapter(films);
        recyclerView.setAdapter(filmAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        initListener();
    }

    private void initListener() {
        initPullRefresh();
        initLoadMoreListener();
    }

    private void initFilms() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<Film> query = new BmobQuery<>();
                //查询playerName叫“比目”的数据
                //query.addWhereEqualTo("name", name);
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
            case R.id.fab: {
                startActivity(new Intent(this, PushActivity.class));
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
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.search:startActivity(new Intent(this, SearchActivity.class));
                break;
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Fun(1);
                    }

                }, 3000);

            }
        });
    }

    private void initLoadMoreListener() {

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem ;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1 == filmAdapter.getItemCount()){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Fun(2);
                        }
                    }, 3000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

    }

    private static List<Film> fs = new ArrayList<>();
    private void Fun(final int type) {
        fs.clear();
        BmobQuery<Film> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        //query.addWhereEqualTo("name", name);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<Film>() {
            @Override
            public void done(List<Film> object, BmobException e) {
                if(e == null) {
                    fs.addAll(object);
                    handler.sendEmptyMessage(type);
                    Log.d("TAG", object.size() + "=========");
                } else {
                    Log.d("TAG","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

}

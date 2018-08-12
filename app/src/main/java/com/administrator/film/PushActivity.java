package com.administrator.film;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.administrator.bean.Film;
import com.administrator.util.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PushActivity extends BaseActivity implements View.OnClickListener {

    private AppCompatEditText name;
    private AppCompatEditText path;
    private AppCompatEditText pw;
    private AppCompatButton share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        name = findViewById(R.id.name);
        path = findViewById(R.id.path);
        pw = findViewById(R.id.pw);
        share = findViewById(R.id.share);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.share) {
            try {
                String filmName = name.getText().toString();
                String filmPath = path.getText().toString();
                String filmPassword = pw.getText().toString();
                if (TextUtils.isEmpty(filmName) || TextUtils.isEmpty(filmPath) || TextUtils.isEmpty(filmPassword)) {
                    ToastUtil.normalShow(this, "请将三个输入框填写完整!", true);
                } else {
                    shareToBmob(filmName, filmPath, filmPassword);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void shareToBmob(String filmName, String filmPath, String filmPassword) {
        Film film = new Film(filmName, filmPath, filmPassword);
        film.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if(e == null){
                    ToastUtil.normalShow(PushActivity.this, "分享成功!", true);
                }else{
                    ToastUtil.normalShow(PushActivity.this, "分享失败!", true);
                }
            }
        });
    }

}

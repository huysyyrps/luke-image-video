package com.example.luke_imagevideo_send.main.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.main.bean.TokenTest;
import com.example.luke_imagevideo_send.main.module.TokenTestContract;
import com.example.luke_imagevideo_send.main.presenter.TokenTestPresenter;

public class TestActivity extends AppCompatActivity implements TokenTestContract.View {
    TokenTestPresenter tokenTestPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tokenTestPresenter = new TokenTestPresenter(this, this);
        tokenTestPresenter.getTokenTest();
    }

    @Override
    public void setTokenTest(TokenTest tokenTest) {
        if (tokenTest.isSuccess()){
            Toast.makeText(this, tokenTest.getData(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setTokenTestMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
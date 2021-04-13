package com.example.luke_imagevideo_send.cehouyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.views.Header;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThresholdActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.etTop)
    EditText etTop;
    @BindView(R.id.etButton)
    EditText etButton;
    @BindView(R.id.btnConfim)
    Button btnConfim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_threshold;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @OnClick(R.id.btnConfim)
    public void onViewClicked() {
        Intent intent=new Intent();
        intent.putExtra("top",etTop.getText().toString());
        intent.putExtra("button",etButton.getText().toString());
        setResult(Constant.TAG_ONE, intent);
        finish();
    }
}
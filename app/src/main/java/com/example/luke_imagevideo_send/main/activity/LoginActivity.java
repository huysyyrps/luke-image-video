package com.example.luke_imagevideo_send.main.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.magnetic.activity.SendSelectActivity;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.main.bean.Login;
import com.example.luke_imagevideo_send.main.module.LoginContract;
import com.example.luke_imagevideo_send.main.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassWord)
    EditText etPassWord;
    @BindView(R.id.ivSeeNewPw)
    ImageView ivSeeNewPw;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tvForgrtPassword)
    TextView tvForgrtPassword;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    Intent intent;
    private boolean mbDisplayFlg = false;
    private static boolean isExit = false;
    AlertDialogUtil alertDialogUtil;
    LoginPresenter loginPresenter;
    SharePreferencesUtils sharePreferencesUtils;
    //推出程序
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //推出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            alertDialogUtil.showDialog(getResources().getString(R.string.alert_logout), new AlertDialogCallBack() {

                @Override
                public void confirm(String name) {
                    finish();
                }

                @Override
                public void cancel() {
                    isExit = false;
                }

                @Override
                public void save(String name) {

                }

                @Override
                public void checkName(String name) {

                }
            });
            mHandler.sendEmptyMessageDelayed(0, 4000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this, this);
        sharePreferencesUtils = new SharePreferencesUtils();
        String userNamenew = sharePreferencesUtils.getString(this, "userNamenew", "");
        String passWordnew = sharePreferencesUtils.getString(this, "passWordnew", "");
        etUserName.setText(userNamenew);
        etPassWord.setText(passWordnew);

        String userName = sharePreferencesUtils.getString(this, "userName", "");
        String passWord = sharePreferencesUtils.getString(this, "passWord", "");
        if (!userName.equals("") && !passWord.equals("")) {
            etUserName.setText(userName);
            etPassWord.setText(passWord);
        }
        alertDialogUtil = new AlertDialogUtil(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @OnClick({R.id.ivSeeNewPw, R.id.btn_login, R.id.tvForgrtPassword, R.id.tvRegister, R.id.tv1, R.id.tv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSeeNewPw:
                if (!mbDisplayFlg) {
                    ivSeeNewPw.setImageResource(R.drawable.ic_login_unsee);
                    etPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ivSeeNewPw.setImageResource(R.drawable.ic_login_see);
                    etPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mbDisplayFlg = !mbDisplayFlg;
                break;
            case R.id.btn_login:
//                if (etUserName.getText().toString().equals("") || etPassWord.getText().toString().equals("")) {
//                    Toast.makeText(this, getResources().getString(R.string.no_name_password), Toast.LENGTH_SHORT).show();
//                } else {
//                    if ((Boolean) new NetworkTest().goToNetWork(this)) {
//                        loginPresenter.getLogin(etUserName.getText().toString(), etPassWord.getText().toString());
//                    } else {
//                        Toast.makeText(this, getResources().getString(R.string.umeng_socialize_network), Toast.LENGTH_SHORT).show();
//                    }
//                }
                intent = new Intent(this, SendSelectActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tvForgrtPassword:
                intent = new Intent(this, CheckPassWordActivity.class);
                startActivity(intent);
                break;
            case R.id.tvRegister:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv1:
                intent = new Intent(this, AgreeActivity.class);
                intent.putExtra("tag", "tv1");
                startActivity(intent);
                break;
            case R.id.tv2:
                intent = new Intent(this, AgreeActivity.class);
                intent.putExtra("tag", "tv2");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String userName = sharePreferencesUtils.getString(this, "userName", "");
        String passWord = sharePreferencesUtils.getString(this, "passWord", "");
        if (!userName.equals("") && !passWord.equals("")) {
            etUserName.setText(userName);
            etPassWord.setText(passWord);
        }
    }

    @Override
    public void setLogin(Login loginBean) {
        if (loginBean.isLogin()) {
            sharePreferencesUtils.setString(this, "userName", etUserName.getText().toString());
            sharePreferencesUtils.setString(this, "passWord", etPassWord.getText().toString());
            intent = new Intent(this, SendSelectActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, loginBean.getData(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setLoginMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
package com.example.luke_imagevideo_send.main.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.utils.JudgePhone;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.main.bean.Register;
import com.example.luke_imagevideo_send.main.module.RegisterContract;
import com.example.luke_imagevideo_send.main.presenter.RegisterPresenter;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.etPassWord)
    EditText etPassWord;
    @BindView(R.id.ivSeeNewPw)
    ImageView ivSeeNewPw;
    @BindView(R.id.btnUp)
    Button btnUp;

    int time = 60;
    Timer mTimer;
    TimerTask mTask;
    EventHandler eh;
    boolean type = false;
    RegisterPresenter registerPresenter;
    private boolean mbDisplayFlg = false;
    private boolean mbDisplayFlgAgina = false;
    SharePreferencesUtils sharePreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        sharePreferencesUtils = new SharePreferencesUtils();
        registerPresenter = new RegisterPresenter(RegisterActivity.this, this);
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Log.e("XXX", "1");
                        type = false;
                        Message message = new Message();
                        message.what = Constant.TAG_TWO;
                        mHandler.sendMessage(message);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        Log.e("XXX", "2");
//                        Message message = new Message();
//                        message.what = Constant.TAG_THERE;
//                        mHandler.sendMessage(message);
//                        btnUp.setEnabled(true);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        Log.e("XXX", "3");
//                        btnUp.setEnabled(true);
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    Message message = new Message();
                    message.what = Constant.TAG_FOUR;
                    mHandler.sendMessage(message);

                }
            }
        };
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @OnClick({R.id.tvCode, R.id.ivSeeNewPw, R.id.btnUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCode:
                if (etPhone.getText().toString().equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.prompt_phone), Toast.LENGTH_SHORT).show();
                } else {
                    if (new JudgePhone().isMobileNO(etPhone.getText().toString())) {
                        if (mTimer == null && mTask == null) {
                            mTimer = new Timer();
                            mTask = new TimerTask() {
                                @Override
                                public void run() {
                                    Message message = mHandler.obtainMessage(Constant.TAG_ONE);
                                    mHandler.sendMessage(message);
                                }
                            };
                            mTimer.schedule(mTask, 0, 1000);
                        }
                        tvCode.setEnabled(false);
                        //注册一个事件回调监听，用于处理SMSSDK接口请求的结果
                        SMSSDK.registerEventHandler(eh);
                        SMSSDK.getVerificationCode("86", etPhone.getText().toString());
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.error_invalid_phone), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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
            case R.id.btnUp:
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 得到InputMethodManager的实例
                if (imm.isActive()) {
                    // 如果开启
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (new JudgePhone().isMobileNO(etPhone.getText().toString())) {
                    if (etPhone.getText().toString().equals("")) {
                        Toast.makeText(this, getResources().getString(R.string.no_name_password), Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.prompt_relaer_phone), Toast.LENGTH_SHORT).show();
                }
                if (etCode.getText().toString().equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.write_code), Toast.LENGTH_SHORT).show();
                    break;
                }
                if (etPassWord.getText().toString().equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.no_name_password), Toast.LENGTH_SHORT).show();
                    break;
                }
                btnUp.setEnabled(false);
                SMSSDK.registerEventHandler(eh);
                SMSSDK.submitVerificationCode("86", etPhone.getText().toString(), etCode.getText().toString());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SMSSDK.unregisterEventHandler(eh);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.TAG_ONE:
                    time -= 1;
                    tvCode.setText(time + "");
                    if (time == 0) {
                        tvCode.setText(getResources().getString(R.string.get_code));
                        tvCode.setEnabled(true);
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                        }
                        if (mTask != null) {
                            mTask.cancel();
                            mTask = null;
                        }
                        time = 60;
                    }
                    break;
                case Constant.TAG_TWO:
                    registerPresenter.getRegister(etPhone.getText().toString(), etPassWord.getText().toString());
                    break;
                case Constant.TAG_FOUR:
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.faile_code), Toast.LENGTH_SHORT).show();
                    btnUp.setEnabled(true);
                    break;
                case Constant.TAG_THERE:
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.faile_code), Toast.LENGTH_SHORT).show();
                    btnUp.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    public void setRegister(Register bean) {

        if (bean.getCode() == 1) {
            sharePreferencesUtils.setString(this, "userName", etPhone.getText().toString());
            sharePreferencesUtils.setString(this, "passWord", etPassWord.getText().toString());
            Toast.makeText(this, getResources().getString(R.string.register_success), Toast.LENGTH_SHORT).show();
            finish();
        } else if (bean.getCode() == 3) {
            Toast.makeText(this, bean.getMsg(), Toast.LENGTH_SHORT).show();
        }

        btnUp.setEnabled(true);
    }

    @Override
    public void setRegisterMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}

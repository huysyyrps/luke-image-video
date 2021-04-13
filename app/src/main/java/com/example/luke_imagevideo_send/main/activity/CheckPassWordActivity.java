package com.example.luke_imagevideo_send.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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
import com.example.luke_imagevideo_send.http.utils.MD5;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.main.bean.CheckPassWord;
import com.example.luke_imagevideo_send.main.module.CheckPassWordContract;
import com.example.luke_imagevideo_send.main.presenter.CheckPassWordPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class CheckPassWordActivity extends BaseActivity implements CheckPassWordContract.View {

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
    String vers;
    CheckPassWordPresenter checkPassWordPresenter;
    SharePreferencesUtils sharePreferencesUtils;
    private boolean mbDisplayFlg = false;
    private boolean mbDisplayFlgAgina = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        Log.e("XXX", "3");
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    Message message = new Message();
                    message.what = Constant.TAG_FOUR;
                    mHandler.sendMessage(message);
                }
            }
        };
        sharePreferencesUtils = new SharePreferencesUtils();
        String userName = sharePreferencesUtils.getString(this, "userName", "");
        String passWord = sharePreferencesUtils.getString(this, "passWord", "");
        if (!userName.equals("") && !passWord.equals("")) {
            etPhone.setText(userName);
        }
        checkPassWordPresenter = new CheckPassWordPresenter(this, this);
        etPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_forgrt_password;
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
                                    Message message = mHandler.obtainMessage(1);
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
                        Toast.makeText(this, getResources().getString(R.string.prompt_relaer_phone), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.ivSeeNewPw:
                if (!mbDisplayFlgAgina) {
                    etPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mbDisplayFlgAgina = !mbDisplayFlgAgina;
                break;
            case R.id.btnUp:
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
                    Toast.makeText(this, getResources().getString(R.string.write_password), Toast.LENGTH_SHORT).show();
                    break;
                }
                SMSSDK.submitVerificationCode("86", etPhone.getText().toString(), etCode.getText().toString());
                break;
        }
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
                    String newPassWord = new MD5().md5(etPassWord.getText().toString());

                    //当前上下文是activity
                    checkPassWordPresenter.getCheckPassWord(etPhone.getText().toString(), newPassWord);
                    break;
                case Constant.TAG_THERE:
                    try {
                        JSONObject jsonObject = new JSONObject(vers);
                        if (jsonObject.getString("code").equals("1")) {
                            Toast.makeText(CheckPassWordActivity.this, getResources().getString(R.string.toast_change_password_success), Toast.LENGTH_SHORT).show();
                            sharePreferencesUtils.setString(CheckPassWordActivity.this, "userName", etPhone.getText().toString());
                            sharePreferencesUtils.setString(CheckPassWordActivity.this, "passWord", etPassWord.getText().toString());
                            Intent intent = new Intent(CheckPassWordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CheckPassWordActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.TAG_FOUR:
                    Toast.makeText(CheckPassWordActivity.this, getResources().getString(R.string.faile_code), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void setCheckPassWord(CheckPassWord checkPassWord) {
        if (checkPassWord.getCode() == 3) {
            Toast.makeText(this, checkPassWord.getMsg(), Toast.LENGTH_SHORT).show();
        } else if (checkPassWord.getCode() == 1) {
            Toast.makeText(this, getResources().getString(R.string.toast_change_password_success), Toast.LENGTH_SHORT).show();
            sharePreferencesUtils.setString(this, "userName", etPhone.getText().toString());
            sharePreferencesUtils.setString(this, "passWord", etPassWord.getText().toString());
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void setCheckPassWordMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SMSSDK.unregisterEventHandler(eh);
    }
}

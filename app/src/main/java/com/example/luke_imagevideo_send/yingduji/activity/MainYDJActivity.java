package com.example.luke_imagevideo_send.yingduji.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.views.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainYDJActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    @BindView(R.id.rb5)
    RadioButton rb5;
    @BindView(R.id.rb6)
    RadioButton rb6;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvCJCL)
    TextView tvCJCL;
    @BindView(R.id.tvPJZ)
    TextView tvPJZ;
    @BindView(R.id.tvMAX)
    TextView tvMAX;
    @BindView(R.id.tvMIN)
    TextView tvMIN;
    @BindView(R.id.tvCJFX)
    TextView tvCJFX;
    @BindView(R.id.tvWCZ)
    TextView tvWCZ;
    @BindView(R.id.tvDW)
    TextView tvDW;
    @BindView(R.id.tvCSCS)
    TextView tvCSCS;
    @BindView(R.id.tvCJLX)
    TextView tvCJLX;
    @BindView(R.id.tvHSZ)
    TextView tvHSZ;

    Handler handler;
    private PopupWindow mPopWindow;
    BaseRecyclerAdapter baseRecyclerAdapter;
    List<String> CJCLList = new ArrayList<>();
    List<String> CJFXList = new ArrayList<>();
    List<String> DWList = new ArrayList<>();
    List<String> CSCSList = new ArrayList<>();
    List<String> CJLXList = new ArrayList<>();
    List<String> HSZList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTime();
        makeData();
        tvCJCL.setText(CJCLList.get(0));
        tvCJFX.setText(CJFXList.get(0));
        tvDW.setText(DWList.get(0));
        tvCSCS.setText(CSCSList.get(2));
        tvHSZ.setText(HSZList.get(0));
        tvCJLX.setText(CJLXList.get(0));
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main_y_d_j;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    //组装数据
    private void makeData() {
        CJCLList.add("碳钢");
        CJCLList.add("合金工具钢");
        CJCLList.add("不锈钢");
        CJCLList.add("轴承钢");
        CJCLList.add("灰口铸铁");
        CJCLList.add("球墨铸铁");
        CJCLList.add("铸铝");
        CJCLList.add("黄铜");
        CJCLList.add("青铜");
        CJCLList.add("纯铜");

        CJFXList.add("垂直向下");
        CJFXList.add("垂直向上");
        CJFXList.add("水平方向");
        CJFXList.add("斜上45°");
        CJFXList.add("斜下45°");

        DWList.add("里氏");
        DWList.add("维氏");
        DWList.add("布氏");
        DWList.add("洛氏B");
        DWList.add("洛氏C");
        DWList.add("肖氏");

        CSCSList.add("1");
        CSCSList.add("2");
        CSCSList.add("3");
        CSCSList.add("4");
        CSCSList.add("5");
        CSCSList.add("6");
        CSCSList.add("7");
        CSCSList.add("8");
        CSCSList.add("9");
        CSCSList.add("10");

        HSZList.add("NR(不换算强度)");
        HSZList.add("KG(Kgf/mm^2)");
        HSZList.add("KS(Klbs/in^2)");
        HSZList.add("TN(Tons/in^2)");

        CJLXList.add("碳化物（LD）");
        CJLXList.add("金刚石(DD)");
        CJLXList.add("冲击装置(DL)");
    }

    // 初始化时间方法
    public void initTime() {
        // 时间变化
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                tvTime.setText((String) msg.obj);
            }
        };
        Threads thread = new Threads();
        thread.start();
    }

    class Threads extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String str = sdf.format(new Date());
                    handler.sendMessage(handler.obtainMessage(100, str));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.rb1, R.id.rb2, R.id.rb3, R.id.rb4, R.id.rb5, R.id.rb6, R.id.tvCJCL,R.id.tvCJFX,
            R.id.tvWCZ, R.id.tvDW, R.id.tvCSCS, R.id.tvCJLX, R.id.tvHSZ})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb1:
                break;
            case R.id.rb2:
                break;
            case R.id.rb3:
                break;
            case R.id.rb4:
                break;
            case R.id.rb5:
                break;
            case R.id.rb6:
                break;
            case R.id.tvCJCL:
                showPopupWindow("CJCL");
                break;
            case R.id.tvCJFX:
                showPopupWindow("CJFX");
                break;
            case R.id.tvWCZ:
                break;
            case R.id.tvDW:
                showPopupWindow("DW");
                break;
            case R.id.tvCSCS:
                showPopupWindow("CSCS");
                break;
            case R.id.tvCJLX:
                showPopupWindow("CJLX");
                break;
            case R.id.tvHSZ:
                showPopupWindow("HSZ");
                break;
        }
    }

    private void showPopupWindow(String tag) {
        View contentView = LayoutInflater.from(MainYDJActivity.this).inflate(R.layout.popuplayout, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mPopWindow.setFocusable(true);

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        if (tag.equals("CJCL")){
            baseRecyclerAdapter = new BaseRecyclerAdapter<String>(this, R.layout.popuplayout_item, CJCLList) {
                @Override
                public void convert(BaseViewHolder holder, String o) {
                    holder.setText(R.id.textView, o);
                    holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvCJCL.setText(o);
                            mPopWindow.dismiss();
                        }
                    });
                }
            };
            mPopWindow.showAsDropDown(tvCJCL);
        }else if (tag.equals("CJFX")){
            baseRecyclerAdapter = new BaseRecyclerAdapter<String>(this, R.layout.popuplayout_item, CJFXList) {
                @Override
                public void convert(BaseViewHolder holder, String o) {
                    holder.setText(R.id.textView, o);
                    holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvCJFX.setText(o);
                            mPopWindow.dismiss();
                        }
                    });
                }
            };
            mPopWindow.showAsDropDown(tvCJFX);
        }else if (tag.equals("DW")){
            baseRecyclerAdapter = new BaseRecyclerAdapter<String>(this, R.layout.popuplayout_item, DWList) {
                @Override
                public void convert(BaseViewHolder holder, String o) {
                    holder.setText(R.id.textView, o);
                    holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvDW.setText(o);
                            mPopWindow.dismiss();
                        }
                    });
                }
            };
            mPopWindow.showAsDropDown(tvDW);
        }else if (tag.equals("CSCS")){
            baseRecyclerAdapter = new BaseRecyclerAdapter<String>(this, R.layout.popuplayout_item, CSCSList) {
                @Override
                public void convert(BaseViewHolder holder, String o) {
                    holder.setText(R.id.textView, o);
                    holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvCSCS.setText(o);
                            mPopWindow.dismiss();
                        }
                    });
                }
            };
            mPopWindow.showAsDropDown(tvCSCS);
        }else if (tag.equals("HSZ")){
            baseRecyclerAdapter = new BaseRecyclerAdapter<String>(this, R.layout.popuplayout_item, HSZList) {
                @Override
                public void convert(BaseViewHolder holder, String o) {
                    holder.setText(R.id.textView, o);
                    holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvHSZ.setText(o);
                            mPopWindow.dismiss();
                        }
                    });
                }
            };
            mPopWindow.showAsDropDown(tvHSZ);
        }else if (tag.equals("CJLX")){
            baseRecyclerAdapter = new BaseRecyclerAdapter<String>(this, R.layout.popuplayout_item, CJLXList) {
                @Override
                public void convert(BaseViewHolder holder, String o) {
                    holder.setText(R.id.textView, o);
                    holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvCJLX.setText(o);
                            mPopWindow.dismiss();
                        }
                    });
                }
            };
            mPopWindow.showAsDropDown(tvCJLX);
        }

        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.notifyDataSetChanged();
    }
}
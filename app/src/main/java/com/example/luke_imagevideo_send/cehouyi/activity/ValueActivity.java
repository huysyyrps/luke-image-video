package com.example.luke_imagevideo_send.cehouyi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerPositionAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.DialogCallBack;
import com.example.luke_imagevideo_send.http.views.Header;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ValueActivity extends BaseActivity {
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.tv9)
    TextView tv9;
    @BindView(R.id.tv10)
    TextView tv10;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.llSpinner)
    LinearLayout llSpinner;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    List<Integer> valueList = new ArrayList<>();
    List<List<Integer>> myValueList = new ArrayList<>();
    List<String> spinnerList = new ArrayList<>();
    BaseRecyclerPositionAdapter baseRecyclerAdapter;
    private ArrayAdapter<String> arr_adapter;
    int positionNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 注册订阅者
        EventBus.getDefault().register(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        initMyView();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_value;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(List<List<Integer>> myValueList) {
        this.myValueList = myValueList;
    }

    private void initMyView() {
        valueList = myValueList.get(0);
        if (myValueList.size() == 0) {
            llSpinner.setVisibility(View.GONE);
        } else {
            setSpinnerData();
        }
    }

    //设置序号textView是否显示
    public void setTVGV() {
        if (valueList.size() % 3 == 0) {
            if (valueList.size() / 3 == 1) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 2) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 3) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 4) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 5) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 6) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 7) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.VISIBLE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 8) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.VISIBLE);
                tv8.setVisibility(View.VISIBLE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 9) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.VISIBLE);
                tv8.setVisibility(View.VISIBLE);
                tv9.setVisibility(View.VISIBLE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 10) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.VISIBLE);
                tv8.setVisibility(View.VISIBLE);
                tv9.setVisibility(View.VISIBLE);
                tv10.setVisibility(View.VISIBLE);
            }
        } else {
            if (valueList.size() < 3) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 1) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 2) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 3) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 4) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 5) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 6) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.VISIBLE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 7) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.VISIBLE);
                tv8.setVisibility(View.VISIBLE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 8) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.VISIBLE);
                tv8.setVisibility(View.VISIBLE);
                tv9.setVisibility(View.VISIBLE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size() / 3 == 9) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.VISIBLE);
                tv8.setVisibility(View.VISIBLE);
                tv9.setVisibility(View.VISIBLE);
                tv10.setVisibility(View.VISIBLE);
            }
        }
    }

    //设置spinner数据
    public void setSpinnerData() {
        int size = myValueList.size();
        for (int i = 0; i < size; i++) {
            int num = i + 1;
            spinnerList.add("f00" + num);
        }
        arr_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        //设置默认样式 android.R.layout.simple_spinner_dropdown_item
        // 自定义样式与右边三角形一致
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);
        //为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        //设置垂直偏移量 (以下属性均可布局里设置)
        spinner.setDropDownVerticalOffset(80);
        //设置水平偏移量
        spinner.setDropDownHorizontalOffset(80);
        //MODE_DROPDOWN 在控件下面显示   MODE_DIALOG:在中间显示
        spinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        //里面item点击监听 默认选择第一个
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionNum = position;
                valueList = myValueList.get(position);
                baseRecyclerAdapter = new BaseRecyclerPositionAdapter<Integer>(ValueActivity.this, R.layout.swiperrecycler_item, valueList) {
                    @Override
                    public void convert(BaseViewHolder holder, final Integer o, int position) {
                        holder.setText(R.id.tv, o + "");
                        holder.setOnClickListener(R.id.llItem, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (position + 1 <= 3) {
                                    new AlertDialogUtil(ValueActivity.this).showSmallDialog("您确定要删除第1组数据吗", new DialogCallBack() {
                                        @Override
                                        public void confirm(String name) {
                                            if (valueList.size() == 1) {
                                                valueList.remove(0);
                                            }
                                            if (valueList.size() == 2) {
                                                valueList.remove(0);
                                                valueList.remove(0);
                                            }
                                            if (valueList.size() >= 3) {
                                                valueList.remove(0);
                                                valueList.remove(0);
                                                valueList.remove(0);
                                            }
                                            setTVGV();
                                            baseRecyclerAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void cancel() {

                                        }

                                    });
                                } else {
                                    if ((position + 1) % 3 == 0) {
                                        int num = (position + 1) / 3;
                                        new AlertDialogUtil(ValueActivity.this).showSmallDialog("您确定要删除第" + num + "组数据吗", new DialogCallBack() {
                                            @Override
                                            public void confirm(String name) {
                                                valueList.remove(position - 2);
                                                valueList.remove(position - 2);
                                                valueList.remove(position - 2);
                                                setTVGV();
                                                baseRecyclerAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void cancel() {

                                            }
                                        });
                                    } else {
                                        if ((position + 1) % 3 == 1) {
                                            int num = (position + 1) / 3 + 1;
                                            new AlertDialogUtil(ValueActivity.this).showSmallDialog("您确定要删除第" + num + "组数据吗", new DialogCallBack() {
                                                @Override
                                                public void confirm(String name) {
                                                    if (valueList.size() - position >= 3) {
                                                        valueList.remove(position);
                                                        valueList.remove(position);
                                                        valueList.remove(position);
                                                    } else {
                                                        valueList.remove(position);
                                                    }
                                                    setTVGV();
                                                    baseRecyclerAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void cancel() {

                                                }
                                            });
                                        }
                                        if ((position + 1) % 3 == 2) {
                                            int num = (position + 1) / 3 + 1;
                                            new AlertDialogUtil(ValueActivity.this).showSmallDialog("您确定要删除第" + num + "组数据吗", new DialogCallBack() {
                                                @Override
                                                public void confirm(String name) {
                                                    if (valueList.size() - position == 1) {
                                                        valueList.remove(position - 1);
                                                        valueList.remove(position - 1);
                                                    }
                                                    if (valueList.size() - position == 2) {
                                                        valueList.remove(position - 1);
                                                        valueList.remove(position - 1);
                                                        valueList.remove(position - 1);
                                                    }
                                                    if (valueList.size() - position >= 3) {
                                                        valueList.remove(position - 1);
                                                        valueList.remove(position - 1);
                                                        valueList.remove(position - 1);
                                                    }
                                                    setTVGV();
                                                    baseRecyclerAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void cancel() {

                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                };
                recyclerView.setAdapter(baseRecyclerAdapter);
                baseRecyclerAdapter.notifyDataSetChanged();
                setTVGV();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btnSave, R.id.btnCancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                myValueList.set(positionNum,valueList);
                break;
            case R.id.btnCancle:
                finish();
                break;
        }
    }
}
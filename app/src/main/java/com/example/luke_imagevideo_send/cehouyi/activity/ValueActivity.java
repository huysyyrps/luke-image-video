package com.example.luke_imagevideo_send.cehouyi.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    List<Integer> valueList = new ArrayList<>();
    List<Integer> valueNewList = new ArrayList<>();
    List<String> spinnerList = new ArrayList<>();
    BaseRecyclerPositionAdapter baseRecyclerAdapter;
    private ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 注册订阅者
        EventBus.getDefault().register(this);
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
    public void onGetStickyEvent(List<Integer> valueList) {
        this.valueList = valueList;
    }

    private void initMyView() {
        if (valueList.size() < 30) {
            valueNewList = valueList;
        } else {
            for (int i = 0; i < 30; i++) {
                valueNewList.add(valueList.get(i));
            }
            if (valueList.size()<30){
                llSpinner.setVisibility(View.GONE);
            }else {
                setSpinnerData();
            }
        }
        setTVGV();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        baseRecyclerAdapter = new BaseRecyclerPositionAdapter<Integer>(this, R.layout.swiperrecycler_item, valueNewList) {
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
                                    if (valueNewList.size() == 1) {
                                        valueNewList.remove(0);
                                    }
                                    if (valueNewList.size() == 2) {
                                        valueNewList.remove(0);
                                        valueNewList.remove(0);
                                    }
                                    if (valueNewList.size() >= 3) {
                                        valueNewList.remove(0);
                                        valueNewList.remove(0);
                                        valueNewList.remove(0);
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
                                int num = (position + 1) % 3;
                                new AlertDialogUtil(ValueActivity.this).showSmallDialog("您确定要删除第" + num + "组数据吗", new DialogCallBack() {
                                    @Override
                                    public void confirm(String name) {
                                        valueNewList.remove(position - 2);
                                        valueNewList.remove(position - 2);
                                        valueNewList.remove(position - 2);
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
                                            if (valueNewList.size() - position >= 3) {
                                                valueNewList.remove(position);
                                                valueNewList.remove(position);
                                                valueNewList.remove(position);
                                            } else {
                                                valueNewList.remove(position);
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
                                            if (valueNewList.size() - position == 1) {
                                                valueNewList.remove(position - 1);
                                                valueNewList.remove(position - 1);
                                            }
                                            if (valueNewList.size() - position == 2) {
                                                valueNewList.remove(position - 1);
                                                valueNewList.remove(position - 1);
                                                valueNewList.remove(position - 1);
                                            }
                                            if (valueNewList.size() - position >= 3) {
                                                valueNewList.remove(position - 1);
                                                valueNewList.remove(position - 1);
                                                valueNewList.remove(position - 1);
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
        }

        ;
        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.notifyDataSetChanged();
    }

    //设置序号textView是否显示
    public void setTVGV() {
        if (valueNewList.size() % 3 == 0) {
            if (valueNewList.size() / 3 == 1) {
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
            if (valueNewList.size() / 3 == 2) {
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 3) {
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 4) {
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 5) {
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 6) {
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 7) {
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 8) {
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 9) {
                tv10.setVisibility(View.GONE);
            }
        } else {
            if (valueNewList.size() / 3 == 1) {
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 2) {
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 3) {
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 4) {
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 5) {
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 6) {
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 7) {
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueNewList.size() / 3 == 8) {
                tv10.setVisibility(View.GONE);
            }
        }
    }

    //设置spinner数据
    public void setSpinnerData() {
        spinnerList.add("f001");
        for (int i=30;i<valueList.size();i++){
            if (i%30==0&&i<valueList.size()){
                int num = i/30+1;
                spinnerList.add("f00"+num);
            }
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
                //点击事件对应的epostion
                Log.e("XXX", "onItemSelected: " + position);
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
}
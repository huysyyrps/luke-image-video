package com.example.luke_imagevideo_send.cehouyi.activity;

import android.os.Bundle;
import android.view.View;
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
    List<Integer> valueList = new ArrayList<>();
    List<Integer> valueNewList = new ArrayList<>();
    BaseRecyclerPositionAdapter baseRecyclerAdapter;

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
        if (valueList.size()%3==0){
            if (valueList.size()/3==1){
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
            if (valueList.size()/3==2){
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==3){
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==4){
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==5){
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==6){
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==7){
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==8){
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==9){
                tv10.setVisibility(View.GONE);
            }
        }else {
            if (valueList.size()/3==1){
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==2){
                tv4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==3){
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==4){
                tv6.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==5){
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==6){
                tv8.setVisibility(View.GONE);
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==7){
                tv9.setVisibility(View.GONE);
                tv10.setVisibility(View.GONE);
            }
            if (valueList.size()/3==8){
                tv10.setVisibility(View.GONE);
            }
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        if (valueList.size()<30){
            valueNewList = valueList;
        }else {
            for (int i = 0;i<valueList.size();i++){
                while (i<30){
                    valueNewList.add(valueList.get(i));
                }
            }
        }
        baseRecyclerAdapter = new BaseRecyclerPositionAdapter<Integer>(this, R.layout.swiperrecycler_item, valueNewList) {
            @Override
            public void convert(BaseViewHolder holder, final Integer o, int position) {
                holder.setText(R.id.tv,o+"");
                holder.setOnClickListener(R.id.llItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position+1 <= 3) {
                            new AlertDialogUtil(ValueActivity.this).showSmallDialog("您确定要删除第1组数据吗", new DialogCallBack() {
                                @Override
                                public void confirm(String name) {
                                    if (valueNewList.size()==1){
                                        valueNewList.remove(0);
                                    }
                                    if (valueNewList.size()==2){
                                        valueNewList.remove(0);
                                        valueNewList.remove(0);
                                    }
                                    if (valueNewList.size()>=3){
                                        valueNewList.remove(0);
                                        valueNewList.remove(0);
                                        valueNewList.remove(0);
                                    }
                                    baseRecyclerAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void cancel() {

                                }

                            });
                        }else {
                            if ((position+1)%3==0){
                                valueNewList.remove(position-2);
                                valueNewList.remove(position-2);
                                valueNewList.remove(position-2);
                                baseRecyclerAdapter.notifyDataSetChanged();
                            }else {
                                if ((position+1)%3==1){
                                    if (valueNewList.size()-position>=3){
                                        valueNewList.remove(position);
                                        valueNewList.remove(position);
                                        valueNewList.remove(position);
                                    }else {
                                        valueNewList.remove(position);
                                    }
                                    baseRecyclerAdapter.notifyDataSetChanged();
                                }
                                if ((position+1)%3==2){
                                    if (valueNewList.size()-position==1){
                                        valueNewList.remove(position-1);
                                        valueNewList.remove(position-1);
                                    }
                                    if (valueNewList.size()-position==2){
                                        valueNewList.remove(position-1);
                                        valueNewList.remove(position-1);
                                        valueNewList.remove(position-1);
                                    }
                                    if (valueNewList.size()-position>=3){
                                        valueNewList.remove(position-1);
                                        valueNewList.remove(position-1);
                                        valueNewList.remove(position-1);
                                    }
                                    baseRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
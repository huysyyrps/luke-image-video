package com.example.luke_imagevideo_send.cehouyi.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.adapter.DictDialogAdapter;
import com.example.luke_imagevideo_send.cehouyi.adapter.MyPagerAdapter;
import com.example.luke_imagevideo_send.cehouyi.bean.DictUnit;
import com.example.luke_imagevideo_send.cehouyi.util.DictDataManager;
import com.example.luke_imagevideo_send.cehouyi.view.MyViewPager;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.MenuAlertDialogCallBack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MenuActivity extends BaseActivity {
    private Context mContext;
    public DictDataManager mDictDataManager = DictDataManager.getInstance();
    private MyViewPager mViewPager;
    private View view1,view2;
    private ListView mListView1,mListView2;
    private DictDialogAdapter mListView1Adapter, mListView2Adapter;
    private List<View> views = new ArrayList<View>();
    private AlertDialog alertDialog;
    Intent intent;
    List<Integer> valueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        Intent intent = getIntent();
        valueList = intent.getIntegerArrayListExtra("data");
        initViews();
    }

    private void initViews() {
        mViewPager = (MyViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = LayoutInflater.from(this);
        view1 = inflater.inflate(R.layout.menu_page, null);
        view2 = inflater.inflate(R.layout.menu_page, null);
        mListView1 = (ListView) view1.findViewById(R.id.listview1);
        mListView2 = (ListView) view2.findViewById(R.id.listview1);

        List<DictUnit> list1=mDictDataManager.getTripleColumnData(this, "0");
        mListView1Adapter = new DictDialogAdapter(this, list1);
        mListView1Adapter.setSelectedBackground(R.drawable.ic_select_white);
        mListView1Adapter.setHasDivider(false);
        mListView1Adapter.setNormalBackgroundResource(R.color.dictdialog_bg_gray);
        mListView1.setAdapter(mListView1Adapter);

        views.add(view1);
        views.add(view2);
        mViewPager.setAdapter(new MyPagerAdapter(views));

        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView1Adapter != null)
                    mListView1Adapter.setSelectedItem(position);
                if (mListView2Adapter != null)
                    mListView2Adapter.setSelectedItem(-1);

                DictUnit dictUnit = (DictUnit) parent.getItemAtPosition(position);
                if (dictUnit.id.equals("0")) {//不限
                    if (mListView2Adapter != null) {
                        mListView2Adapter.setData(new ArrayList<DictUnit>());
                        mListView2Adapter.notifyDataSetChanged();
                    }

                    setResultDate(dictUnit);
                } else {
                    List<DictUnit> list2 = mDictDataManager.getTripleColumnData(mContext, dictUnit.id);
                    if (mListView2Adapter == null) {
                        mListView2Adapter = new DictDialogAdapter(mContext, list2);
                        mListView2Adapter.setNormalBackgroundResource(R.color.white);
                        mListView2.setAdapter(mListView2Adapter);
                    } else {
                        mListView2Adapter.setData(list2);
                        mListView2Adapter.notifyDataSetChanged();
                    }
                }

            }
        });
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if (mListView2Adapter != null) {
                    mListView2Adapter.setSelectedItem(position);
                    mListView2Adapter.setSelectedBackground(R.drawable.ic_select_gray);
                }
                DictUnit dictUnit = (DictUnit) parent.getItemAtPosition(position);
                if (dictUnit.name.equals("存储读取")){
                    intent = new Intent(MenuActivity.this,ValueActivity.class);
                    intent.putIntegerArrayListExtra("data", (ArrayList<Integer>) valueList);
                    startActivity(intent);
                    finish();
                }
                List<DictUnit> list3 = mDictDataManager.getTripleColumnData(mContext, dictUnit.id);
                if (list3==null){
                    intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("filed",dictUnit.field);
                    intent.putExtra("data",dictUnit.name);
                    //设置返回数据
                    setResult(RESULT_OK, intent);
                    //关闭Activity
                    finish();
                }else {
                    if (dictUnit.tag.equals("change")){
                        new AlertDialogUtil(MenuActivity.this).showListDialog("设置选项","change",list3, new MenuAlertDialogCallBack() {
                            @Override
                            public void confirm(String filed,String name) {
                                intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("filed",filed);
                                intent.putExtra("data",name);
                                //设置返回数据
                                setResult(RESULT_OK, intent);
                                //关闭Activity
                                finish();
                            }
                        });
                    }else {
                        new AlertDialogUtil(MenuActivity.this).showListDialog("设置选项","unchange",list3, new MenuAlertDialogCallBack() {
                            @Override
                            public void confirm(String filed,String name) {
                                intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("filed",filed);
                                intent.putExtra("data",name);
                                //设置返回数据
                                setResult(RESULT_OK, intent);
                                //关闭Activity
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }

    private void setResultDate(DictUnit dictUnit){
        Intent intent=new Intent();
        intent.putExtra("dict",(Serializable)dictUnit);
        setResult(0, intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_menu_activity;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }
}
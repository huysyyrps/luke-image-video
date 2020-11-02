package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.okhttp.QuietOkHttp;
import com.example.luke_imagevideo_send.http.okhttp.StringCallBack;
import com.example.luke_imagevideo_send.http.views.Header;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class PhotoActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<String> imagePaths = new ArrayList<>();
    List<String> selectList = new ArrayList<>();
    List<File> fileList = new ArrayList<>();
    BaseRecyclerAdapter baseRecyclerAdapter;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;

    private int startNum = 0;
    private int lastNum = 24;
    private int allNum;
    File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        linearLayout.setVisibility(View.GONE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(PhotoActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        baseRecyclerAdapter = new BaseRecyclerAdapter<String>(PhotoActivity.this, R.layout.album_item, imagePaths) {
            @Override
            public void convert(BaseViewHolder holder, final String o) {
                holder.setImage(PhotoActivity.this, R.id.imageView, o);
                holder.setOnClickListener(R.id.imageView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PhotoActivity.this, SeeImageOrVideoActivity.class);
                        intent.putExtra("path", o);
                        intent.putExtra("tag", "photo");
                        startActivity(intent);
                    }
                });

                holder.setCheckClickListener(R.id.cbSelect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectList.contains(o)) {
                            selectList.remove(o);
                        } else {
                            if (selectList.size() >= 9) {
                                Toast.makeText(PhotoActivity.this, "最多只能选择9张图片", Toast.LENGTH_SHORT).show();
                            } else {
                                selectList.add(o);
                            }
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.notifyDataSetChanged();
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                startNum = 0;
                lastNum = 24;
                imagePaths.clear();
                setData();
            }

            @Override
            public void loadMore() {
                if (allNum == lastNum){
                    Toast.makeText(PhotoActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                    pullToRefreshLayout.finishLoadMore();
                    pullToRefreshLayout.setCanLoadMore(false);
                }else if (lastNum < allNum){
                    startNum = lastNum;
                    lastNum+=24;
                    if (lastNum>=allNum){
                        lastNum = allNum;
                    }
                    setData();
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFilesAllName(Environment.getExternalStorageDirectory() + "/LUKEVideo/");
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFilesAllName(Environment.getExternalStorageDirectory() + "/LUKEImage/");
    }

    public void getFilesAllName(String path) {
        imagePaths.clear();
        //传入指定文件夹的路径
        File file = new File(path);
        files = file.listFiles();
        allNum = files.length;
        setData();
    }

    private void setData(){
        try {
            if (allNum > 24) {
                for (int i = startNum; i < lastNum; i++) {
                    if (checkIsImageFile(files[i].getPath()) && !files[i].getPath().equals("null")) {
                        imagePaths.add(files[i].getPath());
                    }
                }
            } else {
                for (int i = 0; i < files.length; i++) {
                    if (checkIsImageFile(files[i].getPath()) && files[i].getPath() != null) {
                        Bitmap bitmap = null;
                        bitmap = BitmapFactory.decodeFile(files[i].getPath());
                        if (bitmap != null) {
                            imagePaths.add(files[i].getPath());
                        }
                    }
                }
                lastNum = allNum;
                pullToRefreshLayout.finishLoadMore();
                pullToRefreshLayout.setCanLoadMore(false);
            }

        } catch (Exception e) {
//            Toast.makeText(HaveAudioActivity.this, e.toString() + "", Toast.LENGTH_SHORT).show();
        }
        pullToRefreshLayout.finishLoadMore();
        pullToRefreshLayout.finishRefresh();
        handler.sendEmptyMessage(Constant.TAG_ONE);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.TAG_ONE:
                    baseRecyclerAdapter.notifyDataSetChanged();
                    linearLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    /**
     * 判断是否是照片
     */
    public static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        //获取拓展名
        String fileEnd = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (fileEnd.equals("png")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    @OnClick(R.id.ivSend)
    public void onClick() {
        if (selectList.size() == 0) {
            Toast.makeText(PhotoActivity.this, "您还未选择图片", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PhotoActivity.this, selectList.size() + "", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < selectList.size(); i++) {
                fileList.add(new File(selectList.get(i)));
            }
            QuietOkHttp.postFile("URL_UPLOAD")
                    .uploadFile("files", fileList)
//                    .addParams("key", "10926a9165054566b6df6a8410e45f08")
                    .execute(new StringCallBack() {
                        @Override
                        protected void onSuccess(Call call, String response) {
                            Log.e("TAG", "response:" + response);
                        }

                        @Override
                        public void onFailure(Call call, Exception e) {
                            Log.e("TAG", "onFailure:" + e.toString());
                        }
                    });
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_photo;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }
}

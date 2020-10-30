package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
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
import com.example.luke_imagevideo_send.http.views.Header;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NoAudioActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    List<File> imagePaths = new ArrayList<>();
    List<String> selectList = new ArrayList<>();
    BaseRecyclerAdapter baseRecyclerAdapter;
    private int startNum = 0;
    private int lastNum = 12;
    private int allNum;
    File[] files;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        header.setTvTitle("无声视频");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(NoAudioActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        baseRecyclerAdapter = new BaseRecyclerAdapter<File>(NoAudioActivity.this, R.layout.album_item, imagePaths) {
            @Override
            public void convert(BaseViewHolder holder, final File o) {
                holder.setBitmap(R.id.imageView, getRingBitmap(o));
                holder.setVisitionTextView(R.id.tvTime);
                holder.setText(R.id.tvTime, getRingDuring(o));
                holder.setOnClickListener(R.id.imageView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NoAudioActivity.this, SeeImageOrVideoActivity.class);
                        intent.putExtra("path", o.getAbsolutePath());
                        intent.putExtra("tag", "video");
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                startNum = 0;
                lastNum = 12;
                imagePaths.clear();
                setData();
            }

            @Override
            public void loadMore() {
                if (allNum == lastNum){
                    Toast.makeText(NoAudioActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                    pullToRefreshLayout.finishLoadMore();
                    pullToRefreshLayout.setCanLoadMore(false);
                }else if (lastNum < allNum){
                    startNum = lastNum;
                    lastNum+=12;
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
                getFilesAllName(Environment.getExternalStorageDirectory() + "/LUKENoVideo/");
            }
        }).start();
    }

    public void getFilesAllName(String path) {
        //传入指定文件夹的路径
        File file = new File(path);
        files = file.listFiles();
        allNum = files.length;
        setData();
    }

    private void setData(){
        try {
            if (allNum > 12) {
                for (int i = startNum; i < lastNum+1; i++) {
                    Log.e("XXX", getRingDuring(files[i]));
                    if (getRingDuring(files[i]) != null && !getRingDuring(files[i]).equals("null")) {
                        imagePaths.add(files[i]);
                    }
                }
            } else {
                for (int i = startNum; i < files.length; i++) {
                    Log.e("XXX", getRingDuring(files[i]));
                    if (getRingDuring(files[i]) != null && !getRingDuring(files[i]).equals("null")) {
                        imagePaths.add(files[i]);
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

    @OnClick(R.id.ivSend)
    public void onClick() {
        if (selectList.size() == 0) {
            Toast.makeText(NoAudioActivity.this, "您还未选择图片", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(NoAudioActivity.this, selectList.size() + "", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取视频时长
     *
     * @param mUri
     * @return
     */
    public static String getRingDuring(File mUri) {
        String duration = null;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            if (mUri != null) {
                mmr.setDataSource(mUri.getAbsolutePath());
                duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            }
        } catch (Exception ex) {
            Log.e("XXX", ex.toString());
        } finally {
            mmr.release();
        }
        if (duration != null) {
            return timeParse(Long.parseLong(duration));
        } else {
            return "null";
        }
    }

    /**
     * 获取视频第一帧图片
     *
     * @param mUri
     * @return
     */
    public static Bitmap getRingBitmap(File mUri) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            if (mUri != null) {
                mmr.setDataSource(mUri.getAbsolutePath());
                bitmap = mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
            }
        } catch (Exception ex) {
            Log.e("XXX", ex.toString());
        } finally {
            mmr.release();
        }
        return bitmap;
    }

    /**
     * 将毫秒转换成分钟
     *
     * @param duration
     * @return
     */
    public static String timeParse(long duration) {
        String time = "";

        long minute = duration / 60000;
        long seconds = duration % 60000;

        long second = Math.round((float) seconds / 1000);

        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";

        if (second < 10) {
            time += "0";
        }
        time += second;

        return time;
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

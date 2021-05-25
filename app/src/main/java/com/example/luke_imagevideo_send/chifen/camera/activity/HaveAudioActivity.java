package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.bean.HaveVideoUp;
import com.example.luke_imagevideo_send.chifen.camera.module.HaveVideoContract;
import com.example.luke_imagevideo_send.chifen.camera.presenter.HaveVideoPresenter;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class HaveAudioActivity extends BaseActivity implements HaveVideoContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    List<File> imagePaths = new ArrayList<>();
    List<File> selectList = new ArrayList<>();
    BaseRecyclerAdapter baseRecyclerAdapter;
    private SVProgressHUD mSVProgressHUD;
    private int startNum = 0;
    private int lastNum = 12;
    private int allNum;
    File[] files;
    File[] files1;

    private HaveVideoPresenter haveVideoPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        ButterKnife.bind(this);
        header.setTvTitle("有声视频");
        haveVideoPresenter = new HaveVideoPresenter(this, this);
        mSVProgressHUD = new SVProgressHUD(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HaveAudioActivity.this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        baseRecyclerAdapter = new BaseRecyclerAdapter<File>(HaveAudioActivity.this, R.layout.album_item, imagePaths) {
            @Override
            public void convert(BaseViewHolder holder, final File o) {
                holder.setBitmap(R.id.imageView, getRingBitmap(o));
                holder.setVisitionTextView(R.id.tvTime);
                holder.setText(R.id.tvTime, getRingDuring(o));
                holder.setOnClickListener(R.id.imageView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HaveAudioActivity.this, SeeImageOrVideoActivity.class);
                        intent.putExtra("path", o.getAbsolutePath());
                        intent.putExtra("tag", "video");
                        startActivity(intent);
                    }
                });

                holder.setCheckClickListener(R.id.cbSelect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectList.contains(o)) {
                            selectList.remove(o);
                        } else {
                            if (selectList.size() >= 3) {
                                Toast.makeText(HaveAudioActivity.this, "最多只能选择3个视频", Toast.LENGTH_SHORT).show();
                            } else {
                                selectList.add(o);
                            }
                        }
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
                if (allNum == lastNum) {
                    Toast.makeText(HaveAudioActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                    pullToRefreshLayout.finishLoadMore();
                    pullToRefreshLayout.setCanLoadMore(false);
                } else if (lastNum < allNum) {
                    startNum = lastNum;
                    lastNum += 12;
                    if (lastNum >= allNum) {
                        lastNum = allNum;
                    }
                    setData();
                }
            }
        });
        mSVProgressHUD.showWithStatus("加载中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFilesAllName(Environment.getExternalStorageDirectory() + "/LUKEVideo/");
            }
        }).start();
    }

    public void getFilesAllName(String path) {
        //传入指定文件夹的路径
//        File file = new File(path);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/LUKEVideo/");
        files = file.listFiles();
        if (files != null) {
            allNum = files.length;
            setData();
        } else {
            handler.sendEmptyMessage(Constant.TAG_TWO);
        }
    }

    private void setData() {
        try {
            if (allNum > 12) {
                for (int i = startNum; i < lastNum; i++) {
                    Log.e("XXX", getRingDuring(files[i]));
                    if (getRingDuring(files[i]) != null && !getRingDuring(files[i]).equals("null")) {
                        imagePaths.add(files[i]);
                    }
                }
            } else {
                for (int i = startNum; i < files.length + 1; i++) {
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
                    mSVProgressHUD.dismiss();
                    break;
                case Constant.TAG_TWO:
                    Toast.makeText(HaveAudioActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    mSVProgressHUD.dismiss();
                    break;
            }
        }
    };

    @OnClick(R.id.ivSend)
    public void onClick() {
        if (selectList.size() == 0) {
            Toast.makeText(HaveAudioActivity.this, "您还未选择图片", Toast.LENGTH_SHORT).show();
        } else {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("video" , selectList);
//            Gson gson = new Gson();
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(map));
//            haveVideoPresenter.getHaveVideo(requestBody);
            MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM); //表单类型
            for (int i=0;i<selectList.size();i++){
                RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),selectList.get(i));
                builder.addFormDataPart("file"+i,selectList.get(i).getName(),requestBody);//"imgfile"+i 后台接收图片流的参数名
            }
            builder.addFormDataPart("company","shangjia002");
            builder.addFormDataPart("device" , "cehouyi001");
            List<MultipartBody.Part> parts = builder.build().parts();
            haveVideoPresenter.getHaveVideo(parts);
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
    public void setHaveVideo(HaveVideoUp HaveVideoUp) {
        Toast.makeText(this, HaveVideoUp.result+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setHaveVideoMessage(String message) {
        Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
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

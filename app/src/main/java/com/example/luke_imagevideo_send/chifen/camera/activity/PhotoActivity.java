package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.bean.PhotoUp;
import com.example.luke_imagevideo_send.chifen.camera.module.PhotoContract;
import com.example.luke_imagevideo_send.chifen.camera.presenter.PhotoPresenter;
import com.example.luke_imagevideo_send.chifen.camera.util.StringBase;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.main.activity.LoginActivity;
import com.google.gson.Gson;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PhotoActivity extends BaseActivity implements PhotoContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;

    List<String> imagePaths = new ArrayList<>();
    List<String> selectList = new ArrayList<>();
    BaseRecyclerAdapter baseRecyclerAdapter;
    SharePreferencesUtils sharePreferencesUtils;
    private SVProgressHUD mSVProgressHUD;
    private PhotoPresenter photoPresenter;
    private String project = "", workName = "", workCode = "",compName = "",device = "";
    private int startNum = 0;
    private int lastNum = 24;
    private int allNum;
    File[] files;
    List<File> fileListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        ButterKnife.bind(this);
        sharePreferencesUtils = new SharePreferencesUtils();
        compName = "鲁科检测";
        device = "磁探机";
        project = sharePreferencesUtils.getString(PhotoActivity.this,"project","");
        workName = sharePreferencesUtils.getString(PhotoActivity.this,"workName","");
        workCode = sharePreferencesUtils.getString(PhotoActivity.this,"workCode","");
        header.setTvTitle("图库");
        photoPresenter = new PhotoPresenter(this,this);
        mSVProgressHUD = new SVProgressHUD(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(PhotoActivity.this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        baseRecyclerAdapter = new BaseRecyclerAdapter<String>(PhotoActivity.this, R.layout.album_item, imagePaths) {
            @Override
            public void convert(BaseViewHolder holder, final String o) {
                holder.setImage(PhotoActivity.this, R.id.imageView, o);
                holder.setText( R.id.tvName, getFileName(o));
                holder.setOnClickListener(R.id.imageView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PhotoActivity.this, SeeImageOrVideoActivity.class);
                        intent.putExtra("path", o);
                        intent.putExtra("tag", "photo");
//                        startActivity(intent);
                        startActivityForResult(intent,Constant.TAG_ONE);
                    }
                });

                holder.setCheckClickListener(R.id.cbSelect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectList.contains(o)) {
                            selectList.remove(o);
                            if (selectList.size()==0){
                                header.setTvTitle("图库");
                            }else {
                                header.setTvTitle("图库"+"("+selectList.size()+"/9)");
                            }
                        } else {
                            if (selectList.size() >= 9) {
                                holder.setCheckBoxFalse( R.id.cbSelect);
                                Toast.makeText(PhotoActivity.this, "最多只能选择9张图片", Toast.LENGTH_SHORT).show();
                            } else {
                                selectList.add(o);
                                String[] strarray = o.split("/");
                                int leng = strarray.length;
                                compName = strarray[leng-5];
                                project = strarray[leng-4];
                                device = strarray[leng-3];
                                workName = strarray[leng-2];
                                header.setTvTitle("图库"+"("+selectList.size()+"/9)");
                                holder.setCheckBoxTrue( R.id.cbSelect);
                            }
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        pullToRefreshLayout.setCanLoadMore(false);
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                startNum = 0;
                lastNum = 24;
                imagePaths.clear();
                setData(fileListData);
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
                    setData(fileListData);
                }
            }
        });
        mSVProgressHUD.showWithStatus("加载中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFilesAllName(Environment.getExternalStorageDirectory() + "/LUKEImage/"+project+"/"+"设备/"+workName+"/"+workCode+"/");
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getFilesAllName(String path) {
        imagePaths.clear();
        fileListData = listFileSortByModifyTime(path);
        Collections.reverse(fileListData);
        if (fileListData.size()!=0){
            allNum = fileListData.size();
            setData(fileListData);
        } else {
            handler.sendEmptyMessage(Constant.TAG_TWO);
        }
    }

    private void setData(List<File> fileListData){
        try {
            if (allNum > 24) {
                pullToRefreshLayout.setCanLoadMore(true);
                for (int i = startNum; i < lastNum; i++) {
                    if (checkIsImageFile(fileListData.get(i).getPath()) && !fileListData.get(i).getPath().equals("null")) {
                        imagePaths.add(fileListData.get(i).getPath());
                    }
                }
            } else {
                for (int i = 0; i < fileListData.size(); i++) {
                    if (checkIsImageFile(fileListData.get(i).getPath()) && fileListData.get(i).getPath() != null) {
                        Bitmap bitmap = null;
                        bitmap = BitmapFactory.decodeFile(fileListData.get(i).getPath());
                        if (bitmap != null) {
                            Log.e("XXX",fileListData.get(i).getPath());
                            imagePaths.add(fileListData.get(i).getPath());
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

    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param path
     * @return
     */
    public static List<File> listFileSortByModifyTime(String path) {
        List<File> list = getFilesye(path, new ArrayList<File>());
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return -1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        }
        return list;
    }

    /**
     * 获取目录下所有文件
     *
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFilesye(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFilesye(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
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
                    Toast.makeText(PhotoActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    mSVProgressHUD.dismiss();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.ivSend)
    public void onClick() {
        if (sharePreferencesUtils.getString(this,"userName","").equals("")){
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            if (selectList.size() == 0) {
                Toast.makeText(PhotoActivity.this, "您还未选择图片", Toast.LENGTH_SHORT).show();
            } else {
                String base = "";
                String imageName = "";
                for (int i = 0; i < selectList.size(); i++) {
                    if (i == 0) {
                        base = new StringBase().bitmapToString(selectList.get(0));
                        imageName = getFileName(selectList.get(0));
                    } else {
                        base = base + "---" + new StringBase().bitmapToString(selectList.get(i));
                        imageName = imageName + "---" + getFileName(selectList.get(i));
                    }
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("company", compName);
                map.put("project", project);
                map.put("device", device);
                map.put("workpiece", workName);
                map.put("workpiecenum", workCode);
                map.put("name", imageName);
                map.put("pic", base);
                Gson gson = new Gson();
                String s = gson.toJson(map);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(map));
                photoPresenter.getPhoto(requestBody);
            }
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
        if (selectList.size()==0){
            Toast.makeText(this, "请先选择想要删除的文件", Toast.LENGTH_SHORT).show();
        }else {
            for (String path : selectList){
                imagePaths.remove(path);
                File file = new File(path);
                file.delete();
            }
            selectList.clear();
            header.setTvTitle("图库");
            recyclerView.setAdapter(null);
            recyclerView.setAdapter(baseRecyclerAdapter);
        }
    }


    @Override
    public void setPhoto(PhotoUp photoUp) {
        header.setTvTitle("图库");
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        selectList.clear();
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(baseRecyclerAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        baseRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPhotoMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 从路径中提取文件名
     * @param pathandname
     * @return
     */
    public String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.length();
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return "null";
        }

    }

}

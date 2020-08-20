package com.example.luke_imagevideo_send.chifen.camera.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.activity.SeeImageOrVideoActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.Constant;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoHaveAudioFragment extends Fragment {
    View view;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<File> imagePaths = new ArrayList<>();
    List<String> selectList = new ArrayList<>();
    BaseRecyclerAdapter baseRecyclerAdapter;
    @BindView(R.id.ivSend)
    ImageView ivSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);
        getFilesAllName(Environment.getExternalStorageDirectory() + "/LUKEVideo/");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        baseRecyclerAdapter = new BaseRecyclerAdapter<File>(getActivity(), R.layout.album_item, imagePaths) {
            @Override
            public void convert(BaseViewHolder holder, final File o) {
                holder.setBitmap(R.id.imageView, getRingBitmap(o));
                holder.setVisitionTextView(R.id.tvTime);
                holder.setText(R.id.tvTime,getRingDuring(o));
                holder.setOnClickListener(R.id.imageView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SeeImageOrVideoActivity.class);
                        intent.putExtra("path", o.getAbsolutePath());
                        intent.putExtra("tag", "video");
                        startActivity(intent);
                    }
                });

//                holder.setCheckClickListener(R.id.cbSelect, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (selectList.contains(o)) {
//                            selectList.remove(o);
//                        } else {
//                            if (selectList.size()>=9){
//                                Toast.makeText(getActivity(), "最多只能选择9张图片", Toast.LENGTH_SHORT).show();
//                            }else {
//                                selectList.add(o);
//                            }
//                        }
//                    }
//                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.notifyDataSetChanged();
        return view;
    }

    public List<File> getFilesAllName(String path) {
        //传入指定文件夹的路径
        File file = new File(path);
        File[] files = file.listFiles();
        try {
            for (int i = 0; i < files.length; i++) {
                if (checkIsImageFile(files[i])) {
                    imagePaths.add(files[i]);
                }
            }
        }
        catch(Exception e) {
            Toast.makeText(getActivity(), e.toString()+"", Toast.LENGTH_SHORT).show();
        }

        return imagePaths;
    }

    /**
     * 判断是否是照片
     */
    public static boolean checkIsImageFile(File fName) {
        boolean isImageFile = false;
        if (!fName.isDirectory()) {
            String filename = fName.getName();
            // 判断是否为MP4结尾
            if (filename.trim().toLowerCase().endsWith(".mp4")) {
                isImageFile = true;
            } else {
                isImageFile = false;
            }
        }
        return isImageFile;
    }

    @OnClick(R.id.ivSend)
    public void onClick() {
        if (selectList.size()==0){
            Toast.makeText(getActivity(), "您还未选择图片", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), selectList.size()+"", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取视频时长
     * @param mUri
     * @return
     */
    public static String getRingDuring(File mUri) {
        String duration = null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
            if (mUri != null) {
                mmr.setDataSource(mUri.getAbsolutePath());
                duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            }
        } catch (Exception ex) {
            Log.e("XXX",ex.toString());
        } finally {
            mmr.release();
        }
        if (duration!=null){
            return timeParse(Long.parseLong(duration));
        }else {
            return "null";
        }
    }

    /**
     * 获取视频第一帧图片
     * @param mUri
     * @return
     */
    public static Bitmap getRingBitmap(File mUri) {
        Bitmap bitmap = null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
            if (mUri != null) {
                mmr.setDataSource(mUri.getAbsolutePath());
                bitmap=mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
            }
        } catch (Exception ex) {
            Log.e("XXX",ex.toString());
        } finally {
            mmr.release();
        }
        return bitmap;
    }

    /**
     * 将毫秒转换成分钟
     * @param duration
     * @return
     */
    public static String timeParse(long duration) {
        String time = "" ;

        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;

        long second = Math.round((float)seconds/1000) ;

        if( minute < 10 ){
            time += "0" ;
        }
        time += minute+":" ;

        if( second < 10 ){
            time += "0" ;
        }
        time += second ;

        return time ;
    }
}
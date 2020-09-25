package com.example.luke_imagevideo_send.chifen.camera.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoFragment extends Fragment {
    View view;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<String> imagePaths = new ArrayList<>();
    List<String> selectList = new ArrayList<>();
    BaseRecyclerAdapter baseRecyclerAdapter;
    @BindView(R.id.ivSend)
    ImageView ivSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        baseRecyclerAdapter = new BaseRecyclerAdapter<String>(getActivity(), R.layout.album_item, imagePaths) {
            @Override
            public void convert(BaseViewHolder holder, final String o) {
                holder.setImage(getActivity(), R.id.imageView, o);
                holder.setOnClickListener(R.id.imageView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SeeImageOrVideoActivity.class);
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
                            if (selectList.size()>=9){
                                Toast.makeText(getActivity(), "最多只能选择9张图片", Toast.LENGTH_SHORT).show();
                            }else {
                                selectList.add(o);
                            }
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFilesAllName(Environment.getExternalStorageDirectory() + "/LUKEImage/");
    }

    public List<String> getFilesAllName(String path) {
        //传入指定文件夹的路径
        File file = new File(path);
        File[] files = file.listFiles();
        try {
            for (int i = 0; i < files.length; i++) {
                if (checkIsImageFile(files[i].getPath())) {
                    imagePaths.add(files[i].getPath());
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
        if (selectList.size()==0){
            Toast.makeText(getActivity(), "您还未选择图片", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), selectList.size()+"", Toast.LENGTH_SHORT).show();
        }
    }
}

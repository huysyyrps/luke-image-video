package com.example.luke_imagevideo_send.chifen.magnetic.util;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.activity.HaveAudioActivity;
import com.example.luke_imagevideo_send.chifen.camera.activity.NoAudioActivity;
import com.example.luke_imagevideo_send.chifen.camera.activity.PhotoActivity;
import com.example.luke_imagevideo_send.chifen.magnetic.activity.MainActivity;
import com.example.luke_imagevideo_send.chifen.magnetic.activity.SpideMainActivity;

public class MainUI {
    public void showPopupMenu(View view, MainActivity mainActivity) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(mainActivity, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.dialog, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("图片")){
                    Intent intent = new Intent(mainActivity, PhotoActivity.class);
                    mainActivity.startActivity(intent);
                }else if (item.getTitle().equals("有声视频")){
                    Intent intent = new Intent(mainActivity, HaveAudioActivity.class);
                    mainActivity.startActivity(intent);
                }else if (item.getTitle().equals("无声视频")){
                    Intent intent = new Intent(mainActivity, NoAudioActivity.class);
                    mainActivity.startActivity(intent);
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });

        popupMenu.show();
    }

    public void showPopupMenu1(View view, SpideMainActivity mainActivity) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(mainActivity, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.dialog, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("图片")){
                    Intent intent = new Intent(mainActivity, PhotoActivity.class);
                    mainActivity.startActivity(intent);
                }else if (item.getTitle().equals("有声视频")){
                    Intent intent = new Intent(mainActivity, HaveAudioActivity.class);
                    mainActivity.startActivity(intent);
                }else if (item.getTitle().equals("无声视频")){
                    Intent intent = new Intent(mainActivity, NoAudioActivity.class);
                    mainActivity.startActivity(intent);
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });

        popupMenu.show();
    }
}

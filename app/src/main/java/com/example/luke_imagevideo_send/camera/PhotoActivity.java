package com.example.luke_imagevideo_send.camera;

import android.os.Bundle;
import android.os.Environment;

import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.http.views.StatusBarUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private int chooseMode = PictureMimeType.ofAll();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        new StatusBarUtils().setWindowStatusBarColor(PhotoActivity.this, R.color.tab_compact_blue);
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(PhotoActivity.this)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量
                .selectionMode( PictureConfig.MULTIPLE)// 多选 or 单选
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .compressSavePath(getCompressPath())//压缩图片自定义保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_photo;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    // 压缩后图片文件存储位置
    private String getCompressPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PictureSelector/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


}
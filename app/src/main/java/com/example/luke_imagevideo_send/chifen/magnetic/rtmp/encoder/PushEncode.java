package com.example.luke_imagevideo_send.chifen.magnetic.rtmp.encoder;

import android.content.Context;
import android.util.Log;

public class PushEncode extends BasePushEncoder {

    public PushEncode(Context context, int textureId) {
        super(context);
        Log.e("chenzhu","id = "+textureId);
        setRender(new VideoPushRender(context,textureId));
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }
    public PushEncode(Context context ) {
        super(context);

    }
}

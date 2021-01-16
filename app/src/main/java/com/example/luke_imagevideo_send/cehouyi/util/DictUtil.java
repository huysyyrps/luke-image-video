package com.example.luke_imagevideo_send.cehouyi.util;

import android.content.Context;
import android.content.res.AssetManager;
import com.example.luke_imagevideo_send.cehouyi.bean.DictUnit;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DictUtil {
	public static List<DictUnit> position;
	public static HashMap<String, ArrayList<DictUnit>> positions;


    public static List<DictUnit> getPositions(Context context, String flag, String fileName) {
        if (position == null) {
            initPositions(context, fileName);
        }
        if (flag.equals("0")) {
            return position;
        } else {
            return positions.get(flag);
        }
    }

    private static void initPositions(Context context, String fileName) {
        String industryString = readAssetsTXT(context, fileName);
        String[] strings = industryString.split(";");
        position = new ArrayList<DictUnit>();
        positions = new HashMap<String, ArrayList<DictUnit>>();
        for (int i = 0; i < strings.length-1; i++) {
            String[] items = strings[i].split(",");
            DictUnit tmp = new DictUnit();
            tmp.id = items[0].trim();
            tmp.name = items[1];
            tmp.flag = items[2].trim();
            tmp.tag = items[3];
            tmp.field = items[4];
            if (tmp.flag.equals("0010")) {
                position.add(tmp);
            } else {
                if (positions.get(tmp.flag) == null) {
                    ArrayList<DictUnit> temp = new ArrayList<DictUnit>();
                    temp.add(tmp);
                    positions.put(tmp.flag, temp);
                } else {
                    positions.get(tmp.flag).add(tmp);
                }
            }
        }
    }

    /**
     * 读取assets文件夹下的txt字典库文�?
     *
     * @param context
     *            句柄
     * @param fName
     *            文件�?
     * @return
     */
    public static String readAssetsTXT(Context context, String fName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fName);
            byte[] bytes = new byte[1024];
            int leng;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((leng = is.read(bytes)) != -1) {
                baos.write(bytes, 0, leng);
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}

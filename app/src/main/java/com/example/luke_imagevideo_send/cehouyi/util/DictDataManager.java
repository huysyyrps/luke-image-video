package com.example.luke_imagevideo_send.cehouyi.util;

import android.content.Context;

import com.example.luke_imagevideo_send.cehouyi.bean.DictUnit;

import java.util.List;

public final class DictDataManager {
	public enum DictType {
	       POSITION_FUNCTION("position_function_new.txt");
	       
	       String fileName;
	       private DictType(String fileName){
	    	   this.fileName = fileName;
	       }
	       public String getFileName(){
	    	   return fileName;
	       }
		}

	private static DictDataManager mInstance;

	private DictDataManager() {

	}

	public static DictDataManager getInstance() {
		if (mInstance == null) {
			mInstance = new DictDataManager();
		}
		return mInstance;
	}

	public List<DictUnit> getTripleColumnData(Context mContext, String flag){
		return DictUtil.getPositions(mContext, flag, DictType.POSITION_FUNCTION.getFileName());
	}
}

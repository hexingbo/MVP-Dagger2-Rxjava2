package com.zenglb.baselib.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 导出DB 文件到getExternalStorageDirectory 目录去！
 * Created by zenglb on 2016/12/27.
 */
public class CopyDBUtils {
	/**
	 * Copy 私密数据库文件到 外部的存储空间 方便调试
	 *
	 * @param databaseName
	 * @param mContext
	 */
	public static  void exportDatabase(String databaseName,Context mContext) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//"+mContext.getPackageName()+"//databases//"+databaseName+"";
				String backupDBPath = "DBCopy0103.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
			Log.e("------ dddd -----",e.toString());
		}
	}
}

package p.mbt._class;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import android.os.Environment;
import android.util.Log;

public class DataManager {
	/**
	 * 写入权限
	 * 向SDCard写入数据权限
	 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
	 * SDCard中创建与删除文件权限
	 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
	 */
	public static final int CacheSize = 1000;
	
	public static String FloderName = "HaHa";

	private File file1;
	private File file2;

	public DataManager() {
		//createNewFile();
	}

	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	/**
	 * 获取SD卡根目录路径
	 * 
	 * @return
	 */
	public static String getSdCardPath() {
		boolean exist = isSdCardExist();
		String sdpath = "";
		if (exist) {
			sdpath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		} else {
			sdpath = "不适用";
		}
		return sdpath;

	}

	public void createNewFile() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		String fileName = time + ".txt";
		try {
			File path = new File("/sdcard/" + FloderName);
			this.file1 = new File("/sdcard/" + FloderName + "/" + "1-" + fileName);
			this.file2 = new File("/sdcard/" + FloderName + "/" + "2-" + fileName);
			if (!path.exists()) {// 目录不存在返回false
				path.mkdirs();// 创建一个目录
			}
			if (!file1.exists()) {
				file1.createNewFile();
			}
			if (!file2.exists()) {
				file2.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeData(List<Float> data, String direct) {
		File file = null;
		if (direct.equals("1")) {
			file = this.file1;
		} else if (direct.equals("2")) {
			file = this.file2;
		}
		if (isSdCardExist()) {
			Log.i("TAG", "有SD卡");
			try {
				FileWriter fileWritter = new FileWriter(file, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				for (Float d : data) {
					bufferWritter.write(d + "\n");
				}
				bufferWritter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.i("TAG", "没有SD卡");

		}
	}

	public static List<Float> loadData(String fileName, String direct) {
		List<Float> dataRead = new ArrayList();
		File file = new File("/sdcard/" + FloderName + "/" + direct + "-" + fileName + ".txt");
		try {
			// 判断是否存在SD
			if (isSdCardExist()) {
				// 判断是否存在该文件
				if (file.exists()) {
					Scanner in = new Scanner(file);
					in.useDelimiter("\\s+");
					while (in.hasNext()) {
						String s = in.next();
						float f = Float.valueOf(s).floatValue();
						dataRead.add(f);
					}
					in.close();
				} else {
					// 文件不存在
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataRead;

	}
}
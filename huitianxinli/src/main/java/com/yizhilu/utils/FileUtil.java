package com.yizhilu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/**
 * @author Marven 
 * 240902545@qq.com

 */
public class FileUtil {
	private static final String TAG = FileUtil.class.getSimpleName();
	private static Context context;

	public FileUtil(Context context) {
		this.context = context;
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * @param instream
	 * @param filepath
	 */
	public void saveFile(InputStream instream, String filepath) {
		if (!isExternalStorageWritable()) {
			return;
		}

		File file = new File(filepath);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int cnt = 0;

			while ((cnt = instream.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}

			instream.close();
			fos.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}
	}

	/**
	 * Copy file
	 * 
	 * @param from
	 *            origin file path
	 * @param to
	 *            target file path
	 */
	public void copyFile(String from, String to) {
		if (!isExternalStorageWritable()) {
			return;
		}

		File fileFrom = new File(from);
		File fileTo = new File(to);

		try {

			FileInputStream fis = new FileInputStream(fileFrom);
			FileOutputStream fos = new FileOutputStream(fileTo);
			byte[] buffer = new byte[1024];
			int cnt = 0;

			while ((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}

			fis.close();
			fos.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}
	}

	/**
	 * 
	 * @param json
	 * @param filepath
	 */
	public boolean saveJson(String json, String filepath) {
		if (!isExternalStorageWritable()) {
			return false;
		}

		File file = new File(filepath);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(json.getBytes());
			fos.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param filepath
	 * @return
	 */
	public boolean deleteJson(String filepath) {
		if (!isExternalStorageWritable()) {
			return false;
		}

		File file = new File(filepath);

		if (file.exists()) {
			file.delete();
		}

		return false;
	}

	/**
	 * 
	 * @param filepath
	 * @return
	 */
	public String readJson(String filepath) {
		if (!isExternalStorageWritable()) {
			return null;
		}

		File file = new File(filepath);
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			reader.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param filepath
	 * @param bitmap
	 */
	public void saveBitmap(String filepath, Bitmap bitmap) {
		if (!isExternalStorageWritable()) {
			return;
		}

		if (bitmap == null) {
			return;
		}

		try {
			File file = new File(filepath);
			FileOutputStream outputstream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputstream);
			outputstream.flush();
			outputstream.close();
		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean cleanCache() {
		if (!isExternalStorageWritable()) {
			return false;
		}

		File dir = context.getExternalFilesDir(null);

		if (dir != null) {
			for (File file : dir.listFiles()) {
				file.delete();
			}
		}

		return true;
	}

	/**
	 * 
	 * @return
	 */
	public String getCacheSize() {
		if (!isExternalStorageWritable()) {
			return null;
		}

		long sum = 0;
		File dir = context.getExternalFilesDir(null);

		if (dir != null) {
			for (File file : dir.listFiles()) {
				sum += file.length();
			}
		}

		if (sum < 1024) {
			return sum + "B";
		} else if (sum < 1024 * 1024) {
			return (sum / 1024) + "K";
		} else {
			return (sum / (1024 * 1024)) + "M";
		}
	}

	/**
	 * /storage/sdcard0/Android/data/com.example.test/files
	 */
	public String getAbsolutePath() {
		File root = context.getExternalFilesDir(null);

		if (root != null) {
			return root.getAbsolutePath();
		}

		return null;
	}

	/**
	 */
	public String getCachePath() {
		File root = context.getExternalCacheDir();

		if (root != null) {
			return root.getAbsolutePath();
		}

		return null;
	}

	public boolean isBitmapExists(String filename) {
		File dir = context.getExternalFilesDir(null);
		File file = new File(dir, filename);

		return file.exists();
	}
	public static String getDirPath(String name) {
		String path = "";
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			java.io.File file = new File(Environment
					.getExternalStorageDirectory().getPath()
					+ "/YanDa/");
			if (!file.exists()) {
				if (!file.mkdirs()) {
					return path;
				}
			}
			file = new File(Environment.getExternalStorageDirectory().getPath()
					+ "/YanDa/" + name);
			if (!file.exists()) {
				if (file.mkdirs()) {
					path = file.getAbsolutePath();
				}
			} else {
				path = file.getAbsolutePath();
			}
		} else {
			File dir = context.getDir(name, Context.MODE_PRIVATE
					| Context.MODE_WORLD_READABLE
					| Context.MODE_WORLD_WRITEABLE);
			path = dir.getAbsolutePath();
		}
		return path;
	}
}

package com.xu81.onlyme.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

public class FileHelper {

	public static void saveFile(String path, String fileName, String content) {
		if (path == null)
			throw new IllegalArgumentException();

		if (fileName == null)
			throw new IllegalArgumentException();

		File filePath = new File(path);
		if (!filePath.canWrite())
			throw new IllegalArgumentException();

		File file = new File(path + fileName);
		if (file.exists()) {
			int result = JOptionPane.showConfirmDialog(null, "文件已存在，是否覆盖?",
					"警告", JOptionPane.YES_NO_OPTION);
			if (result == 0) {
				file.delete();
				try {
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					OutputStreamWriter osw = new OutputStreamWriter(fos);
					osw.write(content);
					osw.flush();
					fos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static File saveFile(File f, String content,boolean saveAs) throws IOException {
		if (!(f.getParentFile()).canWrite())
			throw new IllegalArgumentException();

		if (f.exists() && saveAs) {
			int result = JOptionPane.showConfirmDialog(null, "文件已存在，是否覆盖?",
					"警告", JOptionPane.YES_NO_OPTION);
			if (result == 0) {
				f.delete();
			} else {
				return null;
			}
		}
		try {
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write(content);
			osw.flush();
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return f;
	}

	public static String openFile(File f) throws IOException {
		if (!f.exists() || !f.canRead())
			throw new IOException("文件不存在或文件不可读.");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(f)));
		StringBuffer sb = new StringBuffer();
		String data = null;
		while ((data = br.readLine()) != null) {
			sb.append(data);
		}
		return sb.toString();
	}
}

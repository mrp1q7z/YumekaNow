package com.yojiokisoft.yumekanow.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyFile {
	public static List<String> getFileList(String path, String extension) {
		List<String> list = new ArrayList<String>();
		File filePath = new File(path);
		final File[] files = filePath.listFiles(getFileExtensionFilter(extension));
		if (files == null) {
			return list;
		}
		for (File file : files) {
			list.add(file.getName());
		}
		return list;
	}

	public static FilenameFilter getFileExtensionFilter(String extension) {
		final String _extension = extension;
		return new FilenameFilter() {
			public boolean accept(File file, String name) {
				boolean ret = name.endsWith(_extension);
				return ret;
			}
		};
	}

	public static String pathCombine(String dirPath, String fileName) {
		return new File(dirPath, fileName).getPath();
	}

	public static void closeQuietly(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	public static void closeQuietly(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	public static void closeQuietly(BufferedReader br) {
		try {
			if (br != null) {
				br.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}
}

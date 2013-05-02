package com.yojiokisoft.yumekanow.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class MyFile {
	public static List<String> getFileList(String path, String extension) {
		List<String> list = new ArrayList<String>();
		File filePath = new File(path);
		final File[] files = filePath.listFiles(getFileExtensionFilter(extension));
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
}

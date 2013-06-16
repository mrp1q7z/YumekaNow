package com.yojiokisoft.yumekanow.exception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyDialog;
import com.yojiokisoft.yumekanow.utils.MyLog;

public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {
	private static Context sContext;
	private static File sBugReportFile = null;
	private static String sVersionName;
	private static final UncaughtExceptionHandler sDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

	/** 
	 * コンストラクタ 
	 * @param context 
	 */
	public MyUncaughtExceptionHandler(Context context) {
		sContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			MyLog.writeStackTrace(MyConst.getUncaughtBugFilePath(), ex);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sDefaultHandler.uncaughtException(thread, ex);
	}

	private static DialogInterface.OnClickListener mBugDialogOkClicked = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			postBugReportInBackground();
		}
	};

	private static DialogInterface.OnClickListener mBugDialogCancelClicked = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			sBugReportFile.delete();
		}
	};

	/** 
	 * バグレポートの内容をメールで送信します。 
	 * @param activity 
	 */
	public static void sendBugReport(Activity activity) {
		//バグレポートがなければ以降の処理を行いません。  
		sBugReportFile = new File(MyConst.getUncaughtBugFilePath());
		if (sBugReportFile == null || !sBugReportFile.exists()) {
			return;
		}

		setVersionName(activity);

		//AlertDialogを表示します。
		MyDialog.Builder.newInstance(activity)
				.setTitle(activity.getString(R.string.err_dialog_title))
				.setMessage(activity.getString(R.string.err_dialog_msg))
				.setPositiveLabel(activity.getString(R.string.send))
				.setPositiveClickListener(mBugDialogOkClicked)
				.setNegativeLabel(activity.getString(R.string.cancel))
				.setNegativeClickListener(mBugDialogCancelClicked)
				.show();
	}

	/** 
	 * バグレポートの内容をメールで送信します。 
	 * @param activity 
	 */
	public static void sendBugReport(Activity activity, Throwable ex) {
		try {
			MyLog.writeStackTrace(MyConst.getCaughtBugFilePath(), ex);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//バグレポートがなければ以降の処理を行いません。  
		sBugReportFile = new File(MyConst.getCaughtBugFilePath());
		if (sBugReportFile == null || !sBugReportFile.exists()) {
			return;
		}

		setVersionName(activity);

		//AlertDialogを表示します。  
		MyDialog.Builder.newInstance(activity)
				.setTitle(activity.getString(R.string.err_dialog_title))
				.setMessage(activity.getString(R.string.err_dialog_msg2))
				.setPositiveLabel(activity.getString(R.string.send))
				.setPositiveClickListener(mBugDialogOkClicked)
				.setNegativeLabel(activity.getString(R.string.cancel))
				.setNegativeClickListener(mBugDialogCancelClicked)
				.show();
	}

	private static void setVersionName(Activity activity) {
		if (sVersionName != null) {
			return;
		}
		try {
			PackageInfo sPackInfo;
			if (sContext == null) {
				sPackInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			} else {
				sPackInfo = sContext.getPackageManager().getPackageInfo(sContext.getPackageName(), 0);
			}
			sVersionName = sPackInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void postBugReportInBackground() {
		new Thread(new Runnable() {
			public void run() {
				postBugReport();
			}
		}).start();
	}

	private static void postBugReport() {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String bug = getFileBody(sBugReportFile);
		nvps.add(new BasicNameValuePair("dev", Build.DEVICE));
		nvps.add(new BasicNameValuePair("mod", Build.MODEL));
		nvps.add(new BasicNameValuePair("sdk", Build.VERSION.SDK));
		nvps.add(new BasicNameValuePair("ver", sVersionName));
		nvps.add(new BasicNameValuePair("bug", bug));
		try {
			HttpPost httpPost = new HttpPost("http://mrp-bug-report.appspot.com/bug");
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sBugReportFile.delete();
	}

	private static String getFileBody(File file) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}

package com.yojiokisoft.yumekanow.exception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyDialog;
import com.yojiokisoft.yumekanow.utils.MyLog;
import com.yojiokisoft.yumekanow.utils.MyMail;

public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {
	private static Context sContext = null;
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

	/** 
	 * バグレポートの内容をメールで送信します。 
	 * @param activity 
	 */
	public static void sendBugReport(Activity activity) {
		//バグレポートがなければ以降の処理を行いません。  
		final File bugfile = new File(MyConst.getUncaughtBugFilePath());
		if (bugfile == null || !bugfile.exists()) {
			return;
		}
		DialogInterface.OnClickListener okClicked = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					sendMail(bugfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		DialogInterface.OnClickListener cancelClicked = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				bugfile.delete();
			}
		};
		//AlertDialogを表示します。
		MyDialog.Builder.newInstance(activity)
				.setTitle(activity.getString(R.string.err_dialog_title))
				.setMessage(activity.getString(R.string.err_dialog_msg))
				.setPositiveLabel(activity.getString(R.string.send))
				.setPositiveClickListener(okClicked)
				.setNegativeLabel(activity.getString(R.string.cancel))
				.setNegativeClickListener(cancelClicked)
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
		final File bugfile = new File(MyConst.getCaughtBugFilePath());
		if (bugfile == null || !bugfile.exists()) {
			return;
		}

		DialogInterface.OnClickListener okClicked = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					sendMail(bugfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		DialogInterface.OnClickListener cancelClicked = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				bugfile.delete();
			}
		};
		//AlertDialogを表示します。  
		MyDialog.Builder.newInstance(activity)
				.setTitle(activity.getString(R.string.err_dialog_title))
				.setMessage(activity.getString(R.string.err_dialog_msg2))
				.setPositiveLabel(activity.getString(R.string.send))
				.setPositiveClickListener(okClicked)
				.setNegativeLabel(activity.getString(R.string.cancel))
				.setNegativeClickListener(cancelClicked)
				.show();
	}

	/** 
	 * バグレポートの内容をメールで送信します。 
	 * @param activity 
	 * @param bugfile 
	 * @throws IOException 
	 */
	private static void sendMail(File bugfile) throws IOException {
		//バグレポートの内容を読み込みます。  
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(bugfile));
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}

		//メールで送信します。  
		MyMail.Builder.newInstance(sContext)
				.setTo(sContext.getResources().getString(R.string.developer_email))
				.setSubject("[BugReport]" + R.string.app_name)
				.setBody(sb.toString())
				.send();

		//バグレポートを削除します。  
		bugfile.delete();
	}
}

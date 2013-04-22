package com.fanshuo.android.floatwifiadb;

import java.io.DataOutputStream;

import android.util.Log;

/**
 * @author fanshuo
 * @date 2013-4-22 下午6:29:39
 */
public class CommandUtil {
	// 如果需要修改文件权限则 将 以下字符串传入以下方法 777则是 rwx-rwx-rwx权限
	// String cmd= "chmod 777 " + java.io.File.separator + "data"
	// + java.io.File.separator + "system" + java.io.File.separator
	// + "accounts.db";
	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				// nothing
			}
		}
		Log.d("*** DEBUG ***", "Root SUC ");
		return true;
	}

}

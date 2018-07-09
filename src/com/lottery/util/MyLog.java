package com.lottery.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLog {
	private static boolean WRITE_LOG=true;
	// 本类输出的日志文件名称
	private static String LOG_PATH = "log.txt"; // 日志文件的路径

	private static SimpleDateFormat LogSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss"); // 日志的输出格式

	public static void d(String tag, String text) {
		if(WRITE_LOG)
			log(tag, text);
	}

	private static void log(String tag, String msg) {
		writeLogtoFile(tag, msg);
	}

	/**
	 * 打开日志文件并写入日志
	 * 
	 * @return
	 * **/
	private static void writeLogtoFile(String tag, String text) {
		Date nowtime = new Date();
		String needWriteMessage = LogSdf.format(nowtime) + " " + tag + " "
				+ text;
		File file = new File(LOG_PATH);
		try {
			FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
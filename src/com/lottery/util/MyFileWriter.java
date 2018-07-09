package com.lottery.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFileWriter {
	private boolean APPEND=true;

    public void ClearFile() {
        APPEND = false;
    }

    // 本类输出的日志文件名称
	private String filename = "log.txt"; // 日志文件的路径

	private SimpleDateFormat msgSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss"); // 日志的输出格式

    public MyFileWriter(String filename) {
        this.filename = filename;
    }

    public void writeLn(String tag, String msg) {
        writeToFile(tag, msg);
	}

	/**
	 * 打开日志文件并写入日志
	 * 
	 * @return
	 * **/
	private void writeToFile(String tag, String text) {
		Date nowtime = new Date();
		String needWriteMessage = msgSdf.format(nowtime) + " " + tag + " "
				+ text;
		File file = new File(filename);
		try {
			FileWriter filerWriter = new FileWriter(file, APPEND);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
            APPEND=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
package com.lottery.util;

public class Log {
	private static boolean logEnable=true;

	public static void setLogEnable(boolean logEnable) {
		Log.logEnable = logEnable;
	}

	public static void println(String buf){
		if(logEnable) System.out.println(buf);
	}
	public static void print(String buf){
		if(logEnable) System.out.print(buf);
	}
	public static void println(String buf,boolean logEnable){
		if(logEnable) System.out.println(buf);
	}
	public static void print(String buf,boolean logEnable){
		if(logEnable) System.out.print(buf);
	}
}

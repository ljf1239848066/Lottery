package com.lottery.pk10;

import java.util.Arrays;

public class Lottery implements Comparable{
	public final static int COUNT=10;
	private String number;
	private String time;
	private int[] results;
	private int[] types;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int[] getResults() {
		return results;
	}
	public void setResults(int[] results) {
		this.results = results;
		setTypes();
	}
	private void setTypes(){
		int len=results.length;
		types=new int[len*2+len/2];
		for(int i=0;i<len;i++){
			if(results[i]<5){
				types[i]=0;
			}else{
				types[i]=1;
			}
			if(results[i]%2==0){
				types[len+i]=1;
			}else{
				types[len+i]=0;
			}
			if(i<5){
				if(results[i]>results[9-i]){
					types[len*2+i]=1;
				}else{
					types[len*2+i]=0;
				}
			}
		}
	}
	
	public int[] getTypes() {
		return types;
	}
	public Lottery() {
		super();
	}
	public Lottery(String number, String time, int[] results) {
		super();
		this.number = number;
		this.time = time;
		this.results = results;
		setTypes();
	}
	/**
	 * @param str
	 * @param type 
	 * 		0:toString format:Lottery [number=679417期, time=2018-04-30 21:14:06, results=[6, 4, 3, 10, 2, 1, 7, 8, 9, 5]]
	 * 		1:api68 json format:{"preDrawIssue":688400,"preDrawCode":"02,09,01,08,10,06,04,05,03,07","groupCode":1,"preDrawTime":"2018-06-19 23:57:30","sumSingleDouble":0,"fifthDT":0,"firstDT":1,"fourthDT":0,"secondDT":0,"sumFS":11,"sumBigSamll":1,"thirdDT":1}
	 */
	public Lottery(String str,int type){
		if(type==0){
			InitFromToStringStr(str);
		}else if(type==1){
			InitFromJsonStr(str);
		}
	}
	private void InitFromToStringStr(String toStringStr){
		if(toStringStr.contains("Lottery")){
			int eqIdx=toStringStr.indexOf("=")+1;
			int dhIdx=toStringStr.indexOf(",",eqIdx);
			this.number=toStringStr.substring(eqIdx, dhIdx);
			eqIdx=toStringStr.indexOf("=",dhIdx)+1;
			dhIdx=toStringStr.indexOf(",",eqIdx);
			this.time=toStringStr.substring(eqIdx, dhIdx);
			eqIdx=toStringStr.indexOf("=",dhIdx)+2;
			dhIdx=toStringStr.indexOf("]]",eqIdx);
			String[] rstStrs=toStringStr.substring(eqIdx, dhIdx).replace(" ", "").split(",");
			this.results=new int[COUNT];
			for(int i=0;i<COUNT;i++){
				this.results[i]=Integer.parseInt(rstStrs[i]);
			}
			setTypes();
		}
	}
	private void InitFromJsonStr(String jsonStr){
		if(jsonStr.contains("preDrawIssue")){
			int sIdx=jsonStr.indexOf("\"preDrawIssue\":")+15;
			int eIdx=jsonStr.indexOf(",",sIdx);
			this.number=jsonStr.substring(sIdx, eIdx)+"期";
			sIdx=jsonStr.indexOf("\"preDrawCode\":\"",eIdx)+15;
			eIdx=jsonStr.indexOf("\",\"groupCode\"",sIdx);
			String[] rstStrs=jsonStr.substring(sIdx, eIdx).replace(" ", "").split(",");
			this.results=new int[COUNT];
			for(int i=0;i<COUNT;i++){
				this.results[i]=Integer.parseInt(rstStrs[i]);
			}
			setTypes();
			sIdx=jsonStr.indexOf("\"preDrawTime\":\"",eIdx)+15;
			eIdx=jsonStr.indexOf("\",\"sumSingleDouble\"",sIdx);
			this.time=jsonStr.substring(sIdx, eIdx);
		}
	}
	
	@Override
	public int compareTo(Object obj) {
		return this.number.compareTo(((Lottery)obj).number);
	}
	@Override
	public boolean equals(Object obj) {
		return this.number.equals(((Lottery)obj).number);
	}

	@Override
	public int hashCode() {
		return this.number.hashCode();
	}

	@Override
	public String toString() {
		return "Lottery [number=" + number + ", time=" + time + ", results=" + Arrays.toString(results) + "]";
	}
	
}

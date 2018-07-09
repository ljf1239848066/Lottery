package com.lottery.pk10;

public class Range {
	public final static int DX_D=1;//大小-大
	public final static int DX_X=0;//大小-小
	public final static int DS_D=0;//单双-单
	public final static int DS_S=1;//单双-双
	public final static int LH_L=1;//龙虎-龙
	public final static int LH_H=0;//龙虎-虎
	/**
	 * 期号索引
	 */
	private int qidx;
	/**
	 * 1-10
	 */
	private int index;
	/**
	 * award type: length=3
	 * idx=0 大小 1:大 0:小
	 * idx=1 单双 1:双 0:单
	 * idx=2 龙虎 1:龙 0:虎 number index<=5
	 */
	private int type;
	/**
	 * award type: length=3
	 * idx=0 大小 1:大 0:小
	 * idx=1 单双 1:双 0:单
	 * idx=2 龙虎 1:龙 0:虎 number index<=5
	 */
	private int tpva;
	private String start;
	private int count;
	public Range() {
		super();
	}
	
	public int getQidx() {
		return qidx;
	}
	public void setQidx(int qidx) {
		this.qidx = qidx;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTpva() {
		return tpva;
	}
	public void setTpva(int tpva) {
		this.tpva = tpva;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void incCount(){
		this.count++;
	}
	public static int[] getType(int val,int opval){
		int[] type=new int[3];
		if(val<=5){
			type[0]=DX_X;
		}else{
			type[0]=DX_D;
		}
		if(val%2==0){
			type[1]=DS_S;
		}else{
			type[1]=DS_D;
		}
		if(val>opval){
			type[2]=LH_L;
		}else{
			type[2]=LH_H;
		}
		return type;
	}
	@Override
	public String toString() {
		String msg="";
		switch(type){
		case 0:
			if(tpva==0){
				msg="小";
			}else{
				msg="大";
			}
			break;
		case 1:
			if(tpva==0){
				msg="单";
			}else{
				msg="双";
			}
			break;
		case 2:
			if(tpva==0){
				msg="虎";
			}else{
				msg="龙";
			}
			break;
		}
		return "Range [qidx="+qidx+", index=" + index + ", type=" + type+"("+msg + "), start=" + start + ", count=" + count + "]";
	}
	
}

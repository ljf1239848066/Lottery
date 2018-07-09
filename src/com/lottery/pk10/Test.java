package com.lottery.pk10;

import java.util.*;

import com.lottery.util.Log;

public class Test {
	private static List<Lottery> lotAll;
	
	public static void TestInit1(){
		LotteryUtil lotUtil=new LotteryUtil();
		int pages=300;//解析页数 1页30条记录
		lotAll=lotUtil.getLotteryFromCjcp(pages);
		if(lotAll==null){
			return;
		}
	}

	public static void TestInit2(){
		LotteryUtil lotUtil=new LotteryUtil();
		int pages=119;//解析页数,超过180页数据无法抓取 1页179条记录
		lotAll=lotUtil.getLotteryFromApi68(pages);
		if(lotAll==null){
			return;
		}
	}

    /**
     * 随机生成pk10开奖列表
     */
	public static void TestInit3(){
		LotteryUtil lotUtil=new LotteryUtil();
		lotAll=lotUtil.getRandomLotterys(180);
		if(lotAll==null){
			return;
		}
	}

    /**
     * 从文件初始化pk10开奖列表
     */
	public static void TestInit4(){
		String filepath="lottery.txt";
		LotteryUtil lotUtil=new LotteryUtil();
		lotAll=lotUtil.getLotterysFromFile(filepath);
		if(lotAll==null){
			return;
		}
	}

	public static void update(){
        LotteryUtil lotUtil=new LotteryUtil();
        lotAll=lotUtil.updateAllLotterys(lotAll);
        Collections.sort(lotAll,new LotterySortor(true));
    }

	public static void Test1(){
		int cnt=lotAll.size();
		Object[][] ranges=new Object[Lottery.COUNT][3];
		for(int i=0;i<Lottery.COUNT;i++){
			ranges[i][0]=new ArrayList<Range>();
			ranges[i][1]=new ArrayList<Range>();
			ranges[i][2]=new ArrayList<Range>();
		}
		Map<Integer,List<Range>> rangeMap=new HashMap<Integer,List<Range>>();
		for(int i=cnt-1;i>=0;i--){
			Lottery lot=lotAll.get(i);
			int []types=lot.getTypes();
			for(int idx=0;idx<Lottery.COUNT;idx++){
				for(int j=0;j<2||(j==2&&idx<Lottery.COUNT/2);j++){
					int tIdx=j*Lottery.COUNT+idx;
					List<Range> rans=(List<Range>)ranges[idx][j];
					int size=rans.size();
					Range ran;
					if(size==0||types[tIdx]!=rans.get(size-1).getTpva()){
						if(size!=0){
							ran=rans.get(size-1);
							int count=ran.getCount();
							List<Range> tmpList;
							if(rangeMap.containsKey(count)){
								tmpList=rangeMap.get(count);
							}else{
								tmpList=new ArrayList<Range>();
								rangeMap.put(count, tmpList);
							}
							tmpList.add(ran);
						}
						ran=new Range();
						ran.setQidx(i);
						ran.setIndex(idx+1);
						ran.setStart(lot.getNumber());
						ran.setType(j);
						ran.setTpva(types[tIdx]);
						ran.setCount(1);
						rans.add(ran);
					}else{
						ran=rans.get(size-1);
						ran.incCount();
					}
				}
			}
		}
		int max=0;
		for(Integer count:rangeMap.keySet()){
			if(count>max){
				max=count;
			}
			List<Range> rans=rangeMap.get(count);
			Log.println(String.format("count=%2d, size=%6d, rate=%.4f",count,rans.size(),(rans.size()*1.0/cnt)),true);
		}
		for(int k=0;k<3;k++){
			int tmpmax=max-k;
			while(!rangeMap.containsKey(tmpmax)&&tmpmax>0){
				tmpmax--;
			}
			List<Range> rans=rangeMap.get(tmpmax);
			for(int i=0;i<rans.size();i++){
				Range ran=rans.get(i);
				Log.println("第"+ran.getStart()+","+ran.toString(), true);
				for(int j=ran.getQidx(), tmp=ran.getCount();tmp>0;j--,tmp--){
					Log.println(lotAll.get(j).toString(), true);
				}
			}
		}
		int maxchanp=0;
		for(Integer count:rangeMap.keySet()){
			List<Range> rans=rangeMap.get(count);
			for(int i=0;i<rans.size();i++){
				if(rans.get(i).getIndex()==1){
					if(maxchanp<count){
						maxchanp=count;
					}
				}
			}
		}
		for(int k=0;k<3;k++){
			int tmpmax=maxchanp-k;
			while(!rangeMap.containsKey(tmpmax)&&tmpmax>0){
				tmpmax--;
			}
			List<Range> rans=rangeMap.get(tmpmax);
			for(int i=0;i<rans.size();i++){
				Range ran=rans.get(i);
				if(ran.getIndex()==1){
					Log.println("第"+ran.getStart()+",冠军:"+ran.toString(), true);
					for(int j=ran.getQidx(), tmp=ran.getCount();tmp>0;j--,tmp--){
						Log.println(lotAll.get(j).toString(), true);
					}
				}
			}
		}
	}
	
	public static int Test2(int totalM,int startA,float rate,int startL,int totalL){
		totalM*=100;	//总金额 单位/分
		startA*=100;		//起步金额 单位/分
		//float rate=2.1f;	//倍投比例
		//int startL=6;		//起步长度
		//int totalL=8;		//最大轮次
		int cnt=lotAll.size();
		List<Range> rans=new ArrayList<Range>();
		Lottery lot=lotAll.get(0);
		int[] types=lot.getTypes();
		for(int j=0;j<3;j++){
			for(int i=0;i<Lottery.COUNT;i++){
				int idx=j*Lottery.COUNT+i;
				if(idx<25){
					Range ran=new Range();
					ran.setQidx(0);
					ran.setIndex(i);
					ran.setStart(lot.getNumber());
					ran.setType(j);
					ran.setTpva(types[idx]);
					ran.setCount(1);
					rans.add(ran);
				}
			}
		}
		int curT=0;		//当前投注金额
		int curTotal=0;	//当前连续投金额
		int investIdx=-1;//当前投注项目
		int investCnt=0;
		int sCnt=0;		//赢的次数
		boolean conccureent=false;//true:多线 false:单线
		for(int i=1;i<cnt;i++){
			lot=lotAll.get(i);
			types=lot.getTypes();
			if(investIdx>=0){
//				Log.println(lot.toString(),true);
			}
			for(int j=0;j<Lottery.COUNT;j++){
				for(int k=0;k<3;k++){
					int idx=k*Lottery.COUNT+j;
					if(idx<25){
						Range ran=rans.get(idx);
						if(ran.getTpva()==types[idx]){
							ran.incCount();
							if(investIdx<0||investIdx==idx){
								if(ran.getCount()>=startL){
									if(curT==0){
										curT=startA;
										investCnt=1;
										Log.println("\n第"+lot.getNumber()+"开始投资第"+sCnt+"投注,ran="+ran+",lot="+ Arrays.toString(lot.getResults())+
												"idx="+idx+",当前总金额:"+totalM+", 投注金额"+curT,false);
									}else{
										curT=(int)(curT*rate);
										investCnt++;
										Log.println("第"+lot.getNumber()+"继续投资,idx="+investCnt+",当前总金额:"+totalM+", 投注金额"+curT,false);
									}
									if(totalM<=0){//输光了
										Log.println("输光了 idx="+idx+", ran="+ran.toString()+", investCnt="+investCnt+",curT="+curT,false);
										return 0;
									}else if(totalM<curT){
										curT=totalM;
										Log.println("idx="+idx+", ran="+ran.toString()+", investCnt="+investCnt+", curT="+curT+", totalM="+totalM,false);
									}
									if(investCnt>totalL){
										curT=0;
										Log.println("总共投资"+totalL+"轮, 收手重投,"+idx+", ran="+ran.toString()+", investCnt="+investCnt+", curT="+curT+", totalM="+totalM,false);
										investIdx=-1;
									}else{
										totalM-=curT;
										investIdx=idx;
									}
								}
							}
						}else{
							if(investIdx==idx){
								int nowS=(int)(curT*1.99);
								Log.println("第"+lot.getNumber()+"投注结束,投注"+investCnt+"轮,ran="+ran+",idx="+idx+"，investIdx="+
										investIdx+",lot="+ Arrays.toString(lot.getResults())+",收益:"+nowS+", 投注金额"+curT,false);
								totalM+=nowS;//收益增加
								curT=0;//投注金额归零
								investIdx=-1;
								sCnt++;
							}
							ran.setCount(1);
							ran.setTpva(types[idx]);
						}
					}
				}
			}
		}
		Log.println("赢了，总共赢了"+sCnt+"次，当前总金额为:"+(totalM/100), false);
		return totalM/100;
	}

	public static int[] predicate(List<Lottery> lots){
        boolean openPrint=false;
        int[] rst=new int[10];
        Map[] ccMap=new HashMap[Lottery.COUNT];
        for(int j=0;j<Lottery.COUNT;j++){
            ccMap[j]=new HashMap<Integer,Integer>();
            for(int k=0;k<Lottery.COUNT;k++){
                ccMap[j].put(k+1,0);
            }
        }
        int len=lots.size();
        for(int j=0;j<len;j++){
            Lottery lot=lots.get(j);
            int[] vals=lot.getResults();
            for(int k=0;k<Lottery.COUNT;k++){
                int val=vals[k];
                ccMap[k].put(val,(Integer)(ccMap[k].get(val))+1);
            }
            //Log.println(lotAll.get(j).toString(),true);
        }
        for(int j=0;j<Lottery.COUNT;j++){
            ccMap[j]=SorterUtil.sortMapByValue(ccMap[j]);
        }
        Log.println("\n前"+len+"期开奖情况统计:",openPrint);
        int min3=Integer.MAX_VALUE,min5=Integer.MAX_VALUE;
        int min3Idx=-1,min5Idx=-1;
        int[][] lows=new int[Lottery.COUNT][5];
        for(int j=0;j<Lottery.COUNT;j++){
            Log.print("第"+(j+1)+"名:\t",openPrint);
            int idx=0;
            int total3=0,total5=0;
            for (Object key : ccMap[j].keySet()) {
                int val=(Integer)ccMap[j].get(key);
                Log.print(String.format("%2d:%2d\t",key,val),openPrint);

                if(idx<3){
                    total3+=val;
                }
                if(idx<5){
                    total5+=val;
                    lows[j][idx]=(Integer)key;
                }
                idx++;
            }
            Log.println(String.format("概率最低3个号码出现次数和:%3d \t 概率最低5个号码出现次数和:%3d",total3,total5),openPrint);
            if(min3>total3){
                min3=total3;
                min3Idx=j;
            }
            if(min5>total5){
                min5=total5;
                min5Idx=j;
            }
        }
        rst[0]=(min3Idx+1);
        rst[1]=lows[min3Idx][0];
        rst[2]=lows[min3Idx][1];
        rst[3]=lows[min3Idx][2];
        rst[4]=(min5Idx+1);
        rst[5]=lows[min5Idx][0];
        rst[6]=lows[min5Idx][1];
        rst[7]=lows[min5Idx][2];
        rst[8]=lows[min5Idx][3];
        rst[9]=lows[min5Idx][4];

        Log.println(String.format("10名中概率最低3个号码所在序号为:%2d,预测投注号码为:%2d,%2d,%2d",
                (min3Idx+1),lows[min3Idx][0],lows[min3Idx][1],lows[min3Idx][2]),openPrint);
        Log.println(String.format("10名中概率最低5个号码所在序号为:%2d,预测投注号码为:%2d,%2d,%2d,%2d,%2d",
                (min5Idx+1),lows[min5Idx][0],lows[min5Idx][1],lows[min5Idx][2],lows[min5Idx][3],lows[min5Idx][4]),openPrint);
        return rst;
    }

	public static void Test3(){
        boolean printDetail=false;
        int[] tCnts={50,100,200,300};
        int n=tCnts.length;
        Collections.sort(lotAll,new LotterySortor(true));//升序排序
        int cnt=lotAll.size();
        Log.println(String.format("总共%5d期开奖结果，开始预测投注...", cnt), true);
        for(int i=0;i<n;i++){
            int tCnt=tCnts[i];
            int totalMoney3=50000*100;
            int totalMoney5=50000*100;
            int cur3=0;
            int cur5=0;
            int startA=100*100;
            float awardRate=9.9f;
            int win3Cnt=0;
            int win5Cnt=0;
            for(int idx=0;idx<cnt-tCnt-1;idx++){
                int[] prerst=predicate(lotAll.subList(idx,idx+tCnt));
                Lottery lot=lotAll.get(idx+tCnt);
                int open3=lot.getResults()[prerst[0]-1];
                int open5=lot.getResults()[prerst[4]-1];
                int k=0;
                cur3=3*startA;
                cur5=5*startA;
                boolean win3=true;
                boolean win5=true;
                for (k=0; k < 3; k++) {
                    if (open3 == prerst[k + 1]) {
                        break;
                    }
                }
                if(k==3){
                    win3=false;
                }else{
                    win3Cnt++;
                }
                for (k=0; k < 5; k++) {
                    if (open5 == prerst[k + 5]) {
                        break;
                    }
                }
                if(k==5){
                    win5=false;
                }else{
                    win5Cnt++;
                }
                if(totalMoney3>startA) {
                    if(cur3>totalMoney3){
                        cur3=totalMoney3/3*3;
                    }
                    totalMoney3-=cur3;
                    if(win3){
                        totalMoney3 += awardRate * cur3/3;
                        Log.println(String.format("索引:%3d 预测3:%2d号(%2d,%2d,%2d) 开奖:%2d 中   余额:%.2f",
                                idx,prerst[0],prerst[1],prerst[2],prerst[3],open3,totalMoney3/100.0), printDetail);
                    }else{
                        Log.println(String.format("索引:%3d 预测3:%2d号(%2d,%2d,%2d) 开奖:%2d 未中 余额:%.2f",
                                idx,prerst[0],prerst[1],prerst[2],prerst[3],open3,totalMoney3/100.0), printDetail);
                    }
                    if (totalMoney3 <= startA) {
                        Log.println(String.format("前%3d期开奖结果预测3个号码投注失败,失败索引为:%2d",tCnt, idx), true);
                    }
                }
                if(totalMoney5>startA) {
                    if(cur5>totalMoney5){
                        cur5=totalMoney5/5*5;
                    }
                    totalMoney5-=cur5;
                    if(win5){
                        totalMoney5 += awardRate * cur5/5;
                        Log.println(String.format("索引:%3d 预测5:%2d号(%2d,%2d,%2d,%2d,%2d) 开奖%2d 中   余额:%.2f",
                                idx,prerst[4],prerst[5],prerst[6],prerst[7],prerst[8],prerst[9],open5,totalMoney5/100.0), printDetail);
                    }else{
                        Log.println(String.format("索引:%3d 预测5:%2d号(%2d,%2d,%2d,%2d,%2d) 开奖%2d 未中 余额:%.2f",
                                idx,prerst[4],prerst[5],prerst[6],prerst[7],prerst[8],prerst[9],open5,totalMoney5/100.0), printDetail);
                    }
                    if (totalMoney5 <= startA) {
                        Log.println(String.format("前%3d期开奖结果预测5个号码投注失败,失败索引为:%2d", tCnt, idx), true);
                    }
                }
            }
            if(totalMoney3>startA){
                Log.println(String.format("前%3d期开奖结果预测3个号码投注成功,剩余金额为:%5d",tCnt, totalMoney3), true);
            }
            if(totalMoney5>startA){
                Log.println(String.format("前%3d期开奖结果预测5个号码投注成功,剩余金额为:%5d",tCnt, totalMoney5), true);
            }
            Log.println(String.format("前%3d期开奖结果预测3个号码投注%5d次,成功次数为:%5d,成功率为:%.5f",tCnt, cnt-tCnt-1,
                    win3Cnt, win3Cnt*1.0f/(cnt-tCnt-1)), true);
            Log.println(String.format("前%3d期开奖结果预测5个号码投注%5d次,成功次数为:%5d,成功率为:%.5f",tCnt, cnt-tCnt-1,
                    win5Cnt, win5Cnt*1.0f/(cnt-tCnt-1)), true);
        }

    }

	public static void main(String[] args){
		Log.setLogEnable(false);
		for(int n=0;n<1;n++){
			Log.println("第"+n+"轮测试",true);
			TestInit4();
			if(lotAll==null){
				return;
			}
			Collections.sort(lotAll,new LotterySortor(true));
            update();
			Test1();
			int totalMoney=50000;
			int startMoney=10;
			float rate=2.1f;
			int startL=6;
			int totalL=8;
			Log.println("\nTotalMoney="+totalMoney+", startMoney="+startMoney+", rate="+rate+", startL=i, totalL="+totalL,true);
			for(int i=6;i<10;i++){
				startL=i;
				int rst=Test2(totalMoney,startMoney,rate,startL,totalL);
				Log.println("startL="+startL+", WinMoney="+(rst-totalMoney), true);
			}
			totalMoney=50000;startMoney=10;rate=2.1f;startL=6;
			Log.println("\nTotalMoney="+totalMoney+", startMoney="+startMoney+", rate="+rate+", startL="+startL+", totalL=i",true);
			for(int i=5;i<10;i++){
				totalL=i;
				int rst=Test2(totalMoney,startMoney,rate,startL,totalL);
				Log.println("totalL="+totalL+", WinMoney="+(rst-totalMoney), true);
			}
			totalMoney=67000;startMoney=10;rate=2.1f;totalL=8;
			Log.println("\nTotalMoney="+totalMoney+", startMoney="+startMoney+", rate="+rate+", startL=i, totalL="+totalL,true);
			for(int i=6;i<10;i++){
				startL=i;
				int rst=Test2(totalMoney,startMoney,rate,startL,totalL);
				Log.println("startL="+startL+", WinMoney="+(rst-totalMoney), true);
			}
			totalMoney=67000;startMoney=10;rate=2.1f;startL=6;
			Log.println("\nTotalMoney="+totalMoney+", startMoney="+startMoney+", rate="+rate+", startL="+startL+", totalL=i",true);
			for(int i=5;i<10;i++){
				totalL=i;
				int rst=Test2(totalMoney,startMoney,rate,startL,totalL);
				Log.println("totalL="+totalL+", WinMoney="+(rst-totalMoney), true);
			}
		}

        Test3();
	}
}

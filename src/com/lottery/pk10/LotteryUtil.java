package com.lottery.pk10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.*;

import com.lottery.util.*;

public class LotteryUtil {
	private static String patternTr="<tr>(.*?)</tr>";
	private static String patternTd="<td.*?>(.*?)</td>";
	private static String patternImg="(\\d+).png\"";

	private static String patternData="\\[(.*)\\]";
	private static Pattern rTr,rTd,rImg,rData;
	
	public LotteryUtil(){
		Init();
	}
	
	private void Init(){
		rTr=Pattern.compile(patternTr,Pattern.MULTILINE);
	 	rTd=Pattern.compile(patternTd);
	 	rImg=Pattern.compile(patternImg);

		rData=Pattern.compile(patternData);
	}
	
	private List<Lottery> getLotteryFromHtml(String content){
		List<Lottery> lots=new ArrayList<Lottery>();
		Matcher mTr=rTr.matcher(content.replace("\n", "").replace("\r", "").replace(" class='fen'", ""));
		int trCnt=0;
		mTr.find();
		while(mTr.find()){
			trCnt++;
			String tr=mTr.group(1);
			Log.println("TR:\n***********"+tr+"\n**********");
			Matcher mTd=rTd.matcher(tr);
			int tdCnt=0;
			Lottery lot=new Lottery();
			while(mTd.find()){
				tdCnt++;
				String td=mTd.group(1);
				Log.println("TD:"+tdCnt+"="+td);
				if(td.contains("img")){
					Matcher img=rImg.matcher(td);
					int imgCnt=0;
					int []imgs=new int[Lottery.COUNT];
					while(img.find()){
						String picStr=img.group(1).substring(15, 17);
//						int num=findPicIdx(picStr);
						int num=Integer.parseInt(picStr);
						if(num<0){
							Log.println("find pic idx error!!!\n picStr="+picStr, true);
							return null;
						}
						imgs[imgCnt++]=num;
						Log.println("("+picStr+","+num+")");
						lot.setResults(imgs);
					}
					lots.add(lot);
				}else if(mTd.group(0).contains("qihao")){
					lot.setNumber(td);
				}else{
					lot.setTime(td);
				}
			}
		}
		Log.println("TR:find count="+trCnt,true);
		return lots;
	}
	
	public List<Lottery> getLotteryFromCjcp(int pages){
		String urlBase="https://kj.cjcp.com.cn/gaopin/bjpk10/index.php?topage=";
		List<Lottery> lotAll=new ArrayList<Lottery>();
		for(int i=1;i<=pages;i++){
			String url=urlBase+i;
			Log.println("解析第"+i+"页,url="+url+",开奖结果如下：",true);
			MyLog.d("","解析第"+i+"页,url="+url+",开奖结果如下：");
			String content=WebAccessTools.getHttpsWebContent(url);
			int staIdx=content.indexOf("<table class=\"qgkj_table\">");
			int endIdx=content.indexOf("</table>");
			content=content.substring(staIdx, endIdx);
			List<Lottery> lots=getLotteryFromHtml(content);
			if(lots==null){
				return null;
			}
			int cnt=lots.size();
			for(int j=0;j<cnt;j++){
				Log.println(lots.get(j).toString(), true);
				MyLog.d("",lots.get(j).toString());
			}
			lotAll.addAll(lots);
//			Log.println(content);
		}
		return lotAll;
	}

	private List<Lottery> getLotteryFromJson(String content){
		List<Lottery> lots=new ArrayList<Lottery>();
		Matcher mData=rData.matcher(content.replace("\n", "").replace("\r", ""));
		while(mData.find()){
			String data=mData.group(1);
			String[] item=data.split("}");
			int cnt=item.length;
//			MyLog.d("CNT", cnt+"");
			for(int i=0;i<cnt;i++){
				Lottery lot=new Lottery(item[i],1);
				MyLog.d("", lot.toString());
				lots.add(lot);
			}
		}
		return lots;
	}

    public List<Lottery> getLotteryFromApi68(){
        String urlBase="http://api.api68.com/pks/getPksHistoryList.do";
        String param="date=$date&lotCode=10001";
        Date date=new Date();
        String dateStr= Common.getFormatDateString(date);
        String tmpParam=param.replace("$date",dateStr);
        Log.println("日期="+dateStr+",url="+urlBase+"?"+tmpParam+",开奖结果如下：",true);
//			MyLog.d("", "解析第"+i+"页,url="+urlBase+"?"+tmpParam+",开奖结果如下：");
        String content=HttpRequest.sendGet(urlBase, tmpParam);
        List<Lottery> lotAll=getLotteryFromJson(content);
        return lotAll;
    }

	public List<Lottery> getLotteryFromApi68(int days){
		String urlBase="http://api.api68.com/pks/getPksHistoryList.do";
		String param="date=$date&lotCode=10001";
		List<Lottery> lotAll=new ArrayList<Lottery>();
		Date date=new Date();
		for(int i=0;i<=days;i++){
			String dateStr= Common.addDaysToDate(date,-i);
			String tmpParam=param.replace("$date",dateStr);
			Log.println("解析第"+i+"页,url="+urlBase+"?"+tmpParam+",开奖结果如下：",true);
//			MyLog.d("", "解析第"+i+"页,url="+urlBase+"?"+tmpParam+",开奖结果如下：");
			String content=HttpRequest.sendGet(urlBase, tmpParam);
			List<Lottery> lots=getLotteryFromJson(content);
			if(lots==null){
				return null;
			}
			int cnt=lots.size();
			for(int j=0;j<cnt;j++){
//				Log.println(lots.get(j).toString(), true);
			}
			lotAll.addAll(lots);
//			Log.println(content);
		}
		return lotAll;
	}

	public List<Lottery> updateAllLotterys(List<Lottery> lotAll){
        if(lotAll!=null){
            Log.println("updateLotterys1",true);
            Collections.sort(lotAll,new LotterySortor(false));
            Lottery first=lotAll.get(0);
            String dateStr=first.getTime();
            Log.println("updateLotterys:dateStr="+dateStr,true);
            Date date=Common.getDateFromString(dateStr);
            Date dateNow=new Date();
            int days=Common.getDeltaDays(dateNow,date);
            Log.println("updateLotterys:days="+days,true);
            if(days<=0){
                days=1;
            }
            Log.println("updateLotterys:size="+lotAll.size(),true);
            List<Lottery> lots=getLotteryFromApi68(days);
            for (Lottery lot : lots) {
                if (!lotAll.contains(lot)) {
                    lotAll.add(lot);
                }
            }
            Log.println("updateLotterys:size="+lots.size(),true);
        }else{
            lotAll=getLotteryFromApi68(50);
        }
        Collections.sort(lotAll,new LotterySortor(true));
        int size=lotAll.size();
        MyFileWriter mfw=new MyFileWriter("lottery.txt");
        mfw.ClearFile();
        for(int i=0;i<size;i++){
            mfw.writeLn("",lotAll.get(i).toString());
        }
        return lotAll;
    }

    public List<Lottery> updateLotterys(List<Lottery> lotAll){
        List<Lottery> lots=getLotteryFromApi68();
        for (Lottery lot : lots) {
            if (!lotAll.contains(lot)) {
                lotAll.add(lot);
            }
        }
        Collections.sort(lotAll,new LotterySortor(true));
        int size=lotAll.size();
        MyFileWriter mfw=new MyFileWriter("lottery.txt");
        mfw.ClearFile();
        for(int i=0;i<size;i++){
            mfw.writeLn("",lotAll.get(i).toString());
        }
        return lotAll;
    }

	private int[] getRandomResults(int cnt){
		int[] rst=new int[cnt];
		List<Integer> seeds=new ArrayList<Integer>();
		for(int i=1;i<=cnt;i++){
			seeds.add(i);
		}
		Random rand=new Random();
		int tmpcnt=seeds.size();
		for(int i=0;i<cnt;i++){
			int idx=rand.nextInt(tmpcnt);
			rst[i]=seeds.get(idx);
			seeds.remove(idx);
			tmpcnt--;
		}
		return rst;
	}
	
	public List<Lottery> getRandomLotterys(int count){
		List<Lottery> lotAll=new ArrayList<Lottery>();
		int startNum=532819;
		Date date=new Date();
		for(int i=0;i<count;i++){
			Lottery lot=new Lottery();
			lot.setNumber((startNum+i)+"");
			lot.setTime(date.toString());
			lot.setResults(getRandomResults(Lottery.COUNT));
			lotAll.add(lot);
		}
		return lotAll;
	}
	
	public List<Lottery> getLotterysFromFile(String filepath){
		List<Lottery> lotAll=new ArrayList<Lottery>();
		File file = new File(filepath);
		FileReader fileReader=null;
		BufferedReader bufReader=null;
		try {
			fileReader = new FileReader(file);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			bufReader = new BufferedReader(fileReader);
			String line;
			while((line=bufReader.readLine())!=null){
				Lottery lot=new Lottery(line,0);
				lotAll.add(lot);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(bufReader!=null){
					bufReader.close();
				}
				if(fileReader!=null){
					fileReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lotAll;
	}
	
	public void sortLotterys(List<Lottery> lots){
		Collections.sort(lots,new LotterySortor(true));
	}
}

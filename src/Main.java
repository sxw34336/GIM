import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

	public static void main(String[] args) {
		int averagetime=0;
		int avercc=0;
		for(int i=0;i<10;i++){
			dataProcess data=new dataProcess();
			QuerySpace querySpace=new QuerySpace(2700,4900,20900,30800,200);
			List<User> userList=data.dataGen("src/now.txt",querySpace);	
			Anonymizer anonymizer=new Anonymizer();
			LBS lbs=new LBS();
			List<Map<String, Integer>> identifiers=new ArrayList<Map<String,Integer>>();
			anonymizer.setUserIdentifiers(identifiers);
			lbs.querySpace=querySpace;
			long sumtime=0;
			int radius=750;
			int k=40;
			int ccostu2a=0;//暂定通信开销
			int ccostu2l=0;
			int communicationcost=0;
			for(User user:userList){
				Map<String, Object> userMSG=user.generateMSG(radius, k, userList);
				ccostu2a+=k;
				long time1 = System.currentTimeMillis();
				anonymizer.createAnonymityArea((List<Area>) userMSG.get("Region"));
				Map<String, Object> MSGa2l=anonymizer.generateMSGA2L(userMSG);//匿名器生成信息
				ccostu2l++;
				long time2 = System.currentTimeMillis();
				List<User> result=lbs.search(MSGa2l, userList);//LBS查询得到结果
				ccostu2l+=result.size();
				long time3 = System.currentTimeMillis();
				List<User> now=anonymizer.resultFilter(result);
				ccostu2a++;
				//System.out.println(now.size());
				long time4 = System.currentTimeMillis();
				sumtime+=time4-time3+time2-time1;
				communicationcost=ccostu2a+ccostu2l;
			}
			averagetime+=sumtime;
			avercc+=communicationcost;
		}
		//System.out.println("运行时间:"+averagetime/10+" ms");
		System.out.println("通信次数："+avercc/10);
	}

}

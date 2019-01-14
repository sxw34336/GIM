import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		int averagetime=0;
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
			int radius=500;
			int k=60;
			PrintWriter pw=new PrintWriter(new File("src/out.txt"));
			for(User user:userList){
				Map<String, Object> userMSG=user.generateMSG(radius, k, userList);
				pw.write(userMSG.toString());
				long time1 = System.currentTimeMillis();
				anonymizer.createAnonymityArea((List<Area>) userMSG.get("Region"));
				Map<String, Object> MSGa2l=anonymizer.generateMSGA2L(userMSG);//匿名器生成信息
				pw.write(MSGa2l.toString());
				long time2 = System.currentTimeMillis();
				List<User> result=lbs.search(MSGa2l, userList);//LBS查询得到结果
				pw.write(result.toString());
				long time3 = System.currentTimeMillis();
				List<User> now=anonymizer.resultFilter(result);
				pw.write(now.toString());
				//System.out.println(now.size());
				long time4 = System.currentTimeMillis();
				sumtime+=time4-time3+time2-time1;
			}
			averagetime+=sumtime;
		}
		//System.out.println("运行时间:"+averagetime/10+" ms");
	}

}

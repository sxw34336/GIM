import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LBS {
	
	QuerySpace querySpace;
	
	public List<User> search(Map<String, Object> MSGa2l,List<User> poisList){
		Area queryArea = (Area) MSGa2l.get("C-Region");
		int poi_type = (int) MSGa2l.get("POI");
		//System.out.println("C-Region:"+queryArea+"  POI:"+poi_type);
		List<User> resultList=new ArrayList<User>();
		for(User poi:poisList){
			if(isIN(queryArea, poi)&&poi.getPoiClass()==poi_type){
				resultList.add(poi);
			}
		}
		return resultList;
	}
	
	private boolean isIN(Area queryArea,User poi){
		if(poi.getGridx()<=queryArea.getMaxx()&&poi.getGridx()>=queryArea.getMinx()&&poi.getGridy()<=queryArea.getMaxy()&&poi.getGridy()>=queryArea.getMiny()){
			return true;
		}else{
			return false;
	 }
	}

}

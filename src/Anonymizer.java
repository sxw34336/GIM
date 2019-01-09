import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.text.StyledEditorKit.ForegroundAction;


public class Anonymizer {
	private List<Map<String, Integer>> userIdentifiers;
	private Area queryArea;

	/**
	 * ���������ռ�
	 * @param areaList �õ�region����
	 * @return ���ɴ�������ռ�
	 */
	public void createAnonymityArea(List<Area> areaList){
		int maxx=0;
		int maxy=0;
		int minx=Integer.MAX_VALUE;
		int miny=Integer.MAX_VALUE;
		Area kanonymityArea=new Area();
		for(Area area:areaList){
			int amaxx=area.getMaxx();
			int aminx=area.getMinx();
			int amaxy=area.getMaxy();
			int aminy=area.getMiny();
			if(amaxx>maxx){
				maxx=amaxx;
			}
			if(aminx<minx){
				minx=aminx;
			}
			if(amaxy>maxy){
				maxy=amaxy;
			}
			if(aminy<miny){
				miny=aminy;
			}
		}
		kanonymityArea.setMaxx(maxx);
		kanonymityArea.setMinx(minx);
		kanonymityArea.setMaxy(maxy);
		kanonymityArea.setMiny(miny);
		this.queryArea=kanonymityArea;
	}
	
	/**
	 * ���ɲ�ѯMSG
	 * @param MSGu2a �õ������û���MSG
	 * @return ���ɲ�ѯMSG
	 */
	public Map<String, Object> generateMSGA2L(Map<String, Object> MSGu2a){
		Map<String, Object> MSGa2l=new HashMap<String, Object>();
		MSGa2l.put("C-Region", this.queryArea);
		MSGa2l.put("KEY",MSGu2a.get("KEY"));
		MSGa2l.put("grid_structure", MSGu2a.get("grid_structure"));
		MSGa2l.put("POI", MSGu2a.get("POI"));
		return MSGa2l;
	}
	
	public void cacheGridIdentifier(Map<String, Object> MSGu2a){
		this.userIdentifiers=(List<Map<String, Integer>>) MSGu2a.get("S");
	}

	public List<User> resultFilter(List<User> result){
		List<User> filterResult=new ArrayList<>();
		int count=0;
		//System.out.println(userIdentifiers.size());
		for(User poi:result){
			/*Iterator<Map<String, Integer>> iterator=userIdentifiers.iterator();
			while (iterator.hasNext()) {
				Map<String, Integer> grid=iterator.next();
				if(poi.getGridx()==grid.get("X")&&poi.getGridy()==grid.get("Y")){
					filterResult.add(poi);
				}
			}*/
			for(int i=0;i<userIdentifiers.size();i++){
				count++;
			}
		}
		return filterResult;
		
	}

	public List<Map<String, Integer>> getUserIdentifiers() {
		return userIdentifiers;
	}

	public void setUserIdentifiers(List<Map<String, Integer>> userIdentifiers) {
		this.userIdentifiers = userIdentifiers;
	}

	public Area getQueryArea() {
		return queryArea;
	}

	public void setQueryArea(Area queryArea) {
		this.queryArea = queryArea;
	}
	

}

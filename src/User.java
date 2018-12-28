import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User {
	private int userID;//��ǰ�û�id
	private double x;//��ǰ�û���x����
	private double y;//��ǰ�û���y����
	private int poiClass;//��ǰ�û�����Ȥ������
	private int gridx;//��ǰ�û���������Ԫ��x����
	private int gridy;//��ǰ�û���������Ԫ��y����
	private QuerySpace querySpace;//���ò�ѯ�ռ� ��main�����巶Χ�趨��n�����
	
	/**
	 * ����ṹת��
	 * @param x �û���x���꣨��ʵ��
	 * @param y �û���y���꣨��ʵ��
	 */
	public User(double x,double y,QuerySpace querySpace) {
		this.x=x;
		this.y=y;
		this.querySpace=querySpace;
		this.gridx=(int) (x-querySpace.getStartx())/(querySpace.getXlength()/querySpace.getN());
		this.gridy=(int) (y-querySpace.getStarty())/(querySpace.getYlength()/querySpace.getN());
	}
	
	
	
	
	/**
	 * ���㵱ǰ�û���ѯ����
	 * @param r �趨�Ĳ�ѯ�뾶
	 * @return queryArea �õ��û��Ĳ�ѯ����
	 */
	public Area getQueryArea(double x,double y,int r){
		Area queryArea=new Area();
		//��������
		int minx=(int) (x-r-querySpace.getStartx())/(querySpace.getXlength()/querySpace.getN());
		int maxx=(int) (x+r-querySpace.getStartx())/(querySpace.getXlength()/querySpace.getN());
		int miny=(int) (y-r-querySpace.getStarty())/(querySpace.getYlength()/querySpace.getN());
		int maxy=(int) (y+r-querySpace.getStarty())/(querySpace.getYlength()/querySpace.getN());
		queryArea.setMaxx(maxx);
		queryArea.setMaxy(maxy);
		queryArea.setMinx(minx);
		queryArea.setMiny(miny);
		return queryArea;
		
	}
	/**
	 * 
	 * @param r �û���ѯ�뾶
	 * @return ���ؼ��������ʶ����List
	 */
	public List<Map<String, Integer>> generateEncodingSet(int r){
		int n=this.getQuerySpace().getN();
		int count=0;
		List<Map<String, Integer>> identifiers=new ArrayList<>();
		Area queryArea=this.getQueryArea(this.x, this.y, r);
		int maxx=queryArea.getMaxx();
		int minx=queryArea.getMinx();
		int maxy=queryArea.getMaxy();
		int miny=queryArea.getMiny();
		for(int i=miny;i<=maxy;i++){
			for(int j=minx;j<=maxx;j++){
				Map<String, Integer> grid=new HashMap<>();
				grid.put("X", j);
				grid.put("Y", i);
				identifiers.add(grid);
			}
		}
		return identifiers;
	}
	
	/**
	 * ����MSGU2A�����������������Ϲ��̣�
	 * @param r �û��Ĳ�ѯ�뾶
	 * @param k �û���k��������
	 * @param userList ����knn�����������û�
	 */
	public Map<String, Object> generateMSG(Integer r,int k,List<User> userList){
		List<Area> knnAreas=searchKnn(r,k, userList);
		List<Map<String, Integer>> identifiers=generateEncodingSet(r);
		Map<String, Object> MSGu2a=new HashMap<String, Object>();
		MSGu2a.put("ID",userID );
		MSGu2a.put("Region", knnAreas);
		MSGu2a.put("KEY", "K1,K2,K3,K4");
		MSGu2a.put("S", identifiers);
		MSGu2a.put("POI", poiClass);
		MSGu2a.put("grid_structure", querySpace);
		return MSGu2a;
	}
	
	/**
	 * ��������֮�����
	 * @param point ���������û�
	 * @return ���ش��û��뵱ǰ��ѯ�û�֮��ľ���
	 */
	public double getDistance(User user){
		double x=user.getX();
		double y=user.getY();
		double distance=Math.sqrt(Math.pow(this.x-x, 2)+Math.pow(this.y-y, 2));
		return distance;
	}
	
	public void UpToDown(int i,int k,List<User> topkList){
		int t1,t2,pos;
		t1=2*i;
		t2=t1+1;
		if(t1>k)
			return;
		else{
			if(t2>k){
				pos=t1;
			}
			else{
				double d1=getDistance(topkList.get(t1-1));
				double d2=getDistance(topkList.get(t2-1));
				pos=d1>d2?t1:t2;
			}
			double dis1=getDistance(topkList.get(i-1));
			double dis2=getDistance(topkList.get(pos-1));
			if(dis1<dis2){
				Collections.swap(topkList, i-1, pos-1);
				UpToDown(pos, k, topkList);
			}
		}
	}
	
	public void crateHeap(int k,List<User> topkList){
		int i;
		int pos=k/2;
		for(i=pos;i>=1;i--){
			UpToDown(i, k, topkList);
		}
	}
	
	/**
	 * 
	 * @param beforeresult �����������ֹ��˺�Ľ��
	 * @param r ��ѯ�뾶
	 * @return �û������˽��
	 */
	public List<User> refine(List<User> beforeresult,int r){
		List<User> afterresult=new ArrayList<>();
		for(User poi:beforeresult){
			if(getDistance(poi)<=r){
				afterresult.add(poi);
			}
		}
		return afterresult;
	}
	
	public List<Area> searchKnn(int r,int k,List<User> userList){
		List<User> candidate=new ArrayList<User>();
		List<User> kanonymityList=new ArrayList<User>();
		List<Area> kanonymityAreas=new ArrayList<>();
		List<User> topkList=new ArrayList<>();
		int count=0;
		for(int i=0;i<userList.size();i++){
			User user=userList.get(i);
			if(user.getPoiClass()==this.poiClass&&user.getUserID()!=this.userID){
				candidate.add(user);
			}
		}
		//System.out.println("candidate:"+candidate.size());
		if(candidate.size()<k){
			System.out.println("����ʧ��");
			return null;
		}
		for(int j=0;j<k;j++){
			topkList.add(candidate.get(j));
		}
		//System.out.println("topk:"+topkList.size());
		crateHeap(k, topkList);
		for(int z=k;z<candidate.size();z++){
			if(getDistance(candidate.get(z))<getDistance(topkList.get(0))){
				topkList.set(0, candidate.get(z));
				UpToDown(1, k, topkList);
			}
		}
		kanonymityList=topkList;
		for(User user:kanonymityList){
			kanonymityAreas.add(user.getQueryArea(user.getX(), user.getY(), r));
		}
		return kanonymityAreas;
	}	
	
	
	
	
	//getter setter
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getPoiClass() {
		return poiClass;
	}
	public void setPoiClass(int poiClass) {
		this.poiClass = poiClass;
	}
	public int getGridx() {
		return gridx;
	}
	public void setGridx(int gridx) {
		this.gridx = gridx;
	}
	public int getGridy() {
		return gridy;
	}
	public void setGridy(int gridy) {
		this.gridy = gridy;
	}
	public QuerySpace getQuerySpace() {
		return querySpace;
	}
	public void setQuerySpace(QuerySpace querySpace) {
		this.querySpace = querySpace;
	}



}

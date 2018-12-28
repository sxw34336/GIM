import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User {
	private int userID;//当前用户id
	private double x;//当前用户的x坐标
	private double y;//当前用户的y坐标
	private int poiClass;//当前用户的兴趣点类型
	private int gridx;//当前用户所在网格单元的x坐标
	private int gridy;//当前用户所在网格单元的y坐标
	private QuerySpace querySpace;//设置查询空间 （main：具体范围设定，n随机）
	
	/**
	 * 网格结构转换
	 * @param x 用户的x坐标（真实）
	 * @param y 用户的y坐标（真实）
	 */
	public User(double x,double y,QuerySpace querySpace) {
		this.x=x;
		this.y=y;
		this.querySpace=querySpace;
		this.gridx=(int) (x-querySpace.getStartx())/(querySpace.getXlength()/querySpace.getN());
		this.gridy=(int) (y-querySpace.getStarty())/(querySpace.getYlength()/querySpace.getN());
	}
	
	
	
	
	/**
	 * 计算当前用户查询区域
	 * @param r 设定的查询半径
	 * @return queryArea 得到用户的查询区域
	 */
	public Area getQueryArea(double x,double y,int r){
		Area queryArea=new Area();
		//网格坐标
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
	 * @param r 用户查询半径
	 * @return 返回加密网格标识集合List
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
	 * 生成MSGU2A（包含构建匿名集合过程）
	 * @param r 用户的查询半径
	 * @param k 用户的k匿名需求
	 * @param userList 进行knn搜索的其他用户
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
	 * 计算两点之间距离
	 * @param point 输入其他用户
	 * @return 返回此用户与当前查询用户之间的距离
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
	 * @param beforeresult 经过匿名器粗过滤后的结果
	 * @param r 查询半径
	 * @return 用户精过滤结果
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
			System.out.println("匿名失败");
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


public class QuerySpace {
	private int n;//网格单元数（2的n次方）
	private int startx;//起始点x坐标
	private int starty;//起始点y坐标
	private int endx;//终止点x坐标
	private int endy;//终止点y坐标
	private int xlength;//查询区域宽度
	private int ylength;//查询区域长度
	private double ygrid;//查询区域单元格长度
	private double xgrid;//查询区域单元格宽度
	
	
	QuerySpace(int startx,int starty,int endx,int endy,int n){
		this.startx=startx;
		this.starty=starty;
		this.endx=endx;
		this.endy=endy;
		this.xlength=endx-startx;
		this.ylength=endy-starty;
		this.n=n;
		this.setYgrid(ylength/n);
		this.setXgrid(xlength/n);
				
	}
	
		QuerySpace(int startx,int starty,int endx,int endy){
		this.startx=startx;
		this.starty=starty;
		this.endx=endx;
		this.endy=endy;
		this.xlength=endx-startx;
		this.ylength=endy-starty;
	}
	public QuerySpace(QuerySpace querySpace,int n) {
		this.startx=querySpace.getStartx();
		this.starty=querySpace.getStarty();
		this.endx=querySpace.getEndx();
		this.endy=querySpace.getEndy();
		this.n=n;
	}
	


	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public int getStartx() {
		return startx;
	}
	public void setStartx(int startx) {
		this.startx = startx;
	}
	public int getStarty() {
		return starty;
	}
	public void setStarty(int starty) {
		this.starty = starty;
	}
	public int getEndx() {
		return endx;
	}
	public void setEndx(int endx) {
		this.endx = endx;
	}
	public int getEndy() {
		return endy;
	}
	public void setEndy(int endy) {
		this.endy = endy;
	}
	public double getYgrid() {
		return ygrid;
	}
	public void setYgrid(double ygrid) {
		this.ygrid = ygrid;
	}
	public double getXgrid() {
		return xgrid;
	}
	public void setXgrid(double xgrid) {
		this.xgrid = xgrid;
	}


	public int getXlength() {
		return xlength;
	}


	public void setXlength(int xlength) {
		this.xlength = xlength;
	}


	public int getYlength() {
		return ylength;
	}


	public void setYlength(int ylength) {
		this.ylength = ylength;
	}

}

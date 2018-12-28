
public class QuerySpace {
	private int n;//����Ԫ����2��n�η���
	private int startx;//��ʼ��x����
	private int starty;//��ʼ��y����
	private int endx;//��ֹ��x����
	private int endy;//��ֹ��y����
	private int xlength;//��ѯ������
	private int ylength;//��ѯ���򳤶�
	private double ygrid;//��ѯ����Ԫ�񳤶�
	private double xgrid;//��ѯ����Ԫ����
	
	
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

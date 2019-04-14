public class Tuple{
	public int[] coord;
	public double score;

	public Tuple(int x, int y, double score){
		this.coord = new int[2];
		this.coord[0] = x;
		this.coord[1] = y;
		this.score = score;
	}

}
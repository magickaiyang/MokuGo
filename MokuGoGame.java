import java.util.Arrays;

public class MokuGoGame{
	private static final int OPPO = 1;
	private static final int MOKU = 0;
	private static final int boardSize = 15;

	private int[] mokuRowNum;
	private int[] mokuRangeNum;
	private int[] mokuDiagNum;
	private int[] oppoRowNum;
	private int[] oppoRangeNum;
	private int[] oppoDiagNum;

	private int[][] boardVal; //-1 for null, 1 for opponent, 0 for moku
	private final String oppoName;
	private int oppoFinalScore;
	prisvate int gameState; //0 for continue, 1 for opponnent win, 2 for moku win, 3 for tie

	public MokuGoGame(String oppoName){
		this.oppoName = oppoName;
		this.oppoFinalScore = 0;
		this.gameState = 3;
		this.boardVal = new int[boardSize][boardSize];
		Arrays.fill(this.boardVal, -1);

		this.mokuRowNum = new int[boardSize];
		this.mokuRangeNum = new int[boardSize];
		this.mokuDiagNum = new int[2*boardSize-1];
		this.oppoRowNum = new int[boardSize];
		this.oppoRangeNum = new int[boardSize];
		this.oppoDiagNum = new int[2*boardSize-1];
	}

	public String getOppoName(){
		return oppoName;
	}

	public int getFinalSocre(){
		return oppoFinalScore;
	}

	public int getGameState(){
		return gameState;
	}

	public int getBoardVal(int row, int range){
		return boardVal[row][range];
	}

	public viod setBoardVal(int row, int range, int player){
		boardVal[row][range] = player;
		updatePlayerNum(player);
	}

	public void updatePlayerNum(int player){

	}

	public int countConsec(int[] consec){

	}

	public int[] getMokuChoice(){
		int[] coord = getMCTS();
		return coord;
	}
	
}
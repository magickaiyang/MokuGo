import java.util.Arrays;

public class MokuGoGame{
	private static final int OPPO = 1;
	private static final int MOKU = 0;
	private static final int boardSize = 15;

	private int[][] boardVal; //-1 for null, 0 for moku, 1 for opponent
	private final String oppoName;
	private int oppoFinalScore;
	prisvate int gameState; //0 for continue, 1 for opponnent win, 2 for moku win, 3 for tie

	public MokuGoGame(String oppoName){
		this.oppoName = oppoName;
		this.oppoFinalScore = 0;
		this.gameState = 3;
		this.boardVal = new int[boardSize][boardSize];
		Arrays.fill(this.boardVal, -1);
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


	
}
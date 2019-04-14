import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.Math;


public class MokuGoGame {
	private static final int OPPO = 1;
	private static final int MOKU = 0;
	private static final int WINNUM = 5;
	private static final int boardSize = 15;

	private ArrayList<Integer> mokuRowNum;
	private ArrayList<Integer> mokuRangeNum;
	private ArrayList<Integer> mokuDiagNum;
	private ArrayList<Integer> oppoRowNum;
	private ArrayList<Integer> oppoRangeNum;
	private ArrayList<Integer> oppoDiagNum;

	private int[][] boardVal; //-1 for null, 1 for opponent, 0 for moku
	private final String oppoName;
	private int oppoFinalScore;
	private int gameState; //0 for continue, 1 for opponnent win, 2 for moku win, 3 for tie

	public MokuGoGame(String oppoName) {
		this.oppoName = oppoName;
		this.oppoFinalScore = 0;
		this.gameState = 3;
		this.boardVal = new int[boardSize][boardSize];
		Arrays.fill(this.boardVal, -1);

		this.mokuRowNum = new ArrayList<Integer>(boardSize);
		this.mokuRangeNum = new ArrayList<Integer>(boardSize);
		this.mokuDiagNum = new ArrayList<Integer>(2 * boardSize - 1);
		this.oppoRowNum = new ArrayList<Integer>(boardSize);
		this.oppoRangeNum = new ArrayList<Integer>(boardSize);
		this.oppoDiagNum = new ArrayList<Integer>(2 * boardSize - 1);
	}

	public String getOppoName() {
		return oppoName;
	}

	public int getFinalSocre() {
		return oppoFinalScore;
	}

	public int getGameState() {
		return gameState;
	}

	public int getBoardVal(int row, int range) {
		return boardVal[row][range];
	}

	public boolean setBoardVal(int row, int range, int player) {
		if (row <= 0 || row >= boardSize || range <= 0 || range >= boardSize) {
			return false; //out of border
		}
		if (boardVal[row][range] >= 0) {
			return false; //place already taken
		}

		if (player == OPPO) {
			oppoFinalScore++;
		}

		boardVal[row][range] = player;
		updatePlayerNum(row, range, player);
		updateState();
		return true;
	}

	public int[] getMokuChoice() {
		int[] coord = getMCTS();
		return coord;
	}

	public int getState() {
		return gameState;
	}













	private void updatePlayerNum(int row, int range, int player) {
		int diag = boardSize - 1 + row - range;
		int[] tempColumn = new int[boardVal.length];
		for (int i = 0; i < boardVal.length; i++) {
			tempColumn[i] = boardVal[i][range];
		}
		int diagLen = boardSize - Math.abs(row - range);
		int[] tempDiag = new int[diagLen];
		for (int i = 0; i < diagLen; i++) {
			if (diag > boardSize - 1) {
				tempDiag[i] = boardVal[i + row - range][i];
			} else {
				tempDiag[i] = boardVal[i][i - row + range];
			}
		}

		if (player == OPPO) {
			oppoRowNum[row] = countConsec(boardVal[row], player);
			oppoRangeNum[range] = countConsec(tempColumn, player);
			oppoDiagNum[diag] = countConsec(tempDiag, player);
		} else if (player == MOKU) {
			mokuRowNum[row] = countConsec(boardVal[row], player);
			mokuRangeNum[range] = countConsec(tempColumn, player);
			mokuDiagNum[diag] = countConsec(tempDiag, player);
		}
	}

	private void updateState() {
		if (Collections.max(mokuRowNum) >= WINNUM || Collections.max(mokuRangeNum) >= WINNUM || Collections.max(mokuDiagNum) >= WINNUM) {
			gameState = 2;
		}

		if (Collections.max(oppoRowNum) >= WINNUM || Collections.max(oppoRangeNum) >= WINNUM || Collections.max(oppoDiagNum) >= WINNUM) {
			gameState = 1;
		}

		boolean ifMoreEmp = false;
		for (int i = 0; i < boardVal.length; i++) {
			for (int j = 0; j < boardVal[i].length; j++) {
				if (boardVal[i][j] == -1) {
					ifMoreEmp = true;
					break;
				}
			}
		}

		if (ifMoreEmp) {
			gameState = 0;
		} else {
			gameState = 3;
		}
	}

	private int countConsec(int[] consecArray, int player) {
		int longestConsec = 0;
		int tempLength = 0;

		for (int i=0; i<consecArray.length; i++){
			if (consecArray[i]==player){
				tempLength++;
			}else{
				tempLength = 1;
			}
			longestConsec = Math.max(tempLength, longestConsec);
		}

		return longestConsec;
	}


}

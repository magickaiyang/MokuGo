import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.Math;


public class MokuGoGame {
	private static final int OPPO = 1;
	private static final int MOKU = 0;
	private static final int WINNUM = 5;
	private static final int boardSize = 15;

	private PlayerNumCount oppo;
	private PlayerNumCount moku;
	private ArrayList<Tuple> availableMove;
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

		this.availableMove = new ArrayList<Tuple>(boardSize * boardSize);
		for (i = 0; i < boardSize; i++) {
			for (j = 0; i < boardSize; j++) {
				Tuple t = new Tuple(i, j, 0);
				this.availableMove.add(t);
			}
		}

		PlayerNumCount oppo = new PlayerNumCount(oppoName, boardSize);
		PlayerNumCount moku = new PlayerNumCount("moku", boardSize);
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

	public int resetBoardVal(int row, int range, int player) {
		if (player == OPPO) {
			oppoFinalScore--;
		}
		boardVal[row][range] = -1;
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

	public int[] getMokuChoice(int depth) {
		int[] coord = getMove(depth);
		return coord; //[row, range]
	}

	public int getState() {
		return gameState;
	}













	private void updatePlayerNum(int row, int range, int player) {
		int[] tempColumn = new int[boardVal.length];
		for (int i = 0; i < boardVal.length; i++) {
			tempColumn[i] = boardVal[i][range];
		}

		int diagLeft = boardSize - 1 + row - range;
		int diagLeftLen = boardSize - Math.abs(row - range);
		int[] tempLeftDiag = new int[diagLeftLen];
		for (int i = 0; i < diagLeftLen; i++) {
			if (diagLeft > boardSize - 1) {
				tempLeftDiag[i] = boardVal[i + row - range][i];
			} else {
				tempLeftDiag[i] = boardVal[i][i - row + range];
			}
		}

		int diagRight = row + range;
		int diagRightLen;
		if (diagRight < boardSize) {
			diagRightLen = row + range + 1;
		} else {
			diagRightLen = 2 * boardSize - 1 - row - range;
		}
		int[] tempRightDiag = new int[diagRightLen];
		for (int i = 0; i < diagRightLen; i++) {
			if (diagRight > boardSize - 1) {
				tempRightDiag[i] = boardVa[boardSize - 1 - i][boardSize - diagRightLen + i];
			} else {
				tempRightDiag[i] = boardVal[i][diagRight - i];
			}
		}

		if (player == OPPO) {
			oppo.rowNum[row] = countConsec(boardVal[row], player);
			oppo.rangeNum[range] = countConsec(tempColumn, player);
			oppo.diagLeftNum[diag] = countConsec(tempLeftDiag, player);
			oppo.diagRightNum[diag] = countConsec(tempRightDiag, player);
		} else if (player == MOKU) {
			moku.rowNum[row] = countConsec(boardVal[row], player);
			moku.rangeNum[range] = countConsec(tempColumn, player);
			moku.diagLeftNum[diag] = countConsec(tempLeftDiag, player);
			moku.diagRightNum[diag] = countConsec(tempRightDiag, player);
		}
	}

	private void updateState() {
		if (Collections.max(moku.rowNum) >= WINNUM || Collections.max(moku.rangeNum) >= WINNUM || Collections.max(moku.diagLeftNum) >= WINNUM || Collections.max(moku.diagRightNum) >= WINNUM) {
			gameState = 2;
		}

		if (Collections.max(oppo.rowNum) >= WINNUM || Collections.max(oppo.rangeNum) >= WINNUM || Collections.max(oppo.diagLeftNum) >= WINNUM || Collections.max(oppo.diagRightNum) >= WINNUM) {
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

		for (int i = 0; i < consecArray.length; i++) {
			if (consecArray[i] == player) {
				tempLength++;
			} else {
				tempLength = 1;
			}
			longestConsec = Math.max(tempLength, longestConsec);
		}

		return longestConsec;
	}








	// minimax algorithm and alpha-beta pruning functions below are adopted from https://pats.cs.cf.ac.uk/@archive_file?p=525&n=final&f=1-1224795-final-report.pdf&SIG=a852f388b81ea43c0953ec0fb298084d16361371285eb9b836422360627fbd64
	private int[] getMove(int depth) {
		int[] bestCoord;
		double curScore;

		return bestCoord;
	}

	private double minimizeMove(int depth, double alpha, double beta, Tuple move) {
		double result;

		return result;
	}

	private double maximizeMpve(int depth, double alpha, double beta, Tuple move) {
		double result;

		return result;
	}


}

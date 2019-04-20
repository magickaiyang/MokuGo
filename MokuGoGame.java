import java.awt.image.PackedColorModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Scanner;


public class MokuGoGame {
	private static final int OPPO = 1;
	private static final int MOKU = 0;
	private static final int WINNUM = 5;
	private static final int boardSize = 15;

	private PlayerNumCount oppo;
	private PlayerNumCount moku;
	// private ArrayList<Tuple> availableMove;
	private int[][] boardVal; //-1 for null, 1 for opponent, 0 for moku
	private final String oppoName;
	private int oppoFinalCount;
	private int gameState; //0 for continue, 1 for opponnent win, 2 for moku win, 3 for tie

	public MokuGoGame(String oppoName) {
		this.oppoName = oppoName;
		this.oppoFinalCount = 0;
		this.gameState = 3;
		this.boardVal = new int[boardSize][boardSize];

		 for (int i = 0; i < boardSize; i++) {
		 	for (int j = 0; j < boardSize; j++) {
		 		this.boardVal[i][j] = -1;
		 	}
		 }

		// this.availableMove = new ArrayList<Tuple>(boardSize * boardSize);
		// for (i = 0; i < boardSize; i++) {
		// 	for (j = 0; i < boardSize; j++) {
		// 		Tuple t = new Tuple(i, j, 0);
		// 		this.availableMove.add(t);
		// 	}
		// }

		this.oppo = new PlayerNumCount(oppoName, boardSize);
		this.moku = new PlayerNumCount("moku", boardSize);
	}

	private static class Packet { //for test only
		public int status;
		public int color;
		public Position pos;

		public static class Position {
			public int x;
			public int y;
		}
	}

	public String getOppoName() {
		return oppoName;
	}

	public int getOppoFinalCount() {
		return oppoFinalCount;
	}

	public int getGameState() {
		return gameState;
	}

	public int getBoardVal(int row, int range) {
		if (row <= 0 || row >= boardSize || range <= 0 || range >= boardSize) {
			return -1; //out of border
		}
		return boardVal[row][range];
	}

	public boolean resetBoardVal(int row, int range, int player) {
		if (row <= 0 || row >= boardSize || range <= 0 || range >= boardSize) {
			return false; //out of border
		}
		if (boardVal[row][range] == -1) {
			return false; //place already taken
		}

		if (player == OPPO) {
			oppoFinalCount--;
		}

		boardVal[row][range] = -1;
		// Tuple move = new Tuple(row, range, 0);
		// availableMove.add(move);
		return true;
	}

	public boolean setBoardVal(int row, int range, int player) {
		if (row <= 0 || row >= boardSize || range <= 0 || range >= boardSize) {
			return false; //out of border
		}
		if (boardVal[row][range] >= 0) {
			return false; //place already taken
		}

		if (player == OPPO) {
			oppoFinalCount++;
		}

		boardVal[row][range] = player;
//		availabeMove.removeIf(r -> (r.coord[0] == row && r.coord[1] == range));
		updatePlayerNum(row, range, player);
		updateState();
		return true;
	}

	public int[] getMokuChoice(int depth) {
		Minimax m = new Minimax(getTempBoard());
		int[] coord = m.calculateNextMove(depth);
		setBoardVal(coord[0],coord[1],0);
		return coord; //[row, range]
	}












	private void updatePlayerNum(int row, int range, int player) {
		int[] tempColumn = new int[boardVal.length];
		for (int i = 0; i < boardVal.length; i++) {
			tempColumn[i] = boardVal[i][range];
		}

		//tr->bl
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

		//tl->br
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
				tempRightDiag[i] = boardVal[boardSize - 1 - i][boardSize - diagRightLen + i];
			} else {
				tempRightDiag[i] = boardVal[i][diagRight - i];
			}
		}

		if (player == OPPO) {
			oppo.rowNum.set(row,countConsec(boardVal[row], player));
			oppo.rangeNum.set(range, countConsec(tempColumn, player));
			oppo.leftDiagNum.set(diagLeft,countConsec(tempLeftDiag, player));
			oppo.rightDiagNum.set(diagRight,countConsec(tempRightDiag, player));
		} else if (player == MOKU) {
			moku.rowNum.set(row,countConsec(boardVal[row], player));
			moku.rangeNum.set(range, countConsec(tempColumn, player));
			moku.leftDiagNum.set(diagLeft,countConsec(tempLeftDiag, player));
			moku.rightDiagNum.set(diagRight,countConsec(tempRightDiag, player));
		}
	}

	private void updateState() {
		if (Collections.max(moku.rowNum) >= WINNUM || Collections.max(moku.rangeNum) >= WINNUM || Collections.max(moku.leftDiagNum) >= WINNUM || Collections.max(moku.rightDiagNum) >= WINNUM) {
			gameState = 2;
			return;
		}

		if (Collections.max(oppo.rowNum) >= WINNUM || Collections.max(oppo.rangeNum) >= WINNUM || Collections.max(oppo.leftDiagNum) >= WINNUM || Collections.max(oppo.rightDiagNum) >= WINNUM) {
			gameState = 1;
			return;
		}

		if (oppoFinalCount<(((boardSize*boardSize)-1)/2)) {
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
				tempLength = 0;
			}
			longestConsec = Math.max(tempLength, longestConsec);
		}

		return longestConsec;
	}

	private Board getTempBoard(){
		int[][] temp = new int[boardSize][boardSize];
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				if (boardVal[i][j]==-1){
					temp[i][j] = 0;
				}else if (boardVal[i][j]==0){
					temp[i][j] = 2;
				}else if (boardVal[i][j]==1){
					temp[i][j] = 1;
				}
			}
		}
		Board tempBoard = new Board(temp);
		return tempBoard;
	}

	public static void main(String[] args) {
		//create board
		String name = "test";
		MokuGoGame m = new MokuGoGame(name);

		//always let moku go first at the center
		m.setBoardVal((boardSize-1)/2,(boardSize-1)/2,0);

		while(m.getGameState()==0){
			//get input packet
			Packet in = new Packet();
			in.pos = new Packet.Position();
			in.color = 1;

			//local play simulation
			Scanner si = new Scanner(System.in);
			//local simulation
			in.pos.x = si.nextInt(); //in.pos.x;
			in.pos.y = si.nextInt(); //in.pos.y;
			System.out.println("...");
			m.setBoardVal(in.pos.x, in.pos.y, 1);

			//Here we'll use a dummy for test purpose
			Packet response=new Packet();
			response.status=m.getGameState();
			response.pos=new Packet.Position();
			int[] choice = m.getMokuChoice(3); //depth=3
			response.pos.x=choice[0];
			response.pos.y=choice[1];
			response.color=0;

			System.out.printf("in: pos:[%d,%d], color:%d\n", in.pos.x, in.pos.y, in.color);
			System.out.printf("out: status:%d, pos:[%d,%d], color:%d\n", response.status, response.pos.x, response.pos.y, response.color);
			System.out.println("");
		}

		System.out.printf("final: status:%d, score:%d, name:%s\n", m.getGameState(), m.getOppoFinalCount(), m.getOppoName());

	}














	// // minimax algorithm and alpha-beta pruning functions below are adopted from https://pats.cs.cf.ac.uk/@archive_file?p=525&n=final&f=1-1224795-final-report.pdf&SIG=a852f388b81ea43c0953ec0fb298084d16361371285eb9b836422360627fbd64
	// private int[] getMove(int depth) {
	// 	double curScore;
	// 	Tuple bestMove;

	// 	for (Tuple move : availableMove) {
	// 		setBoardVal(move.coord[0], move.coord[1], MOKU);

	// 		curScore = mimimizeMove(depth, Integer.NEGATIVE_INFINITY, Integer.POSITIVE_INFINITY, move);

	// 		if (bestMove == null || curScore > bestMove.score) {
	// 			bestMove = move;
	// 			bestMove.score =  curScore;
	// 		}

	// 		resetBoardVal(move.coord[0], move.coord[1], MOKU);
	// 		if (getGameState() == 1) {
	// 			break;
	// 		}
	// 	}

	// 	return bestMove.coord;
	// }

	// private double minimizeMove(int depth, double alpha, double beta, Tuple move) {
	// 	if (depth == 0 || getGameState() != 0) {
	// 		return getTotalScore()
	// 	}

	// 	double result;

	// 	return result;
	// }

	// private double maximizeMove(int depth, double alpha, double beta, Tuple move) {
	// 	double result;

	// 	return result;
	// }


	// //scoring functions below are modified based on https://blog.theofekfoundation.org/artificial-intelligence/2015/12/11/minimax-for-gomoku-connect-five/
	// private double getTotalScore(int player, boolean mokuTurn) {

	// }

	// private double scoreRecipe(int consecLen, int openEnds, boolean myTurn) {

	// }

	// private double scoreRow(int player, boolean makeTurn) {

	// }

	// private double scoreRange(int player, boolean makeTurn) {

	// }

	// private double scoreDiag(int player, boolean makeTurn) {

	// }


}

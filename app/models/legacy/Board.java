package models.legacy;//extracted from https://github.com/canberkakcali/gomoku-ai-minimax
//04/14/2019
import java.util.ArrayList;

public class Board {

	private int[][] boardMatrix; // 0: Empty 1: White 2: Black

	public Board(int[][] curBoard) {
		this.boardMatrix = curBoard;
	}

	// Fake copy constructor (only copies the boardMatrix)
	public Board(Board board) {
		int[][] matrixToCopy = board.getBoardMatrix();
		boardMatrix = new int[matrixToCopy.length][matrixToCopy.length];
		for (int i = 0; i < matrixToCopy.length; i++) {
			for (int j = 0; j < matrixToCopy.length; j++) {
				boardMatrix[i][j] = matrixToCopy[i][j];
			}
		}
	}

	public int getBoardSize() {
		return boardMatrix.length;
	}

	public void addStoneNoGUI(int posX, int posY, boolean black) {
		boardMatrix[posY][posX] = black ? 2 : 1;
	}

	public int[][] getBoardMatrix() {
		return boardMatrix;
	}

	public ArrayList<int[]> generateMoves() {
		ArrayList<int[]> moveList = new ArrayList<int[]>();

		int boardSize = boardMatrix.length;

		// Look for cells that has at least one stone in an adjacent cell.
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {

				if (boardMatrix[i][j] > 0) continue;

				if (i > 0) {
					if (j > 0) {
						if (boardMatrix[i - 1][j - 1] > 0 ||
						        boardMatrix[i][j - 1] > 0) {
							int[] move = {i, j};
							moveList.add(move);
							continue;
						}
					}
					if (j < boardSize - 1) {
						if (boardMatrix[i - 1][j + 1] > 0 ||
						        boardMatrix[i][j + 1] > 0) {
							int[] move = {i, j};
							moveList.add(move);
							continue;
						}
					}
					if (boardMatrix[i - 1][j] > 0) {
						int[] move = {i, j};
						moveList.add(move);
						continue;
					}
				}
				if ( i < boardSize - 1) {
					if (j > 0) {
						if (boardMatrix[i + 1][j - 1] > 0 ||
						        boardMatrix[i][j - 1] > 0) {
							int[] move = {i, j};
							moveList.add(move);
							continue;
						}
					}
					if (j < boardSize - 1) {
						if (boardMatrix[i + 1][j + 1] > 0 ||
						        boardMatrix[i][j + 1] > 0) {
							int[] move = {i, j};
							moveList.add(move);
							continue;
						}
					}
					if (boardMatrix[i + 1][j] > 0) {
						int[] move = {i, j};
						moveList.add(move);
						continue;
					}
				}

			}
		}

		return moveList;

	}

}
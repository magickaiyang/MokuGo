import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

public class PlayerNumCount {
	public String playerName;
	public ArrayList<Integer> rowNum;
	public ArrayList<Integer> rangeNum;
	public ArrayList<Integer> leftDiagNum;
	public ArrayList<Integer> rightDiagNum;

	public PlayerNumCount(String playerName, int boardSize) {
		this.playerName = playerName;
		this.rowNum = new ArrayList<Integer>(Collections.nCopies(boardSize, 0)); //initialize with all zeros
		this.rangeNum = new ArrayList<Integer>(Collections.nCopies(boardSize, 0));
		this.leftDiagNum = new ArrayList<Integer>(Collections.nCopies(2 * boardSize - 1, 0));
		this.rightDiagNum = new ArrayList<Integer>(Collections.nCopies(2 * boardSize - 1, 0));
	}
}
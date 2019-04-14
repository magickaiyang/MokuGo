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
		this.rowNum = new ArrayList<Integer>(boardSize);
		this.ranegNum = new ArrayList<Integer>(boardSize);
		this.leftDiagNum = new ArrayList<Integer>(2 * boardSize - 1);
		this.rightDiagNum = new ArrayList<Integer>(2 * boardSize - 1);
	}
}
package models.gomocup_wrapper;

import models.Move;
import models.Player;

import java.io.*;
import java.util.Scanner;

public class GomocupPlayer implements Player {
    private boolean isFirst;
    private int boardSize;
    private long moveTimeMillis;
    private State state;
    private Process engineProcess;
    private Scanner engineOutput;
    private PrintStream engineInput;

    public GomocupPlayer() throws IOException {
        this.state = new State(this.boardSize);

        this.engineProcess = Runtime.getRuntime().exec("wine /home/magic/Downloads/pbrain-Yixin2018.exe");
        this.engineOutput = new Scanner(this.engineProcess.getInputStream());
        this.engineInput = new PrintStream(this.engineProcess.getOutputStream());
    }

    @Override
    public void setupGame(int index, int boardSize, long moveTimeMillis, long gameTimeMillis) {
        //total game time limit ignored
        this.isFirst = (index == 1);    //the player with black pieces goes first
        this.boardSize = boardSize;
        this.moveTimeMillis = moveTimeMillis;

        this.engineInput.println("INFO timeout_turn " + this.moveTimeMillis);
        this.engineInput.flush();

        this.engineInput.println("START " + this.boardSize);
        this.engineInput.flush();
        while(this.engineOutput.hasNextLine()) {   //the engine may choose not to respond
            System.out.println(this.engineOutput.nextLine());
        }
    }

    @Override
    public Move getMove(Move opponentsMove, long gameTimeRemainingMillis) {
        this.state.makeMove(opponentsMove);

        Move bestMove = new Move(-1, -1);
        if (this.state.terminal() == 0) { //the game should continue

        }

        return bestMove;
    }

    @Override
    public Move beginGame(long gameTimeRemainingMillis) {
        return null;
    }

    @Override
    public int getGameState() {
        int r = this.state.terminal();

        //transform status code to 1 for opponent win, 2 for computer win
        if (r == 1 && this.state.currentIndex == 2) {
            r = 2;
        } else if (r == 2 && this.state.currentIndex == 1) {
            r = 1;
        }

        return r;
    }

    public static void main (String[] args) throws IOException{
        GomocupPlayer p = new GomocupPlayer();
        p.setupGame(2, 15, 30000, 0);
    }
}

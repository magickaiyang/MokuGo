package actors;

import models.Move;
import models.Player;
import models.blackstone_negamax.NegamaxPlayer;

import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.*;

import java.sql.*;

public class WebsocketActor extends AbstractActor {
    private final ActorRef out;
    private Player m;
    private String oppoName;
    private int oppoFinalCount;

    private static class Packet {
        public int status;
        public int color;
        public Position pos;

        public static class Position {
            public int x;
            public int y;

            public Position() {
                this.x = 7;
                this.y = 7;
            }
        }
    }

    public WebsocketActor(ActorRef out) {
        this.out = out;

        System.out.println("Websocket constructor");
    }

    public static Props props(ActorRef out) {
        return Props.create(WebsocketActor.class, out);
    }

    private void updateLeaderboard(boolean isWin) throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        Connection conn = DriverManager.getConnection(dbUrl);
        PreparedStatement pst = conn.prepareStatement("INSERT INTO leaderboard (\"user\",\"score\") VALUES (?, ?) " +
            "ON CONFLICT (\"user\") DO UPDATE SET score = EXCLUDED.score WHERE EXCLUDED.score > leaderboard.score;");

        if (isWin) { //when the user wins, the lower the stone count, the better
            this.oppoFinalCount = 1000 - this.oppoFinalCount;
            System.out.printf("The user won, count = %d\n", this.oppoFinalCount);
        }

        pst.setString(1, this.oppoName);
        pst.setInt(2, this.oppoFinalCount);
        int rowsUpdated = pst.executeUpdate();

        System.out.printf("%d rows updated\n", rowsUpdated);

        pst.close();
        conn.close();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(JsonNode.class, json -> {
                    System.out.println("Raw json: " + json.toString());

                    String name = json.findPath("name").textValue();    //test if it is a initial packet
                    boolean testPacket = json.findPath("color").isInt();

                    if (testPacket) {   //a Packet class
                        Packet p = Json.fromJson(json, Packet.class);
                        System.out.println("Incoming move: " + "x:" + p.pos.x + " position y:" + p.pos.y);

                        Packet response = new Packet();
                        response.pos = new Packet.Position();

                        //place user's move, compute AI's move and check game state
                        Move aiMove = m.getMove(new Move(p.pos.y, p.pos.x), 0);
                        response.status = m.getGameState(); //0 for continue, 1 for opponent win, 2 for AI win, 3 for tie

                        //sends response of AI
                        response.pos.x = aiMove.col;
                        response.pos.y = aiMove.row;
                        response.color = 0; //-1 for null, 1 for opponent, 0 for AI
                        JsonNode responseJson = Json.toJson(response);
                        out.tell(responseJson, self());

                        if (response.status != 0) { //game has ended
                            System.out.printf("game ends, status:%d\n", response.status);
                            updateLeaderboard(response.status == 1);
                            self().tell(PoisonPill.getInstance(), self());  //terminate connection
                        } else {
                            this.oppoFinalCount++;
                            System.out.println("computer's move: x:" + response.pos.x + " y: " + response.pos.y);
                        }
                    } else if (name != null) {    //contains initial name
                        System.out.println("New player name: " + name);
                        this.oppoName = name;

                        //create new game, computer play black, size is 15x15, max time per move is 30s
                        this.m = new NegamaxPlayer();
                        m.setupGame(1, 15, 30000, 0);

                        //initialize first placement of black stone
                        Move initMove = m.beginGame(0);

                        Packet response = new Packet();
                        response.pos = new Packet.Position();
                        response.pos.x = initMove.col;
                        response.pos.y = initMove.row;
                        response.color = 0; //-1 for null, 1 for opponent, 0 for computer
                        response.status = m.getGameState(); //0 for continue, 1 for opponent win, 2 for computer win, 3 for tie

                        JsonNode responseJson = Json.toJson(response);
                        out.tell(responseJson, self());
                    } else {  //dummy message
                        out.tell(Json.toJson("keep-alive"), self());
                    }
                }
            )
            .build();
    }
}

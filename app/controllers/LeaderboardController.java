package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.*;

import java.sql.*;

public class LeaderboardController extends Controller {
    private Connection conn;

    public static class Board {
        public int userscore;
        public int userrank;
        public String username;
        public Entry[] rank;

        public static class Entry {
            public int score;
            public String username;
            public int pos;

            public Entry(String user, int score, int pos) {
                this.username = user;
                this.score = score;
                this.pos = pos;
            }
        }

        public Board() {
            this.userscore = -1;
            this.userrank = -1;
            this.rank = new Entry[10];
        }
    }

    public LeaderboardController() throws SQLException {
        System.out.println("Leaderboard constructor");
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        this.conn = DriverManager.getConnection(dbUrl);
    }

    public Result get(String username) throws SQLException {
        Board board = new Board();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM leaderboard ORDER BY score DESC LIMIT 10");
        int i = 0;
        while (rs.next()) {
            board.rank[i++] = new Board.Entry(rs.getString("user"), rs.getInt("score"), i);
        }
        rs.close();
        st.close();

        if (!username.isEmpty()) {
            board.username = username;

            PreparedStatement pst = conn.prepareStatement("SELECT count(\"user\") FROM leaderboard where score > (SELECT score FROM leaderboard where \"user\"= ?)");
            pst.setString(1, username);
            rs = pst.executeQuery();
            while (rs.next()) {
                board.userrank = rs.getInt("count");
                board.userrank += 1;    //rank is number of users with lower scores plus one
            }
            rs.close();
            pst.close();

            pst = conn.prepareStatement("SELECT score FROM leaderboard where \"user\"= ?");
            pst.setString(1, username);
            rs = pst.executeQuery();
            while (rs.next()) {
                board.userscore = rs.getInt("score");
            }
            rs.close();
            pst.close();
        }

        return ok(views.html.rank.render(board));
    }
}

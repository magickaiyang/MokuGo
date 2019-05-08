package controllers;

import play.mvc.*;

import java.sql.*;

public class IndexController extends Controller {
    private Connection conn;

    public IndexController() throws SQLException{
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        this.conn = DriverManager.getConnection(dbUrl);
    }

    public Result index(String user) throws SQLException{
        PreparedStatement pst = conn.prepareStatement("SELECT score FROM leaderboard where \"user\"= ?");
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();

        int score = 0;
        if (rs.next()) {
            score = rs.getInt("score");
        }
        rs.close();
        pst.close();
        return ok(views.html.index.render(user, score));
    }
}

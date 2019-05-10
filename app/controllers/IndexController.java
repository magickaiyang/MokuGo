package controllers;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.auth.*;
import play.mvc.*;

import java.io.IOException;
import java.sql.*;

public class IndexController extends Controller {
    private Connection conn;

    public IndexController() throws SQLException, IOException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        this.conn = DriverManager.getConnection(dbUrl);

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .setDatabaseUrl("https://mokugo-firebase.firebaseio.com")
            .build();
        try {
            FirebaseApp.initializeApp(options);
        } catch (IllegalStateException e) {
            //firebase already initialized, nothing to do
        }
    }

    public Result index(String token) throws SQLException, FirebaseAuthException {

        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        //invalid token will through end with an exception
        System.out.printf("User %s authenticated\n", decodedToken.getUid());

        String user = decodedToken.getEmail();

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

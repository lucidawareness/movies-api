package main;

import Config.Config;
import com.mysql.cj.jdbc.Driver;
import data.Movie;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoviesJDBCTest {
    private static final ArrayList<Movie> movies = new ArrayList<>();
    private static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        DriverManager.registerDriver(new Driver());
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://"+ Config.getUrl() +":3306/irvin?allowPublicKeyRetrieval=true&useSSL=false",
                Config.getUser(),
                Config.getPassword()
        );

        Statement st = connection.createStatement();

        String sql = "INSERT INTO movie(title, year, director, actors, rating, poster, genre, plot) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        // For the sake of easier demonstration we are using literals here
        stmt.setString(1, "movie1");
        stmt.setString(2, "2022");
        stmt.setString(3, "Irvin");
        stmt.setString(4, "Irvin 2");
        stmt.setString(5,"5");
        stmt.setString(6, "poster url");
        stmt.setString(7, "genre action");
        stmt.setString(8, "twisty plot");


        ResultSet movieRows = st.executeQuery("SELECT * FROM movie");
        while (movieRows.next()) {
            String title = (movieRows.getString("title"));
            int year = (movieRows.getInt("year"));
            String director = (movieRows.getString("director"));
            String actors = (movieRows.getString("actors"));
            int rating = (movieRows.getInt("rating"));
            String poster = (movieRows.getString("poster"));
            String genre = (movieRows.getString("genre"));
            String plot = (movieRows.getString("plot"));
            int id = (movieRows.getInt("id"));
            Movie movie = new Movie(title, rating, poster, year, genre, director, plot, actors, id);
            movies.add(movie);
        }

        stmt.close();
        movieRows.close();
        st.close();
        connection.close();

        System.out.println(movies.toString());
    }
}

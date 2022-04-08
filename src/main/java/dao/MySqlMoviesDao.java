package dao;

import Config.Config;
import com.mysql.cj.jdbc.Driver;
import data.Movie;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlMoviesDao implements MoviesDao {
    private Connection connection = null;

    public MySqlMoviesDao() {
        try {
            DriverManager.registerDriver(new Driver());

            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://"+ Config.getUrl() +":3306/irvin?allowPublicKeyRetrieval=true&useSSL=false",
                    Config.getUser(),
                    Config.getPassword()
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database!", e);
        }
    }

    @Override
    public List<Movie> all() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM movie");

        List<Movie> movies = new ArrayList<>();

        while (rs.next()) {
            movies.add(new Movie(
                    rs.getString("title"),
                    rs.getInt("rating"),
                    rs.getString("poster"),
                    rs.getInt("year"),
                    rs.getString("genre"),
                    rs.getString("director"),
                    rs.getString("plot"),
                    rs.getString("actors"),
                    rs.getInt("id")
            ));
        }

        rs.close();
        statement.close();
        return movies;
    }

    @Override
    public Movie findOne(int id) {
        return null;
    }

    @Override
    public void insert(Movie movie) {

    }

    @Override

    public void insertAll(Movie[] movies) throws SQLException {

        // Build sql template
        StringBuilder sql = new StringBuilder("INSERT INTO movie (" +
                "title, year, director, actors, rating, poster, genre, plot) " +
                "VALUES ");


        // Add an interpolation template for each element in movies list
        sql.append("(?, ?, ?, ?, ?, ?, ?, ?), ".repeat(movies.length));

        // Create a new String and take off the last comma and whitespace
        sql = new StringBuilder(sql.substring(0, sql.length() - 2));

        // Use the sql string to create a prepared statement
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        // Add each movie to the prepared statement using the index of each sql param: '?'
        // This is done by using a counter
        // You could use a for loop to do this as well and use its incrementor
        int counter = 0;
        for (Movie movie : movies) {
            statement.setString((counter * 8) + 1, movie.getTitle());
            statement.setInt((counter * 8) + 2, movie.getYear());
            statement.setString((counter * 8) + 3, movie.getDirector());
            statement.setString((counter * 8) + 4, movie.getActors());
            statement.setInt((counter * 8) + 5, movie.getRating());
            statement.setString((counter * 8) + 6, movie.getPoster());
            statement.setString((counter * 8) + 7, movie.getGenre());
            statement.setString((counter * 8) + 8, movie.getPlot());
            counter++;
        }
        statement.executeUpdate();
        statement.close();
    }


    @Override
    public void update(Movie movie) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE movie SET title = ?, year= ?, director= ?, actors= ?, rating= ?, poster= ?, genre= ?, plot=? WHERE id= ?");

        PreparedStatement statement = connection.prepareStatement(sql.toString());

        statement.setString(1, movie.getTitle());
        statement.setInt(2, movie.getYear());
        statement.setString(3, movie.getDirector());
        statement.setString(4, movie.getActors());
        statement.setInt(5, movie.getRating());
        statement.setString(6, movie.getPoster());
        statement.setString(7, movie.getGenre());
        statement.setString(8, movie.getPlot());
        statement.setInt(9, movie.getId());

        statement.executeUpdate();
        statement.close();

    }

    @Override
    public void delete(int id) throws SQLException {

        String sql =
                "DELETE FROM movie " +
                        "WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, id);

        statement.execute();
        statement.close();
    }

    public void cleanUp() {
        try{
            System.out.println("Closing connection");
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        };
    }
}

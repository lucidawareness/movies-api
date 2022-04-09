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
                    "jdbc:mysql://" + Config.getUrl() + ":3306/irvin?allowPublicKeyRetrieval=true&useSSL=false",
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
    public Movie findOne(int id) throws SQLException {
        Movie movie = null;
        String sql = "SELECT * FROM movie WHERE id= ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        movie = new Movie(
                resultSet.getString("title"),
                resultSet.getInt("rating"),
                resultSet.getString("poster"),
                resultSet.getInt("year"),
                resultSet.getString("genre"),
                resultSet.getString("director"),
                resultSet.getString("plot"),
                resultSet.getString("actors"),
                resultSet.getInt("id"));

        return movie;
    }

    @Override
    public void insert(Movie movie) {
        try {
            StringBuilder sql = new StringBuilder("INSERT INTO movie (" +
                    "title, year, director, actors, rating, poster, genre, plot) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            PreparedStatement ps = connection.prepareStatement(sql.toString());

            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getYear());
            ps.setString(3, movie.getDirector());
            ps.setString(4, movie.getActors());
            ps.setInt(5, movie.getRating());
            ps.setString(6, movie.getPoster());
            ps.setString(7, movie.getGenre());
            ps.setString(8, movie.getPlot());

            ps.executeQuery();


        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        //Get previous movie object
        int movieId = movie.getId();
        Movie previousMovie = findOne(movieId);
        //Prepare the SQL query
        String sql2 = "UPDATE movie SET title = ?, year= ?, director= ?, actors= ?, rating= ?, poster= ?, genre= ?, plot=? WHERE id= ?";

        PreparedStatement statement2 = connection.prepareStatement(sql2);
        //Check if any fields are not null, if not null use previous movie data to plug in.
        if (movie.getTitle() != null) {
            statement2.setString(1, movie.getTitle());
        } else {
            statement2.setString(1, previousMovie.getTitle());
        }
        if (movie.getYear() != null) {
            statement2.setInt(2, movie.getYear());
        } else {
            statement2.setInt(2, previousMovie.getYear());
        }
        if (movie.getDirector() != null) {
            statement2.setString(3, movie.getDirector());
        } else {
            statement2.setString(3, previousMovie.getDirector());
        }
        if (movie.getActors() != null) {
            statement2.setString(4, movie.getActors());
        } else {
            statement2.setString(4, previousMovie.getActors());
        }
        if (movie.getRating() != null) {
            statement2.setInt(5, movie.getRating());
        } else {
            statement2.setInt(5, previousMovie.getRating());
        }
        if (movie.getPoster() != null) {
            statement2.setString(6, movie.getPoster());
        } else {
            statement2.setString(6, previousMovie.getPoster());
        }
        if (movie.getGenre() != null) {
            statement2.setString(7, movie.getGenre());
        } else {
            statement2.setString(7, previousMovie.getGenre());
        }
        if (movie.getPlot() != null) {
            statement2.setString(8, movie.getPlot());
        } else {
            statement2.setString(8, previousMovie.getPlot());
        }

        statement2.setInt(9, movie.getId());

        statement2.executeUpdate();
        statement2.close();
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
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

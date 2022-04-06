
package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryMoviesDao implements MoviesDao {

    private final ArrayList<Movie> movies = new ArrayList<>();
    private int nextId = 1;

    @Override
    public List<Movie> all() throws SQLException {
        return movies;
    }

    @Override
    public void insertAll(Movie[] newMovies) throws SQLException {
        for (Movie movie : newMovies) {
            movie.setId(nextId++);
            movies.add(movie);
        }
    }

    @Override
    public void update(Movie editedMovie) throws SQLException {
        for (Movie movie : movies) {
            if (Objects.equals(movie.getId(), editedMovie.getId())) {
                if (editedMovie.getTitle() != null) {
                    movie.setTitle(editedMovie.getTitle());
                }

                if (editedMovie.getRating() != null) {
                    movie.setRating(editedMovie.getRating());
                }

                if (editedMovie.getPoster() != null) {
                    movie.setPoster(editedMovie.getPoster());
                }

                if (editedMovie.getYear() != null) {
                    movie.setYear(editedMovie.getYear());
                }

                if (editedMovie.getGenre() != null) {
                    movie.setGenre(editedMovie.getGenre());
                }

                if (editedMovie.getDirector() != null) {
                    movie.setDirector(editedMovie.getDirector());
                }

                if (editedMovie.getPlot() != null) {
                    movie.setPlot(editedMovie.getPlot());
                }

                if (editedMovie.getActors() != null) {
                    movie.setActors(editedMovie.getActors());
                }
            }
        }
    }

    @Override
    public void delete(int targetId) throws SQLException {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == targetId) {
                movies.remove(i);
                break;
            }
        }
    }

    @Override
    public Movie findOne(int id) {
        return null;
    }

    @Override
    public void insert(Movie movie) {
    }

    private HashMap<Integer, Movie> getMoviesMap() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("/Users/vegetasrevenge/IdeaProjects/movies-backend/src/main/resources/movies.json"));
            Type type = TypeToken.getParameterized(ArrayList.class, Movie.class).getType();
            return getMoviesMap(new Gson().fromJson(reader, type));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private HashMap<Integer, Movie> getMoviesMap(List<Movie> movies) {
        HashMap<Integer, Movie> movieHashMap = new HashMap<>();
        int counter = 1;
        for (Movie movie : movies) {
            movieHashMap.put(counter, movie);
            movie.setId(counter);
            counter++;
        }
        return movieHashMap;
    }

}


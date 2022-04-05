package main;

import com.google.gson.Gson;
import data.Movie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {

    ArrayList<Movie> movies = new ArrayList<>();
    int currentAvailableId = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        try {
            PrintWriter out = response.getWriter();
            out.println(movies.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        BufferedReader input = request.getReader();

        Movie[] newMovies = new Gson().fromJson(input, Movie[].class);
        for (Movie movie : newMovies) {
            movie.setId(currentAvailableId++);
            movies.add(movie);
        }

        try {
            PrintWriter out = response.getWriter();
            out.println(movies);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String[] uriParts = request.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
        for (Movie movie : movies) {
            if (movie.getId() == targetId) {
                try {
                    Movie editedMovie = new Gson().fromJson(request.getReader(), Movie.class);
                    editedMovie.setId(targetId);

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

                    PrintWriter out = response.getWriter();
                    out.println("Movie edited");
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String[] uriParts = request.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);

        try {
            for (int i = 0; i < movies.size(); i++) {
                if (movies.get(i).getId() == targetId) {
                    movies.remove(i);
                }
            }
            PrintWriter out = response.getWriter();
            out.println("Movie deleted");
            out.println(movies);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

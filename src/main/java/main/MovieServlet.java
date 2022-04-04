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
    int currentAvailableId = 0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        Movie movie1 = new Movie("Interstellar", 5, "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQUw076GR7JpnfExoBLTMpiE9otUzk518SylUdC1roF6Ah63NS9", 2014, "Sci-fi", "Christopher Nolan", "In Earth's future, a global crop blight and second Dust Bowl are slowly rendering the planet uninhabitable. Professor Brand (Michael Caine), a brilliant NASA physicist, is working on plans to save mankind by transporting Earth's population to a new home via a wormhole", "Matthew McConaughe", 1);
        String movieString = new Gson().toJson(movie1);
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
            currentAvailableId = movies.size() + 1;
            movie.setId(currentAvailableId);
            movies.add(movie);
        }

        try {
            PrintWriter out = response.getWriter();
            out.println(movies);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

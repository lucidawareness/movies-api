package main;

import com.google.gson.Gson;
import dao.MySqlMoviesDao;
import data.Movie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {
//    private InMemoryMoviesDao dao = new InMemoryMoviesDao();
    private final MySqlMoviesDao dao = new MySqlMoviesDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String movieString = new Gson().toJson(dao.all().toArray());

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            out.println(movieString);
        }
        catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Movie newMovies = new Gson().fromJson(request.getReader(), Movie.class);

        try {
            dao.insert(newMovies);

            PrintWriter out = response.getWriter();
            response.setContentType("text/plain");
            out.println("Movie(s) added!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] uriParts = request.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
        Movie editedMovie = new Gson().fromJson(request.getReader(), Movie.class);
        editedMovie.setId(targetId);

        try {
            dao.update(editedMovie);

            PrintWriter out = response.getWriter();
            response.setContentType("text/plain");
            out.println("Movie updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String[] uriParts = request.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);

        try {
            dao.delete(targetId);

            PrintWriter out = response.getWriter();
            out.println("Movie deleted");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy(){
        dao.cleanUp();
    }
}

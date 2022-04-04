package main;

import com.google.gson.Gson;
import data.Movie;

import javax.imageio.IIOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            PrintWriter out = response.getWriter();
            Movie movie1 = new Movie("Interstellar", 5, "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQUw076GR7JpnfExoBLTMpiE9otUzk518SylUdC1roF6Ah63NS9", 2014, "Sci-fi", "Christopher Nolan", "In Earth's future, a global crop blight and second Dust Bowl are slowly rendering the planet uninhabitable. Professor Brand (Michael Caine), a brilliant NASA physicist, is working on plans to save mankind by transporting Earth's population to a new home via a wormhole", "Matthew McConaughe", 1);
            String movieString = new Gson().toJson(movie1);
            out.println(movieString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

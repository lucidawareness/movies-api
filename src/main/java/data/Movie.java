package data;

public class Movie {
    String title;
    Integer rating;
    String poster;
    Integer year;
    String genre;
    String director;
    String plot;
    String actors;
    Integer id;

    public Movie() {

    }

    public Movie(String title, Integer rating, String poster, Integer year, String genre, String director, String plot, String actors, Integer id) {
        this.title = title;
        this.rating = rating;
        this.poster = poster;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.plot = plot;
        this.actors = actors;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

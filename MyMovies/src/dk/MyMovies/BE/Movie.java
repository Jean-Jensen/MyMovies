package dk.MyMovies.BE;


public class Movie {

    private int MovID;
    private String name;
    private double IMDBRating;
    private double rating;
    private String filePath;
    private String lastView;



    public Movie(int id, String name, double rating, double IMDB, String filePath, String lastView){
        MovID = id;
        this.name = name;
        this.rating = rating;
        IMDBRating = IMDB;
        this.filePath = filePath;
        this.lastView = lastView;
    }

    public Movie(int id, String name, String filePath){
        MovID = id;
        this.name = name;
        this.filePath = filePath;
    }

    public Movie(int id, String name, double rating, String filePath){
        MovID = id;
        this.name = name;
        this.rating = rating;
        this.filePath = filePath;
    }
    public Movie(int id, String name, String filePath, String lastView){
        MovID = id;
        this.name = name;
        this.filePath = filePath;
        this.lastView = lastView;
    }

    //For right Click menu
    public Movie() {

    }


    public int getId() {
        return MovID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getIMDBRating() {
        return IMDBRating;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLastView() {
        return lastView;
    }

    public void setLastView(String lastView) {
        this.lastView = lastView;
    }
}

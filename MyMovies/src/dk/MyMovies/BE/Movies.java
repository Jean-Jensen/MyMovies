package dk.MyMovies.BE;


public class Movies {

    private int id;
    private String name;
    private double rating;
    private String filePath;
    private String lastView;

    public Movies(int id, String name, double rating, String filePath, String lastView){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.filePath = filePath;
        this.lastView = lastView;
    }

    public Movies(int id, String name, String filePath){
        this.id = id;
        this.name = name;
        this.filePath = filePath;
    }

    public Movies(int id, String name, double rating, String filePath){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.filePath = filePath;
    }
    public Movies(int id, String name, String filePath, String lastView){
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.lastView = lastView;
    }


    public int getId() {
        return id;
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

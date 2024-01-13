package dk.MyMovies.BE;

public class CatMovConnection extends Movie {
    private int catMovID;
    private String categoryName; //Used to carry over category name

    public CatMovConnection(int id, String name, double rating, double IMDB, String filePath, String lastView, int catMovID) {
        super(id, name, rating, IMDB, filePath, lastView);
        this.catMovID = catMovID;
    }

    public int getCatMovID(){
        return catMovID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
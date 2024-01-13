package dk.MyMovies.BE;

public class CatMovConnectionBE extends Movie {
    private int catMovID;
    private String categoryName; //Used to carry over category name

    public CatMovConnectionBE(int id, String name, double rating, double IMDB, String filePath, String lastView, int catMovID) {
        super(id, name, rating, IMDB, filePath, lastView);
        this.catMovID = catMovID;
    }

    //Constructor for right click remove category from movie
    public CatMovConnectionBE(int catMovID, String categoryName) {
        super();
        this.catMovID = catMovID;
        this.categoryName = categoryName;
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
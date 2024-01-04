package dk.MyMovies.BE;

public class CatMovConnection extends Movies {
    private int catMovID;
    public CatMovConnection(int id, String name, double rating, String filePath, String lastView, int catMovID) {
        super(id, name, rating, filePath, lastView);
        this.catMovID = catMovID;
    }

    public int getCatMovID(){
        return catMovID;
    }

}

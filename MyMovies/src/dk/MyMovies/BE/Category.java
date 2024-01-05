package dk.MyMovies.BE;


public class Category {
    private int catId;
    private String catName;
    public Category(int id, String name){
        this.catId = id;
        this.catName = name;
    }
    public Category(String name){
        this.catName = name;
    }
    public int getId() {
        return catId;
    }
    public String getName() {
        return catName;
    }
    public void setName(String name) {
        this.catName = name;
    }
}

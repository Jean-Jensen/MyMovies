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
    public int getCatId() {
        return catId;
    }
    public String getCatName() {
        return catName;
    }
    public void setCatName(String name) {
        this.catName = name;
    }
}

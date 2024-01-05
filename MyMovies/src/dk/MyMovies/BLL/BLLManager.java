package dk.MyMovies.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movies;
import dk.MyMovies.DAL.CategoriesDAO;
import dk.MyMovies.DAL.MoviesDAO;


import java.util.List;

public class BLLManager {

    MoviesDAO DAO = new MoviesDAO();
    public List<Movies> getAllMovies(){
        try {
            return DAO.getAllMovies();
        } catch (SQLServerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void createMovie(String name, Double Rating, String filePath, String LastView){
        if(filePath == null || LastView == null){
            DAO.createMovie(name,filePath);
        } else{
            DAO.createMovie(name,Rating,filePath,LastView);
        }
    }
}

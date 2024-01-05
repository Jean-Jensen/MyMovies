package dk.MyMovies.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.DAL.MovieDAO;
import java.util.List;


public class BLLMovie {
    MovieDAO DAO = new MovieDAO();
    public List<Movie> getAllMovies(){
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

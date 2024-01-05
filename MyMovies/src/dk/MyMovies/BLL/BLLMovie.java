<<<<<<< Updated upstream:MyMovies/src/dk/MyMovies/BLL/BLLMovie.java
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

    public void deleteMovie(int ID){
        DAO.deleteMovie(ID);
    }

    public void editMovie(int ID, String Name, Double Rating, String FilePath, String LastView){
        DAO.editMovie(ID,Name,Rating,FilePath,LastView);
    }

}
=======
package dk.MyMovies.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.DAL.CategoriesDAO;
import dk.MyMovies.DAL.MoviesDAO;


import java.util.List;

public class BLLManager {

    MoviesDAO DAO = new MoviesDAO();
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
>>>>>>> Stashed changes:MyMovies/src/dk/MyMovies/BLL/BLLManager.java

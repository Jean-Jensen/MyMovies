package dk.MyMovies.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.DAL.MovieDAO;
import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.util.List;

public class BLLMovie {

    MovieDAO DAO = new MovieDAO();

    public List<Movie> getAllMovies() throws MyMoviesExceptions {
        return DAO.getAllMovies();
    }
    public List<Movie> getMoviesByIds(List<Integer> ids) throws MyMoviesExceptions{
        return DAO.getMoviesByIds(ids);
    }
    public List<Movie> getUselessMovies() throws MyMoviesExceptions {
        return DAO.getUselessMovies();
    }

    public void deleteAllUselessMovies() throws MyMoviesExceptions {
        DAO.deleteAllUselessMovies();
    }

    public void createMovie(String name, Double Rating, String filePath, String LastView) throws MyMoviesExceptions {
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
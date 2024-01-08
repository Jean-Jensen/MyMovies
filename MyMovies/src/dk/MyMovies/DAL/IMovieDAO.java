package dk.MyMovies.DAL;

import dk.MyMovies.BE.Movie;
import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.util.List;

public interface IMovieDAO {

    List<Movie> getAllMovies() throws MyMoviesExceptions;

    void createMovie(String name, Double rating, String filePath, String LastView) throws MyMoviesExceptions;

    void createMovie(String name, String filePath);

    void deleteMovie(int ID);

    void editMovie(int ID, String Name, Double Rating, String FilePath, String LastView);

}

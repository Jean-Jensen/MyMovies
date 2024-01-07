package dk.MyMovies.DAL;

import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.ResultSet;
import java.util.List;

public interface ICatMovDAO {

    void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions;

    void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions;

    public ResultSet getCategoriesForMovie(int movID) throws MyMoviesExceptions;

    public ResultSet getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions;

}

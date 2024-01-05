package dk.MyMovies.DAL;

import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.ResultSet;

public interface ICatMovDAO {

    void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions;

    void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions;

    public ResultSet getCategoriesForMovie(int movID) throws MyMoviesExceptions;
}

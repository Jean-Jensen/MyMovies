package dk.MyMovies.DAL;

import dk.MyMovies.Exceptions.MyMoviesExceptions;

public interface ICatMovDAO {

    void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions;

    void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions;
}

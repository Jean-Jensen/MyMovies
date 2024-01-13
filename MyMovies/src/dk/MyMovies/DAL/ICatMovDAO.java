package dk.MyMovies.DAL;

import dk.MyMovies.BE.CatMovConnectionBE;
import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.util.List;

public interface ICatMovDAO {

    void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions;

    void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions;

    List<CatMovConnectionBE> getCategoriesForMovie(int movID) throws MyMoviesExceptions;

    List<Integer> getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions;

    List<CatMovConnectionBE> getAllCatMovConnections() throws MyMoviesExceptions;

    List<CatMovConnectionBE> getCatMovConnectionsByIds(List<Integer> movIDs) throws MyMoviesExceptions;
}

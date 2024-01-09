package dk.MyMovies.DAL;

import dk.MyMovies.BE.CatMovConnection;
import dk.MyMovies.BE.Category;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.ResultSet;
import java.util.List;

public interface ICatMovDAO {

    void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions;

    void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions;

    //public List<Integer> getCategoriesForMovie(int movID) throws MyMoviesExceptions;

   //public List<Category> getCategoriesForMovie(int movID) throws MyMoviesExceptions;

    public List<CatMovConnection> getCategoriesForMovie(int movID) throws MyMoviesExceptions;


    public List<Integer> getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions;

    public List<Movie> getMoviesByNameAndCategories(String movName, List<Integer> catIDs) throws MyMoviesExceptions;

}

package dk.MyMovies.BLL;

import dk.MyMovies.BE.CatMovConnection;
import dk.MyMovies.BE.Category;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.DAL.CatMovDAO;
import dk.MyMovies.DAL.ICatMovDAO;
import dk.MyMovies.Exceptions.MyMoviesExceptions;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BLLCatMov {
    ICatMovDAO catMovDAO = new CatMovDAO();

    public void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions {
        try {
            catMovDAO.addMovieToCategory(catID, movID);
        } catch (MyMoviesExceptions e) {
            throw new MyMoviesExceptions("Error adding movie to category: BLL Error - "+ e.getMessage(), e);
        }
    }

    public void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions {
        try {
            catMovDAO.removeMovieFromCategory(catMovID);
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error removing movie from category: BLL Error - "+ e.getMessage(), e);
        }
    }

    public List<CatMovConnection> getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        try {
            return catMovDAO.getCategoriesForMovie(movID);
        } catch (MyMoviesExceptions e) {
            throw new MyMoviesExceptions("Error retrieving categories for movies: BLL Error - "+ e.getMessage(), e);
        }
    }

    public List<Integer> getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions {
        try {
            return catMovDAO.getMoviesForCategories(catIDs);
        } catch (MyMoviesExceptions e) {
            throw new MyMoviesExceptions("Error retrieving Movies for Categories: BLL Error - "+ e.getMessage(), e);
        }
    }


    public List<CatMovConnection> getAllCatMovConnections() throws MyMoviesExceptions {
        try {
            return catMovDAO.getAllCatMovConnections();
        } catch (MyMoviesExceptions e) {
            throw new MyMoviesExceptions("Error retrieving all movie category connections: BLL Error - " + e.getMessage(), e);
        }
    }

    public List<CatMovConnection> getCatMovConnectionsByIds(List<Integer> movIDs) throws MyMoviesExceptions {
        try {
            return catMovDAO.getCatMovConnectionsByIds(movIDs);
        } catch (MyMoviesExceptions e) {
            throw new MyMoviesExceptions("Error retrieving movie category connections for given movie IDs: BLL Error - " + e.getMessage(), e);
        }
    }
}

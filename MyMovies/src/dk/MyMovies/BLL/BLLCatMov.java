package dk.MyMovies.BLL;

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

    public List<Integer> getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        ResultSet rs = catMovDAO.getCategoriesForMovie(movID);
        List<Integer> categories = new ArrayList<>();
        try {
            //rs.next moves the cursor to next row to get int from each row on the selected column
            while (rs.next()) {
                categories.add(rs.getInt("CatID"));
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error retrieving categories for movies: BLL Error - "+ e.getMessage(), e);
        }
        return categories;
    }

    public List<Integer> getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions {
        List<Integer> movies = new ArrayList<>();
        try {
            ResultSet rs = catMovDAO.getMoviesForCategories(catIDs);
            //rs.next moves the cursor to next row to get int from each row on the selected column
            while (rs.next()) {
                movies.add(rs.getInt("MovID"));
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error retrieving Movies for Categories: BLL Error - "+ e.getMessage(), e);
        }
        return movies;
    }

}

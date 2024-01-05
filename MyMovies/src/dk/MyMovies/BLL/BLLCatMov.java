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
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error adding movie to category: BLL Error", e);
        }
    }

    public void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions {
        try {
            catMovDAO.removeMovieFromCategory(catMovID);
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error removing movie from category: BLL Error", e);
        }
    }

    public List<Integer> getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        ResultSet rs = catMovDAO.getCategoriesForMovie(movID);
        List<Integer> categories = new ArrayList<>();
        try {
            while (rs.next()) {
                categories.add(rs.getInt("CatID"));
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error retrieving categories for movies: BLL Error");
        }
        return categories;
    }
}

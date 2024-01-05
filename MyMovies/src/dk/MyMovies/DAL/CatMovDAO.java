package dk.MyMovies.DAL;

import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.Connection;
import java.sql.SQLException;

public class CatMovDAO implements ICatMovDAO{

    ConnectionManager cm = new ConnectionManager();

    public void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions{
        try (Connection con = cm.getConnection()) {


        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error adding movie to category", e);
        }
    }

    public void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions {
        try (Connection con = cm.getConnection()) {


        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error removing movie from category",e);
        }
    }

    //Will need to add once read is made in category section
    //public List<???> getCategoryConnection(int categoryID) throws MyMoviesExceptions{}


}

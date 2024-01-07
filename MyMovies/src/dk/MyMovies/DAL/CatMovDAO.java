package dk.MyMovies.DAL;

import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CatMovDAO implements ICatMovDAO{

    ConnectionManager cm = new ConnectionManager();

    // This method is used to add a movie to a category in the database.
    public void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions{
        try (Connection con = cm.getConnection()) {
            String sql = "INSERT INTO CatMovie(MovID, CatID) VALUES(?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, movID);
            pstmt.setInt(2, catID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error adding movie to category: DAO Error - " + e.getMessage(), e);
        }
    }

    // This method is used to remove a movie from a category in the database.
    public void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions {
        try (Connection con = cm.getConnection()) {
            String sql = "DELETE FROM CatMovie WHERE ID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, catMovID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error removing movie from category: DAO Error - " + e.getMessage(), e);
        }
    }

    // This method is used to get the categories for a specific movie from the database.
    public ResultSet getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        try(Connection con = cm.getConnection()){
            String sql = "SELECT CatID FROM CatMovie WHERE MovID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, movID);
            ResultSet rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error listing movies categories: DAO Error - " + e.getMessage(), e);
        }
    }

    public ResultSet getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions {
        try(Connection con = cm.getConnection()){
            //This next line is a bit confusing so to break it down.. String.join connects the list in the parameters as a string
            //We use Collections to manipulate the list with .nCopies(int n, Object o)
            //.nCopies uses CatIDs.size as the int for list size and uses "?" for the objects in the list before sending it to the SQL query
            String placeholders = String.join(",", Collections.nCopies(catIDs.size(), "?"));
            String sql = "SELECT MovID FROM CatMovie WHERE CatID IN (" + placeholders + ")";
            PreparedStatement pstmt = con.prepareStatement(sql);
            for (int i = 0; i < catIDs.size(); i++) {
                pstmt.setInt(i + 1, catIDs.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            //Now we return every movie that is in the selected categories
            return rs;
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error retrieving movies for categories: DAO Error" + e.getMessage(), e);
        }
    }
}

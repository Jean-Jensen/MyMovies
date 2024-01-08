package dk.MyMovies.DAL;

import dk.MyMovies.BE.Movie;
import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public List<Integer> getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        List<Integer> categories = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            String sql = "SELECT CatID FROM CatMovie WHERE MovID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, movID);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    categories.add(rs.getInt("CatID"));
                }
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error listing movies categories: DAO Error - " + e.getMessage(), e);
        }
        return categories;
    }


    // This method is used to get the movies for specific categories from the database.
    public List<Integer> getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions {
        List<Integer> movieIds = new ArrayList<>();
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
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    movieIds.add(rs.getInt("MovID"));
                }
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error retrieving movies for categories: DAO Error" + e.getMessage(), e);
        }
        //Now we return every movie that is in the selected categories
        return movieIds;
    }

    // This method is used to get the movies by name and categories from the database.
    public List<Movie> getMoviesByNameAndCategories(String movName, List<Integer> catIDs) throws MyMoviesExceptions {
        List<Movie> movies = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            String placeholders = String.join(",", Collections.nCopies(catIDs.size(), "?"));
            String sql = "SELECT Movie.* FROM Movie " +
                    "JOIN CatMovie ON Movie.MovID = CatMovie.MovID " +
                    "WHERE Movie.Name LIKE ? AND CatMovie.CatID IN (" + placeholders + ")";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + movName + "%");
            for (int i = 0; i < catIDs.size(); i++) {
                pstmt.setInt(i + 2, catIDs.get(i));
            }
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    Movie movie = new Movie(
                            rs.getInt("MovID"),
                            rs.getString("Name"),
                            rs.getDouble("Rating"),
                            rs.getString("FilePath"),
                            rs.getString("LastView")
                    );
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error retrieving movies by name and categories: DAO Error" + e.getMessage(), e);
        }
        return movies;
    }
}

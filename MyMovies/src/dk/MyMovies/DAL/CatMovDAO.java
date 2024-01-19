package dk.MyMovies.DAL;

import dk.MyMovies.BE.CatMovConnectionBE;
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

    // This method is used to add a movie to a category in the database. -Used in rightClickMenuAddCategory
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

    // This method is used to remove a movie from a category in the database. -Used in rightClickMenuRemoveCategory
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

    // This method is used to get the categories for a specific movie from the database. Used in rightClickMenuRemoveCategory
    //to create a list for the submenu
    public List<CatMovConnectionBE> getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        List<CatMovConnectionBE> catMovConnectionBES = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT Category.Name, ID FROM Category " +
                    //I join the CatMovie table with Category via FROM Category
                    "JOIN CatMovie ON Category.CatID = CatMovie.CatID " +
                    //I join my newly joined CatMovie/Category table to Movie
                    "JOIN Movie ON Movie.MovID = CatMovie.MovID " +
                    //Filter out everything unless it matches my parameter
                    "WHERE CatMovie.MovID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, movID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CatMovConnectionBE catMovConnectionBE = new CatMovConnectionBE(
                            rs.getInt("ID"),
                            rs.getString("Name")
                    );
                    catMovConnectionBES.add(catMovConnectionBE);
                }
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error listing movies categories: DAO Error - " + e.getMessage(), e);
        }
        return catMovConnectionBES;
    }


    // This method is used to get all movie-category connections from the database. -Used in displayMovies and updateMovieTable
    public List<CatMovConnectionBE> getAllCatMovConnections() throws MyMoviesExceptions {
        List<CatMovConnectionBE> catMovConnectionBES = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            //In this I rename Category.name to CategoryName using 'as' so I can set it later, due to it having the same name on our tables
            String sql = "SELECT Movie.*, Category.Name as CategoryName, CatMovie.ID as CatMovID FROM CatMovie " +
                    "JOIN Movie ON CatMovie.MovID = Movie.MovID " +
                    "JOIN Category ON CatMovie.CatID = Category.CatID";
            PreparedStatement pstmt = con.prepareStatement(sql);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    CatMovConnectionBE catMovConnectionBE = new CatMovConnectionBE(
                            rs.getInt("MovID"),
                            rs.getString("Name"),
                            rs.getDouble("PersonalRating"),
                            rs.getDouble("Rating"),
                            rs.getString("FilePath"),
                            rs.getString("LastView"),
                            rs.getInt("CatMovID")
                    );
                    //call my setter to set the name as a string so I can get it later
                    catMovConnectionBE.setCategoryName(rs.getString("CategoryName"));
                    catMovConnectionBES.add(catMovConnectionBE);
                }
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error retrieving all movie category connections: DAO Error - " + e.getMessage(), e);
        }
        return catMovConnectionBES;
    }


    // This method is used to get the movies for specific categories from the database. -Used in updateMovieTable & searchName
    //to create a list of movies in each category and input it into getCatMovConnectionsByIds below
    public List<Integer> getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions {
        if (catIDs.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> movieIds = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            //This next line is a bit confusing so to break it down.. String.join connects the list in the parameters as a string
            //We use Collections to manipulate the list with .nCopies(int n, Object o)
            //.nCopies uses CatIDs.size as the int for list size and uses "?" for the objects in the list before sending it to the SQL query
            String placeholders = String.join(",", Collections.nCopies(catIDs.size(), "?"));
            String sql = "SELECT MovID FROM CatMovie WHERE CatID IN (" + placeholders + ") GROUP BY MovID HAVING COUNT(DISTINCT CatID) = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            for (int i = 0; i < catIDs.size(); i++) {
                pstmt.setInt(i + 1, catIDs.get(i));
            }

            pstmt.setInt(catIDs.size() + 1, catIDs.size());

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


    // This method is used to get the movie-category connections for specific movie IDs from the database. -Used in updateMovieTable and SearchName
    public List<CatMovConnectionBE> getCatMovConnectionsByIds(List<Integer> movIDs) throws MyMoviesExceptions {
        if (movIDs.isEmpty()) {
            return new ArrayList<>();
        }
        List<CatMovConnectionBE> catMovConnectionBES = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            String placeholders = String.join(",", Collections.nCopies(movIDs.size(), "?"));
            String sql = "SELECT Movie.*, Category.Name as CategoryName, CatMovie.ID as CatMovID FROM CatMovie " +
                    "JOIN Movie ON CatMovie.MovID = Movie.MovID " +
                    "JOIN Category ON CatMovie.CatID = Category.CatID " +
                    "WHERE CatMovie.MovID IN (" + placeholders + ")";
            PreparedStatement pstmt = con.prepareStatement(sql);
            for (int i = 0; i < movIDs.size(); i++) {
                pstmt.setInt(i + 1, movIDs.get(i));
            }
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    CatMovConnectionBE catMovConnectionBE = new CatMovConnectionBE(
                            rs.getInt("MovID"),
                            rs.getString("Name"),
                            rs.getDouble("PersonalRating"),
                            rs.getDouble("Rating"),
                            rs.getString("FilePath"),
                            rs.getString("LastView"),
                            rs.getInt("CatMovID")
                    );
                    catMovConnectionBE.setCategoryName(rs.getString("CategoryName"));
                    catMovConnectionBES.add(catMovConnectionBE);
                }
            }
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error retrieving movie category connections for given movie IDs: DAO Error - " + e.getMessage(), e);
        }
        return catMovConnectionBES;
    }
}

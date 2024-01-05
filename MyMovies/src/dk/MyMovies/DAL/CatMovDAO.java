package dk.MyMovies.DAL;

import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CatMovDAO implements ICatMovDAO{

    ConnectionManager cm = new ConnectionManager();

    public void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions{
        try (Connection con = cm.getConnection()) {
            String sql = "INSERT INTO CatMovie(MovID, CatID) VALUES(?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, movID);
            pstmt.setInt(2, catID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error adding movie to category: DAO Error", e);
        }
    }

    public void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions {
        try (Connection con = cm.getConnection()) {
            String sql = "DELETE FROM CatMovie WHERE ID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, catMovID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error removing movie from category: DAO Error",e);
        }
    }

    public ResultSet getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        try(Connection con = cm.getConnection()){
            String sql = "SELECT CatID FROM CatMovie WHERE MovID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, movID);
            ResultSet rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error removing movie from category: DAO Error",e);
        }
    }
}

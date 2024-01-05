package dk.MyMovies.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoviesDAO implements IMoviesDAO {
    ConnectionManager cm = new ConnectionManager();
    public List<Movie> getAllMovies() throws SQLServerException {
        List<Movie> movies = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            String sql = "SELECT * FROM Movie";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt("MovID");
                String name = rs.getString("Name");
                double rating = rs.getDouble("Rating");
                String filePath = rs.getString("FilePath");
                String date = "";
                if(rs.getDate("LastView") != null){
                    date = rs.getDate("LastView").toString();
                }
                Movie mov = new Movie(id,name,rating,filePath,date);
                movies.add(mov);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return movies;
    }

    public void createMovie(String name, Double rating, String filePath, String LastView){
        try(Connection con = cm.getConnection()){
            String sql = "INSERT INTO Movie(Name, Rating, FilePath, LastView) VALUES(?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setDouble(2,rating);
            pstmt.setString(3,filePath);
            pstmt.setDate(4, Date.valueOf(LastView));

            pstmt.executeUpdate();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMovie(String name, String filePath){
        try(Connection con = cm.getConnection()){
            String sql = "INSERT INTO Movie(Name, FilePath) VALUES(?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setString(2,filePath);

            pstmt.executeUpdate();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMovie(int ID){
        try(Connection con = cm.getConnection()){
            String sql = "DELETE FROM Movie WHERE MovID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(ID));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editMovie(int ID, String Name, Double Rating, String FilePath, String LastView){
        try(Connection con = cm.getConnection()){
            String sql = "UPDATE Movie SET Name=?, Rating=?, FilePath=?, LastView=? WHERE MovID=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Name);
            pstmt.setString(2, String.valueOf(Rating));
            pstmt.setString(3, FilePath);
            pstmt.setString(4, LastView);
            pstmt.setString(5, String.valueOf(ID));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

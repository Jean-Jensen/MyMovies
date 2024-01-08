package dk.MyMovies.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements IMovieDAO {
    ConnectionManager cm = new ConnectionManager();
    public List<Movie> getAllMovies() throws MyMoviesExceptions {
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
            throw new MyMoviesExceptions("Error retrieving all movies",e);
        }
        return movies;
    }

    public List<Movie> getUselessMovies() throws MyMoviesExceptions {
        List<Movie> movies = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(currentTime);
        currentDate.setYear(currentDate.getYear() - 2);
        try(Connection con = cm.getConnection()){
            String sql = "SELECT * FROM Movie WHERE Rating <= 6 AND LastView >= " + currentDate.toString();
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
            throw new MyMoviesExceptions("Error retrieving all movies where rating <= 6 and LastView " + currentDate,e);
        }

        return movies;
    }

    public void deleteAllUselessMovies() throws MyMoviesExceptions {
        List<Movie> movies = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(currentTime);
        currentDate.setYear(currentDate.getYear() - 2);
        try(Connection con = cm.getConnection()){
            String sql = "SELECT * FROM Movie WHERE Rating <= 6 AND LastView >= " + currentDate.toString();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt("MovID");

                //some repeated code from remove method.
                //chose not to call the method since it would mean opening a new connection
                //which might get laggy
                String sql1 = "DELETE FROM CatMovie WHERE MovID = ?";
                PreparedStatement pstmt1 = con.prepareStatement(sql1);
                pstmt1.setString(1, String.valueOf(id));

                pstmt1.executeUpdate();

                String sql2 = "DELETE FROM Movie WHERE MovID = ?";
                PreparedStatement pstmt2 = con.prepareStatement(sql2);
                pstmt2.setString(1, String.valueOf(id));
                pstmt2.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MyMoviesExceptions("Error retrieving all movies where rating <= 6 and LastView " + currentDate,e);
        }
    }

    /*
    public void deleteAllUselessMovies() throws MyMoviesExceptions {
        List<Movie> movies = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(currentTime);
        currentDate.setYear(currentDate.getYear() + 2);
        System.out.println(currentDate);
        try(Connection con = cm.getConnection()){
            String sql = "DELETE FROM Movie WHERE Rating <= 6 AND LastView > " + currentDate.toString();
            Statement stmt = con.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MyMoviesExceptions("Error deleting all movies where rating <= 6 and LastView " + currentDate,e);
        }
    }

     */

    public void createMovie(String name, Double rating, String filePath, String LastView) throws MyMoviesExceptions {
        try(Connection con = cm.getConnection()){
            String sql = "INSERT INTO Movie(Name, Rating, FilePath, LastView) VALUES(?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setDouble(2,rating);
            pstmt.setString(3,filePath);
            pstmt.setDate(4, Date.valueOf(LastView));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyMoviesExceptions("Error creating movie",e);
        }
    }

    public void createMovie(String name, String filePath){
        try(Connection con = cm.getConnection()){
            String sql = "INSERT INTO Movie(Name, FilePath) VALUES(?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setString(2,filePath);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating movie with name: " + name + "\nand filepath " + filePath + "\n" + e.getMessage(), e);
        }
    }

    public void deleteMovie(int ID){
        try(Connection con = cm.getConnection()){
            //removing all connections to this movie in the CatMovie
            //otherwise the database would stop us from removing the movie
            String sql1 = "DELETE FROM CatMovie WHERE MovID = ?";
            PreparedStatement pstmt1 = con.prepareStatement(sql1);
            pstmt1.setString(1, String.valueOf(ID));

            pstmt1.executeUpdate();


            String sql = "DELETE FROM Movie WHERE MovID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(ID));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting movie with ID " + ID + "\n" + e.getMessage(), e);
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
            throw new RuntimeException("Error editing movie\n" + e.getMessage(), e);
        }
    }

    public void searchMovie(String search){
        try(Connection con = cm.getConnection()){
            String sql = "SELECT * FROM Movie WHERE Name LIKE ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, search);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for movie " + search + "\n" + e.getMessage(), e);
        }
    }

    public void searchMovie(String search, double rating){
        try(Connection con = cm.getConnection()){
            String sql = "SELECT * FROM Movie WHERE Name LIKE ? AND Rating = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, search);
            pstmt.setString(2, search);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for movie " + search + "\n" + e.getMessage(), e);
        }
    }

}

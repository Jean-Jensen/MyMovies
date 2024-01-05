
package dk.MyMovies.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO {
    ConnectionManager cm = new ConnectionManager();
    public List<Category> getAllCategories() throws SQLServerException {
        List<Category> category = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            String sql = "SELECT * FROM Category";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt("CatID");
                String name = rs.getString("Name");
                Category cat = new Category(id,name);
                category.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return category;
    }

    public void createCategory(Category c){
        try(Connection con = cm.getConnection()){
            String sql = "INSERT INTO Category(Name) VALUES(?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1,c.getName());
            pstmt.executeUpdate();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

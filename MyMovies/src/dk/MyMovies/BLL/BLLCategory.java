package dk.MyMovies.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Category;
import dk.MyMovies.DAL.CategoryDAO;
import dk.MyMovies.Exceptions.MyMoviesExceptions;

import java.sql.SQLException;
import java.util.List;

public class BLLCategory {
    CategoryDAO DAO = new CategoryDAO();
    public List<Category> getAllCategory() throws MyMoviesExceptions {
        try {
            return DAO.getAllCategories();
        } catch (
                SQLException e) {
            throw new MyMoviesExceptions("Error getting all the categories", e);
        }
    }
    public void createCategory(Category name){
        DAO.createCategory(name);
    }

    public void deleteCategory(int catId){
        DAO.deleteCategory(catId);
    }

    public void editCategory(int catId, String name){
        DAO.editCategory(catId, name);
    }
}


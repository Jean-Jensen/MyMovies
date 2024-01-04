package dk.MyMovies.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Category;
import dk.MyMovies.BE.Movies;
import dk.MyMovies.DAL.CategoriesDAO;
import dk.MyMovies.DAL.MoviesDAO;

import java.util.List;

public class BLLCategories {
    CategoriesDAO DAO = new CategoriesDAO();
    public List<Category> getAllCategory() throws SQLServerException {
        return DAO.getAllCategories();
    }

    public void createCategory(){

    }
}

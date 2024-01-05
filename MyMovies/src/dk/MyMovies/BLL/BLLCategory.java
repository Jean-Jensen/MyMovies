package dk.MyMovies.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Category;
import dk.MyMovies.DAL.CategoryDAO;
import java.util.List;

public class BLLCategory {
    CategoryDAO DAO = new CategoryDAO();
    public List<Category> getAllCategory() throws SQLServerException {
        return DAO.getAllCategories();
    }

    public void createCategory(){

    }
}


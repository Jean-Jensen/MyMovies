<<<<<<< Updated upstream:MyMovies/src/dk/MyMovies/BLL/BLLCategory.java
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
=======
package dk.MyMovies.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Category;
import dk.MyMovies.BE.Movie;
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
>>>>>>> Stashed changes:MyMovies/src/dk/MyMovies/BLL/BLLCategories.java

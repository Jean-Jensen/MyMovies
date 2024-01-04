package dk.MyMovies.Exceptions;

import java.sql.SQLException;

public class MyMoviesExceptions extends SQLException {
    public MyMoviesExceptions(String message){
        super();
    }
    public MyMoviesExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public MyMoviesExceptions(Throwable cause) {
        super(cause);
    }
}

package sample.AppException;

public class UserIdNotFindException extends Throwable{
    public UserIdNotFindException() {
        super("User ID NOT FIND!");
    }
}

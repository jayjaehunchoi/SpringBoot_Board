package board.bootboard.util.exception;

public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String message) {
        super(message);
    }
}

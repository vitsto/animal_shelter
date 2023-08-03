package pro.sky.exception;

public class VolunteerNotFoundException extends RuntimeException{

    public VolunteerNotFoundException() {
    }

    public VolunteerNotFoundException(String message) {
        super(message);
    }
}

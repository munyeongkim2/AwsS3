package crowdworks.image.common.Response;

public record ErrorResponse(
        String message
) {
    public static ErrorResponse withMessage(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    public static ErrorResponse withMessage(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }
}

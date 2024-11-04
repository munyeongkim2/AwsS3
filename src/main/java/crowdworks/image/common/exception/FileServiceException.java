package crowdworks.image.common.exception;

import crowdworks.image.common.Response.ErrorCode;
import lombok.Getter;

@Getter
public class FileServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public FileServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

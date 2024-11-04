package crowdworks.image.common.Response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    FILENAME_EXTENSION_ERROR("파일 확장자를 지원하지 않습니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    FILE_SIZE_ERROR("파일 크기가 5mb를 초과합니다.", HttpStatus.PAYLOAD_TOO_LARGE),

    FILE_EMPTY_ERROR("파일을 첨부해 주세요.", HttpStatus.BAD_REQUEST),

    FILE_NOT_FOUND_ERROR("삭제할 파일이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    FILE_NAME_DUPLICATED_ERROR("중복된 파일 이름이 있습니다.", HttpStatus.CONFLICT),

    FILE_UPLOAD_FAIL_ERROR("파일 업로드 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    FILE_DELETED_FAIL_ERROR("파일 삭제에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    FILE_DELETED_FAIL_ERROR_500("파일 삭제에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    FILE_UPLOAD_FAIL_ERROR_500("파일 업로드에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

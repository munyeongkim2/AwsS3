package crowdworks.image.common.Response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ApiResponse<T> implements Serializable {

    private final Map<String, Object> header = new HashMap<>();
    private final transient T body;

    @JsonCreator
    public ApiResponse(@JsonProperty("resultMessage") String resultMessage,
                       @JsonProperty("isSuccessful") boolean isSuccessful,
                       @JsonProperty("body") T body) {
        this.header.put("isSuccessful", isSuccessful);
        this.header.put("resultMessage", resultMessage);
        this.body = body;
    }

    public static <T> ApiResponse<T> success(T body) {
        return new ApiResponse<>("SUCCESS", true, body);
    }

    public static <T> ApiResponse<T> error(T body) {
        return new ApiResponse<>("FAIL", false, body);
    }
}
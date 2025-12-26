package org.localvendor.authservice.dto.response;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private boolean success = false;
    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
    private Map<String, List<String>> validationErrors;
    private String traceId;
}
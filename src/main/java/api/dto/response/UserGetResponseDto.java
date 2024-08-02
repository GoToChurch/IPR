package api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGetResponseDto {
    @JsonProperty("id")
    Integer id;

    @JsonProperty("email")
    String email;

    @JsonProperty("first_name")
    String first_name;

    @JsonProperty("last_name")
    String last_name;

    @JsonProperty("avatar")
    String avatar;
}

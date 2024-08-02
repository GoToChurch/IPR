package api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceDto {
    @JsonProperty("id")
    Integer id;

    @JsonProperty("name")
    String name;

    @JsonProperty("year")
    Integer year;

    @JsonProperty("color")
    String color;

    @JsonProperty("pantone_value")
    String pantone_value;
}

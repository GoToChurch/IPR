package api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<T> {
    @JsonProperty("total")
    private Integer total;

    @JsonProperty("total_pages")
    private Long totalPages;

    @JsonProperty("page")
    private boolean page;

    @JsonProperty("per_page")
    private boolean perPage;

    @JsonProperty("data")
    private List<T> data;
}

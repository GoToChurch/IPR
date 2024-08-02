package utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PageRequest<T> {
    @Nonnull
    String endpoint;

    @Nonnull
    Class<T> contentClass;

    Integer pageNumber;

    Integer perPage;

    public Map<String, Integer> asQueryParams() {
        Map<String, Integer> queryParams = new HashMap<>();
        Optional.ofNullable(perPage).ifPresentOrElse(t -> queryParams.put("per_page", perPage), () -> queryParams.put("per_page", 100));
        Optional.ofNullable(pageNumber).ifPresentOrElse(t -> queryParams.put("page", pageNumber), () -> queryParams.put("page", 1));
        return queryParams;
    }
}

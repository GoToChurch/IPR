package config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class DbConfig {

    @Nonnull
    @JsonProperty("url")
    String url;

    @Nonnull
    @JsonProperty("username")
    String username;

    @Nonnull
    @JsonProperty("password")
    String password;

    @Nullable
    @JsonProperty("table_prefix")
    String tablePrefix;

}

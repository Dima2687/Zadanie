package success;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SuccessRegister {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("token")
    private String token;
}

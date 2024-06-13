package data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NewUser {
    @JsonProperty("name")
    private String name;
    @JsonProperty("job")
    private String job;
}

package unsuccess;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnSuccessReg {
    @JsonProperty("error")
    private String error;
}
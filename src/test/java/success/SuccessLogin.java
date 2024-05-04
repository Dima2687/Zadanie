package success;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor

public class SuccessLogin {
    private String token;
    public SuccessLogin(String token){
        this.token = token;
    }
    public String getToken(){
        return token;
    }
}

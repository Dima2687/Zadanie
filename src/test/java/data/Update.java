package data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Update {
    private String name;
    private String job;
    private Date createdAt;

    public Update(String name, String job, Date createdAt) {
        this.name = name;
        this.job = job;
        this.createdAt = createdAt;
    }

}

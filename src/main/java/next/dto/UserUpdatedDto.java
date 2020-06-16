package next.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserUpdatedDto {
    private String name;
    private String email;

    public UserUpdatedDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotEmpty(name) &&
            StringUtils.isNotEmpty(email);
    }
}

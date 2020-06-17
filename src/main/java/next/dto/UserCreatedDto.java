package next.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class UserCreatedDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    public UserCreatedDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotEmpty(userId) &&
            StringUtils.isNotEmpty(password) &&
            StringUtils.isNotEmpty(name) &&
            StringUtils.isNotEmpty(email);
    }
}

package next.dto;

import core.util.StringUtils;
import lombok.Getter;

@Getter
public class UserCreatedDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    private UserCreatedDto() {
    }

    public UserCreatedDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(userId) &&
            StringUtils.isNotEmpty(password) &&
            StringUtils.isNotEmpty(name) &&
            StringUtils.isNotEmpty(email);
    }
}

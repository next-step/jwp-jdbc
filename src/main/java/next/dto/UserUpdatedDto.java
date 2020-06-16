package next.dto;

import core.util.StringUtils;
import lombok.Getter;

@Getter
public class UserUpdatedDto {
    private String name;
    private String email;

    private UserUpdatedDto() {
    }

    public UserUpdatedDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(name) &&
            StringUtils.isNotEmpty(email);
    }
}

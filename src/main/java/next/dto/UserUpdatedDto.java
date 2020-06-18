package next.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserUpdatedDto that = (UserUpdatedDto) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}

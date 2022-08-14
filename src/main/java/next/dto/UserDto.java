package next.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import next.model.User;

import java.util.Objects;

public class UserDto {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    @JsonCreator
    public UserDto(
            @JsonProperty("userId") String userId,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public static UserDto from(User user) {
        return new UserDto(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, name, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto userDto = (UserDto) o;
        return Objects.equals(userId, userDto.userId) && Objects.equals(password, userDto.password) && Objects.equals(name, userDto.name) && Objects.equals(email, userDto.email);
    }
}

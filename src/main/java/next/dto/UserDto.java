package next.dto;

import next.model.User;

public class UserDto {

    private String userId;
    private String name;
    private String email;

    private UserDto() {}

    public UserDto(final String userId, final String name, final String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public static UserDto from(final User user) {
        return new UserDto(user.getUserId(), user.getName(), user.getEmail());
    }

    public String getUserId() { return this.userId; }
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
}

package next.dto;

import next.model.User;

public class UserDto {

    private String userId;
    private String name;
    private String email;

    private UserDto() {}

    public UserDto(final User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public String getUserId() { return this.userId; }
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
}

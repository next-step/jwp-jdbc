package next.dto;

import next.model.User;

public class UserUpdatedDto {
    private String name;
    private String email;

    private UserUpdatedDto() {
    }

    public UserUpdatedDto(final String name, final String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public User toUser(final User user) {
        return new User(user.getUserId(), user.getPassword(), this.name, this.email);
    }
}

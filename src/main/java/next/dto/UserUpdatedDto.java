package next.dto;

import next.model.User;

public class UserUpdatedDto {
    private String name;
    private String email;

    private UserUpdatedDto() {
    }

    public UserUpdatedDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public User updateUser(User user) {
        return new User(user.getUserId(), user.getPassword(), this.name, this.email);
    }
}

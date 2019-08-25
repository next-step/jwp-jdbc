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

    public void update(User user) {
        user.update(name, email);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

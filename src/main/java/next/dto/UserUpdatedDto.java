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

    public static User toUser(UserUpdatedDto updatedDto) {
        return new User(updatedDto.name, updatedDto.email);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

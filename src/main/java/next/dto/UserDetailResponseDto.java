package next.dto;

import next.model.User;

public class UserDetailResponseDto {
    private String userId;
    private String name;
    private String email;

    public UserDetailResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

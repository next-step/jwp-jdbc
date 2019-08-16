package next.dto;

public class UserUpdatedDto {
    private String userId;
    private String name;
    private String email;

    private UserUpdatedDto() {
    }

    public UserUpdatedDto(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
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

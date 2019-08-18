package next.dto;

public class UserUpdatedDto {
    private String name;
    private String email;
    private String password;

    private UserUpdatedDto() {
    }

    public UserUpdatedDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

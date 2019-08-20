package next.dto;

public class UserUpdatedDto {
    private String name;
    private String password;
    private String email;

    private UserUpdatedDto() {
    }

    public UserUpdatedDto(String name, String password, String email) {
        this.name = name;
        this.password = password;
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

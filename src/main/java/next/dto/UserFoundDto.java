package next.dto;

/**
 * Created by iltaek on 2020/07/03 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class UserFoundDto {

    private String userId;
    private String name;
    private String email;

    private UserFoundDto() {
    }

    public UserFoundDto(String userId, String name, String email) {
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

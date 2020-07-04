package next.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.model.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreatedDto {

    private String userId;
    private String password;
    private String name;
    private String email;

    @Builder
    public UserCreatedDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }
}

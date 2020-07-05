package next.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdatedDto {

    private String name;
    private String email;

    @Builder
    public UserUpdatedDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

package next.converter;

import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

public class UserConverter {
    public static User convertToUserBy(UserCreatedDto dto) {
        return new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
    }

    public static User convertToUserBy(UserUpdatedDto dto) {
        return new User(dto.getName(), dto.getEmail());
    }
}

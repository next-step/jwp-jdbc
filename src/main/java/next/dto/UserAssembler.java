package next.dto;

import next.model.User;

public class UserAssembler {

    public static User transferToUser(UserCreatedDto dto) {
        return new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
    }

    public static User transferToUser(UserUpdatedDto dto) {
        return new User(null, null, dto.getName(), dto.getEmail());
    }
}

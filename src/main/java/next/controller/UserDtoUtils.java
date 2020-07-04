package next.controller;

import next.dto.UserCreatedDto;
import next.dto.UserFoundDto;
import next.dto.UserUpdatedDto;
import next.model.User;

/**
 * Created by iltaek on 2020/07/02 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class UserDtoUtils {

    private UserDtoUtils() {
    }

    public static User createdDtoToUser(UserCreatedDto userCreatedDto) {
        return new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
    }

    public static UserFoundDto userToFoundDto(User user) {
        return new UserFoundDto(user.getUserId(), user.getName(), user.getEmail());
    }

    public static User updateUser(User user, UserUpdatedDto userUpdatedDto) {
        user.update(new User("", user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));
        return user;
    }
}

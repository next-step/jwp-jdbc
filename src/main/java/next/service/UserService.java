package next.service;

import core.db.DataBase;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.util.Optional;

public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public static User create(UserCreatedDto createdDto) {
        User user = new User(
                createdDto.getUserId(),
                createdDto.getPassword(),
                createdDto.getName(),
                createdDto.getEmail()
        );

        logger.debug("User : {}", user);
        DataBase.addUser(user);

        return user;
    }

    public static User findByUserId(String userId) {
        return DataBase.findUserById(userId);
    }

    public static void update(String userId, UserUpdatedDto updatedDto) {
        User foundUser = Optional.ofNullable(findByUserId(userId))
                .orElseThrow(EntityNoFoundException::new);

        foundUser.update(UserUpdatedDto.toUser(updatedDto));
    }
}

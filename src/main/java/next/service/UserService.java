package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public void createUser(UserCreatedDto createdDto) {
        User user = new User(createdDto.getUserId(), createdDto.getPassword(), createdDto.getName(), createdDto.getEmail());
        userDao.insert(user);
    }

    public User getUser(String userId) {
        return userDao.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디 입니다. userId : [" + userId + "]"));
    }

    public void updateUser(String userId, UserUpdatedDto updatedDto) {
        User user = userDao.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디 입니다. userId : [" + userId + "]"));

        userDao.update(new User(user.getUserId(), user.getPassword(), updatedDto.getName(), updatedDto.getEmail()));
    }
}

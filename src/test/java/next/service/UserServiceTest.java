package next.service;

import static org.assertj.core.api.Assertions.assertThat;

import core.db.DataBase;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  @Test
  void create() {
    UserCreatedDto userCreatedDto = new UserCreatedDto("lcj", "password", "changjun",
        "leechang0423@naver.com");
    UserService userService = new UserService();
    User user = userService.createUser(userCreatedDto);

    assertThat(user.getUserId()).isEqualTo("lcj");
    assertThat(user.getName()).isEqualTo("changjun");
    assertThat(user.getPassword()).isEqualTo("password");
    assertThat(user.getEmail()).isEqualTo("leechang0423@naver.com");
  }

  @Test
  void getUser() {
    User newUser = new User("lcj", "password", "changjun",
        "leechang0423@naver.com");
    UserService userService = new UserService();
    DataBase.addUser(newUser);

    assertThat(userService.getUser(newUser.getUserId())).isEqualTo(newUser);
  }

  @Test
  void updateUser() {
    User user = new User("lcj", "password", "changjun",
        "leechang0423@naver.com");
    UserService userService = new UserService();
    DataBase.addUser(user);

    UserUpdatedDto updatedDto = new UserUpdatedDto("jun","jun@naver.com");
    User updatedUser = userService.updateUser(user.getUserId(), updatedDto);
    assertThat(updatedUser.getName()).isEqualTo(updatedDto.getName());
    assertThat(updatedUser.getEmail()).isEqualTo(updatedDto.getEmail());
  }
}
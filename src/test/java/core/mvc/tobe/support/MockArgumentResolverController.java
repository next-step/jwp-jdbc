package core.mvc.tobe.support;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;

public class MockArgumentResolverController {

    @RequestMapping("/requestParam")
    String mockRequestParamMethod(@RequestParam(value = "user") String id) {
        return id;
    }

    @RequestMapping("/requestParam")
    String mockRequestParamMethod(@RequestParam int id) {
        return String.valueOf(id);
    }

    @RequestMapping("/pathVariable/{name}")
    String mockPathVariableMethod(@PathVariable(value = "name") String userName) {
        return userName;
    }

    @RequestMapping("/pathVariable/user/{id}")
    String mockPathVariableMethod(@PathVariable int id) {
        return String.valueOf(id);
    }

    @RequestMapping("/user")
    String mockUser(MockUser user) {
        return user.toString();
    }

    @RequestMapping("/requestBody/user")
    String mockRequestBodyUser(@RequestBody MockUser user) {
        return user.toString();
    }

    @RequestMapping("/requestBody/userId")
    String mockRequestBodyUserId(@RequestBody String userId) {
        return userId;
    }

    @RequestMapping("/requestBody/age")
    String mockRequestBodyAge(@RequestBody int age) {
        return String.valueOf(age);
    }

    @RequestMapping("/requestParam/requestBody/age")
    String mockRequestBodyAgeAndMoney(@RequestParam int age, @RequestBody long money) {
        return age + "_" + money;
    }

    @RequestMapping("/double/requestBody/primitiveAndUser")
    String mockRequestBodyAgeAndUser(@RequestBody Integer age, @RequestBody MockUser user) {
        return user.getName() + " is " + age + " years old";
    }

    @RequestMapping("/double/requestBody/users")
    String mockRequestBodyUserAndUser(@RequestBody MockUser firstUser, @RequestBody MockUser secondUser) {
        return firstUser.toString() + secondUser.toString();
    }

}

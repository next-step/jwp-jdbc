package core.mvc.tobe.support;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import next.dto.UserCreatedDto;

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

    @RequestMapping(value = "/requestBody", method = RequestMethod.POST)
    String mockRequestBodyMethod(@RequestBody UserCreatedDto userCreatedDto) {
        return userCreatedDto.toString();
    }

}

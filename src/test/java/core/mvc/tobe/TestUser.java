package core.mvc.tobe;

import java.util.Objects;

public class TestUser {
    private String userId;
    private String password;
    private int age;

    public TestUser() {
    }

    public TestUser(String userId, String password, int age) {
        this.userId = userId;
        this.password = password;
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestUser testUser = (TestUser) o;
        return age == testUser.age &&
                Objects.equals(userId, testUser.userId) &&
                Objects.equals(password, testUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, age);
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}

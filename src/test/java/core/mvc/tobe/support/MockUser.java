package core.mvc.tobe.support;

import java.util.Objects;

public class MockUser {

    public MockUser(String id) {
        this.id = id;
    }

    private String id;

    private String name;

    private String addr;

    private String notSetterPhoneNumber;

    private int age;

    private long money;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotSetterPhoneNumber() {
        return notSetterPhoneNumber;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MockUser mockUser = (MockUser) o;
        return getAge() == mockUser.getAge() && getMoney() == mockUser.getMoney() && Objects.equals(getId(), mockUser.getId())
            && Objects.equals(getName(), mockUser.getName()) && Objects.equals(getAddr(), mockUser.getAddr()) && Objects.equals(
            getNotSetterPhoneNumber(), mockUser.getNotSetterPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAddr(), getNotSetterPhoneNumber(), getAge(), getMoney());
    }

    @Override
    public String toString() {
        return id + "_" + name + "_" + age + "_" + money;
    }
}

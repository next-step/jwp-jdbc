package core.mvc.tobe.support;

import java.util.Objects;

public class MockUser {
    public MockUser() {}

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
    public String toString() {
        return id + "_" + name + "_" + age + "_" + money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockUser mockUser = (MockUser) o;
        return age == mockUser.age &&
            money == mockUser.money &&
            Objects.equals(id, mockUser.id) &&
            Objects.equals(name, mockUser.name) &&
            Objects.equals(addr, mockUser.addr) &&
            Objects.equals(notSetterPhoneNumber, mockUser.notSetterPhoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addr, notSetterPhoneNumber, age, money);
    }
}

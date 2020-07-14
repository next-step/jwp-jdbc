package core.mvc.tobe.support;

public class MockUserDto {

    private String id;

    private String name;

    private int age;

    private long money;

    public MockUserDto() {
    }

    public MockUserDto(String id, String name, int age, long money) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public long getMoney() {
        return money;
    }
}

package core.mvc.tobe.support;

/**
 * Created by iltaek on 2020/07/03 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class MockUserDto {

    private String id;

    private MockUserDto() {
    }

    public MockUserDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MockUserDto that = (MockUserDto) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MockUserDto{" +
            "id='" + id + '\'' +
            '}';
    }
}

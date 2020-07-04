package core.util;

/**
 * Created by iltaek on 2020/07/02 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class Args {

    private Args() {
    }

    public static void check(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

}

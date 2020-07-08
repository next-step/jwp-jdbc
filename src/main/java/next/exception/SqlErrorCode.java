package next.exception;

public enum SqlErrorCode {
    ERR_DUP_ENTRY(23505);

    private int code;

    SqlErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

package support.exception;

public class DuplicatedEntityException extends RuntimeException {
    public DuplicatedEntityException() {
        super("단일 Entity 조회를 시도하였으나 결과가 2개 이상입니다.");
    }
}

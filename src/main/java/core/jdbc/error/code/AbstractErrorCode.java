package core.jdbc.error.code;

import java.util.Arrays;

public abstract class AbstractErrorCode {
    protected int[] badSqlGrammarCodes;
    protected int[] duplicateKeyCodes;
    protected int[] dataIntegrityViolationCodes;
    protected int[] dataAccessResourceFailureCodes;
    protected int[] transientDataAccessResourceCodes;
    protected int[] deadlockLoserCodes;
    protected String databaseProductName;
    protected boolean useSqlStateForTranslation;
    protected int[] cannotAcquireLockCodes;

    public AbstractErrorCode() {
        badSqlGrammarCodes = new int[0];
        duplicateKeyCodes = new int[0];
        dataIntegrityViolationCodes = new int[0];
        dataAccessResourceFailureCodes = new int[0];
        transientDataAccessResourceCodes = new int[0];
        deadlockLoserCodes = new int[0];
        databaseProductName = "";
        useSqlStateForTranslation = false;
        cannotAcquireLockCodes = new int[0];
    }

    public boolean isBadSqlGrammar(int code) {
        return Arrays.asList(badSqlGrammarCodes).contains(code);
    }

    public boolean isDuplicate(int code) {
        return Arrays.stream(duplicateKeyCodes).anyMatch(c -> c == code);
    }

    public boolean isDataIntegrityViolation(int code) {
        return Arrays.stream(dataIntegrityViolationCodes).anyMatch(c -> c == code);
    }

    public boolean isDataAccessResourceFailure(int code) {
        return Arrays.stream(dataAccessResourceFailureCodes).anyMatch(c -> c == code);
    }

    public boolean isTransientDataAccessResource(int code) {
        return Arrays.stream(transientDataAccessResourceCodes).anyMatch(c -> c == code);

    }

    public boolean isDeadlockLoser(int code) {
        return Arrays.asList(deadlockLoserCodes).contains(code);
    }

    public boolean useSqlStateForTranslation() {
        return useSqlStateForTranslation;
    }

    public boolean isCannotAcquireLock(int code) {
        return Arrays.asList(cannotAcquireLockCodes).contains(code);
    }

}

package core.jdbc.error.code;

public class H2ErrorCode extends AbstractErrorCode {

    public H2ErrorCode() {
        badSqlGrammarCodes = new int[]{42000, 42001, 42101, 42102, 42111, 42112, 42121, 42122, 42132};
        duplicateKeyCodes = new int[]{23001, 23505};
        dataIntegrityViolationCodes = new int[]{22001, 22003, 22012, 22018, 22025, 23000, 23002, 23003, 23502, 23503, 23506, 23507, 23513};
        dataAccessResourceFailureCodes = new int[]{90046, 90100, 90117, 90121, 90126};
        deadlockLoserCodes = new int[]{50200};
    }
}


package consts;

public enum FilePathConst {
    NON_VALID_FILE_PATH("data\\nonvalid"),
    STATISTIC_FILE_PATH("data\\statistic\\statistic.txt"),
    EXECUTE_LOG_FILE_PATH("log\\execute_log.txt"),
    EXCEPTION_LOG_FILE_PATH("log\\exception_log.txt");

    private String path;

    FilePathConst(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

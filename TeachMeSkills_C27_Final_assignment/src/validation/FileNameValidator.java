package validation;

import logger.Logger;

import java.io.File;

public class FileNameValidator {
    public static boolean isFileValid(String pattern, File file) {
        Logger.writeExecuteLog("file '" + file.getName() + "' name check");
        if (!file.getName().endsWith(".txt")) {
            Logger.writeExecuteLog("file '" + file.getName() + "' name check filed: not .txt file");
            return false;
        } else if (!file.getName().toLowerCase().matches(pattern)){
            Logger.writeExecuteLog("file '" + file.getName() + "' name check filed: not match the pattern");
            return false;
        } else {
            Logger.writeExecuteLog("file '" + file.getName() + "' name has been verified");
            return true;
        }
    }
}

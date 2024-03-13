package util;

import consts.FilePathConst;
import logger.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StatisticWriter {
    public static void writeDataToStatisticFile(String lineDescription, String data) {
        Logger.writeExecuteLog("recording of statistical data to the file has begun");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FilePathConst.STATISTIC_FILE_PATH.getPath(), true))) {
            bufferedWriter.write(lineDescription + ": " + data + " USD\n");
        } catch (IOException e) {
            Logger.writeExecuteLog("recording of statistical data to the file failed: Exception");
            Logger.writeExceptionLog("file '" + FilePathConst.STATISTIC_FILE_PATH.getPath() + "' access error");
            System.out.println("file '" + FilePathConst.STATISTIC_FILE_PATH.getPath() + "' access error");
        } catch (Exception e) {
            Logger.writeExecuteLog("recording of statistical data to the file failed: Exception");
            Logger.writeExceptionLog("error while writing file '" + FilePathConst.STATISTIC_FILE_PATH.getPath() + "'");
            System.out.println("error while writing file '" + FilePathConst.STATISTIC_FILE_PATH.getPath() + "'");
        }
        Logger.writeExecuteLog("statistical data has been recorded to the file");
    }
}

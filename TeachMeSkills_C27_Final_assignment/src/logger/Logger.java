package logger;

import consts.FilePathConst;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void writeExceptionLog(String message) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FilePathConst.EXCEPTION_LOG_FILE_PATH.getPath(), true))) {
            bufferedWriter.write(simpleDateFormat.format(date) + " -- [" + message + "]\n");
        } catch (IOException e) {
            System.out.println("file 'exception_log.txt' access error");
        } catch (Exception e) {
            System.out.println("error while writing file 'exception_log.txt'");
        }
    }
    public static void writeExecuteLog(String message) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FilePathConst.EXECUTE_LOG_FILE_PATH.getPath(), true))) {
            bufferedWriter.write(simpleDateFormat.format(date) + " -- [" + message + "]\n");
        } catch (IOException e) {
            System.out.println("file 'execute_log.txt' access error");
            Logger.writeExceptionLog("file 'execute_log.txt' access error");
        } catch (Exception e) {
            System.out.println("error while writing file 'execute_log.txt'");
            Logger.writeExceptionLog("error while writing file 'execute_log.txt'");
        }
    }
}

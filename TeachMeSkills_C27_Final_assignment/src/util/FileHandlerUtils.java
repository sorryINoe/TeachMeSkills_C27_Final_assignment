package util;

import consts.*;
import exception.EmptyDirectoryException;
import exception.IncorrectContentInFileException;
import exception.IncorrectDirectoryPathException;
import logger.Logger;
import validation.FileContentValidator;
import validation.FileNameValidator;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileHandlerUtils {
    public static boolean checkInputDirectory(File directory) throws IncorrectDirectoryPathException {
        Logger.writeExecuteLog("checking the entered path");
        if (!directory.isDirectory()) {
            Logger.writeExecuteLog("check failed (Exception): the object at the entered path is not a folder");
            throw new IncorrectDirectoryPathException("the object at the entered path is not a folder");
        } else if (directory.listFiles().length == 0) {
            Logger.writeExecuteLog("check failed (Exception): the folder at the entered path is empty");
            throw new IncorrectDirectoryPathException("the folder at the entered path is empty");
        } else if (!(new File(directory + "\\checks").exists())) {
            Logger.writeExecuteLog("check failed (Exception): There is no \"checks\" folder at the specified path");
            throw new IncorrectDirectoryPathException("There is no \"checks\" folder at the specified path");
        } else if (!(new File(directory + "\\invoices").exists())) {
            Logger.writeExecuteLog("check failed (Exception): There is no \"invoices\" folder at the specified path");
            throw new IncorrectDirectoryPathException("There is no \"invoices\" folder at the specified path");
        } else if (!(new File(directory + "\\orders").exists())) {
            Logger.writeExecuteLog("check failed (Exception): There is no \"orders\" folder at the specified path");
            throw new IncorrectDirectoryPathException("There is no \"orders\" folder at the specified path");
        }
        Logger.writeExecuteLog("check passed");
        return true;
    }

    public static List<File> getFilesFromDirectory(String directoryPath, FileNamePattern fileNamePattern) throws EmptyDirectoryException {
        Logger.writeExecuteLog("getting files '" + fileNamePattern + "' from a folder");
        File file = new File(directoryPath);
        List<File> rowListOfFiles;
        List<File> resultListOfFiles = new ArrayList<>();
        if (file.listFiles() != null) {
            if (file.listFiles().length != 0) {
                rowListOfFiles = Arrays.asList(file.listFiles());
                Iterator<File> iterator = rowListOfFiles.listIterator();
                while (iterator.hasNext()) {
                    File tempFile = iterator.next();
                    if (FileNameValidator.isFileValid(fileNamePattern.getPattern(), tempFile)) {
                        resultListOfFiles.add(tempFile);
                    } else {
                        moveFile(tempFile, FilePathConst.NON_VALID_FILE_PATH);
                    }
                }
            } else {
                Logger.writeExecuteLog("getting files '" + fileNamePattern + "' failed (Exception): the folder '" + directoryPath + "' is empty");
                throw new EmptyDirectoryException("the folder '" + directoryPath + "' is empty");
            }
        }
        return resultListOfFiles;
    }

    public static Map<String, List<String>> getDataFromFiles(List<File> listOfFiles) {
        Map<String, List<String>> resultData = new HashMap<>();
        for (File tempFile : listOfFiles) {
            Logger.writeExecuteLog("getting data from a file '" + tempFile.getName() + "'");
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile))) {
                resultData.put(tempFile.getAbsolutePath(),
                        bufferedReader.lines().map(String::trim).collect(Collectors.toList()));
            } catch (FileNotFoundException e) {
                Logger.writeExecuteLog("getting data from a file '" + tempFile.getName() + "' failed (Exception): file '" + tempFile + "' not found");
                Logger.writeExceptionLog("file '" + tempFile + "' not found");
                System.out.println("file '" + tempFile + "' not found");
            } catch (IOException e) {
                Logger.writeExecuteLog("getting data from a file '" + tempFile.getName() + "' failed (Exception): file '" + tempFile + "' access error");
                Logger.writeExceptionLog("file '" + tempFile + "' access error");
                System.out.println("file '" + tempFile + "' access error");
            } catch (Exception e) {
                Logger.writeExecuteLog("getting data from a file '" + tempFile.getName() + "' error while reading file '" + tempFile + "'");
                Logger.writeExceptionLog("error while reading file '" + tempFile + "'");
                System.out.println("error while reading file '" + tempFile + "'");
            }
        }
        return resultData;
    }

    public static BigDecimal processFiles(FileNamePattern fileNamePattern, Map<String, List<String>> listOfData) {
        BigDecimal sum = new BigDecimal(0);
        for (Map.Entry<String, List<String>> entry : listOfData.entrySet()) {
            Logger.writeExecuteLog("getting statistic from a file '" + entry.getKey() + "'");
            switch (fileNamePattern) {
                case CHECK_NAME_PATTERN:
                    try {
                        if (FileContentValidator.checkCheckContentValid(entry.getKey(), entry.getValue())) {
                            sum = sum.add(getAmount(entry.getValue(), CheckContentPatterns.AMOUNT_LINE));
                        } else {
                            moveFile(new File(entry.getKey()), FilePathConst.NON_VALID_FILE_PATH);
                        }
                    } catch (IncorrectContentInFileException e) {
                        System.out.println(e.getMessage());
                        Logger.writeExceptionLog(e.getMessage());
                    }
                    break;
                case INVOICE_NAME_PATTERN:
                    try {
                        if (FileContentValidator.checkInvoiceContentValid(entry.getKey(), entry.getValue())) {
                            sum = sum.add(getAmount(entry.getValue(), InvoiceContentPatterns.AMOUNT_LINE));
                        } else {
                            moveFile(new File(entry.getKey()), FilePathConst.NON_VALID_FILE_PATH);
                        }
                    } catch (IncorrectContentInFileException e) {
                        System.out.println(e.getMessage());
                        Logger.writeExceptionLog(e.getMessage());
                    }
                    break;
                case ORDER_NAME_PATTERN:
                    try {
                        if (FileContentValidator.checkOrderContentValid(entry.getKey(), entry.getValue())) {
                            sum = sum.add(getAmount(entry.getValue(), OrderContentPattern.AMOUNT_LINE));
                        } else {
                            moveFile(new File(entry.getKey()), FilePathConst.NON_VALID_FILE_PATH);
                        }
                    } catch (IncorrectContentInFileException e) {
                        System.out.println(e.getMessage());
                        Logger.writeExceptionLog(e.getMessage());
                    }
                    break;
            }
        }
        return sum;
    }

    private static boolean moveFile(File fileToMove, FilePathConst pathToMove) {
        Logger.writeExecuteLog("moving a file '" + fileToMove.getName() + "' to 'non_valid' folder");
        File newFile = new File(pathToMove.getPath() + "\\" + fileToMove.getName());
        boolean isFileCreated = false;
        boolean isFileCopied = false;
        boolean isFileDeleted;
        try {
            isFileCreated = newFile.createNewFile();
        } catch (IOException e) {
            Logger.writeExecuteLog("moving a file '" + fileToMove.getName() + "' to 'non_valid' folder failed: Exception");
            Logger.writeExceptionLog("file '" + newFile.getName() + "' access error");
            System.out.println("file '" + newFile.getName() + "' access error during creation");
        } catch (Exception e) {
            Logger.writeExecuteLog("moving a file '" + fileToMove.getName() + "' to 'non_valid' folder failed: Exception");
            Logger.writeExceptionLog("error while writing file '" + newFile.getName() + "'");
            System.out.println("error during creation file '" + newFile.getName() + "'");
        }
        if (isFileCreated) {
            try (FileInputStream fileInputStream = new FileInputStream(fileToMove);
                 FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
                fileOutputStream.write(fileInputStream.readAllBytes());
                isFileCopied = true;

            } catch (FileNotFoundException e) {
                Logger.writeExecuteLog("moving a file '" + fileToMove.getName() + "' to 'non_valid' folder failed: Exception");
                Logger.writeExceptionLog("files '" + fileToMove.getName() + "/" + newFile.getName() + "' not found during copying");
                System.out.println("files '" + fileToMove.getName() + "/" + newFile.getName() + "' not found during copying");
            } catch (IOException e) {
                Logger.writeExecuteLog("moving a file '" + fileToMove.getName() + "' to 'non_valid' folder failed: Exception");
                Logger.writeExceptionLog("files '" + fileToMove.getName() + "/" + newFile.getName() + "' access error during copying");
                System.out.println("files '" + fileToMove.getName() + "/" + newFile.getName() + "' access error during copying");
            } catch (Exception e) {
                Logger.writeExecuteLog("moving a file '" + fileToMove.getName() + "' to 'non_valid' folder failed: Exception");
                Logger.writeExceptionLog("error while copying file '" + fileToMove.getName() + "'");
                System.out.println("error while copying file '" + fileToMove.getName() + "'");
            }
        }
        isFileDeleted = fileToMove.delete();
        Logger.writeExecuteLog("file '" + fileToMove.getName() + "' has been moved to 'non_valid' folder");
        return isFileCreated && isFileCopied && isFileDeleted;
    }

    private static BigDecimal getAmount(List<String> lines, String amountPattern) {
        Logger.writeExecuteLog("getting amount value");
        BigDecimal result = new BigDecimal(0);
        String amountLine = "0";
        for (String line : lines) {
            if (line.toLowerCase().matches(amountPattern)) {
                amountLine = line;
            }
        }
        Pattern pattern = Pattern.compile("[0-9(,|.)*]+((.|,)[0-9]{2})?");
        Matcher matcher = pattern.matcher(amountLine);
        if (matcher.find()) {
            String sumStr = matcher.group();
            if (sumStr.length() > 2) {
                if (sumStr.substring((sumStr.length() - 3), sumStr.length() - 2).equals(",")) {
                    sumStr = sumStr.replace(".", "");
                    sumStr = sumStr.replace(",", ".");
                } else {
                    sumStr = sumStr.replaceAll(",", "");
                }
            }
            Logger.writeExecuteLog("value '" + sumStr + "' received");
            result = new BigDecimal(sumStr);
        }
        String currency = "";
        pattern = Pattern.compile(" ?(\\$|(USD)) ?");
        matcher = pattern.matcher(amountLine);
        if (matcher.find()) {
            currency = "USD";
        } else {
            pattern = Pattern.compile(" ?[A-Z]{3} ?");
            matcher = pattern.matcher(amountLine);
            if (matcher.find()) {
                currency = matcher.group().trim();
            } else {
                currency = "USD";
            }
        }
        Logger.writeExecuteLog("value '" + currency + "' received");
        return CurrencyConverter.convertCurrency(result, currency);
    }
}

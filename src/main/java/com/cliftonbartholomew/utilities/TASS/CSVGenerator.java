package com.cliftonbartholomew.utilities.TASS;

import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.scene.control.TextArea;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class CSVGenerator implements Runnable {
    private static final boolean DEBUG = true;

    private File file;
    private int term;
    private TextArea outputArea;
    private MFXProgressBar progressBar;
    private int totalNumFiles;
    private int filesProcessed;

    public CSVGenerator(File file, int term, TextArea outputArea, MFXProgressBar progressBar){
        this.file = file;
        this.term = term;
        this.outputArea = outputArea;
        this.progressBar = progressBar;

        this.totalNumFiles = countFiles(file);
        outputArea.appendText("Number of files to be processed: " + totalNumFiles + "\n");
        filesProcessed = 0;
    }

    private int countFiles(File f){
        if (f.isFile()) {
            return 1;
        } else if (f.isDirectory()) {
            int sum = 0;
            for (File f2 : f.listFiles()) {
                sum += countFiles(f2);
            }
            return sum;
        }else{
            return 0;
        }
    }

    private String generateCSVFromFile(File f, int t) throws IOException, InvalidFormatException {
        StringBuilder outString = new StringBuilder();

        outputArea.appendText("Currently processing " + f.getName() + "\n");

        //add file to output csv for debug mode
        if(DEBUG){
            outString.append("\nDEBUG: Processing ").append(f.getName()).append("\n");
        }
        if(f.getName().contains(".xls")) {
            //get TASS sheet based on term
            OPCPackage pkg = OPCPackage.open(f);
            XSSFWorkbook workbook = new XSSFWorkbook(pkg);
            Sheet sheet = workbook.getSheet("TASS Term " + t);

            //set up helper variables
            int rowIndex = 0;
            boolean stop = false;

            //for each row
            for (Row row : sheet) {
                StringBuilder rowString = new StringBuilder();

                //marks only start on row 5
                if (rowIndex > 4) {
                    int columnIndex = 0;

                    //for each cell
                    for (Cell cell : row) {
                        //determine the type of cell
                        boolean isString = (cell.getCellType() == CellType.STRING) || (cell.getCellType() == CellType.FORMULA && cell.getCachedFormulaResultType() == CellType.STRING);
                        boolean isNumeric = (cell.getCellType() == CellType.NUMERIC) || (cell.getCellType() == CellType.FORMULA && cell.getCachedFormulaResultType() == CellType.NUMERIC);

                        //get the value
                        String cellValue = "";
                        if (isString) {
                            cellValue = cell.getStringCellValue();
                        } else if (isNumeric) {
                            cellValue = Math.round(cell.getNumericCellValue()) + "";
                        }

                        //once we get to a blank student number (in column 0) stop
                        if (columnIndex == 0) {
                            if (cellValue.isBlank()) {
                                stop = true;
                                break;
                            }
                        }

                        //blank the student name (in column 1 - issues with commas from TASS)
                        if (columnIndex == 1) {
                            rowString.append(",");
                        } else {
                            if (!cellValue.isBlank() && !cellValue.equals("0")) {
                                rowString.append(cellValue).append(",");
                            }
                        }
                        columnIndex++;
                    }
                    if (stop) break;

                    //add to outString (excluding last comma)
                    outString.append(rowString.substring(0, rowString.length() - 1)).append("\n");
                }
                rowIndex++;
            }
            pkg.close();
        }
        filesProcessed++;
        progressBar.progressProperty().set(((double)filesProcessed)/totalNumFiles);
        return outString.toString();
    }

    private String generateCSVString(File f, int t) throws IOException, InvalidFormatException {
        StringBuilder outString = new StringBuilder();
        if (f.isFile()) {
            outString.append(generateCSVFromFile(f, t));
        } else if (f.isDirectory()) {
            for (File f2 : f.listFiles()) {
                outString.append(generateCSVString(f2, t));
            }
        }
        return outString.toString();
    }

    private void generateCSV() throws IOException, InvalidFormatException {
        String outString = generateCSVString(file, term);
        System.out.println(outString);
        outputArea.appendText("All files processed!");

        File outFile = new File("output/output.csv");
        PrintWriter pw = new PrintWriter(new FileWriter(outFile, false));
        pw.print(outString);
        pw.close();
        File f = new File("output");
        Desktop.getDesktop().browse(f.toURI());
    }

    @Override
    public void run() {
        try {
            generateCSV();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}

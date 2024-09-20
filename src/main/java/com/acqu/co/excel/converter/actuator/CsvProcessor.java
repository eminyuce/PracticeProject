package com.acqu.co.excel.converter.actuator;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
// test 
@Component
public class CsvProcessor {

    private static final String[] SIZES = {
        "64''W x 40''H Inches",
        "96''W x 60''H Inches",
        "120''W x 75''H Inches",
        "138''W x 86''H Inches",
        "160''W x 100''H Inches",
        "178''W x 111''H Inches",
        "184''W x 110''H Inches"
    };

    private static final String[] MATERIALS_TYPES = {
        "Nonwoven Wallpaper",
        "Textured Wallpaper",
        "Peel & Stick"
    };
// test
    public void processCsv(String inputFilePath, String outputFilePath) throws CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(inputFilePath));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {

            String[] header = reader.readNext(); // Reading header
            if (header != null) {
                // Add the new SIZES, MATERIALS_TYPES, and IMAGE_ETSY columns to the header
                String[] newHeader = Arrays.copyOf(header, header.length + 3);
                newHeader[newHeader.length - 3] = "SIZES";
                newHeader[newHeader.length - 2] = "MATERIALS_TYPES";
                newHeader[newHeader.length - 1] = "IMAGE_ETSY";
                writer.writeNext(newHeader);

                String[] line;
                while ((line = reader.readNext()) != null) {
                    // Consolidate image columns into one string with spaces
                    StringBuilder imageEtsyBuilder = new StringBuilder();
                    for (int i = 0; i < header.length; i++) {
                        if (header[i].startsWith("IMAGE") && !line[i].isEmpty()) {
                            if (imageEtsyBuilder.length() > 0) {
                                imageEtsyBuilder.append(" ");
                            }
                            imageEtsyBuilder.append(line[i]);
                        }
                    }

                    // Create new line with SIZES, MATERIALS_TYPES, and IMAGE_ETSY columns at the end
                    String[] newLine = Arrays.copyOf(line, line.length + 3);
                    
                    for (String size : SIZES) {
                        for (String materialType : MATERIALS_TYPES) {
                            // Set the SIZES, MATERIALS_TYPES, and IMAGE_ETSY values
                            newLine[newLine.length - 3] = size;
                            newLine[newLine.length - 2] = materialType;
                            newLine[newLine.length - 1] = imageEtsyBuilder.toString();
                            
                            // Write the new row to the CSV file
                            writer.writeNext(newLine);
                        }
                    }
                }
            }else {
            	System.out.println("NO DATA EXISTS");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void printTest() {
    	System.out.println("TEST METHOD");
    }
    
    public String getStrAppend(String prefix) {
    	return prefix+" test Method";
    }
    
    public void printNewColumns() {
    	int i=1;
    	for (String size : SIZES) {
			for (String material : MATERIALS_TYPES) {
				System.out.println(i+")Size:"+size+" Material:"+material);
				i++;
			}
		}
    }
}


package artplancom.test.misc;

import artplancom.test.models.Animal;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelFileServiceImpl implements ExcelFileService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ByteArrayInputStream export(List<Animal> animals) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Animals");
            Row row = sheet.createRow(0);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));


            Cell cell = row.createCell(0);
            cell.setCellValue("Animal ID");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue("Species");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue("Birthdate");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue("Sex");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue("Nickname");
            cell.setCellStyle(cellStyle);

            for (int i = 0; i < animals.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(animals.get(i).getAnimalId());
                dataRow.createCell(1).setCellValue(animals.get(i).getSpecies());
                dataRow.createCell(2).setCellStyle(dateCellStyle);
                dataRow.getCell(2).setCellValue(animals.get(i).getBirthDate());
                dataRow.createCell(3).setCellValue(animals.get(i).getSex());
                dataRow.createCell(4).setCellValue(animals.get(i).getNickname());
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException ex) {
            logger.error("Error during export Excel file", ex);
            return null;
        }
    }
}

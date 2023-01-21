package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import guru.qa.teacher.SelenideFilesTest;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipTest {

    ClassLoader cl = SelenideFilesTest.class.getClassLoader();

    @Test
    void zipParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("example/test_zip.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("maven.pdf")) {
                    PDF pdfContent = new PDF(zis);
                    assertThat(pdfContent.text).contains("The Ten Minute Test - Creating a Project with Maven 1.x");
                } else if (entry.getName().contains("randomcsv.csv")) {
                    CSVReader csvContent = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = csvContent.readAll();
                    assertThat(content.get(4)[1]).contains("green");
                } else if (entry.getName().contains("file_example_XLS_50.xls")) {
                    XLS xlsContent = new XLS(zis);
                    assertThat(xlsContent.excel.getSheetAt(0).getRow(0).getCell(1).getStringCellValue()).contains("First Name");
                }
            }
        }
    }
}

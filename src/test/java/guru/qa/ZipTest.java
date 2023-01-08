package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
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
                    InputStream is = cl.getResourceAsStream("back.zip");
                    ZipInputStream zis = new ZipInputStream(is)
            ) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    if (entryName.contains(".xlsx")) {
                        XLS content = new XLS(zis);
                        assertThat(content.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).contains("Иванова");
                    } else if (entryName.contains(".pdf")) {
                        PDF content = new PDF(zis);
                        assertThat(content.text).contains("PDF");
                    } else if (entryName.contains(".csv")) {
                        CSVReader reader = new CSVReader(new InputStreamReader(zis));
                        List<String[]> content = reader.readAll();
                        assertThat(content.get(1)[1]).contains("A");
                    }
                }
            }
        }
}

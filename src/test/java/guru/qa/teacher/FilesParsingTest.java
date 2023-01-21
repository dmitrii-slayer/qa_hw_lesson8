package guru.qa.teacher;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.qa.model.Glossary;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;


public class FilesParsingTest {

    ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    void pdfParseTest() throws Exception {
        open("https://maven.apache.org/archives/maven-1.x/");
        File downloadedPdf = $("a[href='maven.pdf']").download();
        PDF content = new PDF(downloadedPdf);
        assertThat(content.text).contains("Changes Between Maven 1.0.2 and Maven 1.1");
    }

    @Test
    void xlsParseTest() throws Exception {
        try (InputStream resourceAsStream = cl.getResourceAsStream("example/file_example_XLS_50.xls")) {
            XLS content = new XLS(resourceAsStream);
            assertThat(content.excel.getSheetAt(0).getRow(9).getCell(2).getStringCellValue()).contains("Weiland");
        }
    }

    @Test
    void csvParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("example/randomcsv.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(resource))
        ) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(3)[2]).contains("quiet");
        }
    }

    @Test
    void zipParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("example/sample.txt.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
                assertThat(entry.getName()).contains("sample.txt");
            }
        }
    }

    @Test
    void jsonParseTest() throws Exception {
        Gson gson = new Gson();
        try (
                InputStream resource = cl.getResourceAsStream("example/glossary.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            assertThat(jsonObject.get("title").getAsString()).isEqualTo("example glossary");
            assertThat(jsonObject.get("gloss_div").getAsJsonObject().get("title").getAsString()).isEqualTo("S");
            assertThat(jsonObject.get("gloss_div").getAsJsonObject().get("flag").getAsBoolean()).isTrue();
        }
    }

    @Test
    void jsonParseImprovedTest() throws Exception {
        Gson gson = new Gson();
        try (
                InputStream resource = cl.getResourceAsStream("example/glossary.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            Glossary jsonObject = gson.fromJson(reader, Glossary.class);
            assertThat(jsonObject.title).isEqualTo("example glossary");
            assertThat(jsonObject.glossDiv.title).isEqualTo("S");
            assertThat(jsonObject.glossDiv.flag).isTrue();
        }
    }
}

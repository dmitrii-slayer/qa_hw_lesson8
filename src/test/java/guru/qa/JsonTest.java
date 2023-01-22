package guru.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.model.SuperheroSquad;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonTest {

    ClassLoader cLoader = JsonTest.class.getClassLoader();

    @Test
    void jsonTest() throws IOException {
        ObjectMapper objectMap = new ObjectMapper();
        try (
                InputStream resource = cLoader.getResourceAsStream("example/superhero_squad.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            SuperheroSquad squad = objectMap.readValue(reader, SuperheroSquad.class);
            assertThat(squad.squadName).isEqualTo("Super hero squad");
            assertThat(squad.homeTown).isEqualTo("Metro City");
            assertThat(squad.formed).isEqualTo(2016);
            assertThat(squad.secretBase).isEqualTo("Super tower");
            assertThat(squad.active).isEqualTo(true);

            assertThat(squad.members.get(0).name).isEqualTo("Molecule Man");
            assertThat(squad.members.get(0).age).isEqualTo(29);
            assertThat(squad.members.get(0).secretIdentity).isEqualTo("Dan Jukes");
            assertThat(squad.members.get(0).powers).contains("Radiation blast");
            assertThat(squad.members.get(0).powers.get(1)).isEqualTo("Turning tiny");

            assertThat(squad.members.get(1).name).isEqualTo("Madame Uppercut");
            assertThat(squad.members.get(1).age).isEqualTo(39);
            assertThat(squad.members.get(1).secretIdentity).isEqualTo("Jane Wilson");
            assertThat(squad.members.get(1).powers).contains("Damage resistance");
            assertThat(squad.members.get(1).powers.get(0)).isEqualTo("Million tonne punch");

            assertThat(squad.members.get(2).name).isEqualTo("Eternal Flame");
            assertThat(squad.members.get(2).age).isEqualTo(1000000);
            assertThat(squad.members.get(2).secretIdentity).isEqualTo("Unknown");
            assertThat(squad.members.get(2).powers).contains("Immortality");
            assertThat(squad.members.get(2).powers.get(3)).isEqualTo("Teleportation");
        }
    }
}

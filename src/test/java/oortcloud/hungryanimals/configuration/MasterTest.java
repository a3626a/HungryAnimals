package oortcloud.hungryanimals.configuration;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import oortcloud.hungryanimals.configuration.master.Master;
import oortcloud.hungryanimals.configuration.master.Node;
import oortcloud.hungryanimals.configuration.master.NodePath;
import oortcloud.hungryanimals.utils.Pair;
import oortcloud.hungryanimals.utils.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.junit.Assert.*;

public class MasterTest {

    private TemporaryFolder root;
    private Path config;
    @Before
    public void setUpForEntireClass() {
        root = new TemporaryFolder();
        try {
            root.create();

            config = root.newFolder("config").toPath();
            Path master = config.resolve( "master");
            Path difficulty = Files.createDirectories(master.resolve( "difficulty"));
            Path tempo = Files.createDirectories(master.resolve( "tempo"));
            Files.write(master.resolve("master.json"), Lists.newArrayList(
                    "{",
                    "  \"difficulty\": \"easy\",",
                    "  \"tempo\": \"slow\",",
                    "  \"custom\": []",
                    "}"
            ), StandardOpenOption.CREATE);
            Files.write(difficulty.resolve("easy.json"), Lists.newArrayList(
                    "[",
                    "  {",
                    "    \"domain\" : \"all\",",
                    "    \"pattern\" : \"b.json\",",
                    "    \"modifier\" : {",
                    "      \"field_to_add\" : {",
                    "        \"operation\" : \"+\",",
                    "        \"value\" : 1",
                    "      }",
                    "    }",
                    "  }",
                    "]"
            ), StandardOpenOption.CREATE);
            Files.write(difficulty.resolve("empty.json"), Lists.newArrayList(
                    "[]"
            ), StandardOpenOption.CREATE);
            Files.write(tempo.resolve("slow.json"), Lists.newArrayList(
                    "[]"
            ), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to prepare test environment, Jimfs virtual memory file system");
        }
    }

    @Test
    public void Master_get() throws IOException {
        Node node = new NodePath(config);
        Supplier<List<Pair<Predicate<R>, UnaryOperator<JsonElement>>>> modifiers = Master.get(node, "all");

        JsonObject tester = new JsonObject();
        tester.addProperty("field_to_add", 0);
        assertEquals(0, tester.getAsJsonPrimitive("field_to_add").getAsInt());
        modifiers.get().get(0).right.apply(tester);
        assertEquals(1, tester.getAsJsonPrimitive("field_to_add").getAsInt());
    }

    @Test
    public void Master_get_custom() throws IOException {
        Files.write(config.resolve( "master").resolve("master.json"), Lists.newArrayList(
                "{",
                "  \"difficulty\": \"empty\",",
                "  \"tempo\": \"slow\",",
                "  \"custom\": [",
                "    {",
                "      \"domain\" : \"all\",",
                "      \"pattern\" : \"b.json\",",
                "      \"modifier\" : {",
                "        \"field_to_add\" : {",
                "          \"operation\" : \"+\",",
                "          \"value\" : 2",
                "        }",
                "      }",
                "    }",
                "  ]",
                "}"
        ), StandardOpenOption.WRITE);

        Node node = new NodePath(config);
        Supplier<List<Pair<Predicate<R>, UnaryOperator<JsonElement>>>> modifiers = Master.get(node, "all");

        JsonObject tester = new JsonObject();
        tester.addProperty("field_to_add", 0);
        assertEquals(0, tester.getAsJsonPrimitive("field_to_add").getAsInt());
        modifiers.get().get(0).right.apply(tester);
        assertEquals(2, tester.getAsJsonPrimitive("field_to_add").getAsInt());
    }
}

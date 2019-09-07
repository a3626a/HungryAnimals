package oortcloud.hungryanimals.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import oortcloud.hungryanimals.configuration.master.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;

import oortcloud.hungryanimals.utils.R;

import static org.junit.Assert.*;

public class NodeTest {

    private TemporaryFolder root;

    @Before
    public void setUpForEntireClass() {
        root = new TemporaryFolder();
        try {
            root.create();
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to prepare test environment, Jimfs virtual memory file system");
        }
    }

    @Test
    public void NodeOverride_Build() throws IOException {
		Path directory1 = null;
		Path directory2 = null;

		directory1 = root.newFolder("1").toPath();
		Files.write(directory1.resolve("a.json"), Lists.newArrayList(
				"{",
				"\"field1\" : 1",
				"}"
		), StandardOpenOption.CREATE);
		Files.write(directory1.resolve("b.json"), Lists.newArrayList(
				"{",
				"\"field1\" : 2",
				"}"
		), StandardOpenOption.CREATE);

		directory2 = root.newFolder("2").toPath();
		Files.createDirectories(directory2);
		Files.write(directory2.resolve("a.json"), Lists.newArrayList(
				"{",
				"\"field1\" : 3",
				"}"
		), StandardOpenOption.CREATE);

        Map<R, JsonElement> map1 = new NodeOverride(new NodePath(directory1), new NodePath(directory2)).build();
        assertEquals(1, map1.get(R.get("a.json")).getAsJsonObject().get("field1").getAsInt());
        assertEquals(2, map1.get(R.get("b.json")).getAsJsonObject().get("field1").getAsInt());

        Map<R, JsonElement> map2 = new NodeOverride(new NodePath(directory2), new NodePath(directory1)).build();
        assertEquals(3, map2.get(R.get("a.json")).getAsJsonObject().get("field1").getAsInt());
        assertEquals(2, map2.get(R.get("b.json")).getAsJsonObject().get("field1").getAsInt());
    }

    @Test
    public void NodePath_Build() throws IOException {
		Path directory3 = null;
		Path directory4 = null;

		directory3 = root.newFolder("master").toPath();
		Files.createDirectories(directory3);
		Files.write(directory3.resolve("master.json"), Lists.newArrayList(
				"{",
				"\"field1\" : 4",
				"}"
		), StandardOpenOption.CREATE);

		directory4 = directory3.resolve("tempo");
		Files.createDirectories(directory4);
		Files.write(directory4.resolve("slow.json"), Lists.newArrayList(
				"{",
				"\"field1\" : 5",
				"}"
		), StandardOpenOption.CREATE);

		Map<R, JsonElement> map3 = new NodePath(directory3).build();
		assertEquals(
				4,
				map3
						.get(
								R.get("master.json")
						)
						.getAsJsonObject()
						.get("field1")
						.getAsInt()
		);
		assertEquals(
				5,
				map3
						.get(
								R.get("tempo", "slow.json")
						)
						.getAsJsonObject()
						.get("field1")
						.getAsInt()
		);
        Map<R, JsonElement> map4 = new NodePath(directory3, directory4).build();
        assertNull(map4.get(R.get("master.json")));
        assertEquals(
                5,
				map4
                        .get(
                                R.get("tempo", "slow.json")
                        )
                        .getAsJsonObject()
                        .get("field1")
                        .getAsInt()
		);
    }
}

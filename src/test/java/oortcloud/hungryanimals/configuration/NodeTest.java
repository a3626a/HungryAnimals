package oortcloud.hungryanimals.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;

import oortcloud.hungryanimals.configuration.master.NodeOverride;
import oortcloud.hungryanimals.configuration.master.NodePath;
import oortcloud.hungryanimals.utils.R;

public class NodeTest {

	TemporaryFolder root;
	Path directory1 = null;
	Path directory2 = null;

	@Before
	public void setUpForEntireClass() {
		root = new TemporaryFolder();
		try {
			root.create();
			
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
		} catch (IOException e) {
			e.printStackTrace();
			fail("failed to prepare test environment, Jimfs virtual memory file system");
		}
	}
	
	@Test
	public void NodeOverride_Build() {
		Map<R, JsonElement> map1 = new NodeOverride(new NodePath(directory1), new NodePath(directory2)).build();
		assertEquals(1, map1.get(R.get("a.json")).getAsJsonObject().get("field1").getAsInt());
		assertEquals(2, map1.get(R.get("b.json")).getAsJsonObject().get("field1").getAsInt());
		
		Map<R, JsonElement> map2 = new NodeOverride(new NodePath(directory2), new NodePath(directory1)).build();
		assertEquals(3, map2.get(R.get("a.json")).getAsJsonObject().get("field1").getAsInt());
		assertEquals(2, map2.get(R.get("b.json")).getAsJsonObject().get("field1").getAsInt());
	}
	
}

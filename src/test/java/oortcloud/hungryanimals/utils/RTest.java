package oortcloud.hungryanimals.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RTest {

	@Test
	public void R_equals() {
		assertEquals(R.get("a", "b"), R.get("a", "b"));
		assertEquals(R.get("a", "b"), R.get(Paths.get("a", "b")));
		assertEquals(R.get("a", ""), R.get("a"));
	}
	
	@Test
	public void R_getParent() {
		assertEquals(R.get("a", "b").getParent(), R.get("a"));
		assertNull(R.get("a").getParent());
	}
	
	@Test
	public void R_HashMap() {
		Map<R, Integer> map = new HashMap<>();
		map.put(R.get("a", "c"), 0);
		map.put(R.get("b"), 1);
		map.put(R.get("a", "d"), 2);
		map.put(R.get("ac"), 3);
		
		assertEquals(map.get(R.get("a", "c")).intValue(), 0);
		assertEquals(map.get(R.get("b")).intValue(), 1);
		assertEquals(map.get(R.get("a", "d")).intValue(), 2);
		assertEquals(map.get(R.get("ac")).intValue(), 3);
		assertNull(map.get(R.get("ad")));
	}
}

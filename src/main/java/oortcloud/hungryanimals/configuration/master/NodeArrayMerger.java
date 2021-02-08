package oortcloud.hungryanimals.configuration.master;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import oortcloud.hungryanimals.utils.R;

import java.util.Map;

public class NodeArrayMerger extends Node {

	private Node parent;
	private String path1;
	private String path2;
	private String dest;

	public NodeArrayMerger() {
		this(null, null, null, null);
	}

	public NodeArrayMerger(Node parent, String path1, String path2, String dest) {
		this.parent = parent;
		this.path1 = path1;
		this.path2 = path2;
		this.dest = dest;
	}

	@Override
	public Map<R, JsonElement> build() {
		Map<R, JsonElement> built = parent.build();

		R key1 = R.get(path1);
		R key2 = R.get(path2);

		JsonElement jsonEle1 = built.get(key1);
		JsonElement jsonEle2 = built.get(key2);

		JsonArray jsonArr = new JsonArray();

		if (jsonEle1 != null && jsonEle1.isJsonArray()) {
			JsonArray jsonArr1 = jsonEle1.getAsJsonArray();
			if (jsonArr1 != null) {
				jsonArr.addAll(jsonArr1);
			}
		}
		if (jsonEle2 != null && jsonEle2.isJsonArray()) {
			JsonArray jsonArr2 = jsonEle2.getAsJsonArray();
			if (jsonArr2 != null) {
				jsonArr.addAll(jsonArr2);
			}
		}

		built.remove(key1);
		built.remove(key2);
		built.put(R.get(dest), jsonArr);

		return built;
	}
}

package oortcloud.hungryanimals.configuration.master;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

public class Selector {

	private List<JsonElement> selectees;

	private Selector() {
		this.selectees = new ArrayList<>();
	}

	public Selector get(String key) {
		if (selectees.size() > 0) {
			if (selectees.get(0).isJsonArray()) {
				Selector ret = new Selector();
				for (JsonElement i : selectees) {
					if (i.isJsonArray()) {
						for (JsonElement j : i.getAsJsonArray()) {
							ret.selectees.add(j);
						}
					} else {
						// TODO ERROR un-unified type, Json Array expected
						return null;
					}
				}
				return ret.get(key);
			} else if (selectees.get(0).isJsonObject()) {
				Selector ret = new Selector();
				for (JsonElement i : selectees) {
					if (i.isJsonObject()) {
						ret.selectees.add(i.getAsJsonObject().get(key));
					} else {
						// TODO ERROR un-unified type, Json Object expected
						return null;
					}
				}
				return ret;
			} else {
				// TODO ERROR not json Array or json Object
				return null;
			}
		} else {
			// TODO ERROR no selectees
			return null;
		}
	}

	public List<JsonElement> build() {
		return selectees;
	}

}

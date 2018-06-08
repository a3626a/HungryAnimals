package oortcloud.hungryanimals.configuration.master;

public class SelectorDomain {

	private String domain;
	
	public SelectorDomain(String domain) {
		this.domain = domain;
	}
	
	public Selector get(String key) {
		if (domain.equals("default")) {
			return null;
		} else if (domain.equals("custom")) {
			// TODO not yet :)
			return null;
		} else if (domain.equals("all")) {
			// TODO not yet :)
			return null;
		} else {
			// TODO unsupported domain name
			return null;
		}
	}
}

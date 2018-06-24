package oortcloud.hungryanimals.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nullable;

public class R {

	private String r;
	private static String SEPERATOR = "/";

	private R(String r) {
		this.r = r ;
	}
	
	public static R get(Path key) {
		if (!key.getFileSystem().getSeparator().equals(SEPERATOR)) {
			return new R(key.toString().replace(key.getFileSystem().getSeparator(), SEPERATOR));
		} else {
			return new R(key.toString());
		}
	}
	
	public static R get(String arg0, String... arg1) {
		return R.get(Paths.get(arg0, arg1));
	}
	
	@Override
	public int hashCode() {
		return r.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof R) {
			return r.equals(((R)obj).r);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return r;
	}
	
	@Nullable
	public R getParent() {
		if (!r.contains(SEPERATOR)) {
			return null;
		} else {
			return new R(r.substring(0, r.lastIndexOf(SEPERATOR)));
		}
	}
	
}

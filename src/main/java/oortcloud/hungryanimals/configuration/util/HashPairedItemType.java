package oortcloud.hungryanimals.configuration.util;


public class HashPairedItemType {

	private HashItemType left;
	private HashItemType right;
	
	public HashPairedItemType(HashItemType l, HashItemType r) {
		left=l;
		right=r;
	}
	
	public HashItemType getLeft() {
		return left;
	}
	
	public HashItemType getRight() {
		return right;
	}
	
	@Override
	public boolean equals(Object arg) {
		
		if (arg instanceof HashPairedItemType) {
			
			HashPairedItemType obj = (HashPairedItemType)arg;
			
			if (this == obj) {
				return true;
			} else {
				return (this.left.equals(obj.left)&&this.right.equals(obj.right))||(this.left.equals(obj.right)&&this.right.equals(obj.left));
			}

		} else {
			return false;
		}
		
	}
	
	@Override
	public int hashCode() {
		return left.hashCode() + right.hashCode();
	}
	
}

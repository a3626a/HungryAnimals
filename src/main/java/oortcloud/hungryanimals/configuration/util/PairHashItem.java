package oortcloud.hungryanimals.configuration.util;


public class PairHashItem {

	private HashItem left;
	private HashItem right;
	
	public PairHashItem(HashItem l, HashItem r) {
		left=l;
		right=r;
	}
	
	@Override
	public boolean equals(Object arg) {
		
		if (arg instanceof PairHashItem) {
			
			PairHashItem obj = (PairHashItem)arg;
			
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

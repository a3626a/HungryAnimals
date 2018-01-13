package oortcloud.hungryanimals.utils.graph;

import java.util.LinkedList;

public class  Graph<T> {

	public LinkedList<Vertex<T>> vertices;
	
	public Graph() {
		vertices = new LinkedList<>();
	}
	
}

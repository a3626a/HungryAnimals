package oortcloud.hungryanimals.utils.graph;

import java.util.LinkedList;

public class Vertex<T> {

	public LinkedList<Vertex<T>> parents;
	public LinkedList<Vertex<T>> childs;
	
	public T value;
	
	public Vertex(T value) {
		parents = new LinkedList<>();
		childs = new LinkedList<>();
		this.value = value;
	}
	
}

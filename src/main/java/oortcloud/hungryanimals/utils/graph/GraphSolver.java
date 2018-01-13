package oortcloud.hungryanimals.utils.graph;

import java.util.LinkedList;
import java.util.List;

public class GraphSolver {

	public static <T> List<Vertex<T>> sortTopological(Graph<T> graph) {
		List<Vertex<T>> sorted = new LinkedList<Vertex<T>>();
		List<Vertex<T>> sources = getSources(graph);
		while (!sources.isEmpty()) {
			Vertex<T> n = sources.remove(0);
			sorted.add(n);
			for (Vertex<T> m : n.childs) {
				m.parents.remove(n);
				if (m.parents.size() == 0) {
					sources.add(m);
				}
			}
		}
		return sorted;
	}
	
	public static <T> List<Vertex<T>> getSources(Graph<T> graph) {
		List<Vertex<T>> sources = new LinkedList<>();
		for (Vertex<T> v : graph.vertices) {
			if (v.parents.size() == 0)
				sources.add(v);
		}
		return sources;
	}
	
}

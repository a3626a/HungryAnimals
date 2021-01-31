package oortcloud.hungryanimals.entities.ai.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import oortcloud.hungryanimals.utils.graph.Graph;
import oortcloud.hungryanimals.utils.graph.GraphSolver;
import oortcloud.hungryanimals.utils.graph.Vertex;

public class AIContainerTarget extends AIContainerTask {

	@Override
	public void registerAI(MobEntity entity) {
		if (removeAll) {
			entity.targetTasks.taskEntries.clear();
		} else {
			LinkedList<Goal> removeEntries = new LinkedList<Goal>();
			for (EntityAITaskEntry i : entity.targetTasks.taskEntries) {
				for (IAIRemover j : toRemove) {
					if (j.matches(i)) {
						removeEntries.add(i.action);
					}
				}
			}
			for (Goal i : removeEntries) {
				entity.targetTasks.removeTask(i);
			}
		}

		List<Goal> aibases = new ArrayList<Goal>();

		// Construct aibases from entity's tasks
		List<EntityAITaskEntry> aitaskentries = Lists.newArrayList(entity.targetTasks.taskEntries);
		aitaskentries.sort(new Comparator<EntityAITaskEntry>() {
			@Override
			public int compare(EntityAITaskEntry o1, EntityAITaskEntry o2) {
				return o1.priority - o2.priority;
			}
		});
		for (EntityAITaskEntry i : aitaskentries) {
			aibases.add(i.action);
		}
		entity.targetTasks.taskEntries.clear();

		Graph<Goal> graph = new Graph<Goal>();
		Vertex<Goal> prev = null;

		for (Goal i : aibases) {
			Vertex<Goal> curr = new Vertex<Goal>(i);
			graph.vertices.add(curr);
			if (prev != null) {
				prev.childs.add(curr);
				curr.parents.add(prev);
			}
			prev = curr;
		}

		for (AIFactoryGraph i : factoriesGraph) {
			i.addVertex(graph, entity);
		}
		for (AIFactoryGraph i : factoriesGraph) {
			i.addEdge(graph);
		}
		
		List<Vertex<Goal>> sortedVertex = GraphSolver.sortTopological(graph);
		List<Goal> sortedAI = sortedVertex.stream().map((vertex) -> vertex.value).collect(Collectors.toList());
		
		sortedAI.addAll(0, factoriesFirst.stream().map((factory)->factory.apply(entity)).collect(Collectors.toList()));
		sortedAI.addAll(factoriesLast.stream().map((factory)->factory.apply(entity)).collect(Collectors.toList()));
		
		int cnt = 0;
		for (Goal i : sortedAI) {
			entity.targetTasks.addTask(cnt++, i);
		}
	}
	
}

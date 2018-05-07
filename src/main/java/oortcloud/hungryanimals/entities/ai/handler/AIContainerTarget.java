package oortcloud.hungryanimals.entities.ai.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.utils.graph.Graph;
import oortcloud.hungryanimals.utils.graph.GraphSolver;
import oortcloud.hungryanimals.utils.graph.Vertex;

public class AIContainerTarget extends AIContainerTask {

	@Override
	public void registerAI(EntityAnimal entity) {
		if (removeAll) {
			entity.targetTasks.taskEntries.clear();
		} else {
			LinkedList<EntityAIBase> removeEntries = new LinkedList<EntityAIBase>();
			for (EntityAITaskEntry i : entity.targetTasks.taskEntries) {
				for (IAIRemover j : toRemove) {
					if (j.matches(i)) {
						removeEntries.add(i.action);
					}
				}
			}
			for (EntityAIBase i : removeEntries) {
				entity.targetTasks.removeTask(i);
			}
		}

		List<EntityAIBase> aibases = new ArrayList<EntityAIBase>();

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

		Graph<EntityAIBase> graph = new Graph<EntityAIBase>();
		Vertex<EntityAIBase> prev = null;

		for (EntityAIBase i : aibases) {
			Vertex<EntityAIBase> curr = new Vertex<EntityAIBase>(i);
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
		
		List<Vertex<EntityAIBase>> sortedVertex = GraphSolver.sortTopological(graph);
		List<EntityAIBase> sortedAI = sortedVertex.stream().map((vertex) -> vertex.value).collect(Collectors.toList());
		
		sortedAI.addAll(0, factoriesFirst.stream().map((factory)->factory.apply(entity)).collect(Collectors.toList()));
		sortedAI.addAll(factoriesLast.stream().map((factory)->factory.apply(entity)).collect(Collectors.toList()));
		
		int cnt = 0;
		for (EntityAIBase i : sortedAI) {
			entity.targetTasks.addTask(cnt++, i);
		}
	}
	
}

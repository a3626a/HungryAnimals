package oortcloud.hungryanimals.entities.ai.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import oortcloud.hungryanimals.utils.graph.Graph;
import oortcloud.hungryanimals.utils.graph.GraphSolver;
import oortcloud.hungryanimals.utils.graph.Vertex;

public class AIContainerTarget extends AIContainerTask {

	@Override
	public void registerAI(MobEntity entity) {
		if (removeAll) {
			entity.targetSelector.goals.forEach(
					prioritizedGoal -> {
						entity.targetSelector.removeGoal(prioritizedGoal.getGoal());
					}
			);
		} else {
			LinkedList<Goal> removeEntries = new LinkedList<>();
			for (PrioritizedGoal i : entity.targetSelector.goals) {
				for (IAIRemover j : toRemove) {
					if (j.matches(i)) {
						removeEntries.add(i.getGoal());
					}
				}
			}
			for (Goal i : removeEntries) {
				entity.targetSelector.removeGoal(i);
			}
		}

		List<Goal> aibases = new ArrayList<>();

		// Construct aibases from entity's tasks
		List<PrioritizedGoal> aitaskentries = Lists.newArrayList(entity.targetSelector.goals);
		aitaskentries.sort(Comparator.comparingInt(PrioritizedGoal::getPriority));
		for (PrioritizedGoal i : aitaskentries) {
			aibases.add(i.getGoal());
		}
		entity.targetSelector.goals.forEach(
				prioritizedGoal -> {
					entity.targetSelector.removeGoal(prioritizedGoal.getGoal());
				}
		);

		Graph<Goal> graph = new Graph<>();
		Vertex<Goal> prev = null;

		for (Goal i : aibases) {
			Vertex<Goal> curr = new Vertex<>(i);
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
			entity.targetSelector.addGoal(cnt++, i);
		}
	}
	
}

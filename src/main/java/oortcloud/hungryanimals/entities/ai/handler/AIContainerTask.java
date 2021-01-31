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

public class AIContainerTask implements IAIContainer<MobEntity> {

	protected LinkedList<AIFactoryGraph> factoriesGraph;
	protected LinkedList<AIFactory> factoriesFirst;
	protected LinkedList<AIFactory> factoriesLast;

	protected List<IAIRemover> toRemove;
	protected boolean removeAll;

	protected List<Class<? extends Goal>> prior;
	protected List<Class<? extends Goal>> posterior;

	public AIContainerTask() {
		this(null);
	}

	public AIContainerTask(AIContainerTask parent) {
		this.factoriesGraph = new LinkedList<AIFactoryGraph>();
		this.factoriesFirst = new LinkedList<AIFactory>();
		this.factoriesLast = new LinkedList<AIFactory>();
		this.toRemove = new LinkedList<IAIRemover>();

		if (parent != null) {
			this.removeAll = parent.removeAll;
			this.factoriesGraph.addAll(parent.factoriesGraph);
			this.factoriesFirst.addAll(parent.factoriesFirst);
			this.factoriesLast.addAll(parent.factoriesLast);
			this.toRemove.addAll(parent.toRemove);
		}
	}

	@Override
	public void registerAI(MobEntity entity) {
		if (removeAll) {
			entity.goalSelector.goals.forEach(
					prioritizedGoal -> {
						entity.goalSelector.removeGoal(prioritizedGoal.getGoal());
					}
			);
		} else {
			LinkedList<Goal> removeEntries = new LinkedList<>();
			for (PrioritizedGoal i : entity.goalSelector.goals) {
				for (IAIRemover j : toRemove) {
					if (j.matches(i)) {
						removeEntries.add(i.getGoal());
					}
				}
			}
			for (Goal i : removeEntries) {
				entity.goalSelector.removeGoal(i);
			}
		}

		List<Goal> aibases = new ArrayList<>();

		// Construct aibases from entity's tasks
		List<PrioritizedGoal> aitaskentries = Lists.newArrayList(entity.goalSelector.goals);
		aitaskentries.sort(new Comparator<PrioritizedGoal>() {
			@Override
			public int compare(PrioritizedGoal o1, PrioritizedGoal o2) {
				return o1.getPriority() - o2.getPriority();
			}
		});
		for (PrioritizedGoal i : aitaskentries) {
			aibases.add(i.getGoal());
		}
		entity.goalSelector.goals.forEach(
				prioritizedGoal -> {
					entity.goalSelector.removeGoal(prioritizedGoal.getGoal());
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

		sortedAI.addAll(0, factoriesFirst.stream().map((factory) -> factory.apply(entity)).collect(Collectors.toList()));
		sortedAI.addAll(factoriesLast.stream().map((factory) -> factory.apply(entity)).collect(Collectors.toList()));
		
		int cnt = 0;
		for (Goal i : sortedAI) {
			entity.goalSelector.addGoal(cnt++, i);
		}
	}

	public void putFirst(AIFactory target) {
		factoriesFirst.add(target);
	}

	public void putLast(AIFactory target) {
		factoriesLast.add(target);
	}

	public void put(AIFactory target) {
		factoriesGraph.add(new AIFactoryGraph(target, getPrior(), getPosterior()));
		prior = null;
		posterior = null;
	}

	public AIContainerTask before(Class<? extends Goal> target) {
		getPrior().add(target);
		return this;
	}

	public AIContainerTask before(List<Class<? extends Goal>> targets) {
		getPrior().addAll(targets);
		return this;
	}

	public AIContainerTask after(Class<? extends Goal> target) {
		getPosterior().add(target);
		return this;
	}

	public AIContainerTask after(List<Class<? extends Goal>> targets) {
		getPosterior().addAll(targets);
		return this;
	}

	private List<Class<? extends Goal>> getPrior() {
		if (prior == null)
			prior = new ArrayList<Class<? extends Goal>>();
		return prior;
	}

	private List<Class<? extends Goal>> getPosterior() {
		if (posterior == null)
			posterior = new ArrayList<Class<? extends Goal>>();
		return posterior;
	}

	public void remove(Class<? extends Goal> target) {
		toRemove.add(new AIRemoverByClass(target));
	}

	public void remove(IAIRemover remover) {
		toRemove.add(remover);
	}

	public void remove(List<Class<? extends Goal>> target) {
		toRemove.addAll(target.stream().map((i) -> new AIRemoverByClass(i)).collect(Collectors.toList()));
	}

	public void removeAll() {
		removeAll = true;
	}

	public static interface IAIRemover {
		public boolean matches(PrioritizedGoal goal);
	}

	public static class AIRemoverByClass implements IAIRemover {
		private Class<? extends Goal> target;

		public AIRemoverByClass(Class<? extends Goal> target) {
			this.target = target;
		}

		@Override
		public boolean matches(PrioritizedGoal goal) {
			return goal.getGoal().getClass() == target;
		}
	}

	public static class AIRemoverIsInstance implements IAIRemover {
		private Class<? extends Goal> target;

		public AIRemoverIsInstance(Class<? extends Goal> target) {
			this.target = target;
		}

		@Override
		public boolean matches(PrioritizedGoal goal) {
			return target.isInstance(goal.getGoal());
		}
	}

	protected static class AIFactoryGraph {
		
		// THIS IS NOT THREAD SAFE!
		
		public Vertex<Goal> v;
		public AIFactory aiFactory;
		public List<Class<? extends Goal>> prior;
		public List<Class<? extends Goal>> posterior;

		public AIFactoryGraph(AIFactory aiFactory, List<Class<? extends Goal>> prior, List<Class<? extends Goal>> posterior) {
			this.aiFactory = aiFactory;
			this.prior = prior;
			this.posterior = posterior;
		}

		public Vertex<Goal> addVertex(Graph<Goal> g, MobEntity entity) {
			this.v = new Vertex<Goal>(aiFactory.apply(entity));
			g.vertices.add(v);
			return v;
		}

		public void addEdge(Graph<Goal> g) {
			for (Vertex<Goal> w : g.vertices) {
				if (in(w, prior)) {
					v.childs.add(w);
					w.parents.add(v);
				}
				if (in(w, posterior)) {
					v.parents.add(w);
					w.childs.add(v);
				}
			}
		}

		private boolean in(Vertex<Goal> v, List<Class<? extends Goal>> list) {
			for (Class<? extends Goal> i : list) {
				if (v.value.getClass() == i) {
					return true;
				}
			}
			return false;
		}
	}
}

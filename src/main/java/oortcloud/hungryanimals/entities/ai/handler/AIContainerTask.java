package oortcloud.hungryanimals.entities.ai.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import oortcloud.hungryanimals.utils.graph.Graph;
import oortcloud.hungryanimals.utils.graph.GraphSolver;
import oortcloud.hungryanimals.utils.graph.Vertex;

public class AIContainerTask implements IAIContainer<EntityLiving> {

	protected LinkedList<AIFactoryGraph> factoriesGraph;
	protected LinkedList<AIFactory> factoriesFirst;
	protected LinkedList<AIFactory> factoriesLast;

	protected List<IAIRemover> toRemove;
	protected boolean removeAll;

	protected List<Class<? extends EntityAIBase>> prior;
	protected List<Class<? extends EntityAIBase>> posterior;

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
	public void registerAI(EntityLiving entity) {
		if (removeAll) {
			entity.tasks.taskEntries.clear();
		} else {
			LinkedList<EntityAIBase> removeEntries = new LinkedList<EntityAIBase>();
			for (EntityAITaskEntry i : entity.tasks.taskEntries) {
				for (IAIRemover j : toRemove) {
					if (j.matches(i)) {
						removeEntries.add(i.action);
					}
				}
			}
			for (EntityAIBase i : removeEntries) {
				entity.tasks.removeTask(i);
			}
		}

		List<EntityAIBase> aibases = new ArrayList<EntityAIBase>();

		// Construct aibases from entity's tasks
		List<EntityAITaskEntry> aitaskentries = Lists.newArrayList(entity.tasks.taskEntries);
		aitaskentries.sort(new Comparator<EntityAITaskEntry>() {
			@Override
			public int compare(EntityAITaskEntry o1, EntityAITaskEntry o2) {
				return o1.priority - o2.priority;
			}
		});
		for (EntityAITaskEntry i : aitaskentries) {
			aibases.add(i.action);
		}
		entity.tasks.taskEntries.clear();

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

		sortedAI.addAll(0, factoriesFirst.stream().map((factory) -> factory.apply(entity)).collect(Collectors.toList()));
		sortedAI.addAll(factoriesLast.stream().map((factory) -> factory.apply(entity)).collect(Collectors.toList()));
		
		int cnt = 0;
		for (EntityAIBase i : sortedAI) {
			entity.tasks.addTask(cnt++, i);
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

	public AIContainerTask before(Class<? extends EntityAIBase> target) {
		getPrior().add(target);
		return this;
	}

	public AIContainerTask before(List<Class<? extends EntityAIBase>> targets) {
		getPrior().addAll(targets);
		return this;
	}

	public AIContainerTask after(Class<? extends EntityAIBase> target) {
		getPosterior().add(target);
		return this;
	}

	public AIContainerTask after(List<Class<? extends EntityAIBase>> targets) {
		getPosterior().addAll(targets);
		return this;
	}

	private List<Class<? extends EntityAIBase>> getPrior() {
		if (prior == null)
			prior = new ArrayList<Class<? extends EntityAIBase>>();
		return prior;
	}

	private List<Class<? extends EntityAIBase>> getPosterior() {
		if (posterior == null)
			posterior = new ArrayList<Class<? extends EntityAIBase>>();
		return posterior;
	}

	public void remove(Class<? extends EntityAIBase> target) {
		toRemove.add(new AIRemoverByClass(target));
	}

	public void remove(IAIRemover remover) {
		toRemove.add(remover);
	}

	public void remove(List<Class<? extends EntityAIBase>> target) {
		toRemove.addAll(target.stream().map((i) -> new AIRemoverByClass(i)).collect(Collectors.toList()));
	}

	public void removeAll() {
		removeAll = true;
	}

	public static interface IAIRemover {
		public boolean matches(EntityAITaskEntry entry);
	}

	public static class AIRemoverByClass implements IAIRemover {
		private Class<? extends EntityAIBase> target;

		public AIRemoverByClass(Class<? extends EntityAIBase> target) {
			this.target = target;
		}

		@Override
		public boolean matches(EntityAITaskEntry entry) {
			return entry.action.getClass() == target;
		}
	}

	public static class AIRemoverIsInstance implements IAIRemover {
		private Class<? extends EntityAIBase> target;

		public AIRemoverIsInstance(Class<? extends EntityAIBase> target) {
			this.target = target;
		}

		@Override
		public boolean matches(EntityAITaskEntry entry) {
			return target.isInstance(entry.action);
		}
	}

	protected static class AIFactoryGraph {
		
		// THIS IS NOT THREAD SAFE!
		
		public Vertex<EntityAIBase> v;
		public AIFactory aiFactory;
		public List<Class<? extends EntityAIBase>> prior;
		public List<Class<? extends EntityAIBase>> posterior;

		public AIFactoryGraph(AIFactory aiFactory, List<Class<? extends EntityAIBase>> prior, List<Class<? extends EntityAIBase>> posterior) {
			this.aiFactory = aiFactory;
			this.prior = prior;
			this.posterior = posterior;
		}

		public Vertex<EntityAIBase> addVertex(Graph<EntityAIBase> g, EntityLiving entity) {
			this.v = new Vertex<EntityAIBase>(aiFactory.apply(entity));
			g.vertices.add(v);
			return v;
		}

		public void addEdge(Graph<EntityAIBase> g) {
			for (Vertex<EntityAIBase> w : g.vertices) {
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

		private boolean in(Vertex<EntityAIBase> v, List<Class<? extends EntityAIBase>> list) {
			for (Class<? extends EntityAIBase> i : list) {
				if (v.value.getClass() == i) {
					return true;
				}
			}
			return false;
		}
	}
}

package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityAnimal;

public class AIContainer implements IAIContainer<EntityAnimal> {

	private LinkedList<IAIPlacer> ais;
	private int start;

	private List<Class<? extends EntityAIBase>> toRemove;
	private boolean removeAll;

	private List<Class<? extends EntityAIBase>> prior;
	private List<Class<? extends EntityAIBase>> posterior;

	public AIContainer() {
		this(0);
	}

	public AIContainer(int start) {
		this(start, null);
	}

	public AIContainer(AIContainer parent) {
		this(0, parent);
	}
	
	public AIContainer(int start, AIContainer parent) {
		this.ais = new LinkedList<IAIPlacer>();
		this.toRemove = new LinkedList<Class<? extends EntityAIBase>>();

		if (parent != null) {
			this.start = parent.start;
			this.removeAll = parent.removeAll;
			this.ais.addAll(parent.ais);
			this.toRemove.addAll(parent.toRemove);
		}
		
		this.start = start;
	}

	@Override
	public void registerAI(EntityAnimal entity) {
		if (removeAll) {
			entity.tasks.taskEntries.clear();
		} else {
			LinkedList<EntityAIBase> removeEntries = new LinkedList<EntityAIBase>();
			for (EntityAITaskEntry i : entity.tasks.taskEntries) {
				for (Class<? extends EntityAIBase> j : toRemove) {
					if (i.action.getClass() == j) {
						removeEntries.add(i.action);
					}
				}
			}
			for (EntityAIBase i : removeEntries) {
				entity.tasks.removeTask(i);
			}
		}

		List<EntityAIBase> aibases = new ArrayList<EntityAIBase>();
		for (IAIPlacer i : ais) {
			i.add(aibases, entity);
		}

		int cnt = start;
		for (EntityAIBase i : aibases) {
			entity.tasks.addTask(cnt++, i);
		}
	}

	public void putFirst(AIFactory target) {
		ais.add(new AIPlacerFirst(target));
	}

	public void putLast(AIFactory target) {
		ais.add(new AIPlacerLast(target));
	}

	public void put(AIFactory target) {
		ais.add(new AIPlacerPriority(target, getPrior(), getPosterior()));
		prior = null;
		posterior = null;
	}

	public AIContainer priorTo(Class<? extends EntityAIBase> target) {
		getPrior().add(target);
		return this;
	}

	public AIContainer priorTo(List<Class<? extends EntityAIBase>> targets) {
		getPrior().addAll(targets);
		return this;
	}

	public AIContainer posteriorTo(Class<? extends EntityAIBase> target) {
		getPosterior().add(target);
		return this;
	}

	public AIContainer posteriorTo(List<Class<? extends EntityAIBase>> targets) {
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
		toRemove.add(target);
	}

	public void remove(List<Class<? extends EntityAIBase>> target) {
		toRemove.addAll(target);
	}

	public void removeAll() {
		removeAll = true;
	}

	@FunctionalInterface
	public static interface AIFactory {
		public EntityAIBase apply(EntityAnimal entity);
	}

	private static interface IAIPlacer {
		public boolean add(List<EntityAIBase> list, EntityAnimal entity);
	}

	private static class AIPlacerFirst implements IAIPlacer {
		public AIFactory aiFactory;

		public AIPlacerFirst(AIFactory aiFactory) {
			this.aiFactory = aiFactory;
		}

		@Override
		public boolean add(List<EntityAIBase> list, EntityAnimal entity) {
			list.add(0, aiFactory.apply(entity));
			return true;
		}

	}

	private static class AIPlacerLast implements IAIPlacer {
		public AIFactory aiFactory;

		public AIPlacerLast(AIFactory aiFactory) {
			this.aiFactory = aiFactory;
		}

		@Override
		public boolean add(List<EntityAIBase> list, EntityAnimal entity) {
			list.add(aiFactory.apply(entity));
			return true;
		}

	}

	private static class AIPlacerPriority implements IAIPlacer {

		public AIFactory aiFactory;
		public List<Class<? extends EntityAIBase>> prior;
		public List<Class<? extends EntityAIBase>> posterior;

		public AIPlacerPriority(AIFactory aiFactory, List<Class<? extends EntityAIBase>> prior,
				List<Class<? extends EntityAIBase>> posterior) {
			this.aiFactory = aiFactory;
			this.prior = prior;
			this.posterior = posterior;
		}

		@Override
		public boolean add(List<EntityAIBase> list, EntityAnimal entity) {
			int maxIndex = list.size();
			int minIndex = 0;

			for (Class<? extends EntityAIBase> i : prior) {
				if (contains(list, i)) {
					minIndex = Math.max(minIndex, indexOf(list, i));
				}
			}

			for (Class<? extends EntityAIBase> i : posterior) {
				if (contains(list, i)) {
					maxIndex = Math.min(minIndex, indexOf(list, i));
				}
			}

			if (minIndex < maxIndex) {
				list.add(maxIndex, aiFactory.apply(entity));
				return true;
			} else {
				return false;
			}
		}

		private boolean contains(List<EntityAIBase> list, Class<? extends EntityAIBase> AIClass) {
			for (EntityAIBase iAIBase : list) {
				if (iAIBase.getClass() == AIClass)
					return true;
			}
			return false;
		}

		private int indexOf(List<EntityAIBase> list, Class<? extends EntityAIBase> aiClass) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getClass() == aiClass) {
					return i;
				}
			}
			return -1;
		}
	}
}

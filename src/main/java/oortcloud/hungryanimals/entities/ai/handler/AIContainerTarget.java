package oortcloud.hungryanimals.entities.ai.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityAnimal;

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
		
		for (IAIPlacer i : ais) {
			i.add(aibases, entity);
		}

		int cnt = 0;
		for (EntityAIBase i : aibases) {
			entity.targetTasks.addTask(cnt++, i);
		}
	}
	
}

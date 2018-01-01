package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityAnimal;

public class AIContainerTarget extends AIContainer {

	@Override
	public void registerAI(EntityAnimal entity) {
		if (removeAll) {
			entity.targetTasks.taskEntries.clear();
		} else {
			LinkedList<EntityAIBase> removeEntries = new LinkedList<EntityAIBase>();
			for (EntityAITaskEntry i : entity.targetTasks.taskEntries) {
				for (Class<? extends EntityAIBase> j : toRemove) {
					if (i.action.getClass() == j) {
						removeEntries.add(i.action);
					}
				}
			}
			for (EntityAIBase i : removeEntries) {
				entity.targetTasks.removeTask(i);
			}
		}

		List<EntityAIBase> aibases = new ArrayList<EntityAIBase>();
		for (IAIPlacer i : ais) {
			i.add(aibases, entity);
		}

		int cnt = start;
		for (EntityAIBase i : aibases) {
			entity.targetTasks.addTask(cnt++, i);
		}
	}
	
}

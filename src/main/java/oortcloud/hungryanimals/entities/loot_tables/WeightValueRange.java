package oortcloud.hungryanimals.entities.loot_tables;

import java.util.Random;

import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public class WeightValueRange
{
    private final float weight_per_meat;

    public WeightValueRange(float weight_per_meat)
    {
        this.weight_per_meat = weight_per_meat;
    }

    public float getWeightPerMeat()
    {
        return this.weight_per_meat;
    }

    public int generateInt(ICapabilityHungryAnimal cap, Random rand)
    {
    	float weight_meat = (float) (cap.getWeight()-cap.getNormalWeight()*0.5);
    	if (weight_meat > 0) {
    		float num_meat = weight_meat/weight_per_meat;
    		int num_meat_base = (int)num_meat;
    		float num_meat_prob = num_meat - num_meat_base;
    		if (rand.nextFloat() < num_meat_prob) {
    			return num_meat_base+1;
    		} else {
    			return num_meat_base;
    		}
    	}
    	return 0;
    }

    public float generateFloat(ICapabilityHungryAnimal cap)
    {
    	float weight_meat = (float) (cap.getWeight()-cap.getNormalWeight()*0.5);
    	if (weight_meat > 0) {
    		return weight_meat/weight_per_meat;
    	}
    	return 0.0F;
    }

    public boolean isInRange(int weight_per_meat)
    {
        return 0 < weight_per_meat;
    }

}

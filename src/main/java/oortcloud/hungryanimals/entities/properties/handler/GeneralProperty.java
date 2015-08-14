package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;
import java.util.HashMap;

import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;

public class GeneralProperty {

	public double default_hunger_max;
	public double default_hunger_bmr;
	public HashMap<HashItem, Double> default_hunger_food = new HashMap<HashItem, Double>();
	public HashMap<HashBlock, Double> default_hunger_block = new HashMap<HashBlock, Double>();
	public ArrayList<DropMeat> default_drop_meat = new ArrayList<DropMeat>();
	public ArrayList<DropRandom> default_drop_random = new ArrayList<DropRandom>();
	public ArrayList<DropRare> default_drop_rare = new ArrayList<DropRare>();
	public double default_courtship_hunger;
	public double default_courtship_probability;
	public double default_courtship_hungerCondition;
	public double default_excretion_factor;
	public double default_child_hunger;
	
}

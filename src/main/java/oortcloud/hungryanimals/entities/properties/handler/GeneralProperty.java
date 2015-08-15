package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;
import java.util.HashMap;

import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;

public class GeneralProperty {

	public double hunger_max;
	public double hunger_bmr;
	public HashMap<HashItem, Double> hunger_food = new HashMap<HashItem, Double>();
	public HashMap<HashBlock, Double> hunger_block = new HashMap<HashBlock, Double>();
	public ArrayList<DropMeat> drop_meat = new ArrayList<DropMeat>();
	public ArrayList<DropRandom> drop_random = new ArrayList<DropRandom>();
	public ArrayList<DropRare> drop_rare = new ArrayList<DropRare>();
	public double courtship_hunger;
	public double courtship_probability;
	public double courtship_hungerCondition;
	public double excretion_factor;
	public double child_hunger;
	public double attribute_maxhealth;
	public double attribute_movespeed;

}

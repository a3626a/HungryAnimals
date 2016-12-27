package oortcloud.hungryanimals.entities.properties.handler;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import oortcloud.hungryanimals.core.lib.References;

public class ModAttributes {
	
	public static String NAME_hunger_max = References.MODID+".hunger_max";
	public static String NAME_hunger_bmr = References.MODID+".hunger_bmr";
	public static String NAME_courtship_hunger = References.MODID+".courtship_hunger";
	public static String NAME_courtship_probability = References.MODID+".courtship_probability";
	public static String NAME_courtship_hungerCondition = References.MODID+".courtship_hungerCondition";
	public static String NAME_excretion_factor = References.MODID+".excretion_factor";
	public static String NAME_child_hunger = References.MODID+".child_hunger";
	public static String NAME_milk_hunger = References.MODID+".milk_hunger";
	public static String NAME_milk_delay = References.MODID+".milk_delay";
	public static String NAME_wool_hunger = References.MODID+".wool_hunger";
	public static String NAME_wool_delay = References.MODID+".wool_delay";
	
	public static IAttribute hunger_max = new RangedAttribute((IAttribute)null, NAME_hunger_max, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	public static IAttribute hunger_bmr = new RangedAttribute((IAttribute)null, NAME_hunger_bmr, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute courtship_hunger = new RangedAttribute((IAttribute)null, NAME_courtship_hunger, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute courtship_probability = new RangedAttribute((IAttribute)null, NAME_courtship_probability, 0.0, 0.0, 1.0).setShouldWatch(false);
	public static IAttribute courtship_hungerCondition = new RangedAttribute((IAttribute)null, NAME_courtship_hungerCondition, 0.0, 0.0, 1.0).setShouldWatch(false);
	public static IAttribute excretion_factor = new RangedAttribute((IAttribute)null, NAME_excretion_factor, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute child_hunger = new RangedAttribute((IAttribute)null, NAME_child_hunger, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute milk_hunger = new RangedAttribute((IAttribute)null, NAME_milk_hunger, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute milk_delay = new RangedAttribute((IAttribute)null, NAME_milk_delay, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	public static IAttribute wool_hunger = new RangedAttribute((IAttribute)null, NAME_wool_hunger, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute wool_delay = new RangedAttribute((IAttribute)null, NAME_wool_delay, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
}

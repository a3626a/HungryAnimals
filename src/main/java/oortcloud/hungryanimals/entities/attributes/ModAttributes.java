package oortcloud.hungryanimals.entities.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import oortcloud.hungryanimals.core.lib.References;

public class ModAttributes {
	
	public static String NAME_hunger_weight_bmr = References.MODID+".hunger_weight_bmr";
	public static String NAME_hunger_stomach_max = References.MODID+".hunger_stomach_max";
	public static String NAME_hunger_stomach_digest = References.MODID+".hunger_stomach_digest";
	public static String NAME_hunger_weight_normal = References.MODID+".hunger_weight_normal";
	public static String NAME_courtship_weight = References.MODID+".courtship_weight";
	public static String NAME_courtship_probability = References.MODID+".courtship_probability";
	public static String NAME_courtship_stomach_condition = References.MODID+".courtship_stomach_condition";
	public static String NAME_excretion_factor = References.MODID+".excretion_factor";
	public static String NAME_child_weight = References.MODID+".child_weight";
	public static String NAME_milk_hunger = References.MODID+".milk_hunger";
	public static String NAME_milk_delay = References.MODID+".milk_delay";
	public static String NAME_wool_hunger = References.MODID+".wool_hunger";
	public static String NAME_wool_delay = References.MODID+".wool_delay";
	

	public static IAttribute hunger_weight_bmr = new RangedAttribute((IAttribute)null, NAME_hunger_weight_bmr, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute hunger_stomach_max = new RangedAttribute((IAttribute)null, NAME_hunger_stomach_max, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	public static IAttribute hunger_stomach_digest = new RangedAttribute((IAttribute)null, NAME_hunger_stomach_digest, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute hunger_weight_normal = new RangedAttribute((IAttribute)null, NAME_hunger_weight_normal, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute courtship_weight = new RangedAttribute((IAttribute)null, NAME_courtship_weight, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute courtship_probability = new RangedAttribute((IAttribute)null, NAME_courtship_probability, 0.0, 0.0, 1.0).setShouldWatch(false);
	public static IAttribute courtship_stomach_condition = new RangedAttribute((IAttribute)null, NAME_courtship_stomach_condition, 0.0, 0.0, 1.0).setShouldWatch(false);
	public static IAttribute excretion_factor = new RangedAttribute((IAttribute)null, NAME_excretion_factor, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute child_weight = new RangedAttribute((IAttribute)null, NAME_child_weight, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute milk_hunger = new RangedAttribute((IAttribute)null, NAME_milk_hunger, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute milk_delay = new RangedAttribute((IAttribute)null, NAME_milk_delay, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	public static IAttribute wool_hunger = new RangedAttribute((IAttribute)null, NAME_wool_hunger, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute wool_delay = new RangedAttribute((IAttribute)null, NAME_wool_delay, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	
}

package oortcloud.hungryanimals.entities.loot_tables;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;

public class SetCountBaseOnWeight  extends LootFunction {

	private final WeightValueRange countRange;

    public SetCountBaseOnWeight(LootCondition[] conditionsIn, WeightValueRange countRangeIn)
    {
        super(conditionsIn);
        this.countRange = countRangeIn;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context)
    {
        stack.setCount(this.countRange.generateInt(context.getLootedEntity().getCapability(ProviderHungryAnimal.CAP, null), rand));
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetCountBaseOnWeight>
        {
            protected Serializer()
            {
                super(new ResourceLocation("set_count_based_on_weight"), SetCountBaseOnWeight.class);
            }

            public void serialize(JsonObject object, SetCountBaseOnWeight functionClazz, JsonSerializationContext serializationContext)
            {
                object.add("weight_per_meat", new JsonPrimitive(functionClazz.countRange.getWeightPerMeat()));
            }

            public SetCountBaseOnWeight deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
            {
                return new SetCountBaseOnWeight(conditionsIn, new WeightValueRange(JsonUtils.getFloat(object, "weight_per_meat")));
            }
        }
	

}

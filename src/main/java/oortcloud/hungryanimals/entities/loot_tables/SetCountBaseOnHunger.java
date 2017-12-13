package oortcloud.hungryanimals.entities.loot_tables;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;

public class SetCountBaseOnHunger  extends LootFunction {

	private final HungerValueRange countRange;

    public SetCountBaseOnHunger(LootCondition[] conditionsIn, HungerValueRange countRangeIn)
    {
        super(conditionsIn);
        this.countRange = countRangeIn;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context)
    {
        stack.setCount(this.countRange.generateInt(context.getLootedEntity().getCapability(ProviderHungryAnimal.CAP, null)));
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetCountBaseOnHunger>
        {
            protected Serializer()
            {
                super(new ResourceLocation("set_count_based_on_hunger"), SetCountBaseOnHunger.class);
            }

            public void serialize(JsonObject object, SetCountBaseOnHunger functionClazz, JsonSerializationContext serializationContext)
            {
                object.add("count", serializationContext.serialize(functionClazz.countRange));
            }

            public SetCountBaseOnHunger deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
            {
                return new SetCountBaseOnHunger(conditionsIn, (HungerValueRange)JsonUtils.deserializeClass(object, "count", deserializationContext, HungerValueRange.class));
            }
        }
	

}

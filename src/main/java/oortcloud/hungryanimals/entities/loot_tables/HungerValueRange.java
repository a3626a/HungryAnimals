package oortcloud.hungryanimals.entities.loot_tables;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public class HungerValueRange
{
    private final float min;
    private final float max;

    public HungerValueRange(float minIn, float maxIn)
    {
        this.min = minIn;
        this.max = maxIn;
    }

    public HungerValueRange(float value)
    {
        this.min = value;
        this.max = value;
    }

    public float getMin()
    {
        return this.min;
    }

    public float getMax()
    {
        return this.max;
    }

    public int generateInt(ICapabilityHungryAnimal cap)
    {
    	float x;
        if (cap != null) {
            x = (float) (cap.getHunger()/cap.getMaxHunger());
        } else {
            x = 1f;
        }
        return MathHelper.floor_float((this.max + 1 - this.min)*x + this.min);
    }

    public float generateFloat(ICapabilityHungryAnimal cap)
    {
    	float x;
        if (cap != null) {
            x = (float) (cap.getHunger()/cap.getMaxHunger());
        } else {
            x = 1f;
        }
        return (this.max - this.min)*x + this.min;
    }

    public boolean isInRange(int value)
    {
        return (float)value <= this.max && (float)value >= this.min;
    }

    public static class Serializer implements JsonDeserializer<HungerValueRange>, JsonSerializer<HungerValueRange>
        {
            public HungerValueRange deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
            {
                if (JsonUtils.isNumber(p_deserialize_1_))
                {
                    return new HungerValueRange(JsonUtils.getFloat(p_deserialize_1_, "value"));
                }
                else
                {
                    JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "value");
                    float f = JsonUtils.getFloat(jsonobject, "min");
                    float f1 = JsonUtils.getFloat(jsonobject, "max");
                    return new HungerValueRange(f, f1);
                }
            }

            public JsonElement serialize(HungerValueRange p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                if (p_serialize_1_.min == p_serialize_1_.max)
                {
                    return new JsonPrimitive(Float.valueOf(p_serialize_1_.min));
                }
                else
                {
                    JsonObject jsonobject = new JsonObject();
                    jsonobject.addProperty("min", (Number)Float.valueOf(p_serialize_1_.min));
                    jsonobject.addProperty("max", (Number)Float.valueOf(p_serialize_1_.max));
                    return jsonobject;
                }
            }
        }
}

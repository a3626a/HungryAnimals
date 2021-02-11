package oortcloud.hungryanimals.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import oortcloud.hungryanimals.HungryAnimals;

import javax.annotation.Nullable;

public class ModJsonUtils {

	public static List<Ingredient> getIngredients(JsonElement jsonEle) {
		List<Ingredient> list = new ArrayList<Ingredient>();
		if (jsonEle instanceof JsonArray) {
			// parse list
			JsonArray jsonArr = (JsonArray) jsonEle;
			for (JsonElement jsonObj : jsonArr) {
				Ingredient ing = getIngredient(jsonObj);
				if (ing != null) {
					list.add(ing);
				}
			}
		} else {
			// parse one
			Ingredient ing = getIngredient(jsonEle);
			if (ing != null) {
				list.add(ing);
			}
		}

		return list;
	}

	@Nullable
	public static Ingredient getIngredient(JsonElement jsonEle) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("Ingredient must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObj = (JsonObject) jsonEle;
		Ingredient ing = null;
		try {
			ing = CraftingHelper.getIngredient(jsonObj);
		} catch (JsonSyntaxException e) {
			HungryAnimals.logger.error("Failed to read ingredient while parsing {}", jsonObj);
		}

		return ing;
	}

}

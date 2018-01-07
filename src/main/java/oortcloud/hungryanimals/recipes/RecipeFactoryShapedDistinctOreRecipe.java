package oortcloud.hungryanimals.recipes;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreIngredient;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.recipes.ShapedDistinctOreRecipe.ShapedPrimerDistinct;

public class RecipeFactoryShapedDistinctOreRecipe implements IRecipeFactory {

	private static final ResourceLocation name = new ResourceLocation(References.MODID, "shapeddistinctorerecipe");
	
	@Override
	public IRecipe parse(JsonContext context, JsonObject json) {
        Map<Character, Ingredient> ingMap = Maps.newHashMap();
        Map<Character, String> oreMap = Maps.newHashMap();
        for (Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet())
        {
            if (entry.getKey().length() != 1)
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            if (" ".equals(entry.getKey()))
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            Ingredient ing = CraftingHelper.getIngredient(entry.getValue(), context);
            if (ing instanceof OreIngredient) {
            	oreMap.put(entry.getKey().toCharArray()[0], JsonUtils.getString(entry.getValue().getAsJsonObject(), "ore"));
            }
            ingMap.put(entry.getKey().toCharArray()[0], ing);
        }

        ingMap.put(' ', Ingredient.EMPTY);

        JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

        if (patternJ.size() == 0)
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");

        String[] pattern = new String[patternJ.size()];
        for (int x = 0; x < pattern.length; ++x)
        {
            String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
            if (x > 0 && pattern[0].length() != line.length())
                throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
            pattern[x] = line;
        }
        boolean mirrored = JsonUtils.getBoolean(json, "mirrored", true);
        
        ShapedPrimerDistinct primer = ShapedDistinctOreRecipe.parse(mirrored, Arrays.asList(pattern), ingMap, oreMap);

        ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
        
        return new ShapedDistinctOreRecipe(name, result, primer);
	}
}

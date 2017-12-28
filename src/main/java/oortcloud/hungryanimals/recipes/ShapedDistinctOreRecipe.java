package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ShapedDistinctOreRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	//Added in for future ease of change, but hard coded for now.
    public static final int MAX_CRAFT_GRID_WIDTH = 3;
    public static final int MAX_CRAFT_GRID_HEIGHT = 3;

    protected ItemStack output = ItemStack.EMPTY;
    protected NonNullList<Ingredient> input = null;
    protected List<Character> characters = null;
    protected int width = 0;
    protected int height = 0;
    protected boolean mirrored = true;

    public static class ShapedPrimerDistinct {
        public int height, width;
        public boolean mirrored = true;
        public NonNullList<Ingredient> input;
        public List<Character> characters;
    }
    
    public ShapedDistinctOreRecipe(ResourceLocation name, Block     result, Object... recipe){ this(name, new ItemStack(result), parse(recipe)); }
    public ShapedDistinctOreRecipe(ResourceLocation name, Item      result, Object... recipe){ this(name, new ItemStack(result), parse(recipe)); }
    public ShapedDistinctOreRecipe(ResourceLocation name, ItemStack result, ShapedPrimerDistinct primer){
    	setRegistryName(name);
    	
        output = result.copy();
        this.width = primer.width;
        this.height = primer.height;
        this.input = primer.input;
        this.mirrored = primer.mirrored;
        this.characters = primer.characters;
    }
    
    /**
     * This code is based on net.minecraftforge.common.crafting.CraftingHelper.parseShaped
     * @param recipe
     * @return
     */
    public static ShapedPrimerDistinct parse(Object... recipe)
    {
    	ShapedPrimerDistinct ret = new ShapedPrimerDistinct();
        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof Boolean)
        {
            ret.mirrored = (Boolean)recipe[idx];
            if (recipe[idx+1] instanceof Object[])
                recipe = (Object[])recipe[idx+1];
            else
                idx = 1;
        }

        if (recipe[idx] instanceof String[])
        {
            String[] parts = ((String[])recipe[idx++]);

            for (String s : parts)
            {
                ret.width = s.length();
                shape += s;
            }

            ret.height = parts.length;
        }
        else
        {
            while (recipe[idx] instanceof String)
            {
                String s = (String)recipe[idx++];
                shape += s;
                ret.width = s.length();
                ret.height++;
            }
        }

        if (ret.width * ret.height != shape.length() || shape.length() == 0)
        {
            String err = "Invalid shaped ore recipe: ";
            for (Object tmp :  recipe)
            {
                err += tmp + ", ";
            }
            throw new RuntimeException(err);
        }

        HashMap<Character, Ingredient> itemMap = Maps.newHashMap();
        itemMap.put(' ', Ingredient.EMPTY);
        HashMap<String, Integer> oreNum = new HashMap<String, Integer>();
        
        for (; idx < recipe.length; idx += 2)
        {
            Character chr = (Character)recipe[idx];
            Object in = recipe[idx + 1];
            Ingredient ing = CraftingHelper.getIngredient(in);
            
            if (' ' == chr.charValue())
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            if (ing != null)
            {
                itemMap.put(chr, ing);
            }
            else
            {
                String err = "Invalid shaped ore recipe: ";
                for (Object tmp :  recipe)
                {
                    err += tmp + ", ";
                }
                throw new RuntimeException(err);
            }
        }

        ret.input = NonNullList.withSize(ret.width * ret.height, Ingredient.EMPTY);

        Set<Character> keys = Sets.newHashSet(itemMap.keySet());
        keys.remove(' ');

        int x = 0;
        for (char chr : shape.toCharArray())
        {
            Ingredient ing = itemMap.get(chr);
            if (ing == null)
                throw new IllegalArgumentException("Pattern references symbol '" + chr + "' but it's not defined in the key");
            ret.input.set(x++, ing);
            keys.remove(chr);
        }

        if (!keys.isEmpty())
            throw new IllegalArgumentException("Key defines symbols that aren't used in pattern: " + keys);

        return ret;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1){ return output.copy(); }


    @Override
    public ItemStack getRecipeOutput(){ return output; }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
                if (checkMatch(inv, x, y, false) && checkDistinct(inv, x, y, false))
                {
                    return true;
                }

                if (mirrored && checkMatch(inv, x, y, true) && checkDistinct(inv, x, y, true))
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean checkDistinct(InventoryCrafting inv, int startX, int startY, boolean mirror) {
    	
    	HashMap<Character, ItemStack> map = new HashMap<Character, ItemStack>();
    	
    	for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Character target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = characters[width - subX - 1 + subY * width];
                    }
                    else
                    {
                        target = characters[subX + subY * width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);
                
                if (target != null) {
                	if (!map.containsKey(target)) {
                		for (ItemStack i : map.values()) {
                			if (OreDictionary.itemMatches(i, slot, false)) {
                    			return false;
                    		}
                		}
                		map.put(target, slot);
                	}
                	if (map.containsKey(target)) {
                		if (!OreDictionary.itemMatches(map.get(target), slot, false))
                        {
                            return false;
                        }
                	}
                }

            }
        }

        return true;
	}
	@SuppressWarnings("unchecked")
    protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = input[width - subX - 1 + subY * width];
                    }
                    else
                    {
                        target = input[subX + subY * width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack)
                {
                    if (!OreDictionary.itemMatches((ItemStack)target, slot, false))
                    {
                        return false;
                    }
                }
                else if (target instanceof List)
                {
                    boolean matched = false;

                    Iterator<ItemStack> itr = ((List<ItemStack>)target).iterator();
                    while (itr.hasNext() && !matched)
                    {
                        matched = OreDictionary.itemMatches(itr.next(), slot, false);
                    }

                    if (!matched)
                    {
                        return false;
                    }
                }
                else if (target == null && slot != null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public ShapedDistinctOreRecipe setMirrored(boolean mirror)
    {
        mirrored = mirror;
        return this;
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     * @return The recipes input vales.
     */
    public Object[] getInput()
    {
        return this.input;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) //getRecipeLeftovers
    {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    public int getWidth() {
    	return width;
    }
    
    public int getHeight() {
    	return height;
    }

    public Character[] getCharacters() {
    	return characters;
    }
    
    public String[] getOres() {
    	return ores;
    }
    
	@Override
	public boolean canFit(int width, int height) {
		return this.width <= width && this.height <= height;
	}
}

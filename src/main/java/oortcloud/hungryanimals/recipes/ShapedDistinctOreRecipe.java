package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ShapedDistinctOreRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	//Added in for future ease of change, but hard coded for now.
    public static final int MAX_CRAFT_GRID_WIDTH = 3;
    public static final int MAX_CRAFT_GRID_HEIGHT = 3;

    protected ItemStack output = ItemStack.EMPTY;
    protected Object[] input = null;
    protected Character[] characters = null;
    protected String[] ores = null;
    protected int width = 0;
    protected int height = 0;
    protected boolean mirrored = true;

    public ShapedDistinctOreRecipe(ResourceLocation name, Block     result, Object... recipe){ this(name, new ItemStack(result), recipe); }
    public ShapedDistinctOreRecipe(ResourceLocation name, Item      result, Object... recipe){ this(name, new ItemStack(result), recipe); }
    public ShapedDistinctOreRecipe(ResourceLocation name, ItemStack result, Object... recipe)
    {
    	setRegistryName(name);
    	
        output = result.copy();

        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof Boolean)
        {
            mirrored = (Boolean)recipe[idx];
            if (recipe[idx+1] instanceof Object[])
            {
                recipe = (Object[])recipe[idx+1];
            }
            else
            {
                idx = 1;
            }
        }

        if (recipe[idx] instanceof String[])
        {
            String[] parts = ((String[])recipe[idx++]);

            for (String s : parts)
            {
                width = s.length();
                shape += s;
            }

            height = parts.length;
        }
        else
        {
            while (recipe[idx] instanceof String)
            {
                String s = (String)recipe[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        if (width * height != shape.length())
        {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp :  recipe)
            {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, Object> itemMap = new HashMap<Character, Object>();
        HashMap<Character, String> oreMap = new HashMap<Character, String>();
        HashMap<String, Integer> oreNum = new HashMap<String, Integer>();
        
        for (; idx < recipe.length; idx += 2)
        {
            Character chr = (Character)recipe[idx];
            Object in = recipe[idx + 1];

            if (in instanceof ItemStack)
            {
                itemMap.put(chr, ((ItemStack)in).copy());
            }
            else if (in instanceof Item)
            {
                itemMap.put(chr, new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                itemMap.put(chr, new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE));
            }
            else if (in instanceof String)
            {
            	String oreName = (String)in;
            	if (oreNum.containsKey(oreName)) {
            		oreNum.put(oreName, oreNum.get(oreName)+1);
            	} else {
            		oreNum.put(oreName, 1);
            	}
            	
            	List<ItemStack> ores = OreDictionary.getOres((String)in);
            	if (oreNum.get(oreName) > ores.size()) {
            		throw new RuntimeException("Invalid recipe");
            	}
            	    
            	List<ItemStack> ore_clone = new ArrayList<ItemStack>();
            	
            	int rotation = oreNum.get(oreName)-1;
            	
            	for (int i = rotation; i < ores.size(); i++) {
            		ore_clone.add(ores.get(i));
            	}
            	for (int i = 0; i < rotation; i++) {
            		ore_clone.add(ores.get(i));
            	}
                itemMap.put(chr, ore_clone);
                oreMap.put(chr, (String)in);
            }
            else
            {
                String ret = "Invalid shaped ore recipe: ";
                for (Object tmp :  recipe)
                {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }

        input = new Object[width * height];
        characters = new Character[width * height];
        ores = new String[width * height];
        int x = 0;
        for (char chr : shape.toCharArray())
        {
            input[x] = itemMap.get(chr);
            if (input[x] instanceof List) {
                characters[x] = chr;
                ores[x] = oreMap.get(chr);
            }
            x++;
        }
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

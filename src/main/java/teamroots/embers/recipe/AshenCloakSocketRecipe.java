package teamroots.embers.recipe;

import static teamroots.embers.util.ItemUtil.EMPTY_ITEM_STACK;
import static teamroots.embers.util.ItemUtil.stackEmpty;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.ItemAshenCloak;
import teamroots.embers.item.ItemInflictorGem;
import teamroots.embers.util.ItemUtil;

public class AshenCloakSocketRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean hasCloak = false;
		boolean moreThanOneCloak = false;
		boolean hasString = false;
		boolean moreThanOneString = false;
		boolean hasGem = false;
		if (inv.getSizeInventory() > 4){
			for (int i = 0; i < inv.getSizeInventory(); i ++){
				if (!stackEmpty(inv.getStackInSlot(i))){
					if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest){
						if (!inv.getStackInSlot(i).hasTagCompound() || !inv.getStackInSlot(i).getTagCompound().hasKey("gem1")
								&& !inv.getStackInSlot(i).getTagCompound().hasKey("gem2")
								&& !inv.getStackInSlot(i).getTagCompound().hasKey("gem3")
								&& !inv.getStackInSlot(i).getTagCompound().hasKey("gem4")
								&& !inv.getStackInSlot(i).getTagCompound().hasKey("gem5")
								&& !inv.getStackInSlot(i).getTagCompound().hasKey("gem6")
								&& !inv.getStackInSlot(i).getTagCompound().hasKey("gem7")){
							if (!hasCloak && !moreThanOneCloak){
								hasCloak = true;
							}
							else if (hasCloak){
								hasCloak = false;
								moreThanOneCloak = true;
							}
						}
					}
					else if (inv.getStackInSlot(i).getItem() == Items.STRING){
						if (!hasString && !moreThanOneString){
							hasString = true;
						}
						else if (hasString){
							hasString = false;
							moreThanOneString = true;
						}
					}
					else if (inv.getStackInSlot(i).getItem() instanceof ItemInflictorGem){
						hasGem = true;
					}
					else {
						if (inv.getStackInSlot(i) != EMPTY_ITEM_STACK){
							return false;
						}
					}
				}
			}
		}
		return hasGem && hasString && hasCloak;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack capeStack = EMPTY_ITEM_STACK;
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (!stackEmpty(inv.getStackInSlot(i))){
				if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest){
					capeStack = inv.getStackInSlot(i).copy();
				}
			}
		}
		if (capeStack != EMPTY_ITEM_STACK){
			if (!capeStack.hasTagCompound()){
				capeStack.setTagCompound(new NBTTagCompound());
			}
			int counter = 1;
			for (int i = 0; i < inv.getSizeInventory(); i ++){
				if (!stackEmpty(inv.getStackInSlot(i))){
					if (inv.getStackInSlot(i).getItem() instanceof ItemInflictorGem){
						capeStack.getTagCompound().setTag("gem"+counter, inv.getStackInSlot(i).writeToNBT(new NBTTagCompound()));
						counter ++;
					}
				}
			}
			return capeStack;
		}
		return EMPTY_ITEM_STACK;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(RegistryManager.ashen_cloak_chest,1);
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
	    ItemStack[] remaining = new ItemStack[]{};
		inv.clear();
		return remaining;
	}

}

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

public class AshenCloakUnsocketRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean has_cloak = false;
		boolean more_than_one_cloak = false;
		if (inv.getSizeInventory() > 4){
			for (int i = 0; i < inv.getSizeInventory(); i ++){
				if (inv.getStackInSlot(i) != EMPTY_ITEM_STACK){
					if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest && inv.getStackInSlot(i).getTagCompound() != null){
						if (inv.getStackInSlot(i).getTagCompound().hasKey("gem1") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem2") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem3") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem4") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem5") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem6") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem7")){
							if (!has_cloak && !more_than_one_cloak){
								has_cloak = true;
							}
							else if (has_cloak){
								has_cloak = false;
								more_than_one_cloak = true;
							}
						}
					}
					else {
						if (inv.getStackInSlot(i) != EMPTY_ITEM_STACK){
							return false;
						}
					}
				}
			}
		}
		return has_cloak;
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
		if (!stackEmpty(capeStack)){
			for (int i = 1; i < 8; i ++){
				if (capeStack.getTagCompound().hasKey("gem"+i)){
					capeStack.getTagCompound().removeTag("gem"+i);
				}
			}
		}
		return capeStack;
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
		List<ItemStack> gems = new ArrayList<>();
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != EMPTY_ITEM_STACK){
				if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest){
					for (int j = 1; j < 8; j ++){
						if (inv.getStackInSlot(i).getTagCompound().hasKey("gem"+j)){
							gems.add(ItemStack.func_77949_a(inv.getStackInSlot(i).getTagCompound().getCompoundTag("gem"+j)));
						}
					}
				}
			}
		}
		inv.clear();
		return (ItemStack[])gems.toArray();
	}

}

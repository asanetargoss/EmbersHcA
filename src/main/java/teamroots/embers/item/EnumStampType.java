package teamroots.embers.item;

import static teamroots.embers.util.ItemUtil.stackEmpty;

import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;

public enum EnumStampType {
	TYPE_FLAT, TYPE_BAR, TYPE_PLATE, TYPE_NULL;
	public static EnumStampType getType(ItemStack stack){
		if (!stackEmpty(stack)){
			if (stack.getItem() == RegistryManager.stamp_bar){
				return TYPE_BAR;
			}
			if (stack.getItem() == RegistryManager.stamp_flat){
				return TYPE_FLAT;
			}
			if (stack.getItem() == RegistryManager.stamp_plate){
				return TYPE_PLATE;
			}
		}
		return TYPE_NULL;
	}
}

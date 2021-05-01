package teamroots.embers.util;

import static teamroots.embers.util.ItemUtil.getItem;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import teamroots.embers.RegistryManager;

public class EmbersFuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (getItem(fuel) == RegistryManager.dust_ash){
			return 200;
		}
		return 0;
	}
}

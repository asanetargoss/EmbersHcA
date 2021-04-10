package teamroots.embers.item.bauble;

import static teamroots.embers.util.ItemUtil.stackEmpty;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import teamroots.embers.item.ItemBase;
import teamroots.embers.util.ItemUtil;

public class ItemBaubleBase extends ItemBase implements IBauble {
	BaubleType type = BaubleType.CHARM;
	
	public ItemBaubleBase(String name, BaubleType type, boolean addToTab) {
		super(name, addToTab);
		this.type = type;
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return type;
	}

	/**
	 * All following code is borrowed from Vazkii's ItemBauble because I'm a lazy piece of trash.
	 * https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/common/item/equipment/bauble/ItemBauble.java
	 */
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {

		ItemStack toEquip = stack.copy();
		toEquip.stackSize = 1;

		if(canEquip(toEquip, player)) {
			if(world.isRemote)
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);

			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for(int i = 0; i < baubles.getSlots(); i++) {
				if(baubles.isItemValidForSlot(i, toEquip, player)) {
					ItemStack stackInSlot = baubles.getStackInSlot(i);
					if(stackEmpty(stackInSlot) || ((IBauble) stackInSlot.getItem()).canUnequip(stackInSlot, player)) {
						baubles.setStackInSlot(i, toEquip);
						stack.stackSize -= 1; // TODO: This looks dangerous

						if(!stackEmpty(stackInSlot)) {
							((IBauble) stackInSlot.getItem()).onUnequipped(stackInSlot, player);

							if(stackEmpty(stack)) {
								return ActionResult.newResult(EnumActionResult.SUCCESS, stackInSlot);
							} else {
								ItemHandlerHelper.giveItemToPlayer(player, stackInSlot);
							}
						}

						return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
					}
				}
			}
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}
}

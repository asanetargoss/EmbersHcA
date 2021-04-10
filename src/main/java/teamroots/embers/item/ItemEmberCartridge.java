package teamroots.embers.item;

import java.util.List;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.util.Misc;

public class ItemEmberCartridge extends ItemBase implements IHeldEmberCell, IEmberItem {

	public ItemEmberCartridge() {
		super("ember_cartridge", true);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			setEmberCapacity(stack, 6000.0);
			setEmber(stack,0.0);
		}
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack){
		if (stack.hasTagCompound()){
			if (getEmber(stack) < getEmberCapacity(stack)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems){
		ItemStack emptyStack = new ItemStack(this,1);
		emptyStack.setTagCompound(new NBTTagCompound());
		setEmberCapacity(emptyStack, 6000.0);
		setEmber(emptyStack,0.0);
		subItems.add(emptyStack);
		ItemStack fullStack = new ItemStack(this,1);
		fullStack.setTagCompound(new NBTTagCompound());
		setEmberCapacity(fullStack, 6000.0);
		setEmber(fullStack,6000.0);
		subItems.add(fullStack);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged || oldStack.getItem() != newStack.getItem();
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack){
		if (stack.hasTagCompound()){
			return (getEmberCapacity(stack)-getEmber(stack))/getEmberCapacity(stack);
		}
		return 0.0;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
		if (stack.hasTagCompound()){
			tooltip.add(""+getEmber(stack)+" / "+getEmberCapacity(stack));
		}
	}
	
	public void initNBT(ItemStack stack){
		stack.setTagCompound(new NBTTagCompound());
		setEmberCapacity(stack, 6000.0);
		setEmber(stack,0.0);
	}

	@Override
	public double getEmber(ItemStack stack) {
		if (!stack.hasTagCompound()){
			initNBT(stack);
		}
		return stack.getTagCompound().getDouble(Embers.MODID+":ember");
	}

	@Override
	public double getEmberCapacity(ItemStack stack) {
		if (!stack.hasTagCompound()){
			initNBT(stack);
		}
		return stack.getTagCompound().getDouble(Embers.MODID+":emberCapacity");
	}

	@Override
	public void setEmber(ItemStack stack, double value) {
		if (!stack.hasTagCompound()){
			initNBT(stack);
		}
		stack.getTagCompound().setDouble(Embers.MODID+":ember",value);
	}

	@Override
	public void setEmberCapacity(ItemStack stack, double value) {
		if (!stack.hasTagCompound()){
			initNBT(stack);
		}
		stack.getTagCompound().setDouble(Embers.MODID+":emberCapacity",value);
	}

	@Override
	public double addAmount(ItemStack stack, double value, boolean doAdd) {
		if (!stack.hasTagCompound()){
			initNBT(stack);
		}
		double ember = stack.getTagCompound().getDouble(Embers.MODID+":ember");
		double capacity = stack.getTagCompound().getDouble(Embers.MODID+":emberCapacity");
		if (ember+value > capacity){
			double added = capacity-ember;
			if (doAdd){
				setEmber(stack,capacity);
				ember = capacity;
			}
			return added;
		}
		if (doAdd){
			setEmber(stack,ember+value);
		}
		return value;
	}

	@Override
	public double removeAmount(ItemStack stack, double value, boolean doRemove) {
		if (!stack.hasTagCompound()){
			initNBT(stack);
		}
		double ember = stack.getTagCompound().getDouble(Embers.MODID+":ember");
		double capacity = stack.getTagCompound().getDouble(Embers.MODID+":emberCapacity");
		if (ember-value < 0){
			double removed = ember;
			if (doRemove){
				setEmber(stack,0);
			}
			return removed;
		}
		if (doRemove){
			setEmber(stack, ember-value);
		}
		return value;
	}
	
	public static class EmberChargeColorHandler implements IItemColor {
		public EmberChargeColorHandler(){
			//
		}

		@Override
		public int getColorFromItemstack(ItemStack stack, int layer) {
			if (layer == 0 && stack.hasTagCompound()){
				if (stack.getItem() instanceof IEmberItem){
					double coeff = ((IEmberItem)stack.getItem()).getEmber(stack)/((IEmberItem)stack.getItem()).getEmberCapacity(stack);
					int r = 255;
					int g = (int)(255.0*(1.0-coeff)+64.0*coeff);
					int b = (int)(255.0*(1.0-coeff)+16.0*coeff);
					return 0xFF0000;
				}
			}
			return 0xFFFFFF;
		}

	}

	@SideOnly(Side.CLIENT)
	public static class ColorHandler implements IItemColor {
		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex) { 
			if (tintIndex == 1){
				if (stack.hasTagCompound() && stack.getItem() == RegistryManager.ember_cartridge){
					float coeff = (float)(((IEmberItem)stack.getItem()).getEmber(stack) / ((IEmberItem)stack.getItem()).getEmberCapacity(stack));
					float timerSine = ((float)Math.sin(8.0*Math.toRadians(EventManager.ticks % 360))+1.0f)/2.0f;
					int r = (int)255.0f;
					int g = (int)(255.0f*(1.0f-coeff) + (64.0f*timerSine+64.0f)*coeff);
					int b = (int)(255.0f*(1.0f-coeff) + 16.0f*coeff);
					return (r << 16) + (g << 8) + b;
				}
			}
			return Misc.intColor(255, 255, 255);
		}		
	}

}

package teamroots.embers.tileentity;

import static teamroots.embers.util.ItemUtil.EMPTY_ITEM_STACK;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockStamper;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageStamperFX;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.recipe.ItemStampingOreRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.ItemUtil;
import teamroots.embers.util.Misc;

public class TileEntityStamper extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	public boolean prevPowered = false;
	public boolean powered = false;
	public long ticksExisted = 0;
	Random random = new Random();
	public ItemStackHandler stamp = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityStamper.this.markDirty();
        }
        
        @Override
        public int getStackLimit(int slot, ItemStack stack){
        	return 1;
        }
	};
	
	public TileEntityStamper(){
		super();
		capability.setEmberCapacity(8000);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setBoolean("powered", powered);
		capability.writeToNBT(tag);
		tag.setTag("stamp",stamp.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		powered = tag.getBoolean("powered");
		capability.readFromNBT(tag);
		stamp.deserializeNBT(tag.getCompoundTag("stamp"));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem != EMPTY_ITEM_STACK){
			if (EnumStampType.getType(heldItem) != EnumStampType.TYPE_NULL){
				if (stamp.getStackInSlot(0) == EMPTY_ITEM_STACK){
					ItemStack newStack = new ItemStack(heldItem.getItem(),1,heldItem.getMetadata());
					if (heldItem.hasTagCompound()){
						newStack.setTagCompound(heldItem.getTagCompound());
					}
					player.setHeldItem(hand, this.stamp.insertItem(0,newStack,false));
					markDirty();
					return true;
				}
			}
		}
		else {
			if (stamp.getStackInSlot(0) != EMPTY_ITEM_STACK && !world.isRemote){
				world.spawnEntity(new EntityItem(world,player.posX,player.posY,player.posZ,stamp.getStackInSlot(0)));
				stamp.setStackInSlot(0, EMPTY_ITEM_STACK);
				markDirty();
				return true;
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, stamp);
	}

	@Override
	public void update() {
		this.ticksExisted ++;
		prevPowered = powered;
		if (true){
			EnumFacing face = getWorld().getBlockState(getPos()).getValue(BlockStamper.facing);
			if (getWorld().getBlockState(getPos().offset(face,2)).getBlock() == RegistryManager.stamp_base){
				if (this.ticksExisted % 80 == 0 && !powered && !getWorld().isRemote){
					TileEntityStampBase stamp = (TileEntityStampBase)getWorld().getTileEntity(getPos().offset(face,2));
					FluidStack fluid = null;
					if (stamp.getFluid() != null){
						fluid = new FluidStack(stamp.getFluid(),stamp.getAmount());
					}
					ItemStampingRecipe recipe = RecipeRegistry.getStampingRecipe(stamp.inputs.getStackInSlot(0), fluid, EnumStampType.getType(this.stamp.getStackInSlot(0)));
					if (recipe != null){
						if (this.capability.getEmber() > 80.0){
							this.capability.removeAmount(80.0, true);
							if (!world.isRemote){
								PacketHandler.INSTANCE.sendToAll(new MessageStamperFX(getPos().offset(face,2).getX()+0.5f,getPos().offset(face,2).getY()+1.0f,getPos().offset(face,2).getZ()+0.5f));
							}
							powered = true;
							ItemStack result = recipe.getResult(stamp.inputs.getStackInSlot(0), stamp.getFluid() != null ? new FluidStack(stamp.getFluid(), stamp.getAmount()) : null,EnumStampType.getType(this.stamp.getStackInSlot(0))).copy();
							if (recipe.getStack() != EMPTY_ITEM_STACK){
								stamp.inputs.extractItem(0, recipe.getStack().stackSize, false);
							}
							if (recipe.getFluid() != null){
								stamp.getTank().drain(recipe.getFluid(), true);
							}
							BlockPos off = getPos().offset(face,1);
							if (getWorld().getTileEntity(getPos().offset(face,3)) instanceof TileEntityBin){
								TileEntityBin bin = (TileEntityBin)getWorld().getTileEntity(getPos().offset(face,3));
								ItemStack remainder = bin.inventory.insertItem(0, result, false);
								if (remainder != EMPTY_ITEM_STACK && !getWorld().isRemote){
									EntityItem item = new EntityItem(getWorld(),off.getX()+0.5,off.getY()+0.5,off.getZ()+0.5,remainder);
									getWorld().spawnEntity(item);
								}
								bin.markDirty();
								IBlockState state = getWorld().getBlockState(getPos().offset(face,3));
								markDirty();
							}
							else if (!getWorld().isRemote){
								EntityItem item = new EntityItem(getWorld(),off.getX()+0.5,off.getY()+0.5,off.getZ()+0.5,result);
								getWorld().spawnEntity(item);
							}
							stamp.markDirty();
							IBlockState state = getWorld().getBlockState(getPos().offset(face,2));
						}
					}
					ItemStampingOreRecipe oreRecipe = RecipeRegistry.getStampingOreRecipe(stamp.inputs.getStackInSlot(0), fluid, EnumStampType.getType(this.stamp.getStackInSlot(0)));
					if (oreRecipe != null){
						if (this.capability.getEmber() > 80.0){
							this.capability.removeAmount(80.0, true);
							powered = true;
							ItemStack result = oreRecipe.getResult(stamp.inputs.getStackInSlot(0), new FluidStack(stamp.getFluid(), stamp.getAmount()),EnumStampType.getType(this.stamp.getStackInSlot(0))).copy();
							if (oreRecipe.getClass() != null){
								stamp.inputs.extractItem(0, 1, false);
							}
							if (recipe.getFluid() != null){
								stamp.getTank().drain(recipe.getFluid(), true);
							}
							BlockPos off = getPos().offset(face,1);
							if (getWorld().getTileEntity(getPos().offset(face,3)) instanceof TileEntityBin){
								TileEntityBin bin = (TileEntityBin)getWorld().getTileEntity(getPos().offset(face,3));
								ItemStack remainder = bin.inventory.insertItem(0, result, false);
								if (remainder != EMPTY_ITEM_STACK && !getWorld().isRemote){
									EntityItem item = new EntityItem(getWorld(),off.getX()+0.5,off.getY()+0.5,off.getZ()+0.5,remainder);
									getWorld().spawnEntity(item);
								}
								bin.markDirty();
								markDirty();
							}
							else if (!getWorld().isRemote){
								EntityItem item = new EntityItem(getWorld(),off.getX()+0.5,off.getY()+0.5,off.getZ()+0.5,result);
								getWorld().spawnEntity(item);
							}
							stamp.markDirty();
							IBlockState state = getWorld().getBlockState(getPos().offset(face,2));
						}
					}
					markDirty();
					IBlockState state = getWorld().getBlockState(getPos());
				}
				else if (this.ticksExisted % 80 == 10 && powered && !getWorld().isRemote){
					powered = false;
					markDirty();
					IBlockState state = getWorld().getBlockState(getPos());
				}
			}
			else if (powered){
				powered = false;
				markDirty();
				IBlockState state = getWorld().getBlockState(getPos());
			}
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)stamp;
		}
		return super.getCapability(capability, facing);
	}
	
	public boolean dirty = false;
	
	@Override
	public void markForUpdate(){
		EventManager.markTEForUpdate(getPos(), this);
	}
	
	@Override
	public void markDirty(){
		markForUpdate();
		super.markDirty();
	}
}

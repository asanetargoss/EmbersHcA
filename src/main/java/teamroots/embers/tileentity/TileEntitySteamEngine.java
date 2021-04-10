package teamroots.embers.tileentity;

import static teamroots.embers.util.ItemUtil.EMPTY_ITEM_STACK;
import static teamroots.embers.util.ItemUtil.stackEmpty;

import java.util.List;
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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidEvent.FluidSpilledEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockAxle;
import teamroots.embers.block.BlockSteamEngine;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultMechCapability;
import teamroots.embers.power.MechCapabilityProvider;
import teamroots.embers.util.ItemUtil;
import teamroots.embers.util.Misc;

public class TileEntitySteamEngine extends TileEntity implements ITileEntityBase, ITickable {
	int ticksExisted = 0;
	BlockPos receivedFrom = null;
	int progress = 0;
	EnumFacing front = EnumFacing.UP;
	public FluidTank tank = new FluidTank(8000);
	public DefaultMechCapability capability = new DefaultMechCapability();
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntitySteamEngine.this.markDirty();
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
        	if (TileEntityFurnace.getItemBurnTime(stack) == 0){
        		return stack;
        	}
        	return super.insertItem(slot, stack, simulate);
        }
        
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate){
        	return EMPTY_ITEM_STACK;
        }
        
	};
	
	public TileEntitySteamEngine(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		tag.setInteger("progress", progress);
		tag.setInteger("front", front.getIndex());
		tag.setTag("inventory",inventory.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		tank.readFromNBT(tag.getCompoundTag("tank"));
		if (tag.hasKey("progress")){
			progress = tag.getInteger("progress");
		}
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		front = EnumFacing.getFront(tag.getInteger("front"));
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == MechCapabilityProvider.mechCapability){
			return facing == front;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			return true;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == MechCapabilityProvider.mechCapability){
			return (T)this.capability;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			return (T)tank;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)inventory;
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

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		capability.setPower(0f,null);
		updateNearby();
		world.setTileEntity(pos, null);
	}
	
	public void updateNearby(){
		for (EnumFacing f : EnumFacing.values()){
			TileEntity t = world.getTileEntity(getPos().offset(f));
			if (t != null && f == front){
				if (t.hasCapability(MechCapabilityProvider.mechCapability, Misc.getOppositeFace(f))){
					t.getCapability(MechCapabilityProvider.mechCapability, Misc.getOppositeFace(f)).setPower(capability.getPower(Misc.getOppositeFace(f)),Misc.getOppositeFace(f));
					t.markDirty();
				}
			}
		}
	}

	@Override
	public void update() {
		IBlockState state = world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockSteamEngine){
			this.front = state.getValue(BlockSteamEngine.facing);
		}
		if (progress == 0){
			if (!stackEmpty(inventory.getStackInSlot(0)) && tank.getFluid() != null && tank.getFluid().getFluid() == FluidRegistry.WATER && tank.getFluidAmount() > 10){
				ItemStack stack = inventory.getStackInSlot(0).copy();
				stack.stackSize = 1;
				int fuel = TileEntityFurnace.getItemBurnTime(stack);
				if (fuel > 0){
					progress = fuel;
					inventory.getStackInSlot(0).stackSize -= 1; // TODO: This looks dangerous
					if (stackEmpty(inventory.getStackInSlot(0))){
						inventory.setStackInSlot(0, EMPTY_ITEM_STACK);
					}
					markDirty();
				}
			}
			else if (capability.getPower(null) > 0){
				capability.setPower(0, null);
				markDirty();
			}
		}
		else {
			progress --;
			if (world.isRemote){
				for (int i = 0; i < 4; i ++){
					if (front == EnumFacing.NORTH || front == EnumFacing.SOUTH){
						float offX = 0.8125f * (float)Misc.random.nextInt(2);
						float offZ = 0.4375f * (float)Misc.random.nextInt(2);
						ParticleUtil.spawnParticleSmoke(world, 
									getPos().getX()+0.09375f + offX, getPos().getY()+1.0f, getPos().getZ()+0.28125f+offZ, 
									0.025f*(Misc.random.nextFloat()-0.5f), 0.125f*(Misc.random.nextFloat()), 0.025f*(Misc.random.nextFloat()-0.5f), 
									72, 72, 72, 0.5f, 2.0f+Misc.random.nextFloat(), 24);
					}
					if (front == EnumFacing.EAST || front == EnumFacing.WEST){
						float offZ = 0.8125f * (float)Misc.random.nextInt(2);
						float offX = 0.4375f * (float)Misc.random.nextInt(2);
						ParticleUtil.spawnParticleSmoke(world, 
									getPos().getX()+0.28125f + offX, getPos().getY()+1.0f, getPos().getZ()+0.09375f+offZ, 
									0.025f*(Misc.random.nextFloat()-0.5f), 0.125f*(Misc.random.nextFloat()), 0.025f*(Misc.random.nextFloat()-0.5f), 
									72, 72, 72, 0.5f, 2.0f+Misc.random.nextFloat(), 24);
					}
				}
			}
			tank.drain(4, true);
			if (tank.getFluidAmount() <= 0){
				progress = 0;
				capability.setPower(0.0, null);
				markDirty();
			}
			if (capability.getPower(null) < 20.0){
				capability.setPower(20.0, null);
				markDirty();
			}
		}
		updateNearby();
	}
}

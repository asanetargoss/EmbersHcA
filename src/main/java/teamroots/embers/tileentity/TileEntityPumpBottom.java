package teamroots.embers.tileentity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidEvent.FluidSpilledEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockAxle;
import teamroots.embers.block.BlockPump;
import teamroots.embers.block.BlockSteamEngine;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultMechCapability;
import teamroots.embers.power.MechCapabilityProvider;
import teamroots.embers.util.FluidUtil;
import teamroots.embers.util.Misc;

public class TileEntityPumpBottom extends TileEntity implements ITileEntityBase, ITickable {
	int ticksExisted = 0;
	int progress = 0;
	EnumFacing front = EnumFacing.UP;
	public DefaultMechCapability capability = new DefaultMechCapability(){
		@Override
		public double getPower(EnumFacing face){
			if (face != null){
				return 0.0;
			}
			return super.getPower(face);
		}
		
		@Override
		public void onContentsChanged(){
			TileEntityPumpBottom.this.markDirty();
		}
	};
	
	public TileEntityPumpBottom(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setInteger("progress", progress);
		tag.setInteger("front", front.getIndex());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
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
			return facing.getAxis() == front.getAxis();
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == MechCapabilityProvider.mechCapability){
			return (T)this.capability;
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
		world.setTileEntity(pos, null);
	}
	
	public boolean attemptPump(BlockPos pos){
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IFluidBlock && ((IFluidBlock)state.getBlock()).canDrain(world, pos) || state.getBlock() instanceof BlockStaticLiquid){
			if (capability.getPower(null) > 0 || state.getBlock() == Blocks.WATER){
				FluidStack stack = FluidUtil.getFluid(world, pos, state);
				if (stack != null){
					TileEntityPumpTop t = (TileEntityPumpTop)world.getTileEntity(getPos().up());
					int filled = t.getTank().fill(stack, false);
					if (filled == stack.amount){
						if (!world.isRemote){
							t.getTank().fill(stack, true);
						}
						t.markDirty();
						world.setBlockToAir(pos);
						world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
						world.notifyNeighborsOfStateChange(pos.north(), Blocks.AIR);
						world.notifyNeighborsOfStateChange(pos.south(), Blocks.AIR);
						world.notifyNeighborsOfStateChange(pos.east(), Blocks.AIR);
						world.notifyNeighborsOfStateChange(pos.west(), Blocks.AIR);
						world.notifyNeighborsOfStateChange(pos.up(), Blocks.AIR);
						world.notifyNeighborsOfStateChange(pos.down(), Blocks.AIR);
						world.notifyNeighborsOfStateChange(pos, Blocks.AIR);
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void update() {
		IBlockState state = world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockPump){
			this.front = state.getValue(BlockPump.facing);
		}
		this.progress += Math.min(100, Math.max(0, (int)Math.floor(0.5*capability.getPower(null))));
		if (this.progress > 400){
			progress -= 400;
			boolean doContinue = true;
			for (int r = 0; r < 6 && doContinue; r ++){
				for (int i = -r; i < r+1 && doContinue; i ++){
					for (int j = -r; j < 1 && doContinue; j ++){
						for (int k = -r; k < r+1 && doContinue; k ++){
							doContinue = attemptPump(getPos().add(i, j-1, k));
						}
					}
				}
			}
		}
		markDirty();
	}
}

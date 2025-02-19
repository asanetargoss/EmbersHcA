package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.tileentity.TileEntityEmitter;
import teamroots.embers.tileentity.TileEntityItemPipe;

public class BlockEmberEmitter extends BlockTEBase {
	public static final PropertyDirection facing = PropertyDirection.create("facing");
	
	public BlockEmberEmitter(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return true;
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(facing).getIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(facing,EnumFacing.getFront(meta));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(facing, face);
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return side != state.getValue(facing);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityEmitter();
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (world.getTileEntity(pos) instanceof TileEntityEmitter){
			((TileEntityEmitter)world.getTileEntity(pos)).updateNeighbors(world);
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block){
		TileEntity t = world.getTileEntity(pos);
		if (t instanceof TileEntityEmitter){
			((TileEntityEmitter)t).updateNeighbors(world);
			t.markDirty();
		}
		if (world.isAirBlock(pos.offset(state.getValue(facing),-1))){
			world.setBlockToAir(pos);
			this.dropBlockAsItem(world, pos, state, 0);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		switch (state.getValue(facing)){
		case UP:
			return new AxisAlignedBB(0.25,0,0.25,0.75,0.9375,0.75);
		case DOWN:
			return new AxisAlignedBB(0.25,0.0625,0.25,0.75,1.0,0.75);
		case NORTH:
			return new AxisAlignedBB(0.25,0.25,0.0625,0.75,0.75,1.0);
		case SOUTH:
			return new AxisAlignedBB(0.25,0.25,0,0.75,0.75,0.9375);
		case WEST:
			return new AxisAlignedBB(0.0625,0.25,0.25,1.0,0.75,0.75);
		case EAST:
			return new AxisAlignedBB(0.0,0.25,0.25,0.9375,0.75,0.75);
		}
		return new AxisAlignedBB(0.25,0,0.25,0.75,0.9375,0.75);
	}
}

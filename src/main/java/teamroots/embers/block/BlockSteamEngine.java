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
import net.minecraftforge.fluids.FluidTank;
import teamroots.embers.tileentity.TileEntityAxle;
import teamroots.embers.tileentity.TileEntityGearbox;
import teamroots.embers.tileentity.TileEntityItemExtractor;
import teamroots.embers.tileentity.TileEntityRelay;
import teamroots.embers.tileentity.TileEntitySteamEngine;
import teamroots.embers.util.Misc;

public class BlockSteamEngine extends BlockTEBase {
	public static final PropertyDirection facing = PropertyDirection.create("facing");
	
	public BlockSteamEngine(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
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
		return getDefaultState().withProperty(facing, Misc.getOppositeFace(placer.getHorizontalFacing()));
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return side != state.getValue(facing);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySteamEngine();
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block){
		TileEntitySteamEngine p = (TileEntitySteamEngine)world.getTileEntity(pos);
		p.updateNearby();
		p.markDirty();
	}
}

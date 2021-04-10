package teamroots.embers.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.particle.ParticleUtil;

public class BlockGlow extends BlockBase {
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.25,0.25,0.25,0.75,0.75,0.75);
	
	public BlockGlow(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
		this.needsRandomTick = true;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		return new ArrayList<ItemStack>();
	}

    @Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
    {
    }

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list){
		
	}
	
	@Override
	public int tickRate(World world){
		return 1;
	}
	
	@Override
	public boolean requiresUpdates(){
		return true;
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random){
		for (int i = 0; i < 2; i ++){
			ParticleUtil.spawnParticleGlow(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.015f, (random.nextFloat()-0.5f)*0.003f, 255, 64, 16, 3.0f, 120);
		}
		for (int i = 0; i < 2; i ++){
			ParticleUtil.spawnParticleStar(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.003f, (random.nextFloat()-0.5f)*0.003f, 255, 255, 16, 3.0f, 120);
		}
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return AABB_BASE;
    }
}

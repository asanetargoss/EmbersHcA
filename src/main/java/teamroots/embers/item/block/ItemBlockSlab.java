package teamroots.embers.item.block;

import static teamroots.embers.util.ItemUtil.EMPTY_ITEM_STACK;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.item.IModeledItem;
import teamroots.embers.util.ItemUtil;

public class ItemBlockSlab extends ItemBlock implements IModeledItem {
	Block doubleSlab;
	CreativeTabs tab;
	public ItemBlockSlab(Block block, Block doubleSlabBlock) {
		super(block);
		doubleSlab = doubleSlabBlock;
		setRegistryName(block.getRegistryName());
		tab = Embers.tab;
		GameRegistry.register(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
	
	@Override
	public CreativeTabs getCreativeTab(){
		return tab;
	}
	
	public void decrementHeldStack(EntityPlayer player, ItemStack stack, EnumHand hand){
		if (!player.capabilities.isCreativeMode){
			stack.stackSize -= 1; // TODO: This looks dangerous
			if (stack.stackSize == 0){
				player.setItemStackToSlot(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND, EMPTY_ITEM_STACK);
			}
		}
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize == 0)
		{
			return EnumActionResult.FAIL;
		}
		else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);

			if (iblockstate.getBlock() == getBlock())
			{
				BlockSlab.EnumBlockHalf enumblockhalf = iblockstate.getValue(BlockSlab.HALF);

				if ((side == EnumFacing.UP && enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM
					     || side == EnumFacing.DOWN && enumblockhalf == BlockSlab.EnumBlockHalf.TOP))
				{
					IBlockState iblockstate1 = this.doubleSlab.getDefaultState();

					if (worldIn.checkNoEntityCollision(
						this.doubleSlab.getBoundingBox(iblockstate1, worldIn, pos)) && worldIn
							                                                                         .setBlockState(pos,
							                                                                                        iblockstate1,
							                                                                                        3))
					{
						worldIn
							.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F,
							                 pos.getZ() + 0.5F,
							                 this.doubleSlab.getSoundType().getPlaceSound(),
							                 SoundCategory.BLOCKS,(this.doubleSlab.getSoundType().getVolume() + 1.0F) / 2.0F,
							                 this.doubleSlab.getSoundType().getPitch() * 0.8F,true);
						stack.stackSize -= 1; // TODO: This looks dangerous
					}

					return EnumActionResult.SUCCESS;
				}
			}

			return (this.func_180615_a(stack, worldIn, pos.offset(side)) || (super.onItemUse(stack, playerIn,
			                                                                                       worldIn, pos, hand, side,
			                                                                                       hitX, hitY, hitZ) == EnumActionResult.SUCCESS ? true : false)) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos p_179222_2_, EnumFacing p_179222_3_, EntityPlayer p_179222_4_, ItemStack p_179222_5_)
	{
		BlockPos blockpos1 = p_179222_2_;
		IBlockState iblockstate = worldIn.getBlockState(p_179222_2_);

		if (iblockstate.getBlock() == getBlock())
		{
			boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;

			if ((p_179222_3_ == EnumFacing.UP && !flag || p_179222_3_ == EnumFacing.DOWN && flag))
			{
				return true;
			}
		}

		p_179222_2_ = p_179222_2_.offset(p_179222_3_);
		IBlockState iblockstate1 = worldIn.getBlockState(p_179222_2_);
		return iblockstate1.getBlock() == getBlock() || super.canPlaceBlockOnSide(worldIn, blockpos1, p_179222_3_,
		                                                                         p_179222_4_, p_179222_5_);
	}

	private boolean func_180615_a(ItemStack p_180615_1_, World worldIn, BlockPos p_180615_3_)
	{
		IBlockState iblockstate = worldIn.getBlockState(p_180615_3_);

		if (iblockstate.getBlock() == getBlock())
		{
			IBlockState iblockstate1 = this.doubleSlab.getDefaultState();

			if (worldIn.checkNoEntityCollision(
				this.doubleSlab.getBoundingBox(iblockstate1, worldIn, p_180615_3_)) && worldIn.setBlockState(
				p_180615_3_, iblockstate1, 3))
			{
				worldIn.playSound(p_180615_3_.getX() + 0.5F,
				                        p_180615_3_.getY() + 0.5F,
				                        p_180615_3_.getZ() + 0.5F,
				                        this.doubleSlab.getSoundType().getPlaceSound(),
				                        SoundCategory.BLOCKS, (this.doubleSlab.getSoundType().getVolume() + 1.0F) / 2.0F,
				                        this.doubleSlab.getSoundType().getPitch() * 0.8F, true);
				p_180615_1_.stackSize -= 1; //TODO: This looks dangerous
			}

			return true;
		}

		return false;
	}
}

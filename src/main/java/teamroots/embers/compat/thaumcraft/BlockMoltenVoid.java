package teamroots.embers.compat.thaumcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.block.IModeledBlock;
import teamroots.embers.compat.RegistryManagerCompat;

public class BlockMoltenVoid extends BlockFluidClassic implements IModeledBlock {
	public static FluidStack stack = new FluidStack(RegistryManagerCompat.ThaumcraftCompat.fluid_molten_void,1000);
	
	public BlockMoltenVoid(String name, boolean addToTab) {
		super(RegistryManagerCompat.ThaumcraftCompat.fluid_molten_void,Material.LAVA);
		setRegistryName(Embers.MODID+":"+name);
		if (addToTab){
			this.setCreativeTab(Embers.tab);
		}
		this.setQuantaPerBlock(6);
		RegistryManagerCompat.ThaumcraftCompat.fluid_molten_void.setBlock(this);
		GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess world, BlockPos pos, EnumFacing side){
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void initModel(){
        Block block = RegistryManagerCompat.ThaumcraftCompat.block_molten_void;
        Item item = Item.getItemFromBlock(block);   

        ModelBakery.registerItemVariants(item);
        
        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Embers.MODID + ":fluid", stack.getFluid().getName());

        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);

        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
                return modelResourceLocation;
            }
        });
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getBlockState().getBaseState().withProperty(LEVEL, meta);
    }
}

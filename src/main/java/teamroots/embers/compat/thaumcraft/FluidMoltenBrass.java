package teamroots.embers.compat.thaumcraft;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.compat.RegistryManagerCompat;

public class FluidMoltenBrass extends Fluid {
	public FluidMoltenBrass() {
		super("brass",new ResourceLocation(Embers.MODID+":blocks/molten_brass_still"),new ResourceLocation(Embers.MODID+":blocks/molten_brass_flowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManagerCompat.ThaumcraftCompat.block_molten_brass);
		setUnlocalizedName("brass");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}

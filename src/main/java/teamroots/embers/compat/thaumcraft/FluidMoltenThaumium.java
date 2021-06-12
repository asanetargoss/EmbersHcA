package teamroots.embers.compat.thaumcraft;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.compat.RegistryManagerCompat;

public class FluidMoltenThaumium extends Fluid {
	public FluidMoltenThaumium() {
		super("thaumium",new ResourceLocation(Embers.MODID+":blocks/molten_thaumium_still"),new ResourceLocation(Embers.MODID+":blocks/molten_thaumium_flowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		//setBlock(RegistryManagerCompat.ThaumcraftCompat.block_molten_thaumium);
		setUnlocalizedName("thaumium");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}

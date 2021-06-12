package teamroots.embers.compat;

import static teamroots.embers.util.ItemUtil.EMPTY_ITEM_STACK;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import teamroots.embers.RegistryManager;
import teamroots.embers.compat.thaumcraft.BlockMoltenBrass;
import teamroots.embers.compat.thaumcraft.BlockMoltenQuicksilver;
import teamroots.embers.compat.thaumcraft.BlockMoltenThaumium;
import teamroots.embers.compat.thaumcraft.BlockMoltenVoid;
import teamroots.embers.compat.thaumcraft.FluidMoltenBrass;
import teamroots.embers.compat.thaumcraft.FluidMoltenQuicksilver;
import teamroots.embers.compat.thaumcraft.FluidMoltenThaumium;
import teamroots.embers.compat.thaumcraft.FluidMoltenVoid;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.ItemTCBase;

public class RegistryManagerCompat {
	public static Map<String, ModContainer> modMap;
	
	public static boolean thaumcraftLoaded() { return RegistryManagerCompat.modMap.containsKey("thaumcraft"); }
	
	public static class ThaumcraftCompat {
		
		public static Block block_molten_quicksilver, block_molten_brass, block_molten_thaumium, block_molten_void;
		public static Fluid fluid_molten_quicksilver, fluid_molten_brass, fluid_molten_thaumium, fluid_molten_void;
		
		public static void registerFluids(){
			FluidRegistry.registerFluid(fluid_molten_quicksilver = new FluidMoltenQuicksilver());
			RegistryManager.blocks.add(block_molten_quicksilver = (new BlockMoltenQuicksilver("quicksilver",false)));
			FluidRegistry.addBucketForFluid(fluid_molten_quicksilver);
			FluidRegistry.registerFluid(fluid_molten_brass = new FluidMoltenBrass());
			RegistryManager.blocks.add(block_molten_brass = (new BlockMoltenBrass("brass",false)));
			FluidRegistry.addBucketForFluid(fluid_molten_brass);
			FluidRegistry.registerFluid(fluid_molten_thaumium = new FluidMoltenThaumium());
			RegistryManager.blocks.add(block_molten_thaumium = (new BlockMoltenThaumium("thaumium",false)));
			FluidRegistry.addBucketForFluid(fluid_molten_thaumium);
			FluidRegistry.registerFluid(fluid_molten_void = new FluidMoltenVoid());
			RegistryManager.blocks.add(block_molten_void = (new BlockMoltenVoid("void",false)));
			FluidRegistry.addBucketForFluid(fluid_molten_void);
		}
		
		protected static ItemStack tcStack(ItemTCBase item, int count, @Nullable String variant) {
			int meta = 0;
			if (variant != null) {
				String[] variants = item.getVariantNames();
				while (meta < variants.length - 1 && variants[meta] != variant) {
					++meta;
				}
			}
			return new ItemStack(item, count, meta);
		}
		
		public static void initRecipes() {
			GameRegistry.addRecipe(new ShapelessOreRecipe(tcStack((ItemTCBase)ItemsTC.plate, 1, "brass"),
					new Object[]{"ingotBrass","ingotBrass","ingotBrass","ingotBrass",RegistryManager.tinker_hammer}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(tcStack((ItemTCBase)ItemsTC.plate, 1, "thaumium"),
					new Object[]{"ingotThaumium","ingotThaumium","ingotThaumium","ingotThaumium",RegistryManager.tinker_hammer}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(tcStack((ItemTCBase)ItemsTC.plate, 1, "void"),
					new Object[]{"ingotVoid","ingotVoid","ingotVoid","ingotVoid",RegistryManager.tinker_hammer}));

			// Recipes for melting quicksilver/alloys
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("quicksilver",new FluidStack(ThaumcraftCompat.fluid_molten_quicksilver,144)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetQuicksilver",new FluidStack(ThaumcraftCompat.fluid_molten_quicksilver,16)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotBrass",new FluidStack(ThaumcraftCompat.fluid_molten_brass,144)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetBrass",new FluidStack(ThaumcraftCompat.fluid_molten_brass,16)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("plateBrass",new FluidStack(ThaumcraftCompat.fluid_molten_brass,144)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotThaumium",new FluidStack(ThaumcraftCompat.fluid_molten_thaumium,144)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetThaumium",new FluidStack(ThaumcraftCompat.fluid_molten_thaumium,16)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("plateThaumium",new FluidStack(ThaumcraftCompat.fluid_molten_thaumium,144)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotVoid",new FluidStack(ThaumcraftCompat.fluid_molten_void,144)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetVoid",new FluidStack(ThaumcraftCompat.fluid_molten_void,16)));
			RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("plateVoid",new FluidStack(ThaumcraftCompat.fluid_molten_void,144)));
			
			// Recipe for melting quicksilver ore, with appropriate bonus
			RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(new ItemStack(BlocksTC.oreCinnabar),new FluidStack(RegistryManagerCompat.ThaumcraftCompat.fluid_molten_quicksilver,288), false, false));
			
			// Ore cluster melting recipes, with appropriate bonus
			RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(tcStack((ItemTCBase)ItemsTC.clusters, 1, "cinnabar"),new FluidStack(RegistryManagerCompat.ThaumcraftCompat.fluid_molten_quicksilver,576), true, false));
			RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(tcStack((ItemTCBase)ItemsTC.clusters, 1, "iron"),new FluidStack(RegistryManager.fluid_molten_iron,576), true, false));
			RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(tcStack((ItemTCBase)ItemsTC.clusters, 1, "gold"),new FluidStack(RegistryManager.fluid_molten_gold,576), true, false));
			RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(tcStack((ItemTCBase)ItemsTC.clusters, 1, "silver"),new FluidStack(RegistryManager.fluid_molten_silver,576), true, false));
			RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(tcStack((ItemTCBase)ItemsTC.clusters, 1, "copper"),new FluidStack(RegistryManager.fluid_molten_copper,576), true, false));
			RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(tcStack((ItemTCBase)ItemsTC.clusters, 1, "lead"),new FluidStack(RegistryManager.fluid_molten_lead,576), true, false));
			RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(tcStack((ItemTCBase)ItemsTC.clusters, 1, "tin"),new FluidStack(RegistryManager.fluid_molten_tin,576), true, false));
			
			// Stamping recipes to extract the respective fluids
			RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(EMPTY_ITEM_STACK,new FluidStack(ThaumcraftCompat.fluid_molten_quicksilver,144),EnumStampType.TYPE_BAR,tcStack((ItemTCBase)ItemsTC.quicksilver, 1, null),false,false));
			RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(EMPTY_ITEM_STACK,new FluidStack(ThaumcraftCompat.fluid_molten_brass,144),EnumStampType.TYPE_BAR,tcStack((ItemTCBase)ItemsTC.ingots, 1, "brass"),true,false));
			RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(EMPTY_ITEM_STACK,new FluidStack(ThaumcraftCompat.fluid_molten_thaumium,144),EnumStampType.TYPE_BAR,tcStack((ItemTCBase)ItemsTC.ingots, 1, "thaumium"),true,false));
			RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(EMPTY_ITEM_STACK,new FluidStack(ThaumcraftCompat.fluid_molten_void,144),EnumStampType.TYPE_BAR,tcStack((ItemTCBase)ItemsTC.ingots, 1, "void"),true,false));
			RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(EMPTY_ITEM_STACK,new FluidStack(ThaumcraftCompat.fluid_molten_brass,144),EnumStampType.TYPE_PLATE,tcStack((ItemTCBase)ItemsTC.plate, 1, "brass"),true,false));
			RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(EMPTY_ITEM_STACK,new FluidStack(ThaumcraftCompat.fluid_molten_thaumium,144),EnumStampType.TYPE_PLATE,tcStack((ItemTCBase)ItemsTC.plate, 1, "thaumium"),true,false));
			RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(EMPTY_ITEM_STACK,new FluidStack(ThaumcraftCompat.fluid_molten_void,144),EnumStampType.TYPE_PLATE,tcStack((ItemTCBase)ItemsTC.plate, 1, "void"),true,false));
		}
		
	}

	public static void registerFluids(){
		if (thaumcraftLoaded()) {
			ThaumcraftCompat.registerFluids();
		}
	}
	
	public static void initRecipes(){
		if (thaumcraftLoaded()) {
			ThaumcraftCompat.initRecipes();
		}
	}
}

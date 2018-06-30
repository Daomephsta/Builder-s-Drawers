package daomephsta.buildersdrawers.client;

import com.jaquadro.minecraft.chameleon.Chameleon;

import daomephsta.buildersdrawers.common.BuildersDrawers;
import daomephsta.buildersdrawers.common.blocks.BlockRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = BuildersDrawers.MODID, value = Side.CLIENT)
public class ModelRegistry
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		Chameleon.instance.modelRegistry.registerModel(new ModelRegisterBuildersDrawer(BlockRegistry.BUILDERS_DRAWERS_FULL1));
		Chameleon.instance.modelRegistry.registerModel(new ModelRegisterBuildersDrawer(BlockRegistry.BUILDERS_DRAWERS_FULL2));
		Chameleon.instance.modelRegistry.registerModel(new ModelRegisterBuildersDrawer(BlockRegistry.BUILDERS_DRAWERS_FULL4));
		Chameleon.instance.modelRegistry.registerModel(new ModelRegisterBuildersDrawer(BlockRegistry.BUILDERS_DRAWERS_HALF2));
		Chameleon.instance.modelRegistry.registerModel(new ModelRegisterBuildersDrawer(BlockRegistry.BUILDERS_DRAWERS_HALF4));
	}
}

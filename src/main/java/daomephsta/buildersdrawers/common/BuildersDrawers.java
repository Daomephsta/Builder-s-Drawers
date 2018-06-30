package daomephsta.buildersdrawers.common;

import java.io.File;

import daomephsta.buildersdrawers.common.blockshapes.ShapeMaterialBlockRelationships;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BuildersDrawers.MODID, name = BuildersDrawers.NAME, version = BuildersDrawers.VERSION, dependencies = BuildersDrawers.DEPENDENCIES)
public class BuildersDrawers
{
	public static final String MODID = "buildersdrawers",
			NAME = "Builder's Drawers", VERSION = "0.0.1",
			DEPENDENCIES = "required-after: storagedrawers";
	
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		File configSubdir = new File(event.getModConfigurationDirectory(), MODID);
		configSubdir.mkdirs();
		ShapeMaterialBlockRelationships.load(configSubdir);
	}
}

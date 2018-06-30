package daomephsta.buildersdrawers.common.blockshapes;

import java.util.HashMap;
import java.util.Map;

class BlockMaterial
{
	private static final Map<String, BlockMaterial> MATERIALS = new HashMap<>();
	
	private final String name;
	
	private BlockMaterial(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return "BlockMaterial [name=" + name + "]";
	}

	public static BlockMaterial fromName(String name)
	{
		return MATERIALS.computeIfAbsent(name, BlockMaterial::new);
	}
}

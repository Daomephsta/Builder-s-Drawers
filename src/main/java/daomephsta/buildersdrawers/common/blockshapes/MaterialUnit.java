package daomephsta.buildersdrawers.common.blockshapes;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public enum MaterialUnit
{
	VOXEL("vx", 1),
	BLOCK("b", 16 * 16 * 16);
	
	private static final Map<String, MaterialUnit> NAME_TO_UNIT = new HashMap<>();
	static
	{
		for(MaterialUnit unit : values())
		{
			NAME_TO_UNIT.put(unit.getSymbol(), unit);
		}
	}
	
	private final String symbol;
	private final int voxelCount;
	
	private MaterialUnit(String unit, int voxelCount)
	{
		this.symbol = unit;
		this.voxelCount = voxelCount;
	}
	
	public String getSymbol()
	{
		return symbol;
	}
	
	public int getVoxelCount()
	{
		return voxelCount;
	}
	
	@Nullable
	public static MaterialUnit getUnit(String symbol)
	{
		return NAME_TO_UNIT.get(symbol);
	}
}

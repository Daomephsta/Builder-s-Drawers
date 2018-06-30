package daomephsta.buildersdrawers.common.blockshapes;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.primitives.Floats;
import com.google.gson.*;

import net.minecraft.util.JsonUtils;

class BlockShape
{
	private static final Pattern QUANTIFIED_NUMBER;
	static
	{
		StringBuilder patternBuilder = new StringBuilder("(\\d+.?\\d*)(");
		String unitRegex = Arrays.stream(MaterialUnit.values()).map(MaterialUnit::getSymbol).collect(Collectors.joining("|"));
		patternBuilder.append(unitRegex);
		patternBuilder.append(")");
		QUANTIFIED_NUMBER = Pattern.compile(patternBuilder.toString());
	} 
	private static final Map<String, BlockShape> SHAPES = new HashMap<>();
	
	private final String name;
	private final int materialQuantity;
	
	private BlockShape(String name, int materialQuantity)
	{
		this.name = name;
		this.materialQuantity = materialQuantity;
		SHAPES.put(name, this);
	}
	
	public static BlockShape fromName(String name)
	{
		BlockShape shape = SHAPES.get(name);
		if(shape == null)
			throw new IllegalArgumentException("No shape registered for the name " + name);
		return shape;
	}
	
	public static BlockShape createFromJson(JsonElement json)
	{
		JsonObject jsonObj = json.getAsJsonObject();
		String materialQuantity = JsonUtils.getString(jsonObj, "material_quantity");
		Matcher quantifiedNumberMatcher = QUANTIFIED_NUMBER.matcher(materialQuantity);
		if(quantifiedNumberMatcher.matches())
		{
			String numberString = quantifiedNumberMatcher.group(1);
			Float number = Floats.tryParse(numberString);
			if (number == null)
				throw new JsonSyntaxException("Could not parse " + numberString + " as a decimal");
			
			String unitString = quantifiedNumberMatcher.group(2);
			MaterialUnit unit = MaterialUnit.getUnit(unitString);
			if(unit == null)
				throw new JsonSyntaxException(unitString + " is not a valid material unit");
			
			return new BlockShape(JsonUtils.getString(jsonObj, "name"), (int) Math.ceil(number * unit.getVoxelCount()));
		}
		else throw new JsonSyntaxException("Could not parse " + materialQuantity + " as a material quantity");
	}

	public String getName()
	{
		return name;
	}

	public int getMaterialQuantity()
	{
		return materialQuantity;
	}
	
	@Override
	public String toString()
	{
		return "BlockShape [name=" + name + ", materialQuantity=" + materialQuantity + "]";
	}
}

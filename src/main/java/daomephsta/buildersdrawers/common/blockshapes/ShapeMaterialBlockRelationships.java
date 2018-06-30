package daomephsta.buildersdrawers.common.blockshapes;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.google.common.primitives.Ints;
import com.google.gson.*;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ShapeMaterialBlockRelationships
{
	private static final Gson DESERIALISER = new GsonBuilder()
			.registerTypeAdapter(ItemStack.class, (JsonDeserializer<ItemStack>) ShapeMaterialBlockRelationships::deserialiseItemStack)
			.registerTypeAdapter(BlockShape.class, (JsonDeserializer<BlockShape>) ShapeMaterialBlockRelationships::deserialiseBlockShape).create();
	private static final Splitter STACK_DEFINITION_PARTS = Splitter.on(':');
	private static final MutableValueGraph<Object, BlockShape> RELATIONSHIPS = ValueGraphBuilder
			.undirected().build();

	public BlockShape shapeOf(ItemStack shapedBlock)
	{
		/*
		 * Checking is done on insertion, so it is safe to assume a 
		 * block is only connected to one node (its material) by one edge
		 */
		Set<Object> adjacentNodes = RELATIONSHIPS.adjacentNodes(shapedBlock);
		return RELATIONSHIPS.edgeValue(Iterables.getFirst(adjacentNodes, null),
				shapedBlock);
	}

	private static void addRelationship(BlockMaterial material, BlockShape shape, ItemStack shapedBlock)
	{
		if (!RELATIONSHIPS.addNode(shapedBlock))
			throw new IllegalStateException(String.format("%s is already registered as a shape", shapedBlock));
		BlockShape existingShape = RELATIONSHIPS.putEdgeValue(material, shapedBlock,
				shape);
		if (existingShape != null)
			throw new IllegalStateException(String.format("%s is already registered as the %s shape for %s", 
					shapedBlock, existingShape, material));
	}
	
	public static void load(File configDir)
	{
		File shapesDir = new File(configDir, "blockshapes");
		shapesDir.mkdirs();
		BlockShapes.loadShapes(shapesDir);
		
		File materialsDir = new File(configDir, "materials");
		materialsDir.mkdirs();
		loadMaterials(materialsDir);
	}
	
	static void loadMaterials(File materialsDir)
	{
		try
		{
			for(File materialsFile : materialsDir.listFiles())
			{
				JsonArray jsonArray = JsonUtils.getJsonArray(new JsonParser().parse(new FileReader(materialsFile)), materialsFile.getPath());
				for(JsonElement element : jsonArray)
				{
					JsonObject jsonObj = element.getAsJsonObject();
					BlockMaterial material = BlockMaterial.fromName(JsonUtils.getString(jsonObj, "name"));
					JsonObject shapes = JsonUtils.getJsonObject(jsonObj, "shapes");
					for(Entry<String, JsonElement> entry : shapes.entrySet())
					{
						BlockShape shape = BlockShape.fromName(entry.getKey());
						// The value can be either a single itemstack, or an array of stacks
						if(entry.getValue().isJsonArray())
						{
							ItemStack[] shapedBlocks = DESERIALISER.fromJson(entry.getValue(), ItemStack[].class);
							for(ItemStack shapedBlock : shapedBlocks)
							{
								addRelationship(material, shape, shapedBlock);
							}
						}
						else
						{
							ItemStack shapedBlock = DESERIALISER.fromJson(entry.getValue(), ItemStack.class);
							addRelationship(material, shape, shapedBlock);
						}
					}
				}
			}
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	private static ItemStack deserialiseItemStack(JsonElement json, Type type, JsonDeserializationContext context)
	{
		String stackString = json.getAsString();
		List<String> parts = STACK_DEFINITION_PARTS.splitToList(stackString);
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts.get(0), parts.get(1)));
		if(item == null)
			throw new JsonSyntaxException("No item found with registry ID " + parts.get(0) + ":" + parts.get(1));
		int meta = 0;
		if(parts.size() > 2)
		{
			Integer parsed = Ints.tryParse(parts.get(2));
			if(parsed == null)
				throw new JsonSyntaxException("Could not parse " + parts.get(2) + " as an integer");
			meta = parsed.intValue();
		}
		return new ItemStack(item, 1, meta);
	}
	
	private static BlockShape deserialiseBlockShape(JsonElement json, Type type, JsonDeserializationContext context)
	{
		return BlockShape.fromName(json.getAsString());
	}
}

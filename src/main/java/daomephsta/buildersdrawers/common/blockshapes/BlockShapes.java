package daomephsta.buildersdrawers.common.blockshapes;

import java.io.*;

import com.google.gson.*;

import net.minecraft.util.JsonUtils;

class BlockShapes
{	
	static void loadShapes(File shapesDir)
	{
		try
		{
			for(File shapesFile : shapesDir.listFiles())
			{
				JsonArray jsonArray = JsonUtils.getJsonArray(new JsonParser().parse(new FileReader(shapesFile)), shapesFile.getPath());
				for(JsonElement element : jsonArray)
				{
					BlockShape.createFromJson(element);
				}
			}
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
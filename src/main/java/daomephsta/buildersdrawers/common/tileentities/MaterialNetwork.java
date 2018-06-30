package daomephsta.buildersdrawers.common.tileentities;

import java.util.*;

import com.google.common.collect.Iterables;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MaterialNetwork
{
	// Networking variables
	private static int nextID = 0;
	// An ID for debug purposes. Not synced between sides.
	private final int networkID;
	private final World world;
	private BlockPos master;
	private Set<BlockPos> nodes = new HashSet<>();
	
	// Payload
	private Material storedMaterial;
	private int storedQuantity;

	private MaterialNetwork(World world, BlockPos master)
	{
		this.networkID = nextID++;
		this.world = world;
		this.master = master;
		nodes.add(master);
	}
	
	public static MaterialNetwork createSingletonNetwork(World world, BlockPos master)
	{
		return new MaterialNetwork(world, master);
	}
	
	public static MaterialNetwork findNetworkFor(TileEntityBuildersDrawers drawers)
	{
		Collection<MaterialNetwork> neighbouringNetworks = new HashSet<>();
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos neighbourPos = drawers.getPos().offset(facing);
			TileEntity te = drawers.getWorld().getTileEntity(neighbourPos);
			if (te instanceof TileEntityBuildersDrawers)
				neighbouringNetworks.add(((TileEntityBuildersDrawers) te).getMaterialNetwork());
		}
		if(neighbouringNetworks.isEmpty())
			return createSingletonNetwork(drawers.getWorld(), drawers.getPos());
		if(neighbouringNetworks.size() == 1)
			return Iterables.getOnlyElement(neighbouringNetworks);
		return merge(neighbouringNetworks);
	}
	
	private static MaterialNetwork merge(Collection<MaterialNetwork> networks)
	{
		/* Which network the others are merged with doesn't matter, so use the 
		 * first one returned by the iterator.*/
		MaterialNetwork result = Iterables.getFirst(networks, networks.iterator().next());
		networks.remove(result);
		for(MaterialNetwork network : networks)
		{
			result = result.merge(network);
		}
		return result;
	}
	
	private MaterialNetwork merge(MaterialNetwork other)
	{
		for(BlockPos nodePos : other.nodes)
		{
			TileEntity te = world.getTileEntity(nodePos);
			if (te instanceof TileEntityBuildersDrawers)
				((TileEntityBuildersDrawers) te).connectTo(this);
		}
		return this;
	}
	
	MaterialNetwork connect(BlockPos pos)
	{
		nodes.add(pos);
		return this;
	}
	
	void disconnect(BlockPos pos)
	{
		// Remove the node from the network 
		nodes.remove(pos);
		Collection<BlockPos> neighbouringNodes = new ArrayList<>(); 
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos neighbourPos = pos.offset(facing);
			TileEntity te = world.getTileEntity(neighbourPos);
			if (te instanceof TileEntityBuildersDrawers && nodes.contains(neighbourPos))
				neighbouringNodes.add(neighbourPos);
		}
		// If there are multiple neighbouring nodes, the network needs to split
		if (neighbouringNodes.size() > 1)
		{
			for (BlockPos neighbourNode : neighbouringNodes)
			{
				TileEntity te = world.getTileEntity(neighbourNode);
				((TileEntityBuildersDrawers) te).connectTo(constructNewNetwork(world, neighbourNode));
			}
		}
	}
	
	private static MaterialNetwork constructNewNetwork(World world, BlockPos master)
	{
		MaterialNetwork newNetwork = new MaterialNetwork(world, master);
		/** Breadth first search **/
		// Nodes to visit
		Queue<BlockPos> toVisit = new ArrayDeque<>();
		// Already visited nodes
		Set<BlockPos> visited = new HashSet<>();
		// Add the root node to the visit queue
		toVisit.add(master);
		while(!toVisit.isEmpty())
		{
			BlockPos pos = toVisit.remove();
			for(EnumFacing facing : EnumFacing.VALUES)
			{
				BlockPos neighbourPos = pos.offset(facing);
				// Skip previously visited nodes
				if(visited.contains(neighbourPos)) continue;
				TileEntity te = world.getTileEntity(neighbourPos);
				if (te instanceof TileEntityBuildersDrawers)
				{
					toVisit.add(neighbourPos);
					((TileEntityBuildersDrawers) te).connectTo(newNetwork);
				}
			}
			visited.add(pos);
		}
		return newNetwork;
	}

	public Material getStoredMaterial()
	{
		return storedMaterial;
	}

	public void setStoredMaterial(Material storedMaterial)
	{
		this.storedMaterial = storedMaterial;
	}

	public int getStoredQuantity()
	{
		return storedQuantity;
	}

	public void setStoredQuantity(int storedQuantity)
	{
		this.storedQuantity = storedQuantity;
	}

	public int getNetworkID()
	{
		return networkID;
	}
}

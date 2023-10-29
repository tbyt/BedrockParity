package com.tbyt;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BlockEntityDataPacket;
import org.geysermc.api.Geyser;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.GeyserImpl;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;

/*
 * Made by TBYT
 * 
 * References
 * * https://programtalk.com/java-more-examples/com.nukkitx.nbt.NbtMapBuilder
 */
public class AnimateHeadsForGeyser implements Listener {
	private final Plugin plugin;
	private ArrayList<Location> headsToAnimate = new ArrayList<Location>();
	private ArrayList<Location> animatedHeads = new ArrayList<Location>();
	private int radius;

	public AnimateHeadsForGeyser(Plugin plugin, int animateHeadBlockDistance, int serverRenderDistance) {
		this.plugin = plugin;
		radius = animateHeadBlockDistance;
	}
	
	@EventHandler
	public void geyserPlayerDimensionSwitch(PlayerChangedWorldEvent event)
    {
		if (!FloodgateApi.getInstance().isFloodgatePlayer(event.getPlayer().getUniqueId()))
            return;
    	headsToAnimate = new ArrayList<Location>();
    	animatedHeads = new ArrayList<Location>();
    }
	
	@EventHandler
	public void geyserPlayerDeath(PlayerPostRespawnEvent event)
    {
		if (!FloodgateApi.getInstance().isFloodgatePlayer(event.getPlayer().getUniqueId()))
            return;
    	headsToAnimate = new ArrayList<Location>();
    	animatedHeads = new ArrayList<Location>();
    }
	
	public void animateForGeyserPlayer(Connection playerConn) {
		Player player = null;
		try {
		player = Bukkit.getPlayer(playerConn.javaUuid());
		}
		catch(IllegalArgumentException e)
		{
			return;
			//may be null in the very first milliseconds of connecting to the server.
		}
		if(player==null)
			return;
		
		// this for loop is if the head is broken and not at the location anymore or not powered.
		ArrayList<Location> headsToRemove = new ArrayList<Location>();
		for (Location activeHead : headsToAnimate) {
			Material currentMaterial = activeHead.getBlock().getType();
			if (!IsHead(currentMaterial)) {
				headsToRemove.add(activeHead);
				continue;
			}
			if (activeHead.getBlock().isBlockPowered())
				continue;
			Boolean nearbyRedstoneBlock = findNearbyRedstoneBlock(activeHead);
			if (!nearbyRedstoneBlock) {
				headsToRemove.add(activeHead);
				// send deanimate packet
				switch (currentMaterial) {
				case DRAGON_HEAD:
					sendAnimatePacket(playerConn, activeHead.getBlock(), 5, 0);
					break;
				case DRAGON_WALL_HEAD:
					sendAnimatePacket(playerConn, activeHead.getBlock(), 5, 0);
					break;
				case PIGLIN_HEAD:
					sendAnimatePacket(playerConn, activeHead.getBlock(), 6, 0);
					break;
				case PIGLIN_WALL_HEAD:
					sendAnimatePacket(playerConn, activeHead.getBlock(), 6, 0);
					break;
				default:
					break;
				}
			}
		}
		headsToAnimate.removeAll(headsToRemove);
		animatedHeads.removeAll(headsToRemove);
		// locate new heads by giving the player a radius.
		Location PlayerLoc = player.getLocation();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					Block headBlock = PlayerLoc.clone().add(x, y, z).getBlock();
					Material materialOfBlock = headBlock.getType();
					// if Block is actually a dragon or piglin head block.
					if (IsHead(materialOfBlock)) {
						Boolean nearbyRedstoneBlock = findNearbyRedstoneBlock(headBlock.getLocation());
						if (headBlock.isBlockPowered() || nearbyRedstoneBlock) {
							if (!headsToAnimate.contains(headBlock.getLocation()))
								headsToAnimate.add(headBlock.getLocation());
						}
					}
				}
			}
		}
		// send animation packet(s).
		for (Location activeHead : headsToAnimate) {
			//skip animation if already animating.
			if(animatedHeads.contains(activeHead))
				continue;
			animatedHeads.add(activeHead);
			Block headBlock = activeHead.getBlock();
			Material materialOfBlock = headBlock.getType();
			switch (materialOfBlock) {
			case DRAGON_HEAD:
				sendAnimatePacket(playerConn, headBlock, 5, 1);
				break;
			case DRAGON_WALL_HEAD:
				sendAnimatePacket(playerConn, headBlock, 5, 1);
				break;
			case PIGLIN_HEAD:
				sendAnimatePacket(playerConn, headBlock, 6, 1);
				break;
			case PIGLIN_WALL_HEAD:
				sendAnimatePacket(playerConn, headBlock, 6, 1);
				break;
			default:
				break;
			}
		}
	}

	// server does not pick up isBlockPowered if redstone block is powering adjacent
	// to the head.
	public boolean findNearbyRedstoneBlock(Location location) {
		ArrayList<Material> adjacentMaterials = new ArrayList<Material>();
		adjacentMaterials.add(location.clone().add(-1, 0, 0).getBlock().getType());
		adjacentMaterials.add(location.clone().add(1, 0, 0).getBlock().getType());
		adjacentMaterials.add(location.clone().add(0, -1, 0).getBlock().getType());
		adjacentMaterials.add(location.clone().add(0, 1, 0).getBlock().getType());
		adjacentMaterials.add(location.clone().add(0, 0, -1).getBlock().getType());
		adjacentMaterials.add(location.clone().add(0, 0, 1).getBlock().getType());
		for (Material nearbyBlock : adjacentMaterials) {
			if (nearbyBlock.equals(Material.REDSTONE_BLOCK))
				return true;
		}
		return false;
	}

	public boolean IsHead(Material material) {
		switch (material) {
		case DRAGON_HEAD:
			return true;
		case DRAGON_WALL_HEAD:
			return true;
		case PIGLIN_HEAD:
			return true;
		case PIGLIN_WALL_HEAD:
			return true;
		default:
			return false;
		}
	}

	// animate or deanimate bedrock animations for vanilla skulls.
	public void sendAnimatePacket(Connection playerConn, Block head, int HeadType, int AnimationStatus) {
		NbtMapBuilder builder = NbtMap.builder();
		builder.putByte("DoingAnimation", (byte) AnimationStatus);
		builder.putInt("MouthTickCount", AnimationStatus);
		// if head is not on a Wall.
		if (head.getBlockData() instanceof Rotatable) {
			Rotatable HeadRotation = (Rotatable) head.getBlockData();
			float rotationDegree = 0.0f;

			switch (HeadRotation.getRotation()) {
			case EAST:
				rotationDegree = 270.0f;
				break;
			case EAST_NORTH_EAST:
				rotationDegree = 247.5f;
				break;
			case EAST_SOUTH_EAST:
				rotationDegree = 292.5f;
				break;
			case NORTH:
				rotationDegree = 180.0f;
				break;
			case NORTH_EAST:
				rotationDegree = 225.0f;
				break;
			case NORTH_NORTH_EAST:
				rotationDegree = 202.5f;
				break;
			case NORTH_NORTH_WEST:
				rotationDegree = 157.5f;
				break;
			case NORTH_WEST:
				rotationDegree = 135.0f;
				break;
			case SOUTH:
				// rotation already set to 0.0f
				break;
			case SOUTH_EAST:
				rotationDegree = 315.0f;
				break;
			case SOUTH_SOUTH_EAST:
				rotationDegree = 337.5f;
				break;
			case SOUTH_SOUTH_WEST:
				rotationDegree = 22.5f;
				break;
			case SOUTH_WEST:
				rotationDegree = 45.0f;
				break;
			case WEST:
				rotationDegree = 90.0f;
				break;
			case WEST_NORTH_WEST:
				rotationDegree = 112.5f;
				break;
			case WEST_SOUTH_WEST:
				rotationDegree = 67.5f;
				break;
			default:
				break;
			}
			builder.put("Rotation", rotationDegree);
		} else
			builder.put("Rotation", 0f);
		builder.putByte("SkullType", (byte) HeadType);
		builder.putString("id", "Skull");
		builder.putByte("isMovable", (byte) 1);
		builder.putInt("X", head.getX());
		builder.putInt("Y", head.getY());
		builder.putInt("Z", head.getZ());
		BlockEntityDataPacket HeadAnimationPacket = new BlockEntityDataPacket();
		HeadAnimationPacket.setBlockPosition(Vector3i.from(head.getX(), head.getY(), head.getZ()));
		HeadAnimationPacket.setData(builder.build());
		GeyserImpl geyserInstance = (GeyserImpl) Geyser.api();
		geyserInstance.connectionByUuid(playerConn.javaUuid()).sendUpstreamPacket((BedrockPacket) HeadAnimationPacket);
	}
}
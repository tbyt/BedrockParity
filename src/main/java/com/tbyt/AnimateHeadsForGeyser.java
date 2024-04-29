package com.tbyt;

//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BlockEntityDataPacket;
import org.geysermc.api.Geyser;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

//import com.google.gson.Gson;
//import com.google.gson.JsonElement;

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

	public AnimateHeadsForGeyser(Plugin plugin, int animateHeadBlockDistance) {
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
	public void geyserPlayerDeath(PlayerRespawnEvent event)
    {
		if (!FloodgateApi.getInstance().isFloodgatePlayer(event.getPlayer().getUniqueId()))
            return;
    	headsToAnimate = new ArrayList<Location>();
    	animatedHeads = new ArrayList<Location>();
    }
	
	public void animateForBedrockPlayer(FloodgatePlayer player){
		Player BukkitPlayer = null;
		try {
			BukkitPlayer = Bukkit.getPlayer(player.getCorrectUniqueId());
		}
		catch(IllegalArgumentException e)
		{
			return;
			//may be null in the very first milliseconds of connecting to the server.
		}
		if(BukkitPlayer==null)
			return;
		
		// this for loop is if the head is broken and not at the location anymore or not powered.
		ArrayList<Location> headsToRemove = new ArrayList<Location>();
		for (Location activeHead : headsToAnimate) {
			Material currentMaterial = activeHead.getBlock().getType();
			if (!isHead(currentMaterial)) {
				headsToRemove.add(activeHead);
				continue;
			}
			if (activeHead.getBlock().isBlockPowered())
				continue;
			if(!activeHead.getBlock().isBlockIndirectlyPowered()) {
				headsToRemove.add(activeHead);
				// send deanimate packet
				switch (currentMaterial) {
				case DRAGON_HEAD:
					sendAnimatePacket(player, activeHead.getBlock(), 5, 0);
					break;
				case DRAGON_WALL_HEAD:
					sendAnimatePacket(player, activeHead.getBlock(), 5, 0);
					break;
				case PIGLIN_HEAD:
					sendAnimatePacket(player, activeHead.getBlock(), 6, 0);
					break;
				case PIGLIN_WALL_HEAD:
					sendAnimatePacket(player, activeHead.getBlock(), 6, 0);
					break;
				default:
					break;
				}
			}
		}
		headsToAnimate.removeAll(headsToRemove);
		animatedHeads.removeAll(headsToRemove);
		// locate new heads by giving the player a radius.
		Location PlayerLoc = BukkitPlayer.getLocation();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					Block headBlock = PlayerLoc.clone().add(x, y, z).getBlock();
					Material materialOfBlock = headBlock.getType();
					// if Block is actually a dragon or piglin head block.
					if (isHead(materialOfBlock)) {
						if (headBlock.isBlockPowered() || headBlock.isBlockIndirectlyPowered()) {
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
				sendAnimatePacket(player, headBlock, 5, 1);
				break;
			case DRAGON_WALL_HEAD:
				sendAnimatePacket(player, headBlock, 5, 1);
				break;
			case PIGLIN_HEAD:
				sendAnimatePacket(player, headBlock, 6, 1);
				break;
			case PIGLIN_WALL_HEAD:
				sendAnimatePacket(player, headBlock, 6, 1);
				break;
			default:
				break;
			}
		}
	}

	public boolean isHead(Material material) {
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
	public void sendAnimatePacket(FloodgatePlayer player, Block head, int headType, int animationStatus) {
		NbtMapBuilder builder = NbtMap.builder();
		builder.putByte("DoingAnimation", (byte) animationStatus);
		builder.putInt("MouthTickCount", animationStatus);
		// if head is not on a Wall.
		float rotationDegree = 0.0f;
		if (head.getBlockData() instanceof Rotatable) {
			Rotatable HeadRotation = (Rotatable) head.getBlockData();

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
		}
		builder.put("Rotation", rotationDegree);
		builder.putByte("SkullType", (byte) headType);
		builder.putString("id", "Skull");
		builder.putByte("isMovable", (byte) 1);
		builder.putInt("X", head.getX());
		builder.putInt("Y", head.getY());
		builder.putInt("Z", head.getZ());
		BlockEntityDataPacket headAnimationPacket = new BlockEntityDataPacket();
		headAnimationPacket.setBlockPosition(Vector3i.from(head.getX(), head.getY(), head.getZ()));
		headAnimationPacket.setData(builder.build());
		GeyserImpl geyserInstance = (GeyserImpl) Geyser.api();
		geyserInstance.connectionByUuid(player.getCorrectUniqueId()).sendUpstreamPacket((BedrockPacket) headAnimationPacket);
		
		// When I was testing sending packet through floodgate api but obviously this is not how you do it. You have to use geyser api.
		
//		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
//		ObjectOutputStream oos = new ObjectOutputStream(bos);
		//oos.writeObject(builder.build().toString());
		
//		Gson gson = new Gson();
//		String PacketdataString = "{'DoingAnimation':"+animationStatus+",'MouthTickCount':"+animationStatus+",'Rotation':"+rotationDegree+",'SkullType':"+headType+",'id':'Skull','isMovable':"+1+",'x':"+head.getX()+",'y':"+head.getY()+",'z':"+head.getZ()+"}";
//		JsonElement PacketdataJson = gson.fromJson(PacketdataString,JsonElement.class);
		//String HeadPacketString = "BlockEntityDataPacket(blockPosition=("+head.getX()+","+head.getY()+","+head.getZ()+"),data="+PacketdataJson+")";
		//plugin.getLogger().info(headAnimationPacket.toString());
		//FloodgateApi.getInstance().unsafe().sendPacket(player, 0x38, headAnimationPacket.toString().getBytes());
		
	}
}
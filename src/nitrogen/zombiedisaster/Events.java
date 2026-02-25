package nitrogen.zombiedisaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Events implements Listener {
	private ZombieDisaster plugin;
	
	public Events(ZombieDisaster plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void spawnEvent(EntitySpawnEvent e) {
		if(!plugin.isZombieWorld(e.getLocation().getWorld()))
			return;
		
		if(!(e.getEntity() instanceof LivingEntity)) return;
		
		LivingEntity entity = (LivingEntity)e.getEntity();
		EntityType et = e.getEntityType();
		if(et == EntityType.SKELETON || et == EntityType.CREEPER || et == EntityType.SPIDER || et == EntityType.WITCH || et == EntityType.STRAY) {
			e.setCancelled(true);
			entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ZOMBIE);
			return;
		}
		if(et != EntityType.ZOMBIE) return;
		
		Random random = new Random();
		int equipment_chance;
		int zombie_type;
		if (random.nextInt(10000) == 0) {
			entity.setMaxHealth(200.0D);
			entity.setHealth(200.0D);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 3, false, false, false));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, false, false, false));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, false, false, false));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false, false));
			entity.setCustomName("§4§l보스 좀비");
			entity.setCustomNameVisible(true);
			if (random.nextInt(2) == 0) {
				entity.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
				entity.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
				entity.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
				entity.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
			} else {
				entity.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
				entity.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
				entity.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
				entity.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
			}

			equipment_chance = (int)entity.getLocation().getX();
			zombie_type = (int)entity.getLocation().getY();
			int z = (int)entity.getLocation().getZ();
			Bukkit.broadcastMessage("§4§l보스 좀비 §f§l가 출현하였습니다!  좌표 : §6§l" + equipment_chance + " §6§l" + zombie_type + " §6§l" + z);
			entity.setPersistent(true);
			return;
		}

		if (random.nextInt(100) == 0) {
			entity.setMaxHealth(150.0D);
			entity.setHealth(150.0D);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2, false, false, false));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false, false));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, false, false, false));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false, false));
			entity.setCustomName("§5§l슈퍼 좀비");
			entity.setCustomNameVisible(true);
			return;
		}

		equipment_chance = random.nextInt(12);
		if (equipment_chance == 0) {
			entity.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			entity.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			entity.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			entity.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
		} else if (equipment_chance == 1) {
			entity.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
			entity.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
			entity.getEquipment().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
			entity.getEquipment().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
		} else if (equipment_chance == 2) {
			entity.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
		} else if (equipment_chance == 3) {
			ItemStack stick = new ItemStack(Material.STICK);
			entity.getEquipment().setItemInMainHand(stick);
		}
		
		final boolean isAssassinNerfed = plugin.config.getBoolean("nerf-assassin-zombie", false);

		zombie_type = random.nextInt(12);
		// 2026-02-15 수정: 암살자 스폰 확률 감소
		if(isAssassinNerfed) {
			if(zombie_type == 10 && random.nextInt(2) != 0) {
				e.setCancelled(true);
				return;
			}
		}
		// 수정 끝
		switch(zombie_type) {
			case 0:
				entity.addScoreboardTag("strong");
				entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 3, false, true, false));
				break;
			case 1:
				entity.addScoreboardTag("speed");
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, false, true, false));
				break;
			case 2:
				entity.addScoreboardTag("poison");
				break;
			case 3:
				entity.addScoreboardTag("invisible");
				entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, true, false));
				break;
			case 4:
				entity.setMaxHealth(50.0D);
				entity.setHealth(50.0D);
				entity.addScoreboardTag("guard");
				break;
			case 5:
				ItemStack tntStack = new ItemStack(Material.TNT);
				entity.getEquipment().setHelmet(tntStack);
				if (random.nextInt(10) == 0) {
					entity.addScoreboardTag("explode_variation");
					entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false, false));
					entity.getEquipment().setItemInMainHand(new ItemStack(Material.FLINT_AND_STEEL));
				} else {
					entity.addScoreboardTag("explode");
				}
				break;
			case 6:
				entity.addScoreboardTag("split");
				break;
			case 7:
				entity.addScoreboardTag("lift");
				break;
			case 8:
				entity.addScoreboardTag("heavy");
				break;
			case 9:
				entity.addScoreboardTag("day");
				entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, true, false));
				break;
			case 10:
				entity.addScoreboardTag("assassin");
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false, false));
				// 2026-02-15 수정: 암살자 인식 범위 너프
				if(isAssassinNerfed) {
					AttributeInstance followRange = entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
					if(followRange != null)
						followRange.setBaseValue(20.0D);
				}
				// 수정 끝
				break;
			case 11:
				entity.addScoreboardTag("dark");
				entity.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
				entity.getEquipment().setHelmetDropChance(0.0F);
		}
	}

	@EventHandler
	public void onAssassinZombieTarget(EntityTargetLivingEntityEvent e) {
		Entity entity = e.getEntity();
		Entity target = e.getTarget();
		
		if(target == null) return;
		if(entity.getType() != EntityType.ZOMBIE) return;
		if(!entity.getScoreboardTags().contains("assassin")) return;
		if(!(target instanceof Player)) return;
		
		Zombie zombie = (Zombie) entity;
		Player p = (Player) target;
		if(!zombie.getScoreboardTags().contains("checked")) {
			zombie.addScoreboardTag("checked");
			p.sendMessage("§c§l암살자 좀비 출현! 뒤를 보세요!");
			this.teleportBehindPlayer(zombie, p);
		}
	}

	private void teleportBehindPlayer(Zombie zombie, Player player) {
		Location playerLocation = player.getLocation();
		Vector playerDirection = playerLocation.getDirection().normalize();
		double distanceBehind = -1.2D;
		Location zombieLocation = playerLocation.clone().add(playerDirection.multiply(distanceBehind));
		zombie.teleport(zombieLocation);
	}

	@EventHandler
	public void entityDamageEvent(EntityDamageByEntityEvent e) {
		LivingEntity victim = (LivingEntity) e.getEntity();
		Entity damager = e.getDamager();
		
		if(!(e.getEntity() instanceof LivingEntity)) return;
		
		Random random = new Random();
		Zombie z;
		if (victim instanceof Player && damager instanceof Zombie) {
			if (damager.getScoreboardTags().contains("poison")) {
				if (random.nextInt(3) == 0) {
					victim.sendMessage("§2§l맹독 발생!");
					victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 1, false, true, true));
					return;
				}

				victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1, false, true, true));
			} else if (damager.getScoreboardTags().contains("lift")) {
				victim.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 10, 50, false, false, false));
			} else if (damager.getScoreboardTags().contains("heavy")) {
				victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 7, false, true, true));
			} else if (damager.getScoreboardTags().contains("explode_variation")) {
				TNTPrimed tnt = (TNTPrimed)damager.getWorld().spawnEntity(damager.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(20);
				damager.remove();
				victim.sendMessage("§c§l자폭병 좀비가 폭팔합니다!");
			} else if (damager.getScoreboardTags().contains("dark")) {
				victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1, false, true, true));
			}

			z = (Zombie) damager;
			if (z.getEquipment() != null && z.getEquipment().getItemInMainHand().getType() == Material.STICK && z.getEquipment().getItemInMainHand().getDurability() == 1) {
				victim.setHealth(victim.getHealth() / 2.0D);
				victim.sendMessage("§c§l체력이 절반으로 줄어듭니다!");
				z.getEquipment().setItemInMainHand((ItemStack)null);
			}
		} else if (victim instanceof Zombie && damager instanceof Player) {
			if (victim.getScoreboardTags().contains("guard")) {
				if (random.nextInt(2) == 0) {
					e.setCancelled(true);
					damager.sendMessage("§7§l공격 빗나감!");
				}
			} else if (victim.getCustomName() != null && victim.getCustomName().equals("§4§l보스 좀비") && random.nextInt(3) == 0) {
				z = (Zombie)victim.getWorld().spawnEntity(victim.getLocation(), EntityType.ZOMBIE);
				if (z.getEquipment() == null) {
					z.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_SWORD, 1));
				}
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		LivingEntity entity = e.getEntity();
		Player p = e.getEntity().getKiller();
		
		Random random = new Random();
		if (entity instanceof Zombie) {
			int n = random.nextInt(2);
			if (p != null && plugin.isInZombieWorld(entity)) {
				p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.BOOK, n)});
				p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.LAPIS_LAZULI, n)});
			}

			if (entity.getScoreboardTags().contains("explode")) {
				TNTPrimed tnt = (TNTPrimed)entity.getWorld().spawnEntity(entity.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(20);
				int num = random.nextInt(2) + 1;
				if (p != null) {
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.GUNPOWDER, num)});
				}
			} else if (entity.getScoreboardTags().contains("split")) {
				Zombie z, z1;
				if (random.nextInt(2) == 0) {
					z = (Zombie)entity.getWorld().spawnEntity(entity.getLocation().add(0.5D, 0.0D, 0.5D), EntityType.ZOMBIE);
					z1 = (Zombie)entity.getWorld().spawnEntity(entity.getLocation().add(0.5D, 0.0D, -0.5D), EntityType.ZOMBIE);
					Zombie z2 = (Zombie)entity.getWorld().spawnEntity(entity.getLocation().add(-0.5D, 0.0D, 0.5D), EntityType.ZOMBIE);
					Zombie z3 = (Zombie)entity.getWorld().spawnEntity(entity.getLocation().add(-0.5D, 0.0D, -0.5D), EntityType.ZOMBIE);
					return;
				}

				z = (Zombie)entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ZOMBIE);
				z1 = (Zombie)entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ZOMBIE);
			} else {
				int itemType;
				if (entity.getScoreboardTags().contains("lift")) {
					e.getDrops().clear();
					itemType = random.nextInt(2) + 1;
					e.getDrops().add(new ItemStack(Material.STRING, itemType));
				} else if (entity.getScoreboardTags().contains("dark")) {
					if (p != null) {
						p.sendMessage("§0§l암흑 좀비로 인해 시야가 차단 되었습니다!");
						p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 3, false, true, true));
						itemType = random.nextInt(2) + 1;
						p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.SPIDER_EYE, itemType)});
					}
				} else if (entity.getCustomName() != null && entity.getCustomName().equals("§5§l슈퍼 좀비")) {
					if (p == null) {
						return;
					}

					e.getDrops().clear();
					itemType = random.nextInt(3) + 1;
					e.getDrops().add(new ItemStack(Material.DIAMOND, itemType));
				} else if (entity.getCustomName() != null && entity.getCustomName().equals("§4§l보스 좀비")) {
					if (p == null) {
						return;
					}

					p.sendMessage("§6§l보스 좀비를 처치하였습니다!");
					e.getDrops().clear();
					itemType = random.nextInt(4);
					Material material = null;
					switch(itemType) {
						case 0:
							material = Material.DIAMOND_BLOCK;
							p.sendMessage("§d§l보상 : 다이아몬드 블록");
							break;
						case 1:
							material = Material.TOTEM_OF_UNDYING;
							p.sendMessage("§d§l보상 : 불사의 토템");
							break;
						case 2:
							material = Material.NETHERITE_INGOT;
							p.sendMessage("§d§l보상 : 네더라이트 주괴");
							break;
						case 3:
							material = Material.HEART_OF_THE_SEA;
							p.sendMessage("§d§l보상 : 바다의 심장");
					}

					e.getDrops().add(new ItemStack(material, 1));
				}
			}
		} else if(entity instanceof Enderman && plugin.isInZombieWorld(entity)) {
			e.setDroppedExp(0);
		}
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity();
		boolean isZombieWorld =  plugin.isInZombieWorld(p);
		
		if (p.getLastDamageCause() != null && p.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK && p.getLastDamageCause().getEntity() instanceof Zombie) {
			Entity zombie = p.getLastDamageCause().getEntity();
			if (zombie.getScoreboardTags().contains("strong")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 힘 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("speed")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 속도 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("poison")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 독 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("invisible")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 투명 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("guard")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 방패 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("split")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 분열 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("explode")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 폭팔 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("explode_variation")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 자폭병 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("lift")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 투척 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("heavy")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 무거운 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("day")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 낮 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("assassin")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 암살자 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getScoreboardTags().contains("dark")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 암흑 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getCustomName() != null && zombie.getCustomName().equals("§5§l슈퍼 좀비")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 슈퍼 좀비에 의해 사망하셨습니다.");
			} else if (zombie.getCustomName() != null && zombie.getCustomName().equals("§4§l보스 좀비")) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 보스 좀비에 의해 사망하셨습니다.");
			} else if(isZombieWorld) {
				e.setDeathMessage("§c§l" + p.getName() + " §c님이 사망하셨습니다.");
			}
		} else if(isZombieWorld) {
			e.setDeathMessage("§c§l" + p.getName() + " §c님이 사망하셨습니다.");
		}
		
		if(isZombieWorld)
			p.setGameMode(GameMode.SPECTATOR);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(!plugin.isInZombieWorld(p)) return;
		e.setJoinMessage("§a§l" + p.getName() + " §a님이 좀비 아포칼립스 세계에 입장하셨습니다.");
		p.sendMessage("§c§l좀비 아포칼립스 세계에 오신 것을 환영합니다.\n§f§l생명은 오로지 한 번 뿐이고, 부활하실 수 있습니다.\n§b§l부활 아이템 : 바다의 심장(우클릭)\n§f- 바다의 심장 조합법 : /recipe");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!plugin.isInZombieWorld(p)) return;
		Action a = e.getAction();
		if ((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) && p.getInventory().getItemInMainHand().getType() == Material.HEART_OF_THE_SEA) {
			Player[] onlinePlayers = (Player[])Bukkit.getOnlinePlayers().toArray(new Player[0]);
			List<Player> spectators = new ArrayList<Player>();
			Player[] var6 = onlinePlayers;
			int var7 = onlinePlayers.length;

			for(int i=0; i<var7; i++) {
				Player player = var6[i];
				if(player.getGameMode() == GameMode.SPECTATOR && plugin.isInZombieWorld(player))
					spectators.add(player);
			}

			if (spectators.isEmpty()) {
				p.sendMessage("§c§l부활 가능한 영혼이 존재하지 않습니다.");
				return;
			}

			Random random = new Random();
			Player revivedPlayer = (Player)spectators.get(random.nextInt(spectators.size()));
			revivedPlayer.teleport(p.getLocation());
			revivedPlayer.setGameMode(GameMode.SURVIVAL);
			Bukkit.broadcastMessage("§a§l" + revivedPlayer.getName() + " §a님이 부활하셨습니다.");
			p.getInventory().removeItem(new ItemStack[]{new ItemStack(Material.HEART_OF_THE_SEA, 1)});
		}

	}

	@EventHandler
	public void stopRecipeItemInteract(InventoryClickEvent e) {
		if(e.getClickedInventory() == null) return;
		if(e.getCurrentItem() == null) return;
		
		if(e.getClickedInventory().getHolder() instanceof RecipeGui)
			e.setCancelled(true);
	}
}

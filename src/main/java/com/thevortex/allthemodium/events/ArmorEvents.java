package com.thevortex.allthemodium.events;

import java.util.Collection;
import java.util.Iterator;

import com.thevortex.allthemodium.AllTheModium;
import com.thevortex.allthemodium.init.ModItems;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorEvents {
	


	@SubscribeEvent
	public static void onPlayerFall(LivingFallEvent event) {
		Iterable<ItemStack> armorlist = event.getEntityLiving().getArmorInventoryList();
		Iterator<ItemStack> iterator = armorlist.iterator();
		while(iterator.hasNext()) {
			ItemStack armor = iterator.next();
			if((armor.getItem() == ModItems.ALLTHEMODIUM_BOOTS) || (armor.getItem() == ModItems.VIBRANIUM_BOOTS) || (armor.getItem() == ModItems.UNOBTAINIUM_BOOTS)) { event.setCanceled(true);}
		}	
			
	}


	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if (!event.getEntityLiving().getEntityWorld().isRemote) {
			Iterable<ItemStack> armorlist = event.getEntityLiving().getArmorInventoryList();
			Iterator<ItemStack> iterator = armorlist.iterator();
			while (iterator.hasNext()) {
				ItemStack armor = iterator.next();
				if ((armor.getItem() == ModItems.ALLTHEMODIUM_CHESTPLATE) || (armor.getItem() == ModItems.VIBRANIUM_CHESTPLATE) || (armor.getItem() == ModItems.UNOBTAINIUM_CHESTPLATE)) {
					if ((event.getSource() == DamageSource.DRAGON_BREATH) && (armor.getItem() == ModItems.UNOBTAINIUM_CHESTPLATE)) {
						if(!event.isCancelable()) { event.setAmount(0.0F); }
						event.setCanceled(true);

					}

					if ((event.getSource() == DamageSource.HOT_FLOOR) || (event.getSource() == DamageSource.IN_FIRE) || (event.getSource() == DamageSource.LAVA) || (event.getSource() == DamageSource.ON_FIRE)) {
						event.getEntityLiving().extinguish();
						event.setCanceled(true);

					}
					if (!event.isCanceled()) {
						event.setAmount(event.getAmount() - (event.getAmount() / 4));
					}

				}
				if ((armor.getItem() == ModItems.VIBRANIUM_LEGGINGS) || (armor.getItem() == ModItems.UNOBTAINIUM_LEGGINGS)) {
					if (event.getSource() == DamageSource.WITHER) {
						event.getEntityLiving().removePotionEffect(Effects.WITHER);

						event.setCanceled(true);
					}
					if (!event.isCanceled() && armor.getItem() == ModItems.UNOBTAINIUM_LEGGINGS) {
						event.getEntityLiving().removePotionEffect(Effects.LEVITATION);
					}
					if (event.getSource() == DamageSource.MAGIC) {
						event.setCanceled(true);
					}
					if (!event.isCanceled()) {
						event.setAmount(event.getAmount() - (event.getAmount() / 4));
					}

				}

				if ((armor.getItem() == ModItems.ALLTHEMODIUM_HELMET) || (armor.getItem() == ModItems.VIBRANIUM_HELMET) || (armor.getItem() == ModItems.UNOBTAINIUM_HELMET)) {
					if (event.getSource() == DamageSource.FLY_INTO_WALL) {
						event.setCanceled(true);

					}

					if (event.getSource() == DamageSource.DROWN) {
						event.getEntityLiving().setAir(event.getEntityLiving().getMaxAir());

						event.setCanceled(true);
					}
					if (!event.isCanceled()) {
						event.setAmount(event.getAmount() - (event.getAmount() / 4));
					}

				}
				if ((armor.getItem() == ModItems.ALLTHEMODIUM_BOOTS) || (armor.getItem() == ModItems.VIBRANIUM_BOOTS) || (armor.getItem() == ModItems.UNOBTAINIUM_BOOTS)) {
					if (!event.isCanceled()) {
						event.setAmount(event.getAmount() - (event.getAmount() / 4));
					}

				}

			}

		}
	}
	@SubscribeEvent
	public static void onEntityCollide(ProjectileImpactEvent event) {

	}
}


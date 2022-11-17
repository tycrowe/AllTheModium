package com.thevortex.allthemodium.items.toolitems.tools;

import com.google.common.collect.ImmutableMap;
import com.thevortex.allthemodium.material.ToolTiers;
import com.thevortex.allthemodium.registry.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.server.command.TextComponentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AlloyPaxel extends DiggerItem {

    public static Map<Block, Block> STRIPPABLES = (new ImmutableMap.Builder<Block, Block>()).put(ModRegistry.DEMONIC_LOG.get(), ModRegistry.DEMONIC_LOG_STRIPPED.get()).put(ModRegistry.SOUL_LOG_2.get(), ModRegistry.SOUL_LOG_STRIPPED.get()).put(ModRegistry.SOUL_LOG_1.get(), ModRegistry.SOUL_LOG_STRIPPED.get()).put(ModRegistry.SOUL_LOG_0.get(), ModRegistry.SOUL_LOG_STRIPPED.get()).put(ModRegistry.ANCIENT_LOG_2.get(), ModRegistry.ANCIENT_LOG_STRIPPED.get()).put(ModRegistry.ANCIENT_LOG_1.get(), ModRegistry.ANCIENT_LOG_STRIPPED.get()).put(ModRegistry.ANCIENT_LOG_0.get(), ModRegistry.ANCIENT_LOG_STRIPPED.get()).put(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN).put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD).put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG).build();
    public AlloyPaxel(float attack, float speed, Tier tier, TagKey<Block> effectiveBlocks, Properties properties) {
        super(attack, speed, tier, effectiveBlocks, properties);
    }
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) return speed *1.4f;
        if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) return speed*1.8f;
        if (state.is(BlockTags.MINEABLE_WITH_AXE)) return speed*1.8f;
        if (state.is(BlockTags.MINEABLE_WITH_HOE)) return speed*1.9f;
        if (state.is(Tags.Blocks.GLASS))  return speed*3.0F;
        return super.getDestroySpeed(stack, state);
    }
    @Override
    public net.minecraft.world.InteractionResult interactLivingEntity(ItemStack stack, net.minecraft.world.entity.player.Player playerIn, LivingEntity entity, net.minecraft.world.InteractionHand hand) {
        if (entity instanceof net.minecraftforge.common.IForgeShearable target) {
            if (entity.level.isClientSide) return net.minecraft.world.InteractionResult.SUCCESS;
            BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
            if (target.isShearable(stack, entity.level, pos)) {
                java.util.List<ItemStack> drops = target.onSheared(playerIn, stack, entity.level, pos,
                        net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.BLOCK_FORTUNE, stack));
                java.util.Random rand = new java.util.Random();
                drops.forEach(d -> {
                    net.minecraft.world.entity.item.ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                    ent.setDeltaMovement(ent.getDeltaMovement().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });
                stack.hurtAndBreak(1, playerIn, e -> e.broadcastBreakEvent(hand));
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        return net.minecraft.world.InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SHEARS_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity player) {
        //entity.setSecondsOnFire(30);
        return super.hurtEnemy(stack, entity, player);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.getBlock() == Blocks.OBSIDIAN) {
            BlockState bpos = Blocks.CRYING_OBSIDIAN.defaultBlockState();
            Player playerentity = context.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.NETHER_ORE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                world.setBlock(blockpos, bpos, 11);
                if (playerentity != null) {
                    context.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
                        p_220040_1_.broadcastBreakEvent(context.getHand());
                    });
                }
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        if (blockstate.getBlock() instanceof GrowingPlantHeadBlock growingplantheadblock) {
            if (!growingplantheadblock.isMaxAge(blockstate)) {
                Player player = context.getPlayer();
                assert player != null;
                ItemStack itemstack = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                }

                world.playSound(player, blockpos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.setBlockAndUpdate(blockpos, growingplantheadblock.getMaxAgeState(blockstate));
                itemstack.hurtAndBreak(1, player, (p_186374_) -> {
                    p_186374_.broadcastBreakEvent(context.getHand());
                });

                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        if ((blockstate.is(BlockTags.DIRT))) {
            //tags dirt
            boolean isSneaking = context.getPlayer().isCrouching();
            BlockState blockPath = isSneaking ? Blocks.FARMLAND.defaultBlockState() : Blocks.DIRT_PATH.defaultBlockState();
            Player playerentity = context.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                world.setBlock(blockpos, blockPath, 11);
                if (playerentity != null) {
                    context.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
                        p_220040_1_.broadcastBreakEvent(context.getHand());
                    });
                }
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        if (blockstate.is(BlockTags.LOGS)) {

            //tags logs
            Block check = STRIPPABLES.get(blockstate.getBlock());
            if (check != null) {
                BlockState block = check.defaultBlockState();
                Player playerentity = context.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide) {
                    world.setBlock(blockpos, block, 11);
                    if (playerentity != null) {
                        context.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
                            p_220040_1_.broadcastBreakEvent(context.getHand());
                        });
                    }
                }
                return InteractionResult.sidedSuccess(world.isClientSide);
            }



        }
        return InteractionResult.PASS;
    }



    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    @Override
    public boolean canBeDepleted() {
        return false;
    }
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        tooltip.add(TextComponentHelper.createComponentTranslation(null,"indestructible" , new Object()).withStyle(ChatFormatting.GOLD));

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
    protected TranslatableContents getTooltip(String key){
        return new TranslatableContents(key);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state)
    {
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE))
            return TierSortingRegistry.isCorrectTierForDrops(ToolTiers.ALLTHEMODIUM_TIER, state);
        if (state.is(BlockTags.MINEABLE_WITH_HOE))
            return TierSortingRegistry.isCorrectTierForDrops(ToolTiers.ALLTHEMODIUM_TIER, state);
        if (state.is(BlockTags.MINEABLE_WITH_SHOVEL))
            return TierSortingRegistry.isCorrectTierForDrops(ToolTiers.ALLTHEMODIUM_TIER, state);
        if (state.is(BlockTags.MINEABLE_WITH_AXE))
            return TierSortingRegistry.isCorrectTierForDrops(ToolTiers.ALLTHEMODIUM_TIER, state);
        if (state.is(ToolTiers.ALLTHEMODIUM_TOOL_TAG) || state.is(ToolTiers.VIBRANIUM_TOOL_TAG) || state.is(ToolTiers.UNOBTAINIUM_TOOL_TAG))
            return TierSortingRegistry.isCorrectTierForDrops(ToolTiers.ALLTHEMODIUM_TIER, state);
        return false;
    }
}

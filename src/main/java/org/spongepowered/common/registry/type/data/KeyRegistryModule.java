/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.registry.type.data;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.api.data.key.KeyFactory.makeListKey;
import static org.spongepowered.api.data.key.KeyFactory.makeMapKey;
import static org.spongepowered.api.data.key.KeyFactory.makeOptionalKey;
import static org.spongepowered.api.data.key.KeyFactory.makeSetKey;
import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.MapMaker;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.meta.PatternLayer;
import org.spongepowered.api.data.type.*;
import org.spongepowered.api.data.value.BoundedValue;
import org.spongepowered.api.data.value.immutable.ImmutableBoundedValue;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.data.value.mutable.PatternListValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.extra.fluid.FluidStackSnapshot;
import org.spongepowered.api.item.FireworkEffect;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.merchant.TradeOffer;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.registry.AdditionalCatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Axis;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.common.data.SpongeDataManager;

import java.awt.Color;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("rawTypes")
public class KeyRegistryModule implements AdditionalCatalogRegistryModule<Key> {

    public static KeyRegistryModule getInstance() {
        return Holder.INSTANCE;
    }

    @RegisterCatalog(Keys.class)
    private final Map<String, Key<?>> keyMap = new MapMaker().concurrencyLevel(4).makeMap();

    @Override
    public void registerDefaults() {
        checkState(!SpongeDataManager.areRegistrationsComplete(), "Attempting to register Keys illegally!");
        this.keyMap.put("axis", makeSingleKey(Axis.class, Value.class, of("Axis"), "sponge:axis", "Axis"));
        this.keyMap.put("color", makeSingleKey(Color.class, Value.class, of("Color"), "sponge:color", "Color"));
        this.keyMap.put("health", makeSingleKey(Double.class, MutableBoundedValue.class, of("Health"), "sponge:health", "Health"));
        this.keyMap.put("max_health", makeSingleKey(Double.class, MutableBoundedValue.class, of("MaxHealth"), "sponge:max_health", "Max Health"));
        this.keyMap.put("display_name", makeSingleKey(Text.class, Value.class, of("DisplayName"), "sponge:display_name", "Display Name"));
        this.keyMap.put("career", makeSingleKey(Career.class, Value.class, of("Career"), "sponge:career", "Career"));
        this.keyMap.put("sign_lines", makeListKey(Text.class, of("SignLines"), "sponge:sign_lines", "Sign Lines"));
        this.keyMap.put("skull_type", makeSingleKey(SkullType.class, Value.class, of("SkullType"), "sponge:skull_type", "Skull Type"));
        this.keyMap.put("is_sneaking", makeSingleKey(Boolean.class, Value.class, of("IsSneaking"), "sponge:sneaking", "Is Sneaking"));
        this.keyMap.put("velocity", makeSingleKey(Vector3d.class, Value.class, of("Velocity"), "sponge:velocity", "Velocity"));
        this.keyMap.put("food_level", makeSingleKey(Integer.class, Value.class, of("FoodLevel"), "sponge:food_level", "Food Level"));
        this.keyMap.put("saturation", makeSingleKey(Double.class, Value.class, of("FoodSaturationLevel"), "sponge:food_saturation_level", "Food Saturation Level"));
        this.keyMap.put("exhaustion", makeSingleKey(Double.class, Value.class, of("FoodExhaustionLevel"), "sponge:food_exhaustion_level", ""));
        this.keyMap.put("max_air", makeSingleKey(Integer.class, Value.class, of("MaxAir"), "sponge:max_air", "Max Air"));
        this.keyMap.put("remaining_air", makeSingleKey(Integer.class, Value.class, of("RemainingAir"), "sponge:remaining_air", "Remaining Air"));
        this.keyMap.put("fire_ticks", makeSingleKey(Integer.class, MutableBoundedValue.class, of("FireTicks"), "sponge:fire_ticks", "Fire Ticks"));
        this.keyMap.put("fire_damage_delay", makeSingleKey(Integer.class, MutableBoundedValue.class, of("FireDamageDelay"), "sponge:fire_damage_delay", "Fire Damage Delay"));
        this.keyMap.put("game_mode", makeSingleKey(GameMode.class, Value.class, of("GameMode"), "sponge:game_mode", "Game Mode"));
        this.keyMap.put("is_screaming", makeSingleKey(Boolean.class, Value.class, of("IsScreaming"), "sponge:screaming", "Is Screaming"));
        this.keyMap.put("can_fly", makeSingleKey(Boolean.class, Value.class, of("CanFly"), "sponge:can_fly", "Can Fly"));
        this.keyMap.put("can_grief", makeSingleKey(Boolean.class, Value.class, of("CanGrief"), "sponge:can_grief", "Can Grief"));
        this.keyMap.put("shrub_type", makeSingleKey(ShrubType.class, Value.class, of("ShrubType"), "sponge:shrub_type", "Shrub Type"));
        this.keyMap.put("plant_type", makeSingleKey(PlantType.class, Value.class, of("PlantType"), "sponge:plant_type", "Plant Type"));
        this.keyMap.put("tree_type", makeSingleKey(TreeType.class, Value.class, of("TreeType"), "sponge:tree_type", "Tree Type"));
        this.keyMap.put("log_axis", makeSingleKey(LogAxis.class, Value.class, of("LogAxis"), "sponge:log_axis", "Log Axis"));
        this.keyMap.put("invisible", makeSingleKey(Boolean.class, Value.class, of("Invisible"), "sponge:invisible", "Invisible"));
        this.keyMap.put("powered", makeSingleKey(Boolean.class, Value.class, of("Powered"), "sponge:powered", "Powered"));
        this.keyMap.put("layer", makeSingleKey(Integer.class, MutableBoundedValue.class, of("Layer"), "sponge:layer", "Layer"));
        this.keyMap.put("represented_item", makeSingleKey(ItemStackSnapshot.class, Value.class, of("ItemStackSnapshot"), "sponge:item_stack_snapshot", "Item Stack Snapshot"));
        this.keyMap.put("command", makeSingleKey(String.class, Value.class, of("Command"), "sponge:command", "Command"));
        this.keyMap.put("success_count", makeSingleKey(Integer.class, Value.class, of("SuccessCount"), "sponge:success_count", "SuccessCount"));
        this.keyMap.put("tracks_output", makeSingleKey(Boolean.class, Value.class, of("TracksOutput"), "sponge:tracks_output", "Tracks Output"));
        this.keyMap.put("last_command_output", makeOptionalKey(Text.class, of("LastCommandOutput"), "sponge:last_command_output", "Last Command Output"));
        this.keyMap.put("trade_offers", makeListKey(TradeOffer.class, of("TradeOffers"), "sponge:trade_offers", "Trade Offers"));
        this.keyMap.put("dye_color", makeSingleKey(DyeColor.class, Value.class, of("DyeColor"), "sponge:dye_color", "Dye Color"));
        this.keyMap.put("firework_flight_modifier", makeSingleKey(Integer.class, BoundedValue.class, of("FlightModifier"), "sponge:flight_modifier", "Flight Modifier"));
        this.keyMap.put("firework_effects", makeListKey(FireworkEffect.class, of("FireworkEffects"), "sponge:firework_effects", "Firework Effects"));
        this.keyMap.put("spawner_remaining_delay", makeSingleKey(Short.class, MutableBoundedValue.class, of("SpawnerRemainingDelay"), "sponge:spawner_remaining_delay", "Spawner Remaining Delay"));
        this.keyMap.put("spawner_minimum_delay", makeSingleKey(Short.class, MutableBoundedValue.class, of("SpawnerMinimumDelay"), "sponge:spawner_minimum_delay", "Spawner Minimum Delay"));
        this.keyMap.put("connected_directions", makeSetKey(Direction.class, of("ConnectedDirections"), "sponge:connected_directions", "Connected Directions"));
        this.keyMap.put("connected_north", makeSingleKey(Boolean.class, Value.class, of("ConnectedNorth"), "sponge:connected_north", "Connected North"));
        this.keyMap.put("connected_south", makeSingleKey(Boolean.class, Value.class, of("ConnectedSouth"), "sponge:connected_south", "Connected South"));
        this.keyMap.put("connected_east", makeSingleKey(Boolean.class, Value.class, of("ConnectedEast"), "sponge:connected_east", "Connected East"));
        this.keyMap.put("connected_west", makeSingleKey(Boolean.class, Value.class, of("ConnectedWest"), "sponge:connected_west", "Connected West"));
        this.keyMap.put("direction", makeSingleKey(Direction.class, Value.class, of("Direction"), "sponge:direction", "Direction"));
        this.keyMap.put("dirt_type", makeSingleKey(DirtType.class, Value.class, of("DirtType"), "sponge:dirt_type", "Dirt Type"));
        this.keyMap.put("disguised_block_type", makeSingleKey(DisguisedBlockType.class, Value.class, of("DisguisedBlockType"), "sponge:disguised_block_type", "Disguised Block Type"));
        this.keyMap.put("disarmed", makeSingleKey(Boolean.class, Value.class, of("Disarmed"), "sponge:disarmed", "Disarmed"));
        this.keyMap.put("item_enchantments", makeListKey(ItemEnchantment.class, of("ItemEnchantments"), "sponge:item_enchantments", "Item Enchantments"));
        this.keyMap.put("banner_patterns", makeListKey(PatternLayer.class, of("BannerPatterns"), "sponge:banner_patterns", "Banner Patterns"));
        this.keyMap.put("banner_base_color", makeListKey(DyeColor.class, of("BannerBaseColor"), "sponge:banner_base_color", "Banner Base Color"));
        this.keyMap.put("horse_color", makeSingleKey(HorseColor.class, Value.class, of("HorseColor"), "sponge:horse_color", "Horse Color"));
        this.keyMap.put("horse_style", makeSingleKey(HorseStyle.class, Value.class, of("HorseStyle"), "sponge:horse_style", "Horse Style"));
        this.keyMap.put("horse_variant", makeSingleKey(HorseVariant.class, Value.class, of("HorseVariant"), "sponge:horse_variant", "Horse Variant"));
        this.keyMap.put("item_lore", makeListKey(Text.class, of("ItemLore"), "sponge:item_lore", "Item Lore"));
        this.keyMap.put("book_pages", makeListKey(Text.class, of("BookPages"), "sponge:book_pages", "Book Pages"));
        this.keyMap.put("golden_apple_type", makeSingleKey(GoldenApple.class, Value.class, of("GoldenAppleType"), "sponge:golden_apple_type", "Golden Apple Type"));
        this.keyMap.put("is_flying", makeSingleKey(Boolean.class, Value.class, of("IsFlying"), "sponge:is_flying", "Is Flying"));
        this.keyMap.put("experience_level", makeSingleKey(Integer.class, MutableBoundedValue.class, of("ExperienceLevel"), "sponge:experience_level", "Experience Level"));
        this.keyMap.put("total_experience", makeSingleKey(Integer.class, MutableBoundedValue.class, of("TotalExperience"), "sponge:total_experience", "Total Experience"));
        this.keyMap.put("experience_since_level", makeSingleKey(Integer.class, MutableBoundedValue.class, of("ExperienceSinceLevel"), "sponge:experience_since_level", "Experience Since Level"));
        this.keyMap.put("experience_from_start_of_level", makeSingleKey(Integer.class, ImmutableBoundedValue.class, of("ExperienceFromStartOfLevel"), "sponge:experience_from_start_of_level", "Experience From Start Of Level"));
        this.keyMap.put("book_author", makeSingleKey(Text.class, Value.class, of("BookAuthor"), "sponge:book_author", "Book Author"));
        this.keyMap.put("breakable_block_types", makeSetKey(BlockType.class, of("CanDestroy"), "sponge:can_destroy", "Can Destroy"));
        this.keyMap.put("placeable_blocks", makeSetKey(BlockType.class, of("CanPlaceOn"), "sponge:can_place_on", "Can Place On"));
        this.keyMap.put("walking_speed", makeSingleKey(Double.class, Value.class, of("WalkingSpeed"), "sponge:walking_speed", "Walking Speed"));
        this.keyMap.put("flying_speed", makeSingleKey(Double.class, Value.class, of("FlyingSpeed"), "sponge:flying_speed", "Flying Speed"));
        this.keyMap.put("slime_size", makeSingleKey(Integer.class, MutableBoundedValue.class, of("SlimeSize"), "sponge:slime_size", "Slime Size"));
        this.keyMap.put("villager_zombie_profession", makeSingleKey(Profession.class, Value.class, of("VillagerZombieProfession"), "sponge:villager_zombie_profession", "Villager Zombie Profession"));
        this.keyMap.put("is_playing", makeSingleKey(Boolean.class, Value.class, of("IsPlaying"), "sponge:is_playing", "Is Playing"));
        this.keyMap.put("is_sitting", makeSingleKey(Boolean.class, Value.class, of("IsSitting"), "sponge:is_sitting", "Is Sitting"));
        this.keyMap.put("is_sheared", makeSingleKey(Boolean.class, Value.class, of("IsSheared"), "sponge:is_sheared", "Is Sheared"));
        this.keyMap.put("pig_saddle", makeSingleKey(Boolean.class, Value.class, of("IsPigSaddled"), "sponge:is_pig_saddled", "Is Pig Saddled"));
        this.keyMap.put("tamed_owner", makeOptionalKey(UUID.class, of("TamerUUID"), "sponge:tamer_uuid", "Tamer UUID"));
        this.keyMap.put("is_wet", makeSingleKey(Boolean.class, Value.class, of("IsWet"), "sponge:is_wet", "Is Wet"));
        this.keyMap.put("elder_guardian", makeSingleKey(Boolean.class, Value.class, of("Elder"), "sponge:elder", "Elder"));
        this.keyMap.put("coal_type", makeSingleKey(CoalType.class, Value.class, of("CoalType"), "sponge:coal_type", "Coal Type"));
        this.keyMap.put("cooked_fish", makeSingleKey(CookedFish.class, Value.class, of("CookedFishType"), "sponge:cooked_fish_type", "Cooked Fish Type"));
        this.keyMap.put("fish_type", makeSingleKey(Fish.class, Value.class, of("RawFishType"), "sponge:raw_fish_type", "Raw Fish Type"));
        this.keyMap.put("represented_player", makeSingleKey(GameProfile.class, Value.class, of("RepresentedPlayer"), "sponge:represented_player", "Represented Player"));
        this.keyMap.put("passed_burn_time", makeSingleKey(Integer.class, MutableBoundedValue.class, of("PassedBurnTime"), "sponge:passed_burn_time", "Passed Burn Time"));
        this.keyMap.put("max_burn_time", makeSingleKey(Integer.class, MutableBoundedValue.class, of("MaxBurnTime"), "sponge:max_burn_time", "Max Burn Time"));
        this.keyMap.put("passed_cook_time", makeSingleKey(Integer.class, MutableBoundedValue.class, of("PassedCookTime"), "sponge:passed_cook_time", "Passed Cook Time"));
        this.keyMap.put("max_cook_time", makeSingleKey(Integer.class, MutableBoundedValue.class, of("MaxCookTime"), "sponge:max_cook_time", "Max Cook Time"));
        this.keyMap.put("contained_experience", makeSingleKey(Integer.class, Value.class, of("ContainedExperience"), "sponge:contained_experience", "Contained Experience"));
        this.keyMap.put("remaining_brew_time", makeSingleKey(Integer.class, MutableBoundedValue.class, of("RemainingBrewTime"), "sponge:remaining_brew_time", "Remaining Brew Time"));
        this.keyMap.put("stone_type", makeSingleKey(StoneType.class, Value.class, of("StoneType"), "sponge:stone_type", "Stone Type"));
        this.keyMap.put("prismarine_type", makeSingleKey(PrismarineType.class, Value.class, of("PrismarineType"), "sponge:prismarine_type", "Prismarine Type"));
        this.keyMap.put("brick_type", makeSingleKey(BrickType.class, Value.class, of("BrickType"), "sponge:brick_type", "Brick Type"));
        this.keyMap.put("quartz_type", makeSingleKey(QuartzType.class, Value.class, of("QuartzType"), "sponge:quartz_type", "Quartz Type"));
        this.keyMap.put("sand_type", makeSingleKey(SandType.class, Value.class, of("SandType"), "sponge:sand_type", "Sand Type"));
        this.keyMap.put("sandstone_type", makeSingleKey(SandstoneType.class, Value.class, of("SandstoneType"), "sponge:sandstone_type", "Sandstone Type"));
        this.keyMap.put("slab_type", makeSingleKey(SlabType.class, Value.class, of("SlabType"), "sponge:slab_type", "Slab Type"));
        this.keyMap.put("sandstone_type", makeSingleKey(SandstoneType.class, Value.class, of("SandstoneType"), "sponge:sandstone_type", "Sandstone Type"));
        this.keyMap.put("comparator_type", makeSingleKey(ComparatorType.class, Value.class, of("ComparatorType"), "sponge:comparator_type", "Comparator Type"));
        this.keyMap.put("hinge_position", makeSingleKey(Hinge.class, Value.class, of("HingePosition"), "sponge:hinge_position", "Hinge Position"));
        this.keyMap.put("piston_type", makeSingleKey(PistonType.class, Value.class, of("PistonType"), "sponge:piston_type", "Piston Type"));
        this.keyMap.put("portion_type", makeSingleKey(PortionType.class, Value.class, of("PortionType"), "sponge:portion_type", "Portion Type"));
        this.keyMap.put("rail_direction", makeSingleKey(RailDirection.class, Value.class, of("RailDirection"), "sponge:rail_direction", "Rail Direction"));
        this.keyMap.put("stair_shape", makeSingleKey(StairShape.class, Value.class, of("StairShape"), "sponge:stair_shape", "Stair Shape"));
        this.keyMap.put("wall_type", makeSingleKey(WallType.class, Value.class, of("WallType"), "sponge:wall_type", "Wall Type"));
        this.keyMap.put("double_plant_type", makeSingleKey(DoublePlantType.class, Value.class, of("DoublePlantType"), "sponge:double_plant_type", "Double Plant Type"));
        this.keyMap.put("big_mushroom_type", makeSingleKey(BigMushroomType.class, Value.class, of("BigMushroomType"), "sponge:big_mushroom_type", "Big Mushroom Type"));
        this.keyMap.put("ai_enabled", makeSingleKey(Boolean.class, Value.class, of("IsAiEnabled"), "sponge:is_ai_enabled", "Is Ai Enabled"));
        this.keyMap.put("creeper_charged", makeSingleKey(Boolean.class, Value.class, of("IsCreeperCharged"), "sponge:is_creeper_charged", "Is Creeper Charged"));
        this.keyMap.put("item_durability", makeSingleKey(Integer.class, MutableBoundedValue.class, of("ItemDurability"), "sponge:item_durability", "Item Durability"));
        this.keyMap.put("unbreakable", makeSingleKey(Boolean.class, Value.class, of("Unbreakable"), "sponge:unbreakable", "Unbreakable"));
        this.keyMap.put("spawnable_entity_type", makeSingleKey(EntityType.class, Value.class, of("SpawnableEntityType"), "sponge:spawnable_entity_type", "Spawnable Entity Type"));
        this.keyMap.put("fall_distance", makeSingleKey(Float.class, MutableBoundedValue.class, of("FallDistance"), "sponge:fall_distance", "Fall Distance"));
        this.keyMap.put("cooldown", makeSingleKey(Integer.class, Value.class, of("Cooldown"), "sponge:cooldown", "Cooldown"));
        this.keyMap.put("note_pitch", makeSingleKey(NotePitch.class, Value.class, of("Note"), "sponge:note", "Note"));
        this.keyMap.put("vehicle", makeSingleKey(EntitySnapshot.class, Value.class, of("Vehicle"), "sponge:vehicle", "Vehicle"));
        this.keyMap.put("base_vehicle", makeSingleKey(EntitySnapshot.class, Value.class, of("BaseVehicle"), "sponge:base_vehicle", "Base Vehicle"));
        this.keyMap.put("art", makeSingleKey(Art.class, Value.class, of("Art"), "sponge:art", "Art"));
        this.keyMap.put("fall_damage_per_block", makeSingleKey(Double.class, Value.class, of("FallDamagePerBlock"), "sponge:fall_damage_per_block", "Fall Damage Per Block"));
        this.keyMap.put("max_fall_damage", makeSingleKey(Double.class, Value.class, of("MaxFallDamage"), "sponge:max_fall_damage", "Max Fall Damage"));
        this.keyMap.put("falling_block_state", makeSingleKey(BlockState.class, Value.class, of("FallingBlockState"), "sponge:falling_block_state", "Falling Block State"));
        this.keyMap.put("can_place_as_block", makeSingleKey(Boolean.class, Value.class, of("CanPlaceAsBlock"), "sponge:can_place_as_block", "Can Place As Block"));
        this.keyMap.put("can_drop_as_item", makeSingleKey(Boolean.class, Value.class, of("CanDropAsItem"), "sponge:can_drop_as_item", "Can Drop As Item"));
        this.keyMap.put("fall_time", makeSingleKey(Integer.class, Value.class, of("FallTime"), "sponge:fall_time", "Fall Time"));
        this.keyMap.put("falling_block_can_hurt_entities", makeSingleKey(Boolean.class, Value.class, of("CanFallingBlockHurtEntities"), "sponge:can_falling_block_hurt_entities", "Can Falling Block Hurt Entities"));
        this.keyMap.put("represented_block", makeSingleKey(BlockState.class, Value.class, of("RepresentedBlock"), "sponge:represented_block", "Represented Block"));
        this.keyMap.put("offset", makeSingleKey(Integer.class, Value.class, of("BlockOffset"), "sponge:block_offset", "Block Offset"));
        this.keyMap.put("attached", makeSingleKey(Boolean.class, Value.class, of("Attached"), "sponge:attached", "Attached"));
        this.keyMap.put("should_drop", makeSingleKey(Boolean.class, Value.class, of("ShouldDrop"), "sponge:should_drop", "Should Drop"));
        this.keyMap.put("extended", makeSingleKey(Boolean.class, Value.class, of("Extended"), "sponge:extended", "Extended"));
        this.keyMap.put("growth_stage", makeSingleKey(Integer.class, MutableBoundedValue.class, of("GrowthStage"), "sponge:growth_stage", "Growth Stage"));
        this.keyMap.put("open", makeSingleKey(Boolean.class, Value.class, of("Open"), "sponge:open", "Open"));
        this.keyMap.put("power", makeSingleKey(Integer.class, MutableBoundedValue.class, of("Power"), "sponge:power", "Power"));
        this.keyMap.put("seamless", makeSingleKey(Boolean.class, Value.class, of("Seamless"), "sponge:seamless", "Seamless"));
        this.keyMap.put("snowed", makeSingleKey(Boolean.class, Value.class, of("Snowed"), "sponge:snowed", "Snowed"));
        this.keyMap.put("suspended", makeSingleKey(Boolean.class, Value.class, of("Suspended"), "sponge:suspended", "Suspended"));
        this.keyMap.put("occupied", makeSingleKey(Boolean.class, Value.class, of("Occupied"), "sponge:occupied", "Occupied"));
        this.keyMap.put("decayable", makeSingleKey(Boolean.class, Value.class, of("Decayable"), "sponge:decayable", "Decayable"));
        this.keyMap.put("in_wall", makeSingleKey(Boolean.class, Value.class, of("InWall"), "sponge:in_wall", "In Wall"));
        this.keyMap.put("delay", makeSingleKey(Integer.class, MutableBoundedValue.class, of("Delay"), "sponge:delay", "Delay"));
        this.keyMap.put("player_created", makeSingleKey(Boolean.class, Value.class, of("PlayerCreated"), "sponge:player_created", "Player Created"));
        this.keyMap.put("item_blockstate", makeSingleKey(BlockState.class, Value.class, of("ItemBlockState"), "sponge:item_block_state", "Item Block State"));
        this.keyMap.put("skeleton_type", makeSingleKey(SkeletonType.class, Value.class, of("SkeletonType"), "sponge:skeleton_type", "Skeleton Type"));
        this.keyMap.put("ocelot_type", makeSingleKey(OcelotType.class, Value.class, of("OcelotType"), "sponge:ocelot_type", "Ocelot Type"));
        this.keyMap.put("rabbit_type", makeSingleKey(RabbitType.class, Value.class, of("RabbitType"), "sponge:rabbit_type", "Rabbit Type"));
        this.keyMap.put("lock_token", makeSingleKey(String.class, Value.class, of("Lock"), "sponge:lock", "Lock"));
        this.keyMap.put("banner_base_color", makeSingleKey(DyeColor.class, Value.class, of("BannerBaseColor"), "sponge:banner_base_color", "Banner Base Color"));
        this.keyMap.put("banner_patterns", new PatternKey());
        this.keyMap.put("respawn_locations", makeMapKey(UUID.class, Vector3d.class, of("RespawnLocations"), "sponge:respawn_locations", "Respawn Locations"));
        this.keyMap.put("expiration_ticks", makeSingleKey(Integer.class, MutableBoundedValue.class, of("ExpirationTicks"), "sponge:expiration_ticks", "Expiration Ticks"));
        this.keyMap.put("skin_unique_id", makeSingleKey(UUID.class, Value.class, of("SkinUUID"), "sponge:skin_uuid", "Skin UUID"));
        this.keyMap.put("moisture", makeSingleKey(Integer.class, MutableBoundedValue.class, of("Moisture"), "sponge:moisture", "Moisture"));
        this.keyMap.put("angry", makeSingleKey(Boolean.class, Value.class, of("Angry"), "sponge:angry", "Angry"));
        this.keyMap.put("anger", makeSingleKey(Integer.class, MutableBoundedValue.class, of("Anger"), "sponge:anger", "Anger"));
        this.keyMap.put("rotation", makeSingleKey(Rotation.class, Value.class, of("Rotation"), "sponge:rotation", "Rotation"));
        this.keyMap.put("is_splash_potion", makeSingleKey(Boolean.class, Value.class, of("IsSplashPotion"), "sponge:is_splash_potion", "Is Splash Potion"));
        this.keyMap.put("affects_spawning", makeSingleKey(Boolean.class, Value.class, of("AffectsSpawning"), "sponge:affects_spawning", "Affects Spawning"));
        this.keyMap.put("critical_hit", makeSingleKey(Boolean.class, Value.class, of("CriticalHit"), "sponge:critical_hit", "Critical Hit"));
        this.keyMap.put("generation", makeSingleKey(Integer.class, MutableBoundedValue.class, of("Generation"), "sponge:generation", "Generation"));
        this.keyMap.put("passenger", makeSingleKey(EntitySnapshot.class, Value.class, of("PassengerSnapshot"), "sponge:passenger_snapshot", "Passenger Snapshot"));
        this.keyMap.put("knockback_strength", makeSingleKey(Integer.class, MutableBoundedValue.class, of("KnockbackStrength"), "sponge:knockback_strength", "Knockback Strength"));
        this.keyMap.put("persists", makeSingleKey(Boolean.class, Value.class, of("Persists"), "sponge:persists", "Persists"));
        this.keyMap.put("stored_enchantments", makeListKey(ItemEnchantment.class, of("StoredEnchantments"), "sponge:stored_enchantments", "Stored Enchantments"));
        this.keyMap.put("is_sprinting", makeSingleKey(Boolean.class, Value.class, of("Sprinting"), "sponge:sprinting", "Sprinting"));
        this.keyMap.put("stuck_arrows", makeSingleKey(Integer.class, MutableBoundedValue.class, of("StuckArrows"), "sponge:stuck_arrows", "Stuck Arrows"));
        this.keyMap.put("invisibility_ignores_collision", makeSingleKey(Boolean.class, Value.class, of("InvisiblityIgnoresCollision"), "sponge:invisiblity_ignores_collision", "Invisiblity Ignores Collision"));
        this.keyMap.put("invisibility_prevents_targeting", makeSingleKey(Boolean.class, Value.class, of("InvisibilityPreventsTargeting"), "sponge:invisibility_prevents_targeting", "Invisibility Prevents Targeting"));
        this.keyMap.put("is_aflame", makeSingleKey(Boolean.class, Value.class, of("IsAflame"), "sponge:is_aflame", "Is Aflame"));
        this.keyMap.put("can_breed", makeSingleKey(Boolean.class, Value.class, of("CanBreed"), "sponge:can_breed", "Can Breed"));
        this.keyMap.put("fluid_item_stack", makeSingleKey(FluidStackSnapshot.class, Value.class, of("FluidItemContainerSnapshot"), "sponge:fluid_item_container_snapshot", "Fluid Item Container Snapshot"));
        this.keyMap.put("fluid_tank_contents", makeMapKey(Direction.class, List.class, of("FluidTankContents"), "sponge:fluid_tank_contents", "Fluid Tank Contents"));
        this.keyMap.put("custom_name_visible", makeSingleKey(Boolean.class, Value.class, of("CustomNameVisible"), "sponge:custom_name_visible", "Custom Name Visible"));
        this.keyMap.put("first_date_played", makeSingleKey(Instant.class, Value.class, of("FirstTimeJoined"), "sponge:first_time_joined", "First Time Joined"));
        this.keyMap.put("last_date_played", makeSingleKey(Instant.class, Value.class, of("LastTimePlayed"), "sponge:last_time_played", "Last Time Played"));
        this.keyMap.put("hide_enchantments", makeSingleKey(Boolean.class, Value.class, of("HideEnchantments"), "sponge:hide_enchantments", "Hide Enchantments"));
        this.keyMap.put("hide_attributes", makeSingleKey(Boolean.class, Value.class, of("HideAttributes"), "sponge:hide_attributes", "Hide Attributes"));
        this.keyMap.put("hide_unbreakable", makeSingleKey(Boolean.class, Value.class, of("HideUnbreakable"), "sponge:hide_unbreakable", "Hide Unbreakable"));
        this.keyMap.put("hide_can_destroy", makeSingleKey(Boolean.class, Value.class, of("HideCanDestroy"), "sponge:hide_can_destroy", "Hide Can Destroy"));
        this.keyMap.put("hide_can_place", makeSingleKey(Boolean.class, Value.class, of("HideCanPlace"), "sponge:hide_can_place", "Hide Can Place"));
        this.keyMap.put("hide_miscellaneous", makeSingleKey(Boolean.class, Value.class, of("HideMiscellaneous"), "sponge:hide_miscellaneous", "Hide Miscellaneous"));
        this.keyMap.put("potion_effects", makeListKey(PotionEffect.class, of("PotionEffects"), "sponge:potion_effects", "Potion Effects"));
        this.keyMap.put("body_rotations", makeMapKey(BodyPart.class, Vector3d.class, of("BodyRotations"), "sponge:body_rotations", "Body Rotations"));
        this.keyMap.put("head_rotation", makeSingleKey(Vector3d.class, Value.class, of("HeadRotation"), "sponge:head_rotation", "Head Rotation"));
        this.keyMap.put("chest_rotation", makeSingleKey(Vector3d.class, Value.class, of("ChestRotation"), "sponge:chest_rotation", "Chest Rotation"));
        this.keyMap.put("left_arm_rotation", makeSingleKey(Vector3d.class, Value.class, of("LeftArmRotation"), "sponge:left_arm_rotation", "Left Arm Rotation"));
        this.keyMap.put("right_arm_rotation", makeSingleKey(Vector3d.class, Value.class, of("RightArmRotation"), "sponge:right_arm_rotation", "Right Arm Rotation"));
        this.keyMap.put("left_leg_rotation", makeSingleKey(Vector3d.class, Value.class, of("LeftLegRotation"), "sponge:left_leg_rotation", "Left Leg Rotation"));
        this.keyMap.put("right_leg_rotation", makeSingleKey(Vector3d.class, Value.class, of("RightLegRotation"), "sponge:right_leg_rotation", "Right Leg Rotation"));
        this.keyMap.put("beacon_primary_effect", makeOptionalKey(PotionEffectType.class, of("BeaconPrimaryEffect"), "sponge:beacon_primary_effect", "Beacon Primary Effect"));
        this.keyMap.put("beacon_secondary_effect", makeOptionalKey(PotionEffectType.class, of("BeaconSecondaryEffect"), "sponge:beacon_secondary_effect", "Beacon Secondary Effect"));
        this.keyMap.put("targeted_location", makeSingleKey(Vector3d.class, Value.class, of("TargetedVector3d"), "sponge:targeted_vector_3d", "Targeted Vector3d"));
        this.keyMap.put("fuse_duration", makeSingleKey(Integer.class, Value.class, of("FuseDuration"), "sponge:fuse_duration", "Fuse Duration"));
        this.keyMap.put("ticks_remaining", makeSingleKey(Integer.class, Value.class, of("TicksRemaining"), "sponge:ticks_remaining", "Ticks Remaining"));
        this.keyMap.put("explosion_radius", makeSingleKey(Integer.class, Value.class, of("ExplosionRadius"), "sponge:explosion_radius", "Explosion Radius"));
        this.keyMap.put("armor_stand_has_arms", makeSingleKey(Boolean.class, Value.class, of("HasArms"), "sponge:has_arms", "Has Arms"));
        this.keyMap.put("armor_stand_has_base_plate", makeSingleKey(Boolean.class, Value.class, of("HasBasePlate"), "sponge:has_base_plate", "Has Base Plate"));
        this.keyMap.put("armor_stand_has_gravity", makeSingleKey(Boolean.class, Value.class, of("HasGravity"), "sponge:has_gravity", "Has Gravity"));
        this.keyMap.put("armor_stand_marker", makeSingleKey(Boolean.class, Value.class, of("IsMarker"), "sponge:is_marker", "Is Marker"));
        this.keyMap.put("armor_stand_is_small", makeSingleKey(Boolean.class, Value.class, of("IsSmall"), "sponge:is_small", "Is Small"));

    }

    @Override
    public void registerAdditionalCatalog(Key extraCatalog) {
        checkState(!SpongeDataManager.areRegistrationsComplete(), "Cannot register new Keys after Data Registration has completed!");
        checkNotNull(extraCatalog, "Key cannot be null!");
        final String id = extraCatalog.getId().toLowerCase(Locale.ENGLISH);
        checkArgument(!id.startsWith("sponge:"), "A plugin is trying to register custom keys under the sponge id namespace! This is a fake key! " + id);
        this.keyMap.put(id, extraCatalog);
    }

    @Override
    public Optional<Key> getById(String id) {
        if (checkNotNull(id, "Key id cannot be null!").contains("sponge:")) {
            id = id.replace("sponge:", "");
        }
        return Optional.ofNullable(this.keyMap.get(id));
    }

    @Override
    public Collection<Key> getAll() {
        return Collections.unmodifiableCollection(this.keyMap.values());
    }

    private static final class PatternKey implements Key<PatternListValue> {
        PatternKey() {
        }

        @Override
        public Class<PatternListValue> getValueClass() {
            return PatternListValue.class;
        }

        @Override
        public Class<?> getElementClass() {
            return PatternLayer.class;
        }

        @Override
        public DataQuery getQuery() {
            return of("BannerPatterns");
        }

        @Override
        public String getId() {
            return "sponge:banner_patterns";
        }

        @Override
        public String getName() {
            return "Banner Patterns";
        }
    }

    KeyRegistryModule() {
    }

    static final class Holder {
        static final KeyRegistryModule INSTANCE = new KeyRegistryModule();
    }
}

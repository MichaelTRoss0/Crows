package dev.michaeltross.crows.init;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import dev.michaeltross.crows.Crows;
import dev.michaeltross.crows.entity.Crow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = Crows.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CrowRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_DEFERRED = DeferredRegister
            .create(ForgeRegistries.ENTITIES, Crows.MOD_ID);
    private static final List<Item> SPAWN_EGGS = Lists.newArrayList();

    public static final RegistryObject<EntityType<Crow>> CROW = createEntity("crow", Crow::new, 0.4F, 0.95F, 0x262626,
            0x636363);

    private static <T extends Animal> RegistryObject<EntityType<T>> createEntity(String name,
            EntityType.EntityFactory<T> factory, float width, float height, int eggPrimary, int eggSecondary) {
        ResourceLocation location = new ResourceLocation(Crows.MOD_ID, name);
        EntityType<T> entity = EntityType.Builder.of(factory, MobCategory.CREATURE).sized(width, height)
                .setTrackingRange(64).setUpdateInterval(1).build(location.toString());
        Item spawnEgg = new ForgeSpawnEggItem((Supplier<? extends EntityType<? extends Mob>>) entity, eggPrimary,
                eggSecondary, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC));
        spawnEgg.setRegistryName(new ResourceLocation(Crows.MOD_ID, name + "_spawn_egg"));
        SPAWN_EGGS.add(spawnEgg);

        return ENTITY_DEFERRED.register(name, () -> entity);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        SpawnPlacements.register(CROW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Crow::canCrowSpawn);
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(CROW.get(), Crow.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        for (Item spawnEgg : SPAWN_EGGS) {
            Preconditions.checkNotNull(spawnEgg.getRegistryName(), "registryName");
            event.getRegistry().register(spawnEgg);
        }
    }
}

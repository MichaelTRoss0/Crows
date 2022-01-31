package dev.michaeltross.crows.init;

import dev.michaeltross.crows.Crows;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;

public class CrowTags {
    public static void init() {
        EntityTypes.init();
    }

    public static class EntityTypes {
        private static void init() {}

        public static final Tags.IOptionalNamedTag<EntityType<?>> CROW_CIRCLE_MOBS = tag("crow_circle_mobs");
        public static final Tags.IOptionalNamedTag<EntityType<?>> CROW_PRIORITY_MOBS = tag("crow_priority_mobs");

        private static Tags.IOptionalNamedTag<EntityType<?>> tag(String name) {
            return EntityTypeTags.createOptional(new ResourceLocation(Crows.MOD_ID, name));
        }
    }
}

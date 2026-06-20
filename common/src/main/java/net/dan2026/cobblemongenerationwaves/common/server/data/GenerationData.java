/*
 *
 * CobblemonGenerationWaves - A NeoForge Minecraft Mod.
 *
 * Copyright (c) 2026 DAN2026. All rights reserved.
 *
 * This software is licensed under the CobblemonGenerationWaves License v1.0.
 *  A copy of this License should have been included with this software.
 *  If not, you can obtain a copy at [https://github.com/DAN2026/CobblemonGenerationWaves/blob/master/LICENSE].
 */

package net.dan2026.cobblemongenerationwaves.common.server.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class GenerationData extends SavedData {

    private final Set<String> activeGenerations = new HashSet<>();

    /**
     *
     * Gets all current active generations.
     *
     * @return The set of currently active generation IDs.
     */

    public Set<String> getActiveGenerations() {
        return activeGenerations;
    }

    /**
     * Adds a generation to the data.
     * @param gen The generation ID to add.
     */

    public void addGeneration(String gen) {
        if (activeGenerations.add(gen)) {
            setDirty();
        }
    }

    /**
     * Removes a generation from the data.
     * @param gen The generation ID to remove.
     */

    public void removeGeneration(String gen) {
        if (activeGenerations.remove(gen)) {
            setDirty();
        }
    }

    /**
     *
     * Saves the generation list.
     * @param tag The tag to save data into.
     * @param provider The holder lookup provider.
     * @return The updated compound tag.
     */

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        for (String gen : activeGenerations) {
            list.add(StringTag.valueOf(gen));
        }
        tag.put("generations", list);
        return tag;
    }

    /**
     * Loads the generation list.
     *
     * @param tag The tag to load data from.
     * @param provider The holder lookup provider.
     * @return A new instance of GenerationData.
     */


    public static GenerationData load(CompoundTag tag, HolderLookup.Provider provider) {
        GenerationData data = new GenerationData();
        ListTag list = tag.getList("generations", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            data.activeGenerations.add(list.getString(i));
        }
        return data;
    }

    /**
     * Gets the generation wave data for the level.
     *
     * @param level The server level.
     * @return The persistent GenerationData instance for the provided level.
     */

    public static GenerationData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<>(
                        GenerationData::new,
                        GenerationData::load,
                        null
                ),
                "cobblemongenerationwaves_data"
        );
    }
}
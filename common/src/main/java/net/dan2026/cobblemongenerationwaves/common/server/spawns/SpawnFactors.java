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

package net.dan2026.cobblemongenerationwaves.common.server.spawns;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.pokemon.Species;
import net.dan2026.cobblemongenerationwaves.common.server.data.GenerationData;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public class SpawnFactors implements SpawningInfluence {


    /**
     * Dictates whether debugging is Enabled / Disabled.
     */

    private static final boolean DEBUG = false;

    @Override
    public boolean affectSpawnable(@NotNull SpawnDetail detail, @NotNull SpawnablePosition spawnablePosition) {
        if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) {
            return true;
        }

        String speciesName = pokemonDetail.getPokemon().getSpecies();
        if (speciesName == null) return false;

        Species species = getSpeciesByName(speciesName);
        if (species == null) {
            Cobblemon.LOGGER.warn("Could not find species with name: {}", speciesName);
            return false;
        }

        ServerLevel serverLevel = spawnablePosition.getWorld();
        Set<String> activeGenerations = GenerationData.get(serverLevel).getActiveGenerations();

        boolean spawnable = species
                .getLabels()
                .stream()
                .anyMatch(label -> activeGenerations
                        .stream()
                        .anyMatch(label::startsWith));

        if (DEBUG) {
            Cobblemon.LOGGER.info("Spawn check for {}: Allowed? {}", species.getName(), spawnable);
        }

        if (DEBUG & spawnable) {
            logSpawnDebug(species);
        }

        return spawnable;
    }

    /**
     * Resolves a species from a name string using the Cobblemon registry.
     *
     * @param name The species name (e.g., "aerodactyl").
     * @return The {@link Species} object, or null if no match exists.
     */

    @Nullable
    private Species getSpeciesByName(@NotNull String name) {
        try {
            return PokemonSpecies.getByName(name);
        } catch (Exception e) {
            Cobblemon.LOGGER.error("Error retrieving species by name: {}", name, e);
            return null;
        }
    }

    /**
     * Logs the spawn debug information in the required format.
     *
     * @param species The species to log.
     * @see Species#getLabels()
     */

    private void logSpawnDebug(@NotNull Species species) {
        String generation = "Unknown";

        for (String label : species.getLabels()) {
            if (label.startsWith("gen")) {
                generation = label;
                break;
            }
        }

        Cobblemon.LOGGER.info("Species: {}, Generation: {}", species.getName(), generation);
    }

    /**
     * Adds a generation to the persistent data.
     *
     * @param level The server level.
     * @param gen   The generation string to add.
     */

    public static void addGeneration(@NotNull ServerLevel level, @NotNull String gen) {
        GenerationData.get(level).addGeneration(gen);
    }

    /**
     * Removes a generation from the persistent data.
     *
     * @param level The server level.
     * @param gen   The generation string to remove.
     */

    public static void removeGeneration(@NotNull ServerLevel level, @NotNull String gen) {
        GenerationData.get(level).removeGeneration(gen);
    }

    /**
     * Gets all allowed generations from the persistent data.
     *
     * @param level The server level.
     * @return An unmodifiable set of allowed generations.
     */

    public static Set<String> getAllowedGenerations(@NotNull ServerLevel level) {
        return Collections.unmodifiableSet(GenerationData.get(level).getActiveGenerations());
    }
}
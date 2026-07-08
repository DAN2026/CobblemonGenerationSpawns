/*
 *
 * Cobblemon: Generation Spawning - A NeoForge Minecraft Mod.
 *
 * Copyright (c) 2026 DAN2026. All rights reserved.
 *
 * This software is licensed under the CobblemonGenerationSpawning License v1.0.
 *  A copy of this License should have been included with this software.
 *  If not, you can obtain a copy at [https://github.com/DAN2026/CobblemonGenerationSpawning/blob/master/LICENSE].
 */

package net.dan2026.cobblemongenerationspawns.common.server.registry;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.pokemon.Species;
import net.dan2026.cobblemongenerationspawns.common.server.spawns.SpawnFactors;

public class EventRegistry {

    public static void register() {
        CobblemonEvents.POKE_SNACK_SPAWN_POKEMON_PRE.subscribe(Priority.NORMAL, event -> {
            SpawnDetail detail = event.getSpawnAction().getDetail();

            if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) {
//                Cobblemon.LOGGER.info("[GenerationSpawns] PokeSnack spawn is not a PokemonSpawnDetail, skipping.");
                return;
            }

            String speciesName = pokemonDetail.getPokemon().getSpecies();
            if (speciesName == null) {
//                Cobblemon.LOGGER.info("[GenerationSpawns] PokeSnack spawn has null species, skipping.");
                return;
            }

            Species species = PokemonSpecies.getByName(speciesName);
            if (species == null) {
//                Cobblemon.LOGGER.info("[GenerationSpawns] Could not find species for: {}", speciesName);
                return;
            }

            boolean matches = species.getLabels().stream()
                    .anyMatch(label -> SpawnFactors.getCachedGenerations().stream()
                            .anyMatch(label::startsWith));

//            Cobblemon.LOGGER.info("[GenerationSpawns] PokeSnack attempting to spawn: {} | Labels: {} | Active Gens: {} | Allowed: {}",
//                    speciesName,
//                    species.getLabels(),
//                    SpawnFactors.getCachedGenerations(),
//                    matches
//            );

            if (!matches) {
                event.cancel();
//                Cobblemon.LOGGER.info("[GenerationSpawns] Cancelled PokeSnack spawn for: {}", speciesName);
            }
        });
    }
}

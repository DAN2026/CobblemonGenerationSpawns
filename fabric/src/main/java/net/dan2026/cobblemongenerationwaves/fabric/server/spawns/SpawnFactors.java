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

package net.dan2026.cobblemongenerationwaves.fabric.server.spawns;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SpawnFactors implements SpawningInfluence {


    @Override
    public boolean affectSpawnable(@NotNull SpawnDetail detail, @NotNull SpawnablePosition spawnablePosition) {

        /*
        Check if the entity trying to spawn is a pokemon and not an NPC.
         */

        if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) return true;

        String speciesName = pokemonDetail.getPokemon().getSpecies();

        /*
        Returns early if the pokemon datapack is incorrectly formatted.
         */

        if (speciesName == null) return false;

        Species species = PokemonSpecies.getByName(speciesName);

        System.out.println("Pokemon Species: " + species);

//        if(species.getName().equalsIgnoreCase("bramblin")) return false;

        return true;
    }
}

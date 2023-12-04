package com.github.supergluelib.foundation.input

import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.util.*

object Input {
    object BlockPlace {
        internal val awaitingBlockInput: HashMap<UUID, (Block) -> Unit> = HashMap()

        fun take(player: Player, whenPlaced: (Block) -> Unit) {
            awaitingBlockInput[player.uniqueId] = whenPlaced
        }
    }

}
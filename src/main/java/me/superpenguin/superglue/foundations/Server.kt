package me.superpenguin.superglue.foundations

object Server {

    /** @return true if the server is running paper, or a fork of paper. */
    fun isPaper() = runCatching { Class.forName("com.destroystokyo.paper.event.player.PlayerArmorChangeEvent") }.isSuccess
    fun isFolia() = runCatching { Class.forName("io.papermc.paper.threadedregions.RegionizedServer") }.isSuccess

}
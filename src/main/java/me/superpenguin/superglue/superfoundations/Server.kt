package me.superpenguin.superglue.superfoundations

object Server {

    /** @return true if the server is running paper, or a fork of paper. */
    fun isPaper() = runCatching { Class.forName("com.destroystokyo.paper.event.player.PlayerArmorChangeEvent") }.isSuccess

}
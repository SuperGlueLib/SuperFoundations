package me.superpenguin.superglue.foundations.extensions

import org.bukkit.inventory.ItemStack

/** Decrement the amount of the itemstack by 1 */
fun ItemStack.removeOne() { amount -= 1 }
/** @return true if the itemstack is not null and not air */
fun ItemStack?.isValid() = (this != null) && !type.isAir
package net.qbar.common.network.action;

import net.minecraft.nbt.NBTTagCompound;

@FunctionalInterface
public interface IActionCallback
{
    void call(NBTTagCompound response);
}

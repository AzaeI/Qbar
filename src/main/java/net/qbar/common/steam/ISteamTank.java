package net.qbar.common.steam;

public interface ISteamTank extends ISteamHandler
{
    SteamStack getSteam();

    int getAmount();

    int getCapacity();

    int getPressure();

    int getMaxPressure();
}
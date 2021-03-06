package net.qbar.common.steam;

import java.text.NumberFormat;

public class SteamUtil
{
    public static final int BASE_PRESSURE = 1;

    public static final NumberFormat pressureFormat;

    static
    {
        pressureFormat = NumberFormat.getInstance();
        SteamUtil.pressureFormat.setMaximumFractionDigits(2);
        SteamUtil.pressureFormat.setMinimumFractionDigits(2);
    }
}

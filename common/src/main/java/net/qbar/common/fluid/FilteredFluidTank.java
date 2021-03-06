package net.qbar.common.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.function.Predicate;

public class FilteredFluidTank extends FluidTank
{
    private final Predicate<FluidStack> filter;

    public FilteredFluidTank(final int capacity, final Predicate<FluidStack> filter)
    {
        super(capacity);

        this.filter = filter;
    }

    @Override
    public boolean canDrainFluidType(final FluidStack fluid)
    {
        return this.filter.test(fluid);
    }

    @Override
    public boolean canFillFluidType(final FluidStack fluid)
    {
        return this.filter.test(fluid);
    }
}

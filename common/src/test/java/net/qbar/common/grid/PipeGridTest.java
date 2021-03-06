package net.qbar.common.grid;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PipeGridTest
{
    @Before
    public void setupTest()
    {
        GridManager.getInstance().cableGrids.clear();
    }

    @Test
    public void testPipeList()
    {
        PipeGrid grid = new PipeGrid(0, 64);
        IFluidPipe pipe = mock(IFluidPipe.class);

        grid.addCable(pipe);

        assertThat(grid.getTank().getCapacity()).isEqualTo(4 * grid.getTransferCapacity());

        grid.addCable(mock(IFluidPipe.class));
        grid.addCable(mock(IFluidPipe.class));
        grid.addCable(mock(IFluidPipe.class));
        grid.addCable(mock(IFluidPipe.class));

        assertThat(grid.getTank().getCapacity()).isEqualTo(5 * grid.getTransferCapacity());

        grid.removeCable(pipe);

        assertThat(grid.getTank().getCapacity()).isEqualTo(4 * grid.getTransferCapacity());

        grid.addCable(pipe);
        grid.addOutput(pipe);
        grid.tick();

        verify(pipe, times(1)).fillNeighbors();
    }
}

package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchedulingStrategyTest {

    @Test
    public void testSchedulingStrategyInterface() {
        Class<?> strategyInterface = SchedulingStrategy.class;
        assertTrue(strategyInterface.isInterface());
    }
}
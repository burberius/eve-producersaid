package net.troja.eve.producersaid.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import net.troja.eve.producersaid.CalculationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProductionControllerTest {
    private final ProductionController objectToTest = new ProductionController();

    @Mock
    private CalculationService calculationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectToTest.setCalcService(calculationService);
    }

    @Test
    public void testEmptyCall() {
        objectToTest.queryProduction("", "");

        verify(calculationService, never()).getBlueprintProductions(any(), any());
    }

    @Test
    public void testNullCall() {
        objectToTest.queryProduction(null, null);

        verify(calculationService, never()).getBlueprintProductions(any(), any());
    }

    @Test
    public void testNullSystemCall() {
        objectToTest.queryProduction("1234", null);

        verify(calculationService, times(1)).getBlueprintProductions(Arrays.asList(1234), "Jita");
    }

    @Test
    public void testEmptyListCall() {
        objectToTest.queryProduction(",    ,,", "");

        verify(calculationService, never()).getBlueprintProductions(any(), any());
    }

    @Test
    public void testNormalCall() {
        objectToTest.queryProduction("1234", "");

        verify(calculationService, times(1)).getBlueprintProductions(Arrays.asList(1234), "Jita");
    }
}

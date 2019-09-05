package com.example.yevgensydorenko.tabbed;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ArduinoTest {

    @Test
    public void obtainSignalCorrectData() {
        String input = "27,12";
        String[] output;
        String[] expected = {"27 °C", "12 m"};
        Arduino ard = new Arduino();
        output = ard.obtainSignal(input);
        assertArrayEquals(expected, output);
    }
    @Test
    public void obtainSignalIncorrectData() {
        String input = "fail data";
        String[] output;
        String[] expected = {"00 °C", "00 m"};
        Arduino ard = new Arduino();
        output = ard.obtainSignal(input);
        assertArrayEquals(expected, output);
    }
}
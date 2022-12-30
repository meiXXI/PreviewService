package com.meixxi.service.preview.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensionUtilTest {

    @Test
    public void bytes2readable_1() {

        // act
        String result = DimensionUtil.bytes2readable(46554575);

        // assert
        assertEquals("44.40 MB", result, "Byte Expression is wrong.");

    }

    @Test
    public void bytes2readable_2() {

        // act
        String result = DimensionUtil.bytes2readable(1024);

        // assert
        assertEquals("1.00 KB", result, "Byte Expression is wrong.");

    }

    @Test
    public void bytes2readable_3() {

        // act
        String result = DimensionUtil.bytes2readable(1023);

        // assert
        assertEquals("1023 B", result, "Byte Expression is wrong.");

    }

}
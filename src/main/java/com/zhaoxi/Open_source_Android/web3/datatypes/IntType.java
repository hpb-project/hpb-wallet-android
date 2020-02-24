package com.zhaoxi.Open_source_Android.web3.datatypes;

import java.math.BigInteger;

/**
 * des:
 * Created by ztt on 2018/8/1.
 */

public abstract class IntType extends NumericType {

    public IntType(String typePrefix, int bitSize, BigInteger value) {
        super(typePrefix + bitSize, value);
        if (!valid(bitSize, value)) {
            throw new UnsupportedOperationException(
                    "Bitsize must be 8 bit aligned, and in range 0 < bitSize <= 256");
        }
    }

    boolean valid(int bitSize, BigInteger value) {
        return isValidBitSize(bitSize)
                && isValidBitCount(bitSize, value);
    }

    static boolean isValidBitSize(int bitSize) {
        return bitSize % 8 == 0
                && bitSize > 0
                && bitSize <= MAX_BIT_LENGTH;
    }

    private static boolean isValidBitCount(int bitSize, BigInteger value) {
        return value.bitLength() <= bitSize;
    }
}

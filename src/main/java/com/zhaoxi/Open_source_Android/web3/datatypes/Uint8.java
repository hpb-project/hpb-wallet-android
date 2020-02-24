package com.zhaoxi.Open_source_Android.web3.datatypes;

import java.math.BigInteger;

/**
 * @author ztt
 * @des
 * @date 2019/8/23.
 */

public class Uint8 extends Uint {
    public static final Uint8 DEFAULT;

    public Uint8(BigInteger value) {
        super(8, value);
    }

    public Uint8(long value) {
        this(BigInteger.valueOf(value));
    }

    static {
        DEFAULT = new Uint8(BigInteger.ZERO);
    }
}

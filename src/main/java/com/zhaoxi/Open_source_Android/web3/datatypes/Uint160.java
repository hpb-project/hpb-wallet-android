package com.zhaoxi.Open_source_Android.web3.datatypes;

import java.math.BigInteger;

/**
 * des:
 * Created by ztt on 2018/8/1.
 */

public class Uint160 extends Uint {
    public static final Uint160 DEFAULT = new Uint160(BigInteger.ZERO);

    public Uint160(BigInteger value) {
        super(160, value);
    }

    public Uint160(long value) {
        this(BigInteger.valueOf(value));
    }
}

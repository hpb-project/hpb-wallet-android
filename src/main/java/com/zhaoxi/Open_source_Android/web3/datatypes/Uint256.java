package com.zhaoxi.Open_source_Android.web3.datatypes;

import java.math.BigInteger;

/**
 * des:
 * Created by ztt on 2018/8/1.
 */
public class Uint256 extends Uint {
    public static final Uint256 DEFAULT = new Uint256(BigInteger.ZERO);

    public Uint256(BigInteger value) {
        super(256, value);
    }

    public Uint256(long value) {
        this(BigInteger.valueOf(value));
    }
}

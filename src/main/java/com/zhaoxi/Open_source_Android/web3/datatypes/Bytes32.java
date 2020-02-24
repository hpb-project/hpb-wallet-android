package com.zhaoxi.Open_source_Android.web3.datatypes;

/**
 * @author ztt
 * @des
 * @date 2019/8/23.
 */

public class Bytes32 extends Bytes {
    public static final Bytes32 DEFAULT = new Bytes32(new byte[32]);

    public Bytes32(byte[] value) {
        super(32, value);
    }
}

package com.zhaoxi.Open_source_Android.web3.crypto;

import com.zhaoxi.Open_source_Android.web3.utils.Numeric;

import java.math.BigInteger;

/**
 * Transaction class used for signing transactions locally.<br>
 * For the specification, refer to p4 of the <a href="http://gavwood.com/paper.pdf">
 * yellow paper</a>.
 */
public class RawTransaction {

    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private BigInteger value;
    private String data;

    private RawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                           BigInteger value, String data) {
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.to = to;
        this.value = value;

        if (data != null) {
            this.data = Numeric.cleanHexPrefix(data);
        }
    }

    public static RawTransaction createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value, String data) {

        return new RawTransaction(nonce, gasPrice, gasLimit, to, value, data);
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public String getTo() {
        return to;
    }

    public BigInteger getValue() {
        return value;
    }

    public String getData() {
        return data;
    }
}

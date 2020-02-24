package com.zhaoxi.Open_source_Android.web3.utils;


import android.content.Context;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.net.bean.RedBaseBean;
import com.zhaoxi.Open_source_Android.web3.crypto.Credentials;
import com.zhaoxi.Open_source_Android.web3.crypto.ECKeyPair;
import com.zhaoxi.Open_source_Android.web3.crypto.RawTransaction;
import com.zhaoxi.Open_source_Android.web3.crypto.Sign;
import com.zhaoxi.Open_source_Android.web3.crypto.TransactionEncoder;
import com.zhaoxi.Open_source_Android.web3.datatypes.Address;
import com.zhaoxi.Open_source_Android.web3.datatypes.Bool;
import com.zhaoxi.Open_source_Android.web3.datatypes.Bytes32;
import com.zhaoxi.Open_source_Android.web3.datatypes.DynamicArray;
import com.zhaoxi.Open_source_Android.web3.datatypes.Function;
import com.zhaoxi.Open_source_Android.web3.datatypes.FunctionEncoder;
import com.zhaoxi.Open_source_Android.web3.datatypes.Type;
import com.zhaoxi.Open_source_Android.web3.datatypes.TypeReference;
import com.zhaoxi.Open_source_Android.web3.datatypes.Uint256;
import com.zhaoxi.Open_source_Android.web3.datatypes.Uint8;
import com.zhaoxi.Open_source_Android.web3.datatypes.Utf8String;
import com.zhaoxi.Open_source_Android.web3.rlp.RlpEncoder;
import com.zhaoxi.Open_source_Android.web3.rlp.RlpList;
import com.zhaoxi.Open_source_Android.web3.rlp.RlpString;
import com.zhaoxi.Open_source_Android.web3.rlp.RlpType;
import com.zhaoxi.Open_source_Android.web3.tx.ChainId;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.zhaoxi.Open_source_Android.web3.crypto.Sign.signMessage;

/**
 * des:交易相关 智能合约 签名
 * Created by ztt on 2018/8/1.
 */

public class TransferUtils {
    /**
     * 签名交易
     *
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param to
     * @param value
     * @param data
     * @param chainId
     * @param privateKey
     * @return
     * @throws IOException
     */
    public static String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                         BigInteger value, String data, int chainId, String privateKey) throws IOException {
        byte[] signedMessage;
        //step2:创建交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);
        //step3:签名
        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    /**
     * 智能合约 交易
     *
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param privateKey
     * @param contractAddress
     * @param toAddress
     * @param amount
     * @return
     */
    public static String tokenTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                                          String privateKey, String contractAddress, String toAddress,
                                          BigDecimal amount) {
        BigInteger value = BigInteger.ZERO;
        String methodName = "transfer";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address tAddress = new Address(toAddress);
        Uint256 tokenValue = new Uint256(amount.toBigInteger());
        inputParameters.add(tAddress);
        inputParameters.add(tokenValue);
        TypeReference<Bool> typeReference = new TypeReference<Bool>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);

        int chainId = ChainId.NONE;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HRC-20转账
     *
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param toAddress
     * @param amount
     * @param privateKey
     * @return
     */
    public static String token20Transfer(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String contractAddress, String toAddress,
                                         BigInteger amount, String privateKey) {

        BigInteger value = BigInteger.ZERO;
        Address tAddress = new Address(toAddress);
        Uint256 transferValue = new Uint256(amount);
        Function function = transfer(tAddress, transferValue);
        String data = FunctionEncoder.encode(function);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * HRC-20转账
     *
     * @return
     */
    public static String getToken20TransferData(String toAddress, BigInteger amount) {
        Address tAddress = new Address(toAddress);
        Uint256 transferValue = new Uint256(amount);
        Function function = transfer(tAddress, transferValue);
        return FunctionEncoder.encode(function);
    }

    /**
     * HRC-721代币转移
     *
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param contractAddress
     * @param fromAddress
     * @param toAddress
     * @param tokenIdValue
     * @param privateKey
     * @return
     */
    public static String token721Transfer(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String contractAddress, String fromAddress, String toAddress,
                                          BigInteger tokenIdValue, String privateKey) {

        BigInteger value = BigInteger.ZERO;
        Address tAddress = new Address(toAddress);
        Address fAddress = new Address(fromAddress);
        Uint256 transferValue = new Uint256(tokenIdValue);
        Function function = transferFrom(fAddress, tAddress, transferValue);
        String data = FunctionEncoder.encode(function);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * HRC-721代币转移
     *
     * @param fromAddress
     * @param toAddress
     * @param tokenIdValue
     * @return
     */
    public static String getToken721TransferData(String fromAddress, String toAddress, BigInteger tokenIdValue) {

        Address tAddress = new Address(toAddress);
        Address fAddress = new Address(fromAddress);
        Uint256 transferValue = new Uint256(tokenIdValue);
        Function function = transferFrom(fAddress, tAddress, transferValue);
        return FunctionEncoder.encode(function);
    }


    /**
     * 投票 签名
     *
     * @param methodName
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param privateKey
     * @param contractAddress
     * @param toAddress
     * @param amount
     * @return
     */
    public static String tokenVote(String methodName, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                                   String privateKey, String contractAddress, String toAddress, String defultAddress,
                                   BigDecimal amount) {
        System.out.println("nonce " + nonce + ",gasPrice " + gasPrice + ",gasLimit " + gasLimit + ",amount " + amount);
        BigInteger value = BigInteger.ZERO;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address dfultAddress = new Address(defultAddress);
        Address tAddress = new Address(toAddress);
        Uint256 tokenValue = new Uint256(amount.toBigInteger());
        inputParameters.add(dfultAddress);
        inputParameters.add(tAddress);
        inputParameters.add(tokenValue);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        SystemLog.I("ztt","data:"+data);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVoteData(String methodName, String toAddress, String defultAddress, BigDecimal amount){
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address dfultAddress = new Address(defultAddress);
        Address tAddress = new Address(toAddress);
        Uint256 tokenValue = new Uint256(amount.toBigInteger());
        inputParameters.add(dfultAddress);
        inputParameters.add(tAddress);
        inputParameters.add(tokenValue);
        Function function = new Function(methodName, inputParameters, outputParameters);
        return FunctionEncoder.encode(function);
    }

    /**
     * 第三方支付
     *
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param privateKey
     * @param toAddress
     * @param amount
     * @return
     */
    public static String ThreePayMethod(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                                        String privateKey, String toAddress, BigInteger amount,
                                        String orderId, String backUrl, String desc) {
        System.out.println("nonce " + nonce + ",gasPrice " + gasPrice + ",gasLimit " + gasLimit + ",amount " + amount);
        String methodName = "generateOrderAndPay";
        BigInteger value = amount;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address tAddress = new Address(toAddress);
        Uint256 tokenValue = new Uint256(amount);
        Utf8String sorderId = new Utf8String(orderId);//orderId
        Utf8String sbackUrl = new Utf8String(backUrl);//backUrl
        Utf8String sdesc = new Utf8String(desc);//desc
        inputParameters.add(tAddress);
        inputParameters.add(tokenValue);
        inputParameters.add(sorderId);
        inputParameters.add(sbackUrl);
        inputParameters.add(sdesc);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, DAppConstants.THREE_PAY_TO_ADDRESS, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 第三方授权登录 签名
     *
     * @param massge
     * @param privateKey
     * @return
     */
    public static String LoginSign(String massge, String privateKey) {
        String hexValue = "";
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Sign.SignatureData signatureData = signMessage(massge.getBytes(), ecKeyPair);
        List<RlpType> values = new ArrayList<RlpType>();
        if (signatureData != null) {
            values.add(RlpString.create(signatureData.getV()));
            values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }
        RlpList rlpList = new RlpList(values);
        byte[] result = RlpEncoder.encode(rlpList);
        hexValue = Numeric.toHexString(result);
        return hexValue;
    }

    /**
     * 红包签名
     *
     * @param fromAddress
     * @param redBaseBean
     * @param methodName
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param privateKey
     * @param amount
     * @return
     */
    public static String redSignDataMethod(String fromAddress, RedBaseBean redBaseBean,
                                           String methodName, BigInteger nonce,
                                           BigInteger gasPrice, BigInteger gasLimit,
                                           String privateKey, BigDecimal amount, String contractAddress) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        BigInteger value = amount.toBigInteger();
        List<Uint256> _values = new ArrayList<>();
        List<Address> addressList = new ArrayList<>();
        Address tAddress = new Address(redBaseBean.getProxyAddr());
        for (int i = 0; i < redBaseBean.getValuesArr().size(); i++) {
            String vae = redBaseBean.getValuesArr().get(i);
            Uint256 tokenValue = new Uint256(new BigInteger(vae));
            addressList.add(tAddress);
            _values.add(tokenValue);
        }
        Function function = mintDedaultBatch(addressList, methodName, _values);
        String data = FunctionEncoder.encode(function);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改持币地址
     *
     * @param name
     * @param chibAddress
     * @param contAddress
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param privateKey
     * @param voteContractAddress
     * @param curWalletAddress
     * @return
     */
    public static String ChangeAddrSignDataMethod(String name, String chibAddress, String contAddress, BigInteger nonce,
                                                  BigInteger gasPrice, BigInteger gasLimit, String privateKey, String voteContractAddress, String curWalletAddress) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        BigInteger value = BigInteger.ZERO;
        Function function = null;

        if ("setApproval".equals(name)) {
            Address tAddress = new Address(chibAddress);//持币地址
            function = setApproval(name, tAddress, new Bool(true));
        } else {
            Address _coinBase = new Address(contAddress);//coinBaseBOE地址
            Address _holderAddr = new Address(curWalletAddress);//持币地址
            function = setHolderAddr(name, _coinBase, _holderAddr);
        }

        String data = FunctionEncoder.encode(function);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, voteContractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改持币地址
     *
     * @param name
     * @param chibAddress
     * @param contAddress
     * @param privateKey
     * @param curWalletAddress
     * @return
     */
    public static String getChangeAddrSignDataMethodData(String name, String chibAddress, String contAddress, String curWalletAddress) {
        Function function = null;

        if ("setApproval".equals(name)) {
            Address tAddress = new Address(chibAddress);//持币地址
            function = setApproval(name, tAddress, new Bool(true));
        } else {
            Address _coinBase = new Address(contAddress);//coinBaseBOE地址
            Address _holderAddr = new Address(curWalletAddress);//持币地址
            function = setHolderAddr(name, _coinBase, _holderAddr);
        }

        return FunctionEncoder.encode(function);
    }

    /**
     * 投票治理 投票签名
     *
     * @param issuseId
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param privateKey
     * @param voteContractAddress
     * @param amount
     * @param isSuport
     * @return
     */
    public static String VoteProposalSignDataMethod(String issuseId, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                                                    String privateKey, String voteContractAddress, BigDecimal amount, int isSuport) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        BigInteger value = BigInteger.ZERO;
        Uint256 poll = new Uint256(amount.toBigInteger());
        Function function = voteProposal("voteProposal", new Bytes32(issuseId.getBytes()), new Uint8(BigInteger.valueOf(isSuport)), poll);
        String data = FunctionEncoder.encode(function);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, voteContractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取投票治理data
     *
     * @param issuseId
     * @param amount
     * @param isSuport
     * @return
     */
    public static String getVoteProposalSignDataMethodData(String issuseId, BigDecimal amount, int isSuport) {
        Uint256 poll = new Uint256(amount.toBigInteger());
        Function function = voteProposal("voteProposal", new Bytes32(issuseId.getBytes()), new Uint8(BigInteger.valueOf(isSuport)), poll);
        return FunctionEncoder.encode(function);
    }


    /**
     * 节点分红 分红比率设置
     *
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param privateKey
     * @param voteContractAddress
     * @param bilv
     * @return
     */
    public static String DividendBonusPercentageMethod(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                                                       String privateKey, String voteContractAddress, String fromAddress,
                                                       BigDecimal bilv) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        BigInteger value = BigInteger.ZERO;
        Address _holderAddr = new Address(fromAddress);//持币地址
        Function function = setBonusPercentage(_holderAddr, new Uint8(bilv.toBigInteger()));
        String data = FunctionEncoder.encode(function);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, voteContractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String DividendBonusBiliData(String fromAddress,BigDecimal bilv){
        Address _holderAddr = new Address(fromAddress);//持币地址
        Function function = setBonusPercentage(_holderAddr, new Uint8(bilv.toBigInteger()));
        String data = FunctionEncoder.encode(function);
        return data;
    }


    /**
     * 节点分红 分红金额设置
     *
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param privateKey
     * @param voteContractAddress
     * @param fromAddress
     * @param weiValue
     * @return
     */
    public static String DividendMoneySetMethod(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                                                String privateKey, String voteContractAddress, String fromAddress,
                                                BigInteger weiValue) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        Address _holderAddr = new Address(fromAddress);//持币地址
        Function function = candidateDeposit(_holderAddr);
        String data = FunctionEncoder.encode(function);
        int chainId = ChainId.MAINNET;

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, voteContractAddress, weiValue, data, chainId, privateKey);
            if (signedData != null) {
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String DividendMoneyData(String fromAddress){
        Address _holderAddr = new Address(fromAddress);//持币地址
        Function function = candidateDeposit(_holderAddr);
        String data = FunctionEncoder.encode(function);
        return data;
    }

    public static Function mintDedaultBatch(List<Address> _tos, String _name, List<Uint256> _values) {
        final Function function = new Function(
                _name,
                Arrays.<Type>asList(new DynamicArray<Address>(_tos),
                        new Utf8String(""),
                        new Utf8String(""),
                        new DynamicArray<Uint256>(_values)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    /**
     * 修改持币地址
     *
     * @param name
     * @param to
     * @param approved
     * @return
     */
    public static Function setApproval(String name, Address to, Bool approved) {
        final Function function = new Function(
                name,
                Arrays.<Type>asList(to, approved),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    /**
     * 授权持币地址
     *
     * @param name
     * @param _coinBase
     * @param _holderAddr
     * @return
     */
    public static Function setHolderAddr(String name, Address _coinBase, Address _holderAddr) {//coinBaseBOE地址
        final Function function = new Function(
                name,
                Arrays.<Type>asList(_coinBase, _holderAddr),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    /**
     * 投票治理投票
     *
     * @param no
     * @param flag
     * @param num
     * @return
     */
    public static Function voteProposal(String name, Bytes32 no, Uint8 flag, Uint256 num) {
        final Function function = new Function(
                name,
                Arrays.<Type>asList(no, flag, num),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    /**
     * 节点分红 分红比率设置
     *
     * @param _candidateAddr
     * @param _bonusPercentage
     * @return
     */
    public static Function setBonusPercentage(Address _candidateAddr, Uint8 _bonusPercentage) {
        final Function function = new Function(
                "setBonusPercentage",
                Arrays.<Type>asList(_candidateAddr, _bonusPercentage),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    /**
     * 节点分红 分红金额
     *
     * @param _candidateAddr
     * @return
     */
    public static Function candidateDeposit(Address _candidateAddr) {
        final Function function = new Function(
                "candidateDeposit",
                Arrays.<Type>asList(_candidateAddr),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    /**
     * HRC-20转账
     *
     * @param _to
     * @param _value
     * @return
     */
    private static Function transfer(Address _to, Uint256 _value) {
        final Function function = new Function(
                "transfer"
                , Arrays.<Type>asList(_to, _value)
                , Collections.<TypeReference<?>>emptyList());

        return function;
    }


    /**
     * HRC-721 tokenId转移
     *
     * @param from
     * @param to
     * @param tokenId
     * @return
     */
    private static Function transferFrom(Address from, Address to, Uint256 tokenId) {
        final Function function = new Function(
                "transferFrom"
                , Arrays.<Type>asList(from, to, tokenId)
                , Collections.<TypeReference<?>>emptyList());

        return function;
    }

    /**
     * 字符串签名
     */
    public static String sign(Context context, String message, String privateKey) {
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        Sign.SignatureData signatureData = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            signatureData = signMessage(message.getBytes(StandardCharsets.UTF_8), ecKeyPair, true);
        } else return context.getResources().getString(R.string.view_digital_sign_qr_code_txt_03);
        List<RlpType> values = new ArrayList<>();
        values.add(RlpString.create(message));
        byte[] v = new byte[]{(byte) signatureData.getV()};
        values.add(RlpString.create(Bytes.trimLeadingZeroes(v)));
        values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
        values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        RlpList rlpList = new RlpList(values);
        String signMessage = Numeric.toHexString(RlpEncoder.encode(rlpList));
        return signMessage;
    }
}

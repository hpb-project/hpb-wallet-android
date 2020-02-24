package com.zhaoxi.Open_source_Android.web3.crypto;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;
import com.zhaoxi.Open_source_Android.web3.protocol.ObjectMapperFactory;
import com.zhaoxi.Open_source_Android.web3.utils.AESUtil;
import com.zhaoxi.Open_source_Android.web3.utils.Numeric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.zhaoxi.Open_source_Android.web3.crypto.Hash.sha256;
import static com.zhaoxi.Open_source_Android.web3.crypto.Keys.PRIVATE_KEY_LENGTH_IN_HEX;

/**
 * Cipher exception wrapper.
 * @author Aimee on 2018/5/29
 */

public class Bip39WalletUtil {
    private Context mContext;
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();
    private static List<String> WORD_LIST = null;

    public Bip39WalletUtil(Context context) {
        mContext = context;
        WORD_LIST = populateWordList(mContext);
    }

    /**
     * 生成带助记词的钱包方法
     *
     * @param walletName
     * @param password
     * @return
     * @throws CipherException
     * @throws IOException
     */
    public static WalletBean generateBip39Wallet(String walletName, String password,String prompt) throws CipherException, IOException {
        WalletBean walletBean = new WalletBean();
        //step1:生成助记词
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = generateMnemonic(initialEntropy);
        //step2:生成私钥
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        //step3:生成WalletFile
        Credentials credentials = Credentials.create(privateKey);
        WalletFile walletFile = Wallet.createLight(password, credentials.getEcKeyPair());

        //step4:生成keyStore json
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(walletFile);
        //step5:插入数据
        walletBean.setAddress(walletFile.getAddress());
        //step6:将助记词加密
        walletBean.setMnemonic("".equals(mnemonic)?"": AESUtil.encrypt(password,mnemonic));
        walletBean.setKeyStore(jsonStr);
        //setp7:钱包名称
        walletBean.setWalletBName(walletName.equals("") ? "新钱包" : walletName);
        //step8:密码提示信息
        walletBean.setPrompt(prompt);
        //step9:创建钱包是类型为0
        walletBean.setWalletType("0");

        return walletBean;
    }

    /**
     * 生成助记词
     *
     * @param initialEntropy
     * @return
     */
    public static String generateMnemonic(byte[] initialEntropy) {
        validateInitialEntropy(initialEntropy);

        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;

        byte checksum = calculateChecksum(initialEntropy);
        boolean[] bits = convertToBits(initialEntropy, checksum);

        int iterations = (ent + checksumLength) / 11;
        StringBuilder mnemonicBuilder = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            int index = toInt(nextElevenBits(bits, i));
            mnemonicBuilder.append(WORD_LIST.get(index));

            boolean notLastIteration = i < iterations - 1;
            if (notLastIteration) {
                mnemonicBuilder.append(" ");
            }
        }

        return mnemonicBuilder.toString();
    }

    public static Credentials loadBip39Credentials(String password, String mnemonic) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        return Credentials.create(ECKeyPair.create(sha256(seed)));
    }

    private static List<String> populateWordList(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.en_mnemonic_word_list);
        try {
            return readAllLines(inputStream);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static List<String> readAllLines(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> data = new ArrayList<>();
        for (String line; (line = br.readLine()) != null; ) {
            data.add(line);
        }
        return data;
    }

    private static void validateInitialEntropy(byte[] initialEntropy) {
        if (initialEntropy == null) {
            throw new IllegalArgumentException("Initial entropy is required");
        }

        int ent = initialEntropy.length * 8;
        if (ent < 128 || ent > 256 || ent % 32 != 0) {
            throw new IllegalArgumentException("The allowed size of ENT is 128-256 bits of "
                    + "multiples of 32");
        }
    }

    private static byte calculateChecksum(byte[] initialEntropy) {
        int ent = initialEntropy.length * 8;
        byte mask = (byte) (0xff << 8 - ent / 32);
        byte[] bytes = sha256(initialEntropy);

        return (byte) (bytes[0] & mask);
    }

    private static boolean[] convertToBits(byte[] initialEntropy, byte checksum) {
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        int totalLength = ent + checksumLength;
        boolean[] bits = new boolean[totalLength];

        for (int i = 0; i < initialEntropy.length; i++) {
            for (int j = 0; j < 8; j++) {
                byte b = initialEntropy[i];
                bits[8 * i + j] = toBit(b, j);
            }
        }

        for (int i = 0; i < checksumLength; i++) {
            bits[ent + i] = toBit(checksum, i);
        }

        return bits;
    }

    private static boolean toBit(byte value, int index) {
        return ((value >>> (7 - index)) & 1) > 0;
    }

    private static int toInt(boolean[] bits) {
        int value = 0;
        for (int i = 0; i < bits.length; i++) {
            boolean isSet = bits[i];
            if (isSet) {
                value += 1 << bits.length - i - 1;
            }
        }

        return value;
    }

    private static boolean[] nextElevenBits(boolean[] bits, int i) {
        int from = i * 11;
        int to = from + 11;
        return Arrays.copyOfRange(bits, from, to);
    }

    /**
     * 验证助记词是否正确
     * @param mnemonic
     * @return
     */
    public static boolean isMnemonic(String mnemonic) {
        if(StrUtil.isEmpty(mnemonic)){
            return false;
        }
        boolean isOk = true;
        String[] strs = mnemonic.split(" ");
        if (strs.length >= 12 && strs.length <= 24 && strs.length % 4 == 0) {
            for (int i = 0; i < strs.length; i++) {
                if (!WORD_LIST.contains(strs[i])) {
                    return false;
                }
            }
        } else isOk = false;
        return isOk;
    }

    /**
     * 验证私钥是否正确
     * @param privateKey
     * @return
     */
    public static boolean isValidPrivateKey(String privateKey) {
        String cleanPrivateKey = Numeric.cleanHexPrefix(privateKey);
        return cleanPrivateKey.length() == PRIVATE_KEY_LENGTH_IN_HEX;
    }
}

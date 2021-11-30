package com.tester.testercv.block;

import com.tester.testercommon.util.endecrypt.SHA1Encrypt;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class MyBlock implements Serializable {


    public MyBlock(String data, String preHash) {
        this.data = data;
        this.preHash = preHash;
        this.timestamp = new Date().getTime();
        init();
    }

    private String hash;
    private String preHash;
    private String data;
    private long timestamp;
    private int nonce;

    private void init(){
        this.hash = calculateHash();
        proofOfWork();

    }

    public boolean verify(){
        String s = calculateHash();
        return s.equals(hash);
    }


    private String calculateHash() {
        String str = this.preHash + this.timestamp + this.nonce + this.data;
        String res = SHA1Encrypt.getSha1(str);
        return res;
    }

    private void proofOfWork(){
        int complexity = getComplexity();
        String target = new String(new char[complexity]).replace('\0', '0');
        while(!this.hash.substring(0, complexity).equals(target)){
            ++nonce;
            hash = calculateHash();
        }
    }

    private int getComplexity(){
        return BlockHelper.COMPLEXITY;
    }

}

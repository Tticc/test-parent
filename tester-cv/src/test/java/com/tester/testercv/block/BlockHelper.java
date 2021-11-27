package com.tester.testercv.block;

import com.tester.testercommon.util.RandomUtil;

import java.util.LinkedList;
import java.util.List;

public class BlockHelper {


    public static final int COMPLEXITY = 4;

    private static final List<MyBlock> blockChain = new LinkedList<>();
    private static MyBlock tail = null;

    public static void main(String[] args) throws Exception {
        MyBlock myBlock = new MyBlock("data","0");
        blockChain.add(myBlock);
        tail = myBlock;
        for (int i = 0; i < 5; i++) {
            mine();
        }

        for (MyBlock block : blockChain) {
            System.out.println(block);
            System.out.println();
        }

    }

    private static void mine() throws Exception{
        boolean b = verifyChain();
        if(!b){
            throw new Exception("error chain");
        }
        MyBlock myBlock = new MyBlock(RandomUtil.getRandomString(10),tail.getHash());
        blockChain.add(myBlock);
        tail = myBlock;
    }

    private static boolean verifyChain(){
        for (MyBlock block : blockChain) {
            boolean verify = block.verify();
            if(!verify){
                return false;
            }
        }
        return true;
    }
}

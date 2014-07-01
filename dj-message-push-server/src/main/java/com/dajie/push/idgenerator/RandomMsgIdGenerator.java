package com.dajie.push.idgenerator;

import java.util.Random;

/**
 * Created by wills on 3/13/14.
 */
public class RandomMsgIdGenerator implements IMsgIdGenerator {

    @Override
    public short getMsgId() {
        return (short)(new Random().nextInt(Short.MAX_VALUE));
    }
}

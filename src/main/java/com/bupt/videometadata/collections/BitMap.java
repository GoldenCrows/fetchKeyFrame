package com.bupt.videometadata.collections;

import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.util.BitSet;

/**
 * @author Che Jin <jotline@github>
 */
//使用java自己的BitSet来记录特征在每一帧的是否有取值，这样的话可以o(1)复杂度的知道是否有取值。
// 只需要去获取到该帧在什么地方即可
@Data
public class BitMap implements Serializable{

    protected void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeLong(starttime);
        s.writeObject(bitSet);
    }

    @SuppressWarnings("unchecked")
    protected void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        reinitialize();
        starttime = s.readLong();
        bitSet=(BitSet)s.readObject();
    }

    private void reinitialize() {
        starttime = 0l;
        bitSet=null;
    }
    //第一个问题，BitSet起始从哪里开始，我们知道一秒钟30帧左右，而我们的BitSet从哪一秒开始记录合适呢？
    //因为矩阵是只会append，并不会对之前的数据进行修改，当我们淘汰掉数据的时候，把BitSet中这部分数据进行置零
    //同时截断bitset。更新BitSet的开始时间。

    public BitMap(long starttime) {
        this.starttime = starttime;
        bitSet = new BitSet(31536000 / 2); //初始化为半年的数据
    }

    //把前面的零全部去掉，更新开始时间。为了节省空间啦。bitset可以自己扩容。
    public int reOrder() {
        int index = bitSet.nextSetBit(0);
        starttime += index;
        bitSet = bitSet.get(index, bitSet.size() - 1);
        return index;
    }

    //这个函数用来传入一个范围，start和end来寻找第一次被置为1的时间，缩小查询范围
    public long findStart(long start,long end){
        if(start<starttime||end<starttime)return -1;
        int index=bitSet.nextSetBit((int)(start-starttime));
        if(index>end-starttime) return -2;
        return index+starttime;
    }

    public void set(Long  time){
        bitSet.set((int)(time-starttime));
    }

    //开始时间
    //60*60*24*365  *30=9.4亿帧 一年有9.4亿帧，数量太大了，不按照帧来存储，按照秒来存储
    //一年有31536000 3000W秒
    private Long starttime;

    private BitSet bitSet;

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("starttime="+starttime);
        result.append("bitset="+bitSet.toString());
        return result.toString();
    }

}

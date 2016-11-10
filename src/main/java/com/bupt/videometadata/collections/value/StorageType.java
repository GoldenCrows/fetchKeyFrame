package com.bupt.videometadata.collections.value;

/**
 * @author Che Jin <jotline@github>
 */
public enum StorageType {
    //因为有可能元数据并没有存储而是已经删除了，但是可能由视频恢复。
    PATH,
    GENERATE;
}

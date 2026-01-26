package com.yy.yao.service.impl.storage;

import java.io.InputStream;
import java.util.Map;

/**
 * 文件存储服务接口
 */
public interface StorageService {

    /**
     * 存储文件
     *
     * @param path 文件路径 (相对路径)
     * @param inputStream 文件输入流
     * @param contentType 内容类型 (如: image/png)
     * @param metadata 元数据
     * @return 文件访问 URL
     */
    String store(String path, InputStream inputStream, String contentType, Map<String, String> metadata) throws Exception;

    /**
     * 存储文件 (字节数组)
     *
     * @param path 文件路径
     * @param data 文件数据
     * @param contentType 内容类型
     * @return 文件访问 URL
     */
    String store(String path, byte[] data, String contentType) throws Exception;

    /**
     * 获取文件访问 URL
     *
     * @param path 文件路径
     * @return 访问 URL
     */
    String getUrl(String path);

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @return 是否删除成功
     */
    boolean delete(String path);

    /**
     * 检查文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    boolean exists(String path);

    /**
     * 获取文件大小 (字节)
     *
     * @param path 文件路径
     * @return 文件大小, -1 表示不存在
     */
    long getSize(String path);
}

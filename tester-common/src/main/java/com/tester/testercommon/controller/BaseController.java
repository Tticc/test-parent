package com.tester.testercommon.controller;

import com.alibaba.fastjson.JSON;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author 温昌营
 * @Date
 */
public class BaseController {
    public BaseController() {
    }

    public <T> RestResult<T> success() {
        return RestResult.success();
    }
    public <T> Mono<RestResult<T>> monoSuccess() {
        return monoSuccess(Mono.empty());
    }

    public <T> RestResult<T> success(T data) {
        return RestResult.success(data);
    }

    public <T> RestResult<T> success(String message, T data) {
        return RestResult.success(message,data);
    }

    public <T> RestResult<T> fail(long code, String message) {
        return RestResult.fail(code, message);
    }


    public <T> Mono<RestResult<T>> monoSuccess(Mono<T> data) {
        return monoSuccess("执行成功！", data);
    }
    public <T> Mono<RestResult<T>> monoSuccess(String message, Mono<T> data) {
        return data.map(e -> Optional.of(e))
                .defaultIfEmpty(Optional.empty())
                .handle((e, sink) -> {
                            if (!e.isPresent()) {
                                sink.next(this.success(message, null));
                                return;
                            }
                            sink.next(this.success(message, e.get()));
                        }
                );
    }
    public <T> Mono<RestResult<T>> monoFail(long code, String message) {
        return Mono.just((new RestResult()).code(code).message(message).putTimestamp().data((Object)null));
    }



    protected <T> T getMockData(String filePath, Class<T> clazz) throws BusinessException {
        return JSON.parseObject(this.getFileContent(filePath), clazz);
    }

    protected <T> List<T> getMockListData(String filePath, Class<T> clazz) throws BusinessException {
        return JSON.parseArray(this.getFileContent(filePath), clazz);
    }

    private String getFileContent(String filePath) throws BusinessException {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:mock-data/" + filePath);
            if (resources.length <= 0) {
                return null;
            } else {
                Resource[] var8 = resources;
                int var9 = resources.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    Resource r = var8[var10];
                    String fileName = r.getFilename();
                    InputStream is = r.getInputStream();
                    List<Byte> byteList = new ArrayList();
                    byte[] readByte = new byte[is.available()];

                    byte[] fileBytes;
                    int j;
                    while(is.read(readByte) > 0) {
                        fileBytes = readByte;
                        j = readByte.length;

                        for(int var14 = 0; var14 < j; ++var14) {
                            byte b = fileBytes[var14];
                            byteList.add(b);
                        }
                    }

                    if (!CollectionUtils.isEmpty(byteList)) {
                        fileBytes = new byte[byteList.size()];

                        for(j = 0; j < byteList.size(); ++j) {
                            fileBytes[j] = (Byte)byteList.get(j);
                        }

                        String jsonStr = (new String(fileBytes)).trim();
                        return jsonStr;
                    }

                }

                return null;
            }
        } catch (Exception var16) {
            throw new BusinessException(5000L);
        }
    }
}

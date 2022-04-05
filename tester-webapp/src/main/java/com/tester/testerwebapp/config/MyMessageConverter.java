package com.tester.testerwebapp.config;

import com.tester.base.dto.model.request.convert.ConvertRequest;
import com.tester.base.dto.model.request.convert.TestConvertRequest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 自定义返回数据消息转换器
 * 2021-4-4 16:46:51
 */
public class MyMessageConverter implements HttpMessageConverter<ConvertRequest> {

    // read = false, request不可用
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }
    // write ， response可用
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return clazz.isAssignableFrom(ConvertRequest.class);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return MediaType.parseMediaTypes("application/x-my-restype");
    }

    @Override
    public ConvertRequest read(Class<? extends ConvertRequest> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(ConvertRequest convertRequest, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        TestConvertRequest data = convertRequest.getTestConvertRequest();
        String resData = "EMPTY";
        if(null != data){
            resData = "name is:"+data.getName()+", age is:"+data.getAge();
        }
        OutputStream outputStream = outputMessage.getBody();
        outputStream.write(resData.getBytes());
    }
}

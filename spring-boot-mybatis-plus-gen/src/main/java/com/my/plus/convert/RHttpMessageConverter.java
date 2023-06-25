package com.my.plus.convert;

import cn.hutool.json.JSONUtil;
import com.my.plus.gen.entity.R;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class RHttpMessageConverter extends AbstractHttpMessageConverter<R> {

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        ArrayList<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.ALL);
        return mediaTypes;
    }

    @Override
    protected boolean supports(Class clazz) {
        return R.class == clazz;
    }

    @Override
    protected R readInternal(Class<? extends R> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(R r, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        StreamUtils.copy(JSONUtil.toJsonStr(r)
                , Charset.defaultCharset(), outputMessage.getBody());
    }

    @InitBinder
    public void binder(){

    }
}
package com.dajie.message.service.impl;

import com.dajie.infra.tinyurl.service.TinyUrlService;
import com.dajie.message.service.IShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wills on 6/11/14.
 */
@Component("shortUrlService")
public class ShortUrlServiceImpl implements IShortUrlService {

    @Autowired
    private TinyUrlService tinyUrlService;

    private final static String SHORTURL="http://dajie.me/";

    @Override
    public String genShortUrl(String sourceUrl) {
        return SHORTURL+tinyUrlService.shortenUrl(sourceUrl);
    }
}

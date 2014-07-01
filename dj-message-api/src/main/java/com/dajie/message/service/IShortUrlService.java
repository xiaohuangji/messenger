package com.dajie.message.service;

import com.dajie.message.annotation.rest.RestBean;

/**
 * Created by wills on 6/11/14.
 */
@RestBean
public interface IShortUrlService {

    public String genShortUrl(String sourceUrl);
}

/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.repository.AdvertRepository;
import com.shiroyk.shopsystem.entity.Advert;
import org.springframework.stereotype.Service;

@Service
public class AdvertService {

    private final AdvertRepository advertRepository;

    public AdvertService(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    public void save(Advert advert) {
        advertRepository.save(advert);
    }

    public Advert getAdvert() {
        return advertRepository.getOne(1L);
    }
}

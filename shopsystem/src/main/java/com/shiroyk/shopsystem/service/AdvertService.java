/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Advert;

public interface AdvertService {

    void save(Advert advert);

    Advert getAdvert();
}

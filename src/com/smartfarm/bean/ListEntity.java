package com.smartfarm.bean;

import java.io.Serializable;
import java.util.List;

import com.smartfarm.net.bean.Entity;

public interface ListEntity<T extends Entity> extends Serializable {

    public List<T> getList();
}

package com.fangye.erouterapi.template;

import com.fangye.annotation.model.RouterEntity;

import java.util.Map;

public interface IRouterPath {

    Map<String, RouterEntity> getPathMap();
}

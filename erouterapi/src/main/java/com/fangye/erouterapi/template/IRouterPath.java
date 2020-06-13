package com.fangye.erouterapi.template;

import com.fangye.annotation.model.RouterEntity;

import java.util.Map;

/**
 * Template of path ,标准
 */
public interface IRouterPath {

    Map<String, RouterEntity> getPathMap();
}

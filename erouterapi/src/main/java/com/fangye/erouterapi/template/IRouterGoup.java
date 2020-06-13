package com.fangye.erouterapi.template;

import java.util.Map;

/**
 * Template of group ,标准
 */
public interface IRouterGoup {

    Map<String, Class<? extends IRouterPath>> getGroupMap();
}

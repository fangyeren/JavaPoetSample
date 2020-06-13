package com.fangye.erouterapi.template;

import java.util.Map;

public interface IRouterGoup {

    Map<String, Class<? extends IRouterPath>> getGroupMap();
}

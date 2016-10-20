package com.wf.irulu.logic.service.impl;

import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.manager.ServiceManager;
import com.wf.irulu.logic.service.BaseService;

/**
 * Created by XImoon on 15/10/19.
 */
public class SettingServiceImpl extends BaseService {

    private static final String TAG = SettingServiceImpl.class.getCanonicalName();

    private IruluController controller;
    private ServiceManager serviceManager;

    public SettingServiceImpl(IruluController controller, ServiceManager serviceManager) {
        this.controller = controller;
        this.serviceManager = serviceManager;
    }
}

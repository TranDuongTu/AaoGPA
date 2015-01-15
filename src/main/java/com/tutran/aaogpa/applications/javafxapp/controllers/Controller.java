package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import com.tutran.aaogpa.data.SupportData;
import com.tutran.aaogpa.services.LocalDataRepository;
import com.tutran.aaogpa.services.WebDataRepository;

/**
 * Controller handle each scene's events in javaFx
 */
public abstract class Controller {
    private ControllerActionListener controllerActionListener;

    protected SupportData supportData;

    protected LocalDataRepository localDataRepository;
    protected WebDataRepository webDataRepository;

    public abstract void startScene();

    public void setSupportData(SupportData supportData) {
        this.supportData = supportData;
    }

    public void setLocalDataRepository(LocalDataRepository localDataRepository) {
        this.localDataRepository = localDataRepository;
    }

    public void setWebDataRepository(WebDataRepository webDataRepository) {
        this.webDataRepository = webDataRepository;
    }

    public void setControllerActionListener(ControllerActionListener controllerActionListener) {
        this.controllerActionListener = controllerActionListener;
    }


    protected void close(SceneID nextScene) {
        controllerActionListener.onFinished(nextScene);
    }

    /**
     * For communicating with parent Stage
     */
    public interface ControllerActionListener {
        public void onFinished(SceneID nextSceneToOpen);
    }
}

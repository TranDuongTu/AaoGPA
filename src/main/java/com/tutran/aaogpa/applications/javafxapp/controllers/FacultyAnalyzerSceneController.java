package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FacultyAnalyzerSceneController extends Controller {
    @FXML
    private Button closeBtn;

    @Override
    public void startScene() {

    }

    @FXML
    protected void handleCloseBtnAction(ActionEvent e) {
        close(SceneID.OverView);
    }
}

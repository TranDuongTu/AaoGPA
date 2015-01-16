package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class IndividualMonitorSceneController extends Controller {
    @FXML
    private Button searchBtn, backBtn;

    @FXML
    private TextField stuIdTextField;

    @FXML
    private ListView<String> coursesResultList;

    @FXML
    private BarChart<String, Number> performanceChart;

    @Override
    public void startScene() {

    }

    @FXML
    protected void handleBackBtnAction(ActionEvent e) {
        close(SceneID.OverView);
    }

    @FXML
    protected void handleSearchBtnAction(ActionEvent e) {

    }
}

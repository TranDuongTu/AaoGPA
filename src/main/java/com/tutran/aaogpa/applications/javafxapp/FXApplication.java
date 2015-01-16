package com.tutran.aaogpa.applications.javafxapp;

import com.tutran.aaogpa.data.SupportData;
import com.tutran.aaogpa.applications.javafxapp.controllers.Controller;
import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import com.tutran.aaogpa.applications.javafxapp.scenes.SceneLayout;
import com.tutran.aaogpa.services.LocalDataRepository;
import com.tutran.aaogpa.services.WebDataRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class FXApplication extends Application
        implements com.tutran.aaogpa.applications.Application {
    private SupportData supportData;

    private LocalDataRepository localDataRepository;
    private WebDataRepository webDataRepository;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring_context/application_context.xml"
        );
        localDataRepository = context.getBean(
                "localDataRepository", LocalDataRepository.class);
        webDataRepository = context.getBean(
                "webDataRepository", WebDataRepository.class);
        supportData = context.getBean(
                "dataScope", SupportData.class);

        Map<SceneID, Scene> scenes = createScenes(primaryStage);

        primaryStage.setTitle("AAO GPA Analyer");
        primaryStage.setScene(scenes.get(SceneID.OverView));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // ========================================================================
    // PRIVATE HELPERS
    // ========================================================================

    private Map<SceneID, Scene> createScenes(final Stage primaryStage)
            throws Exception {
        Map<SceneID, FXMLLoader> loaders = createLoaders();
        Map<SceneID, String> locations = getFXMLLocations();
        final Map<SceneID, Scene> scenes = new HashMap<SceneID, Scene>();

        // Action listener for scenes
        Controller.ControllerActionListener listener =
                new Controller.ControllerActionListener() {
                    @Override
                    public void onFinished(SceneID nextSceneToOpen) {
                        primaryStage.setScene(scenes.get(nextSceneToOpen));
                    }
                };

        for (SceneID sceneID : locations.keySet()) {
            FXMLLoader loader = loaders.get(sceneID);
            Parent root = loadRootFromFXML(loader, locations.get(sceneID));
            scenes.put(sceneID, new Scene(root));

            Controller controller = loader.getController();
            if (controller != null) {
                controller.setSupportData(supportData);
                controller.setLocalDataRepository(localDataRepository);
                controller.setWebDataRepository(webDataRepository);
                controller.setControllerActionListener(listener);
                controller.startScene();
            }
        }

        return scenes;
    }

    private Map<SceneID, FXMLLoader> createLoaders() {
        Map<SceneID, FXMLLoader> result = new HashMap<SceneID, FXMLLoader>();
        for (SceneID id : SceneID.values())
            result.put(id, new FXMLLoader());
        return result;
    }

    private Map<SceneID, String> getFXMLLocations() {
        Map<SceneID, String> result = new HashMap<SceneID, String>();
        result.put(SceneID.OverView, SceneLayout.Overview.getDesc());
        result.put(SceneID.IndividualMonitor, SceneLayout.IndividualMonitor.getDesc());
        result.put(SceneID.CourseAnalyzer, SceneLayout.CourseAnalyzer.getDesc());
        result.put(SceneID.FacultyAnalyzer, SceneLayout.FacultyAnalyzer.getDesc());
        return result;
    }

    private Parent loadRootFromFXML(FXMLLoader loader, String location)
            throws Exception {
        File layout = new File(location);
        return (Parent) loader.load(new FileInputStream(layout));
    }

    // ========================================================================
    // PROGRAM ENTRY POINT
    // ========================================================================

    public static void main(String[] args) {
        launch(args);
    }
}

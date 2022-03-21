package com.cliftonbartholomew.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import javafx.stage.StageStyle;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) {
        System.out.println("Starting");
        scene = new Scene(loadView("main-view"));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadView(String path)  {
        var res = Application.class.getResource("fxml/"+path+".fxml");
        System.out.println(res.toString());
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("fxml/"+path+".fxml"));
        try {
            return (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setRoot(String fxml){
        scene.setRoot(loadView(fxml));
        scene.getWindow().sizeToScene();
        scene.getWindow().centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }


}

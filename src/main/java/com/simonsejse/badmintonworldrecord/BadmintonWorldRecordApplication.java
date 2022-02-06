package com.simonsejse.badmintonworldrecord;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BadmintonWorldRecordApplication extends Application {

    private ConfigurableApplicationContext springContext;

    private Parent rootNode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(BadmintonWorldRecordApplication.class);
        final FXMLLoader fxmlLoader = new FXMLLoader(BadmintonWorldRecordApplication.class.getResource("/main.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        primaryStage.setTitle("Badminton-Verdensrekord");
        primaryStage.setScene(new Scene(rootNode, 600, 405));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    @Bean
    public ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}

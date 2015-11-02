/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nseticker;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.labs.scene.control.gauge.Content;
import jfxtras.labs.scene.control.gauge.ContentBuilder;
import jfxtras.labs.scene.control.gauge.MatrixPanel;

/**
 *
 * @author F-effect
 */
public class NseStockTicker extends Application {
    
    final String DEFAULT_TEXT="...........";
    
    StackPane   root         = new StackPane();  
    
    MatrixPanel matrix_panel = new MatrixPanel();
    
    Button      close_btn    = new Button("X");
    
    Content     gainers_cont;
    Content     loosers_cont;
    Content     nochange_cont;
    
    ScheduledExecutorService scheduledExeService;
    
    @Override
public void start(Stage primaryStage) {
 
        mainUI();
        
        exit();
        
        getScheduledService();
      
        Scene scene = new Scene(root,700,350,Color.BLACK);
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("StockTicker.Fx");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
private void mainUI(){
    
    matrix_panel.setLedWidth(140);
    matrix_panel.setLedHeight(40);
    matrix_panel.setPrefSize(760,760);

    close_btn.setFont(Font.font("castellar", FontWeight.BOLD, 16));
            
    gainers_cont=addGainerContent();
    loosers_cont=addLooserContent();
    nochange_cont=addNoChangeContent();

    matrix_panel.setContents(Arrays.asList(gainers_cont,loosers_cont,nochange_cont));

    StackPane.setAlignment(close_btn, Pos.TOP_LEFT);
    StackPane.setMargin(matrix_panel,new Insets(50,5,5,5));
        
    root.getChildren().addAll(close_btn,matrix_panel);
}    
private void exit(){
    
      close_btn.setOnAction((ActionEvent event) -> {
          
           if(!scheduledExeService.isShutdown()){
               scheduledExeService.shutdown();
            }
             System.exit(0);
        });
   
}    
private ScheduledFuture getScheduledService(){
     scheduledExeService =Executors.newSingleThreadScheduledExecutor();  
     return scheduledExeService.scheduleAtFixedRate(getData(), 10, 300, TimeUnit.SECONDS);
}    
private Content addGainerContent(){
        
        return new ContentBuilder()
                
        .color(Content.MatrixColor.GREEN)
        .type(Content.Type.TEXT)
        .origin(new Point2D(0,5))
        .txtContent(DEFAULT_TEXT)
        .fontGap(Content.Gap.NULL)
        .align(Content.Align.RIGHT)
        .lapse(50)
        .effect(Content.Effect.SCROLL_LEFT)
        .postEffect(Content.PostEffect.REPEAT)
        .pause(300)
        .clear(Boolean.TRUE)
        .build(); 
    }
private Content addLooserContent(){
        return new ContentBuilder()
        .color(Content.MatrixColor.RED)
        .type(Content.Type.TEXT)
        .origin(new Point2D(0,15))
        .txtContent(DEFAULT_TEXT)
        .fontGap(Content.Gap.NULL)
        .align(Content.Align.RIGHT)
        .lapse(50)
        .effect(Content.Effect.SCROLL_LEFT)
        .postEffect(Content.PostEffect.REPEAT)
        .pause(100)
        .clear(Boolean.TRUE)
        .build();
}
private Content addNoChangeContent(){
        return new ContentBuilder()
        .color(Content.MatrixColor.YELLOW)
        .type(Content.Type.TEXT)
        .origin(new Point2D(0,25))
        .txtContent(DEFAULT_TEXT)
        .fontGap(Content.Gap.NULL)
        .align(Content.Align.RIGHT)
        .lapse(50)
        .effect(Content.Effect.SCROLL_LEFT)
        .postEffect(Content.PostEffect.REPEAT)
        .pause(80)
        .clear(Boolean.TRUE)
        .build();       
}
private void setContentNoData(){
    
     matrix_panel.getContents().clear();
     
     matrix_panel.setContents(new Content[]{
      new ContentBuilder()
        .color(Content.MatrixColor.BLUE)
        .type(Content.Type.TEXT)
        .origin(new Point2D(20,25))
        .txtContent("!!!NO DATA!!!!")
        .fontGap(Content.Gap.NULL)
        .align(Content.Align.CENTER)
        .effect(Content.Effect.BLINK)
        .postEffect(Content.PostEffect.REPEAT)
        .clear(Boolean.TRUE)
        .build()});     

}
private Runnable getData(){

Runnable run=() -> {
    try {
        NseWebParser company=new NseWebParser();
          
        if(matrix_panel.getContents().size()==1){
            matrix_panel.getContents().clear();
            matrix_panel.setContents(Arrays.asList(gainers_cont,loosers_cont,nochange_cont));  
        }
            Platform.runLater(() -> {
                gainers_cont.setTxtContent(company.getGainers());
                loosers_cont.setTxtContent(company.getLoosers());
                nochange_cont.setTxtContent(company.getNoChange());
        });
          
  
    } catch (IOException ex) {
        
          setContentNoData();
           
    }

     };
return run;
}
}
 
  


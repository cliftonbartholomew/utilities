package com.cliftonbartholomew.utilities;

import com.cliftonbartholomew.utilities.TASS.CSVGenerator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public class MainController {


    //#0
    @FXML
    private MFXButton csvGeneratorMenuButton;

    //#1
    @FXML
    private MFXButton searchMenuButton;

    //#2
    @FXML
    private MFXButton settingsMenuButton;

    //#3
    @FXML
    private MFXButton exitMenuButton;

    @FXML
    private TextField fileFolderTextField;

    @FXML
    private MFXButton generateButton;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private MFXProgressBar progressBar;


    @FXML
    private ComboBox<Integer> termComboBox;

    private File selectedFile;
    private boolean [] selectedButton = {true, false, false, false};
    private MFXButton [] menuButtons;

    public MainController(){

    }

    @FXML
    public void initialize(){
        termComboBox.getItems().addAll(1,2,3,4);
        menuButtons = new MFXButton[]{csvGeneratorMenuButton, searchMenuButton, settingsMenuButton, exitMenuButton};
    }

    @FXML
    void chooseFolderButtonClick(MouseEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedFile = directoryChooser.showDialog(termComboBox.getScene().getWindow());

        fileFolderTextField.setText(selectedFile.getName());
    }

    @FXML
    void csvGeneratorButtonClicked(MouseEvent event) {
        setMenuClickedColor((MFXButton)(event.getSource()));
    }

    @FXML
    void csvGeneratorButtonEntered(MouseEvent event) {
        setMenuEnteredColor((MFXButton)(event.getSource()));
    }

    @FXML
    void csvGeneratorButtonExited(MouseEvent event) {
        setMenuExitColor((MFXButton)(event.getSource()));
    }

    @FXML
    void exitButtonClicked(MouseEvent event) {
        setMenuClickedColor((MFXButton)(event.getSource()));
        System.exit(0);
    }

    @FXML
    void exitButtonEntered(MouseEvent event) {
        setMenuEnteredColor((MFXButton)(event.getSource()));
    }

    @FXML
    void exitButtonExited(MouseEvent event) {
        setMenuExitColor((MFXButton)(event.getSource()));
    }

    @FXML
    void generateButtonClicked(MouseEvent event) throws IOException, InvalidFormatException {
        new Thread(new CSVGenerator(selectedFile, termComboBox.getValue(), outputTextArea, progressBar)).start();
    }

    @FXML
    void searchButtonClicked(MouseEvent event) {
        setMenuClickedColor((MFXButton)(event.getSource()));
    }

    @FXML
    void searchButtonEntered(MouseEvent event) {
        setMenuEnteredColor((MFXButton)(event.getSource()));
    }

    @FXML
    void searchButtonExited(MouseEvent event) {
        setMenuExitColor((MFXButton)(event.getSource()));
    }

    @FXML
    void settingsButtonClicked(MouseEvent event) {
        setMenuClickedColor((MFXButton)(event.getSource()));
    }

    @FXML
    void settingsButtonEntered(MouseEvent event) {
        setMenuEnteredColor((MFXButton)(event.getSource()));
    }

    @FXML
    void settingsButtonExited(MouseEvent event) {
        setMenuExitColor((MFXButton)(event.getSource()));
    }

    @FXML
    void generateButtonExited(MouseEvent event) {
        setButtonExitedColor((MFXButton)(event.getSource()));
    }

    @FXML
    void generateButtonEntered(MouseEvent event) {
        setButtonEnteredColor((MFXButton)(event.getSource()));
    }

    @FXML
    private void setButtonEnteredColor(MFXButton p){
        p.setStyle("-fx-background-color:  #9a67ea;");
    }

    @FXML
    private void setButtonExitedColor(MFXButton p){
        p.setStyle("-fx-background-color:  #673ab7;");
    }

    @FXML
    private void setMenuEnteredColor(MFXButton p){
        //light primary
        p.setStyle("-fx-background-color: #9a67ea;");
    }

    @FXML
    private void setMenuExitColor(MFXButton p){
        for (int i = 0; i < menuButtons.length; i++) {
            if(menuButtons[i] == p && !selectedButton[i]){
                //primary grey
                p.setStyle("-fx-background-color:  #1b1b1b;");
                break;
            }
            else if (menuButtons[i] == p && selectedButton[i]){
                //primary purple
                p.setStyle("-fx-background-color:  #673ab7;");
                break;
            }
        }
    }

    @FXML
    private void setMenuClickedColor(MFXButton p){
        //primary purple
        p.setStyle("-fx-background-color:  #673ab7;");
        for (int i = 0; i < menuButtons.length; i++) {
            if(menuButtons[i] == p){
                selectedButton[i] = true;
            }
            else{
                selectedButton[i] = false;
                menuButtons[i].setStyle("-fx-background-color:  #1b1b1b;");
            }
        }
    }

}

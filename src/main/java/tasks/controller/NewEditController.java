package tasks.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TaskIO;
import tasks.services.TasksService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class NewEditController {

    private static Button clickedButton;

    private static final Logger log = Logger.getLogger(NewEditController.class.getName());

    public static void setClickedButton(Button clickedButton) {
        NewEditController.clickedButton = clickedButton;
    }

    public static void setCurrentStage(Stage currentStage) {
        NewEditController.currentStage = currentStage;
    }

    private static Stage currentStage;

    private Task currentTask;
    private ObservableList<Task> tasksList;
    private TasksService service;
    private DateService dateService;


    private boolean incorrectInputMade;
    @FXML
    private TextField fieldTitle;
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private TextField txtFieldTimeStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private TextField txtFieldTimeEnd;
    @FXML
    private TextField fieldInterval;
    @FXML
    private CheckBox checkBoxActive;
    @FXML
    private CheckBox checkBoxRepeated;

    private static final String DEFAULT_START_TIME = "8:00";
    private static final String DEFAULT_END_TIME = "10:00";
    private static final String DEFAULT_INTERVAL_TIME = "0:30";

    public void setTasksList(ObservableList<Task> tasksList){
        this.tasksList =tasksList;
    }

    public void setService(TasksService service){
        this.service =service;
        this.dateService =new DateService(service);
    }
    public void setCurrentTask(Task task){
        this.currentTask=task;
        switch (clickedButton.getId()){
            case  "btnNew" : initNewWindow("New Task");
                break;
            case "btnEdit" : initEditWindow("Edit Task");
                break;
            default:
                break;
        }
    }

    @FXML
    public void initialize(){
        log.info("new/edit window initializing");
//        switch (clickedButton.getId()){
//            case  "btnNew" : initNewWindow("New Task");
//                break;
//            case "btnEdit" : initEditWindow("Edit Task");
//                break;
//        }

    }
    private void initNewWindow(String title){
        currentStage.setTitle(title);
        datePickerStart.setValue(LocalDate.now());
        txtFieldTimeStart.setText(DEFAULT_START_TIME);
    }

    private void initEditWindow(String title){
        currentStage.setTitle(title);
        fieldTitle.setText(currentTask.getTitle());
        datePickerStart.setValue(dateService.getLocalDateValueFromDate(currentTask.getStartTime()));
        txtFieldTimeStart.setText(dateService.getTimeOfTheDayFromDate(currentTask.getStartTime()));

        if (currentTask.isRepeated()){
            checkBoxRepeated.setSelected(true);
            hideRepeatedTaskModule(false);
            datePickerEnd.setValue(dateService.getLocalDateValueFromDate(currentTask.getEndTime()));
            fieldInterval.setText(service.getIntervalInHours(currentTask));
            txtFieldTimeEnd.setText(dateService.getTimeOfTheDayFromDate(currentTask.getEndTime()));
        }
        if (currentTask.isActive()){
            checkBoxActive.setSelected(true);

        }
    }
    @FXML
    public void switchRepeatedCheckbox(ActionEvent actionEvent){
        CheckBox source = (CheckBox)actionEvent.getSource();
        if (source.isSelected()){
            hideRepeatedTaskModule(false);
        }
        else if (!source.isSelected()){
            hideRepeatedTaskModule(true);
        }
    }
    private void hideRepeatedTaskModule(boolean toShow){
        datePickerEnd.setDisable(toShow);
        fieldInterval.setDisable(toShow);
        txtFieldTimeEnd.setDisable(toShow);

        datePickerEnd.setValue(LocalDate.now());
        txtFieldTimeEnd.setText(DEFAULT_END_TIME);
        fieldInterval.setText(DEFAULT_INTERVAL_TIME);
    }

    @FXML
    public void saveChanges(){
        Task collectedFieldsTask = collectFieldsData();
        if (incorrectInputMade) return;

        if (currentTask == null){//no task was chosen -> add button was pressed
            tasksList.add(collectedFieldsTask);
        }
        else {
            for (int i = 0; i < tasksList.size(); i++){
                if (currentTask.equals(tasksList.get(i))){
                    tasksList.set(i,collectedFieldsTask);
                }
            }
            currentTask = null;
        }
        TaskIO.rewriteFile(tasksList);
        Controller.editNewStage.close();
    }
    @FXML
    public void closeDialogWindow(){
        Controller.editNewStage.close();
    }

    private Task collectFieldsData(){
        incorrectInputMade = false;
        Task result = null;
        try {
            result = makeTask();
        }
        catch (RuntimeException e){
            incorrectInputMade = true;
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/field-validator.fxml"));
                stage.setScene(new Scene(root, 350, 150));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            }
            catch (IOException ioe){
                log.error("error loading field-validator.fxml");
            }
        }
        return result;
    }

    private int parseInterval(String intervalInput) {
        try {
            String[] parts = intervalInput.split(":");
            int minutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
            if (minutes <= 0) {
                throw new IllegalArgumentException("Interval must be greater than zero.");
            }
            return minutes * 60;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid interval format. Use HH:mm.");
        }
    }

    private String validateTimeInput(String timeInput) {
        if (!timeInput.matches("\\d{1,2}:\\d{2}")) {
            throw new IllegalArgumentException("Time format should be HH:mm.");
        }
        return timeInput;
    }

    private Task makeTask() {
        String newTitle = fieldTitle.getText().trim();
        if (newTitle.isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty.");
        }

        Date startDateWithNoTime = dateService.getDateValueFromLocalDate(datePickerStart.getValue());
        Date newStartDate = dateService.getDateMergedWithTime(validateTimeInput(txtFieldTimeStart.getText()), startDateWithNoTime);

        Task result;
        if (checkBoxRepeated.isSelected()) {
            Date endDateWithNoTime = dateService.getDateValueFromLocalDate(datePickerEnd.getValue());
            Date newEndDate = dateService.getDateMergedWithTime(validateTimeInput(txtFieldTimeEnd.getText()), endDateWithNoTime);
            int newInterval = parseInterval(fieldInterval.getText());

            if (newStartDate.after(newEndDate)) {
                throw new IllegalArgumentException("Start date must be before end date.");
            }
            result = new Task(newTitle, newStartDate, newEndDate, newInterval);
        } else {
            result = new Task(newTitle, newStartDate);
        }

        result.setActive(checkBoxActive.isSelected());
        return result;
    }



}

package gak.controller;

import gak.calc.Calculation;
import gak.ui.ChartPane;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 按钮事件
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-9 上午11:43
 */
public class ButtonController extends Service<String> {
    private boolean isResult;
    private SimpleStringProperty textProperty;
    private Calculation calculation;
    private SimpleListProperty<String> data;
    private String memory = "";

    public ButtonController() {
        textProperty = new SimpleStringProperty("0");
        data = new SimpleListProperty<>(FXCollections.observableArrayList());
        calculation = new Calculation();
        isResult = false;
    }

    /**
     * 选择显示的图表
     */
    public void chooseChart(ActionEvent event) {
        List<String> choices = new ArrayList<>();
        choices.add("x²");
        choices.add("x³");
        choices.add("√x");
        choices.add("10ˣ");
        choices.add("eˣ");
        choices.add("sin(x)");
        choices.add("cos(x)");
        choices.add("2x");
        choices.add("-2x");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("选择函数");
        dialog.setHeaderText("请选择您需要生成图表的函数");
        dialog.setContentText("图表函数");

//        担心由于计算过多造成图表加载卡慢，暂时不做
//        HBox expContent = new HBox();
//        Label label = new Label("生成点的数：");
//        TextField number = new TextField("20");
//        expContent.getChildren().addAll(label, number);
//        dialog.getDialogPane().setExpandableContent(expContent);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            // 打开新的图表窗口
            Stage stage = new Stage();
            new ChartPane(stage, s);
        });
    }

    /**
     * 数字键的按钮事件
     */
    public void numberEvent(ActionEvent event) {
        Button button = (Button) event.getSource();
        String field = button.getText();
        // 如果为初始状态或者为以及成功计算完毕的状态需要进行清空
        if (textProperty.isEqualTo("0").or(textProperty.isEqualTo("error")).get() || isResult) {
            textProperty.set(field);
            isResult = false;
        } else {
            textProperty.set(textProperty.concat(field).get());
        }
    }

    /**
     * 为多个按钮一次性添加点击事件，优化代码
     */
    public void optionAddEvent(Button... buttons) {
        for (Button button : buttons) {
            button.setOnAction(this::optionEvent);
        }
    }

    /**
     * 操作符的点击事件
     */
    private void optionEvent(ActionEvent event) {
        Button button = (Button) event.getSource();
        Object userData = button.getUserData();
        if (userData instanceof Boolean && (Boolean) userData) {
            // 单目运算符
            textProperty.set(textProperty.concat(button.getText().toLowerCase() + "(").get());
        } else if (userData instanceof String) {
            textProperty.set(textProperty.concat(userData.toString()).get());
            if ("negate".equalsIgnoreCase(userData.toString())) {
                textProperty.set(textProperty.concat("(").get());
            }
        } else if (userData == null) {
            textProperty.set(textProperty.concat(button.getText()).get());
        }
        isResult = false;
    }

    public void equalEvent(ActionEvent actionEvent) {
        try {
            // 忽略初始时报错（不影响运行）
            reset();
            start();
        } catch (Exception ignored) {
        }
        String express = textProperty.get();
        valueProperty().addListener((ObservableValue<? extends String> observable,
                                     String oldValue, String newValue) -> {
            if (Objects.nonNull(newValue)) {
                isResult = true;
                textProperty.set(newValue);
                data.add(String.format("%s=%s", express, newValue));
            }
        });
    }

    public void deleteEvent(ActionEvent actionEvent) {
        String text = textProperty.get();
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
            textProperty.set(text);
        }
        if (text.length() == 0) {
            textProperty.set("0");
        }
    }

    public void msEvent(ActionEvent actionEvent) {
        memory = textProperty.get();
    }

    public void mrEvent(ActionEvent actionEvent) {
        textProperty.set(textProperty.concat(memory).get());
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                return calculation.calculate(textProperty.get());
            }
        };
    }

    public SimpleStringProperty textPropertyProperty() {
        return textProperty;
    }

    public SimpleListProperty<String> dataProperty() {
        return data;
    }

}

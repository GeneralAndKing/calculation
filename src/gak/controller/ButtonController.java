package gak.controller;

import gak.calc.Calculation;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

/**
 * 按钮事件
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-9 上午11:43
 */
public class ButtonController extends Service<String> {
    private boolean isResult;
    private SimpleStringProperty textProperty;
    private SimpleStringProperty numberProperty;
    private SimpleStringProperty expressProperty;
    private Calculation calculation;
    private SimpleListProperty<String> data;
    private String memory = "";

    public ButtonController() {
        textProperty = new SimpleStringProperty("0");
        numberProperty = new SimpleStringProperty(null);
        expressProperty = new SimpleStringProperty(null);
        data = new SimpleListProperty<>(FXCollections.observableArrayList());
        calculation = new Calculation();
        isResult = false;
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
        if (textProperty.isEqualTo("error").get()) {
            textProperty.set("0");
        }
        if("0".equals(textProperty.get())){
            textProperty.set("");
        }
        if (userData instanceof Boolean && (Boolean) userData) {
            // 单目运算符
            textProperty.set(textProperty.concat(button.getText().toLowerCase() + "(").get());
        } else if (userData instanceof String) {
            textProperty.set(textProperty.concat(userData.toString()).get());
            if ("negate".equalsIgnoreCase(userData.toString())) {
                textProperty.set(textProperty.concat("(").get());
            }
        } else if (userData == null) {
            if ("(".equalsIgnoreCase(button.getText()) && "0".equalsIgnoreCase(textProperty.get())) {
                textProperty.set("(");
            } else {
                textProperty.set(textProperty.concat(button.getText()).get());
            }
        }
        isResult = false;
    }

    public void equalEvent(ActionEvent actionEvent) {
        try {
            // 忽略初始时报错（不影响运行）
            if (isRunning()){
                cancel();
            }
            reset();
            start();
        } catch (Exception ignored) {
        }
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

    @SuppressWarnings("unchecked")
    public void listEvent(MouseEvent event) {
        ListView<String> list = (ListView<String>) event.getSource();
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            String selectedItem = list.getSelectionModel().getSelectedItem();
            if (Objects.isNull(selectedItem)) {
                return;
            }
            int index = selectedItem.indexOf("=");
            if (index == -1) {
                return;
            }
            if (event.getClickCount() == 1) {
                textProperty.set(selectedItem.substring(index + 1));
            }
            if (event.getClickCount() == 2) {
                textProperty.set(selectedItem.substring(0, index));
            }
        }
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        String message = getMessage();
        textProperty.set(message);
        isResult = true;
        data.add(String.format("%s=%s", expressProperty.get(), message));
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() {
                expressProperty.set(textProperty.get());
                Integer scale = 0;
                if (Objects.nonNull(numberProperty.get()) && !numberProperty.get().isEmpty()) {
                    scale = Integer.parseInt(numberProperty.get());
                }
                String result = calculation.calculate(textProperty.get(), scale);
                updateMessage(result);
                return result;
            }
        };
    }

    public SimpleStringProperty textProperty() {
        return textProperty;
    }

    public SimpleStringProperty numberProperty() {
        return numberProperty;
    }

    public SimpleListProperty<String> dataProperty() {
        return data;
    }

}

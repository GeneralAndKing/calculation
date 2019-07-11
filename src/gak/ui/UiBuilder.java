package gak.ui;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-10 下午3:17
 */
class UiBuilder {
    private static Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");


    static Button getButton(String name, int columnIndex, int rowIndex) {
        Button button = new Button(name);
        button.setFont(Font.font(null, 15));
        button.setMinSize(40, 40);
        GridPane.setConstraints(button, columnIndex, rowIndex);
        return button;
    }

    static Button getButton(String name, Object userData, int columnIndex, int rowIndex) {
        Button button = getButton(name, columnIndex, rowIndex);
        button.setUserData(userData);
        return button;
    }

    static Button getButton(String name, int columnIndex, int rowIndex, int columnSpan, int rowSpan) {
        Button button = getButton(name, rowIndex, columnIndex);
        GridPane.setConstraints(button, columnIndex, rowIndex, columnSpan, rowSpan);
        return button;
    }

    static Button getButton(String name, String toolTip, int columnIndex, int rowIndex) {
        Button button = getButton(name, columnIndex, rowIndex);
        button.setTooltip(new Tooltip(toolTip));
        return button;
    }

    static Button getButton(String name, String toolTip, Object userData, int columnIndex, int rowIndex) {
        Button button = getButton(name, userData, columnIndex, rowIndex);
        button.setTooltip(new Tooltip(toolTip));
        return button;
    }

    static Button getButton(String name, String toolTip, int columnIndex, int rowIndex, int columnSpan, int rowSpan) {
        Button button = getButton(name, columnIndex, rowIndex, columnSpan, rowSpan);
        button.setTooltip(new Tooltip(toolTip));
        return button;
    }

    static void buttonSet(double fontSize, int height, int width, Button... buttons) {
        for (Button button : buttons) {
            button.setFont(Font.font(null, fontSize));
            button.setMaxSize(width, height);
            button.setMinSize(width, height);
        }
    }

    static GridPane buildGridPane(int top, int right, int bottom, int left, int h, int v) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(top, right, bottom, left));
        gridPane.setHgap(h);
        gridPane.setVgap(v);
        return gridPane;
    }

    static GridPane buildGridPane() {
        return buildGridPane(10, 10, 10, 10, 10, 10);
    }

    static GridPane buildGridPane(int top, int right, int bottom, int left) {
        return buildGridPane(top, right, bottom, left, 10, 10);
    }

    static GridPane buildGridPane(int h, int v) {
        return buildGridPane(10, 0, 10, 0, h, v);
    }

    /**
     * 选择显示的图表
     */
    static void chooseChart(ActionEvent event) {
        ObservableList<String> choices = FXCollections.observableArrayList(
                "x²", "x³", "√x", "sin(x)", "cos(x)", "10ˣ", "eˣ", "2x", "-2x"
        );
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("选择函数");
        dialog.setHeaderText("请选择您需要生成图表的函数");
        dialog.setWidth(300);
        dialog.setHeight(250);
        dialog.initStyle(StageStyle.UTILITY);

        FlowPane content = new FlowPane();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(5, 15, 5, 15));
        content.setVgap(10);
        content.setHgap(10);

        ChoiceBox<String> choiceBox = new ChoiceBox<>(choices);
        choiceBox.setValue("x²");

        TextField point = new TextField();
        point.setPromptText("点数");
        point.setPrefColumnCount(5);
        point.setText("20");
        point.textProperty().addListener((ObservableValue<? extends String> observable,
                                          String oldValue, String newValue) -> {
            if (Objects.nonNull(newValue) && !isInteger(newValue)) {
                point.setText(oldValue);
            }
        });

        content.getChildren().addAll(new Label("请选择图表："), choiceBox);
        content.getChildren().addAll(new Label("请输入点数："), point);
        dialog.getDialogPane().setContent(content);
        dialog.setResizable(true);

        ButtonType generate = new ButtonType("生成", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(generate, cancel);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generate) {
                return new Pair<>(choiceBox.getSelectionModel().getSelectedItem(), point.getText());
            }
            return null;
        });


        dialog.showAndWait().ifPresent(pair -> {
            String type = pair.getKey();
            String number = pair.getValue();
            Stage stage = new Stage();
            new ChartPane(stage, type, Integer.parseInt(number));
        });
    }

    @SuppressWarnings("unchecked")
    public static void setTipTime(Tooltip tooltip, int millis) {
        try {
            Class tipClass = tooltip.getClass();
            Field f = tipClass.getDeclaredField("BEHAVIOR");
            f.setAccessible(true);
            Class behavior = Class.forName("javafx.scene.control.Tooltip$TooltipBehavior");
            Constructor constructor = behavior.getDeclaredConstructor(Duration.class, Duration.class, Duration.class, boolean.class);
            constructor.setAccessible(true);
            f.set(behavior, constructor.newInstance(new Duration(300), Duration.millis(millis), new Duration(300), false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isInteger(String str) {
        return pattern.matcher(str).matches();
    }
}

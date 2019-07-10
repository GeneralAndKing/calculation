package gak.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-10 下午3:17
 */
class UiBuilder {

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
}

package gak.ui;

import gak.controller.ButtonController;
import gak.controller.FileController;
import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

import static gak.ui.UiBuilder.*;

/**
 * 图形界面主界面
 */
public class Main extends Application {
    private BorderPane pane;
    private TextField textField;
    private SimpleListProperty<String> data;
    private FileController fileController;
    private ButtonController buttonController;
    private Scene scene;
    private ObservableMap<KeyCombination, Runnable> accelerators;


    public Main() {
        fileController = new FileController();
        buttonController = new ButtonController();
        data = new SimpleListProperty<>(FXCollections.observableArrayList());
        fileController.setData(data);
        textField = new TextField("0");
        pane = new BorderPane();
        scene = new Scene(pane);
        accelerators = scene.getAccelerators();
        SimpleListProperty<String> strings = buttonController.dataProperty();
        data.bindBidirectional(strings);
    }

    /**
     * 界面初始化
     */
    @Override
    public void start(Stage primaryStage) {
        pane.setPadding(new Insets(0, 5, 0, 5));
        // 初始化方法
        initTop();
        initLeft();
        initCenter();
        initRight();

        // 设置画布
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Gak 的小小计算器");
        primaryStage.setResizable(false);
    }


    /**
     * 初始化顶部菜单栏以及显示数据框
     */
    private void initTop() {
        VBox menu = new VBox();
        // 显示设置
        textField.setMinHeight(50);
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.setFont(Font.font(null, FontWeight.BOLD, 20));
        textField.setOnMouseClicked(event -> textField.selectAll());
        textField.textProperty().bindBidirectional(buttonController.textPropertyProperty());
        // 设置菜单栏
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("文件");
        menuBar.getMenus().add(file);
        MenuItem exportFile = new MenuItem("导出");
        MenuItem importFile = new MenuItem("导入");

        // 快捷键以及事件处理
        exportFile.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        exportFile.setOnAction(fileController::exportFile);
        importFile.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        importFile.setOnAction(fileController::importFile);
        file.getItems().addAll(exportFile, importFile);
        menu.getChildren().add(menuBar);
        menu.getChildren().add(textField);

        pane.setTop(menu);
    }

    private void initLeft() {
        GridPane left = buildGridPane(10, 10, 10, 0, 10, 5);

        Button sin = getButton("Sin", "正弦", true, 0, 0);
        Button cos = getButton("Cos", "余弦", true, 0, 1);
        Button tan = getButton("Tan", "正切", true, 0, 2);
        Button log = getButton("Log", "常用对数", true, 0, 3);
        Button ln = getButton("Ln", "自然对数", true, 0, 4);
        Button squareRoot = getButton("√x", "开平方", "2√", 0, 5);
        Button ySquareRoot = getButton("ʸ√x", "开方", "√", 0, 6);

        Button rec = getButton("1/x", "倒数", "1/", 1, 0);
        Button fac = getButton("n!", "阶乘", "!", 1, 1);
        Button square = getButton("x²", "平方", "^2", 1, 2);
        Button cube = getButton("x³", "立方", "^3", 1, 3);
        Button xy = getButton("xʸ", "y次方", "^", 1, 4);
        Button ex = getButton("eˣ", "e的x次方", "e^", 1, 5);
        Button ten = getButton("10ˣ", "10的x次方", "10^", 1, 6);

        left.getChildren().addAll(
                sin, cos, tan, log, ln, squareRoot, ySquareRoot,
                rec, fac, square, cube, xy, ex, ten);

        buttonController.optionAddEvent(
                sin, cos, tan, log, ln, squareRoot, ySquareRoot,
                rec, fac, square, cube, xy, ex, ten);

        buttonSet(12, 30, 55,
                sin, cos, tan, log, ln, squareRoot, ySquareRoot,
                rec, fac, square, cube, xy, ex, ten);

        pane.setLeft(left);
    }

    /**
     * 初始化中心部分，添加按钮以及按钮事件
     */
    private void initCenter() {
        // 创建网格布局
        GridPane center = buildGridPane();
        int[][] numberLocation = new int[][]{
                {0, 4},
                {0, 3}, {1, 3}, {2, 3},
                {0, 2}, {1, 2}, {2, 2},
                {0, 1}, {1, 1}, {2, 1}
        };

        // 轮流添加事件与设置布局
        ArrayList<Button> numbers = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Button button = getButton(String.valueOf(i), numberLocation[i][0], numberLocation[i][1]);
            numbers.add(button);
            button.setOnAction(buttonController::numberEvent);
            accelerators.put(new KeyCodeCombination(KeyCode.getKeyCode("Numpad " + i)), button::fire);
            accelerators.put(new KeyCodeCombination(KeyCode.getKeyCode(String.valueOf(i))), button::fire);
        }
        numbers.get(0).setMinSize(90, 40);
        GridPane.setConstraints(numbers.get(0), numberLocation[0][0], numberLocation[0][1], 2, 1);
        center.getChildren().addAll(numbers);
        // 操作按键的初始化
        Button remainder = getButton("%", "取模", 0, 0);
        Button divide = getButton("÷", "除法", 1, 0);
        Button multiply = getButton("×", "乘法", 2, 0);
        Button subtract = getButton("-", "减法", 3, 0);
        Button add = getButton("+", "加法", 3, 1, 1, 2);
        Button equal = getButton("=", "结果", 3, 3, 1, 2);
        Button point = getButton(".", 2, 4);
        center.getChildren().addAll(remainder, divide, multiply, subtract, add, equal, point);
        buttonController.optionAddEvent(remainder, divide, multiply, subtract, add, equal, point);

        accelerators.put(new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.SHIFT_DOWN), add::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.ADD), add::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.MINUS), subtract::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.SUBTRACT), subtract::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.MULTIPLY), multiply::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.SLASH), divide::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.DIVIDE), divide::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.ENTER), equal::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.PERIOD), point::fire);

        // 其他设置以及添加容器
        add.setMinSize(40, 90);
        equal.setMinSize(40, 90);
        equal.setOnAction(buttonController::equalEvent);

        // 事件
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                equal.fire();
            }
        });

        pane.setCenter(center);
    }

    /**
     * 初始化右侧历史记录栏
     */
    private void initRight() {
        GridPane right = buildGridPane(10, 0, 10, 10);
        Button delete = getButton("←", "退格", 0, 0, 2, 1);
        Button negate = getButton("±", "取负数", "negate", 0, 1);
        Button leftBracket = getButton("(", "左括号", 0, 2);
        Button rightBracket = getButton(")", "右括号", 0, 3);
        Button chart = getButton("生成图表", "折线图", 0, 4, 2, 1);

        Button deleteAll = getButton("C", "清空", 1, 1);
        Button ms = getButton("MS", "记忆输入框值", 1, 2);
        Button mr = getButton("MR", "取出输入框值", 1, 3);

        delete.setMinSize(90, 40);
        chart.setMinSize(90, 40);
        right.getChildren().addAll(delete, negate, leftBracket, rightBracket, chart, ms, mr, deleteAll);

        accelerators.put(new KeyCodeCombination(KeyCode.DIGIT9, KeyCombination.SHIFT_DOWN), leftBracket::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.SHIFT_DOWN), rightBracket::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.BACK_SPACE), delete::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.SHIFT_DOWN), deleteAll::fire);

        buttonController.optionAddEvent(negate, leftBracket, rightBracket);
        delete.setOnAction(buttonController::deleteEvent);
        deleteAll.setOnAction(event -> textField.setText("0"));
        chart.setOnAction(buttonController::chooseChart);
        ms.setOnAction(buttonController::msEvent);
        mr.setOnAction(buttonController::mrEvent);
        pane.setRight(right);
    }

}

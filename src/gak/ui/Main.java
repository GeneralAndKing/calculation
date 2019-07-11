package gak.ui;

import gak.bean.ChartBean;
import gak.controller.ButtonController;
import gak.controller.FileController;
import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static gak.ui.UiBuilder.*;

/**
 * 图形界面主界面
 */
public class Main extends Application {
    private Scene scene;
    private Stage primaryStage;
    private BorderPane pane;
    private BorderPane main;
    private Button equal;
    private TextField textField;
    private SimpleListProperty<String> data;
    private FileController fileController;
    private ButtonController buttonController;
    private TextField number;
    private ObservableMap<KeyCombination, Runnable> accelerators;


    public Main() {
        fileController = new FileController();
        buttonController = new ButtonController();
        data = new SimpleListProperty<>(FXCollections.observableArrayList());
        fileController.setData(data);
        textField = new TextField("0");
        pane = new BorderPane();
        main = new BorderPane();
        pane.getStyleClass().add("calc");
        main.getStyleClass().add("calc");
        main.setPadding(new Insets(0, 5, 0, 5));
        pane.setCenter(main);
        scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        accelerators = scene.getAccelerators();
        main.requestFocus();
        SimpleListProperty<String> strings = buttonController.dataProperty();
        data.bindBidirectional(strings);
    }

    /**
     * 界面初始化
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // 初始化方法
        initTop();
        initText();
        initLeft();
        initCenter();
        initRight();
        initResult();

        // 设置画布
        primaryStage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("img" + File.separator + "icon.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(338);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gak 的小小计算器");
        primaryStage.show();
    }

    private void initResult() {
        BorderPane other = new BorderPane();
        other.getStyleClass().add("calc");
        number = new TextField();
        number.setPromptText("小数位数");
        number.setMinHeight(50);
        number.setPadding(new Insets(5));
        number.textProperty().bindBidirectional(buttonController.numberProperty());
        number.textProperty().addListener((ObservableValue<? extends String> observable,
                                           String oldValue, String newValue) -> {
            if (Objects.nonNull(newValue) && !isInteger(newValue)) {
                number.setText(oldValue);
            }
        });
        other.setTop(number);

        ListView<String> list = new ListView<>();
        list.setPadding(new Insets(0, 5, 0, 5));
        list.setItems(data);
        list.setPrefWidth(150);
        list.setMaxHeight(240);
        list.setOnMouseClicked(buttonController::listEvent);
        list.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                equal.fire();
                return;
            }
            if (event.getCode() == KeyCode.DELETE) {
                String selectedItem = list.getSelectionModel().getSelectedItem();
                if (Objects.nonNull(selectedItem)) {
                    data.remove(selectedItem);
                }
            }
        });
        other.setCenter(list);
        this.pane.setRight(other);
    }

    /**
     * 初始化顶部
     */
    private void initTop() {
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("文件(F)");
        MenuItem exportFile = new MenuItem("导出");
        MenuItem importFile = new MenuItem("导入");
        // 快捷键以及事件处理
        exportFile.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        exportFile.setOnAction(fileController::exportFile);
        importFile.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        importFile.setOnAction(fileController::importFile);
        file.getItems().addAll(exportFile, importFile);
        menuBar.getMenus().add(file);
        menuBar.setPadding(new Insets(2));
        pane.setTop(menuBar);
    }


    /**
     * 初始化显示数据框
     */
    private void initText() {
        // 显示设置
        textField.setMinHeight(50);
        textField.setAlignment(Pos.CENTER_LEFT);
        textField.setFont(Font.font(null, FontWeight.BOLD, 20));
        textField.textProperty().bindBidirectional(buttonController.textProperty());
        textField.textProperty().addListener((ObservableValue<? extends String> observable,
                                              String oldValue, String newValue) -> {
            textField.selectPositionCaret(textField.getText().length());
            textField.deselect();
        });
        main.setTop(textField);
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

        accelerators.put(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.SHIFT_DOWN), fac::fire);

        left.getChildren().addAll(
                sin, cos, tan, log, ln, squareRoot, ySquareRoot,
                rec, fac, square, cube, xy, ex, ten);

        buttonController.optionAddEvent(
                sin, cos, tan, log, ln, squareRoot, ySquareRoot,
                rec, fac, square, cube, xy, ex, ten);

        buttonSet(12, 30, 55,
                sin, cos, tan, log, ln, squareRoot, ySquareRoot,
                rec, fac, square, cube, xy, ex, ten);

        main.setLeft(left);
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
        equal = getButton("=", "结果", 3, 3, 1, 2);
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
        accelerators.put(new KeyCodeCombination(KeyCode.DECIMAL), point::fire);

        // 其他设置以及添加容器
        add.setMinSize(40, 90);
        equal.setMinSize(40, 90);
        equal.setOnAction(buttonController::equalEvent);
        buttonController.setOnSucceeded(event -> {
            textField.setText(event.getSource().getMessage());
            textField.requestFocus();
            textField.selectPositionCaret(textField.getText().length());
            textField.deselect();
        });

        main.setCenter(center);
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

        ms.setFont(Font.font(null, 12));
        mr.setFont(Font.font(null, 12));
        delete.setMinSize(90, 40);
        chart.setMinSize(90, 40);
        right.getChildren().addAll(delete, negate, leftBracket, rightBracket, deleteAll, ms, mr, chart);

        accelerators.put(new KeyCodeCombination(KeyCode.DIGIT9, KeyCombination.SHIFT_DOWN), leftBracket::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.SHIFT_DOWN), rightBracket::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.BACK_SPACE), delete::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.ESCAPE), deleteAll::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.SHIFT_DOWN), deleteAll::fire);

        buttonController.optionAddEvent(negate, leftBracket, rightBracket);
        delete.setOnAction(buttonController::deleteEvent);
        deleteAll.setOnAction(event -> textField.setText("0"));
        chart.setOnAction(this::chooseChart);
        ms.setOnAction(buttonController::msEvent);
        mr.setOnAction(buttonController::mrEvent);
        main.setRight(right);
    }

    /**
     * 选择显示的图表
     */
    private void chooseChart(ActionEvent event) {
        ObservableList<String> choices = FXCollections.observableArrayList(
                "x²", "x³", "√x", "sin(x)", "cos(x)", "10ˣ", "eˣ", "2x", "-2x"
        );
        Dialog<ChartBean> dialog = new Dialog<>();
        dialog.setTitle("选择函数");
        dialog.setWidth(200);
        dialog.setHeight(250);
        dialog.initStyle(StageStyle.UTILITY);

        FlowPane content = new FlowPane();
        content.setAlignment(Pos.CENTER);
        content.setPrefWidth(200);
        content.setPadding(new Insets(5, 15, 5, 15));
        content.setVgap(10);
        content.setHgap(10);
        content.getStyleClass().add("calc");

        ChoiceBox<String> choiceBox = new ChoiceBox<>(choices);
        choiceBox.setValue("x²");

        TextField point = buildNumberInput("20", "点数");
        TextField size = buildNumberInput("10", "大小");
        TextField start = buildNumberInput("-10", "起始");
        TextField end = buildNumberInput("10", "终止");

        Label head = new Label("请选择您需要生成图表的函数");
        head.setPadding(new Insets(5));
        head.setFont(Font.font(15));
        content.getChildren().add(head);
        content.getChildren().addAll(new Label("请选择图表："), choiceBox);
        content.getChildren().addAll(new Label("请输入点数："), point);
        content.getChildren().addAll(new Label("数据点大小："), size);
        content.getChildren().addAll(new Label("起始点范围："), start);
        content.getChildren().addAll(new Label("终止点范围："), end);
        dialog.getDialogPane().getStyleClass().add("calc");
        dialog.getDialogPane().setContent(content);
        dialog.setResizable(true);

        ButtonType generate = new ButtonType("生成", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(generate, cancel);
        dialog.initOwner(primaryStage);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generate) {
                return new ChartBean(
                        choiceBox.getValue(),
                        Integer.parseInt(point.getText()), Integer.parseInt(size.getText()),
                        Integer.parseInt(start.getText()), Integer.parseInt(end.getText())
                );
            }
            return null;
        });
        dialog.showAndWait().ifPresent(ChartPane::new);
    }

}

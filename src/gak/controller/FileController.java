package gak.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

/**
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-9 上午11:37
 */
public class FileController {
    private Stage stage;
    private ObservableList<String> data;
    public FileController() {
        this.stage = new Stage();
    }

    /**
     * 导出文件事件
     */
    public void exportFile(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("请选择你需要导出的文件夹");
        File file = directoryChooser.showDialog(stage);
        if (Objects.isNull(file)) {
            return;
        }
        String absolutePath = file.getAbsolutePath();
        File export = new File(absolutePath + File.separator + "data.dat");
        try (OutputStream output = new FileOutputStream(export)) {
            for (String d : data) {
                d += "\r\n";
                output.write(d.getBytes());
            }
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("导出成功");
            info.setHeaderText("成功");
            info.setContentText("导出成功！" + export.getAbsolutePath());
            info.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("导出失败");
            error.setHeaderText("失败");
            error.setContentText("导出失败！" + e.getMessage());
            error.show();
        }
    }

    /**
     * 导入文件事件
     */
    public void importFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择你需要导入的文件");
        File file = fileChooser.showOpenDialog(stage);
        if (Objects.isNull(file)) {
            return;
        }
        try (
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            String str;
            // 按行读取字符串
            while ((str = bufferedReader.readLine()) != null) {
                data.add(str);
                System.out.println(str);
            }
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("导入成功");
            info.setHeaderText("成功");
            info.setContentText("恭喜您，导入成功！");
            info.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("导入失败");
            error.setHeaderText("失败");
            error.setContentText("哦~My God！导入失败！");
            error.show();
        }

    }

    public void setData(ObservableList<String> data) {
        this.data = data;
    }
}

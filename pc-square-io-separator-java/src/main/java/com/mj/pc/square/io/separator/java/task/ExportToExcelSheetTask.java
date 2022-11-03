package com.mj.pc.square.io.separator.java.task;

import com.mj.pc.square.io.separator.java.ApplicationContext;
import com.mj.pc.square.io.separator.java.dialog.AlertDialog;
import com.mj.pc.square.io.separator.java.dialog.LoadingDialog;
import com.mj.pc.square.io.separator.java.exception.NoFileSelectedException;
import com.mj.pc.square.io.separator.java.exception.UnsupportedFileException;
import com.mj.pc.square.io.separator.java.exception.WriteFileException;
import com.mj.pc.square.io.separator.java.util.NodeUtil;
import com.mj.pc.square.io.separator.java.util.SyncValue;
import com.mj.pc.square.io.separator.java.util.TestCase;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 24-10-2022
 */

public final class ExportToExcelSheetTask extends BaseTask<Void> {

    private final StackPane container;

    private final Window window;

    private final LoadingDialog loadingDialog;

    private final List<Node> nodes;

    public ExportToExcelSheetTask(StackPane container, List<Node> nodes) {
        this.container = container;
        this.window = container.getScene().getWindow();
        this.loadingDialog = new LoadingDialog(container);
        this.nodes = nodes;
        this.setEventHandler(WorkerStateEvent.ANY, new TaskEvent());
    }

    @Override
    protected Void call() throws Exception {
        Path file = loadFileChooser();
        Platform.runLater(loadingDialog::show);

        List<TestCase> testCases = NodeUtil.extractUserData(nodes);

        try (OutputStream output = Files.newOutputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);

            Sheet sheet = workbook.createSheet("Sheet 1");

            Row row = sheet.createRow(0);

            String[] headers = {"INPUT", "OUTPUT"};
            for (int i = 0; i < headers.length; i++) {
                CellUtil.createCell(row, i, headers[i], cellStyle);
            }

            for (int i = 0; i < testCases.size(); i++) {
                TestCase testCase = testCases.get(i);

                row = sheet.createRow(i + 1);

                String[] values = {testCase.getInput(), testCase.getOutput()};
                for (int j = 0; j < values.length; j++) {
                    CellUtil.createCell(row, j, values[j], cellStyle);
                }
            }

            workbook.write(output);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WriteFileException(ex.getMessage());
        }

        return null;
    }

    private Path loadFileChooser() throws Exception {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose File");
        chooser.setInitialDirectory(ApplicationContext.getDirectory().toFile());
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Excel files", "*.xls", "*.xlsx"));

        SyncValue<File> wrapper = new SyncValue<>();

        Platform.runLater(() -> wrapper.setValue(chooser.showOpenDialog(window)));

        File file = wrapper.getValue();

        if (Objects.isNull(file)) {
            throw new NoFileSelectedException();
        } else if (!file.isFile()) {
            throw new UnsupportedFileException();
        }

        return file.toPath();
    }

    /*================================================={Inner classes}================================================*/
    private class TaskEvent implements EventHandler<WorkerStateEvent> {

        @Override
        public void handle(WorkerStateEvent event) {
            Worker<?> worker = event.getSource();

            switch (worker.getState()) {
                case READY:
                    break;
                case SCHEDULED:
                    break;
                case RUNNING:
                    break;
                case FAILED:
                    Optional.of(worker)
                            .map(Worker::getException)
                            .map(Throwable::getMessage)
                            .ifPresent(message -> AlertDialog.show(container, message));
                case CANCELLED:
                    loadingDialog.close();
                    break;
                case SUCCEEDED:
                    loadingDialog.close();
                    break;
            }
        }
    }
}

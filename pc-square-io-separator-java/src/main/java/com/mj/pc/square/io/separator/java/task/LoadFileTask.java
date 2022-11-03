package com.mj.pc.square.io.separator.java.task;

import com.mj.pc.square.io.separator.java.ApplicationContext;
import com.mj.pc.square.io.separator.java.constant.Layout;
import com.mj.pc.square.io.separator.java.controller.layout.TestCaseLayoutController;
import com.mj.pc.square.io.separator.java.dialog.AlertDialog;
import com.mj.pc.square.io.separator.java.dialog.LoadingDialog;
import com.mj.pc.square.io.separator.java.exception.EmptyFileException;
import com.mj.pc.square.io.separator.java.exception.LoadFileException;
import com.mj.pc.square.io.separator.java.exception.NoFileSelectedException;
import com.mj.pc.square.io.separator.java.exception.UnsupportedFileException;
import com.mj.pc.square.io.separator.java.util.FXMLUtil;
import com.mj.pc.square.io.separator.java.util.SyncValue;
import com.mj.pc.square.io.separator.java.util.TestCase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 26-10-2022
 */

public final class LoadFileTask extends BaseTask<ObservableList<Node>> {

    private final StackPane container;

    private final Window window;

    private final LoadingDialog loadingDialog;

    public LoadFileTask(StackPane container) {
        this.container = container;
        this.window = container.getScene().getWindow();
        this.loadingDialog = new LoadingDialog(container);
        this.setEventHandler(WorkerStateEvent.ANY, new TaskEvent());
    }

    @Override
    protected ObservableList<Node> call() throws Exception {
        Path filePath = loadFileChooser();
        Platform.runLater(loadingDialog::show);
        ApplicationContext.setDirectory(filePath.getParent());

        // Parsing Excel sheets
        Stream.Builder<String> stream = Stream.builder();
        try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
            DecimalFormat decimal = new DecimalFormat("#.#");

            for (Sheet sheet : workbook) { //Loading excel sheets
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    StringJoiner testCase = new StringJoiner("\n-\n");

                    for (Cell cell : sheet.getRow(i)) {
                        switch (cell.getCellType()) {
                            case NUMERIC:
                                double number = cell.getNumericCellValue();
                                testCase.add(decimal.format(number));
                                break;
                            case STRING:
                                testCase.add(cell.getStringCellValue());
                                break;
                        }
                    }

                    stream.accept(testCase.toString());
                }
            }
        } catch (IOException ex) {
            throw new LoadFileException();
        }

        List<TestCase> testCases;
        testCases = stream.build()
                .map(TestCase::parse)
                .collect(Collectors.toList());

        ObservableList<Node> nodes = FXCollections.observableList(new LinkedList<>());
        for (int i = 0; i < testCases.size(); i++) {
            TestCase testCase = testCases.get(i);
            String testNumber = String.format("Test %03d", i + 1);

            FXMLLoader testCaseInterface = FXMLUtil.getFXMLLoader(Layout.TEST_CASE_LAYOUT);
            Pane testCasePane = FXMLUtil.loadInterface(testCaseInterface);
            TestCaseLayoutController controller = testCaseInterface.getController();

            controller.setTitle(testNumber);
            controller.setTestCase(testCase);
            testCasePane.setUserData(testCase);

            nodes.add(testCasePane);
        }

        return nodes;
    }

    private Path loadFileChooser() throws Exception {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose File");
        chooser.setInitialDirectory(ApplicationContext.getDirectory().toFile());
        chooser.getExtensionFilters().setAll(new ExtensionFilter("Excel files", "*.xls", "*.xlsx"));

        SyncValue<File> wrapper = new SyncValue<>();

        Platform.runLater(() -> wrapper.setValue(chooser.showOpenDialog(window)));

        File file = wrapper.getValue();

        if (Objects.isNull(file)) {
            throw new NoFileSelectedException();
        } else if (!file.isFile()) {
            throw new UnsupportedFileException();
        } else if (file.length() == 0) {
            throw new EmptyFileException();
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

package com.mj.pc.square.io.separator.java.task;

import com.mj.pc.square.io.separator.java.ApplicationContext;
import com.mj.pc.square.io.separator.java.constant.Layout;
import com.mj.pc.square.io.separator.java.controller.layout.TestCaseLayoutController;
import com.mj.pc.square.io.separator.java.dialog.AlertDialog;
import com.mj.pc.square.io.separator.java.dialog.LoadingDialog;
import com.mj.pc.square.io.separator.java.exception.EmptyDirectoryException;
import com.mj.pc.square.io.separator.java.exception.LoadInputFileException;
import com.mj.pc.square.io.separator.java.exception.LoadOutputFileException;
import com.mj.pc.square.io.separator.java.exception.NoDirectorySelectedException;
import com.mj.pc.square.io.separator.java.exception.UnequalIOFilesNumberException;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 26-10-2022
 */

public final class LoadFolderTask extends BaseTask<ObservableList<Node>> {

    private final StackPane container;

    private final Window window;

    private final LoadingDialog loadingDialog;

    public LoadFolderTask(StackPane container) {
        this.container = container;
        this.window = container.getScene().getWindow();
        this.loadingDialog = new LoadingDialog(container);
        this.setEventHandler(WorkerStateEvent.ANY, new TaskEvent());
    }

    @Override
    protected ObservableList<Node> call() throws Exception {
        Path directory = loadDirectoryChooser();
        Platform.runLater(loadingDialog::show);
        ApplicationContext.setDirectory(directory.getParent());

        Map<String, Set<Path>> files;
        files = Files.list(directory)
                .map(Path::toFile)
                .filter(File::isFile)
                .filter(file -> file.getName().matches(".*\\.(in|out)"))
                .sorted()
                .collect(Collectors.groupingBy(
                        file -> file.getName().substring(file.getName().lastIndexOf(".") + 1),
                        Collectors.mapping(File::toPath, Collectors.toCollection(LinkedHashSet::new))
                ));

        Set<Path> inputFiles = files.get("in");
        Set<Path> outputFiles = files.get("out");
        if (inputFiles.size() != outputFiles.size()) {
            throw new UnequalIOFilesNumberException();
        }

        List<TestCase> testCases = new LinkedList<>();

        Iterator<Path> inputIT = inputFiles.iterator();
        Iterator<Path> outputIT = outputFiles.iterator();
        while (inputIT.hasNext() && outputIT.hasNext()) {
            String input;
            try {
                input = Files
                        .lines(inputIT.next())
                        .map(String::trim)
                        .filter(string -> !string.isEmpty())
                        .map(string -> Optional
                                .of(string)
                                .filter(s -> s.contains("#"))
                                .map(s -> s.replace("#", "#\n"))
                                .orElse(string))
                        .collect(Collectors.joining("\n"));
            } catch (Exception ex) {
                throw new LoadInputFileException();
            }

            String output;
            try {
                output = Files
                        .lines(outputIT.next())
                        .map(String::trim)
                        .filter(string -> !string.isEmpty())
                        .map(string -> Optional
                                .of(string)
                                .filter(s -> s.contains("#"))
                                .map(s -> s.replace("#", "#\n"))
                                .orElse(string))
                        .collect(Collectors.joining("\n"));
            } catch (Exception ex) {
                throw new LoadOutputFileException();
            }

            testCases.add(new TestCase(input, output));
        }

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

    private Path loadDirectoryChooser() throws Exception {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose directory");
        chooser.setInitialDirectory(ApplicationContext.getDirectory().toFile());

        SyncValue<File> wrapper = new SyncValue<>();

        Platform.runLater(() -> wrapper.setValue(chooser.showDialog(window)));

        File file = wrapper.getValue();
        if (Objects.isNull(file) || !file.isDirectory()) {
            throw new NoDirectorySelectedException();
        } else {
            FileFilter filter = path -> path.isFile() && path.getName().matches(".*\\.(in|out)");
            if (Optional.of(filter).map(file::listFiles).filter(arr -> arr.length == 0).isPresent()) {
                throw new EmptyDirectoryException();
            }
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

package com.mj.pc.square.io.separator.java.task;

import com.mj.pc.square.io.separator.java.ApplicationContext;
import com.mj.pc.square.io.separator.java.dialog.AlertDialog;
import com.mj.pc.square.io.separator.java.dialog.LoadingDialog;
import com.mj.pc.square.io.separator.java.exception.NoDirectorySelectedException;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.charset.StandardCharsets;
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

public final class ExportToIOFilesTask extends BaseTask<Void> {

    private final StackPane container;

    private final Window window;

    private final LoadingDialog loadingDialog;

    private final List<Node> nodes;

    public ExportToIOFilesTask(StackPane container, List<Node> nodes) {
        this.container = container;
        this.window = container.getScene().getWindow();
        this.loadingDialog = new LoadingDialog(container);
        this.nodes = nodes;
        this.setEventHandler(WorkerStateEvent.ANY, new TaskEvent());
    }

    @Override
    protected Void call() throws Exception {
        Path directory = loadDirectoryChooser();
        Platform.runLater(loadingDialog::show);

        List<TestCase> testCases = NodeUtil.extractUserData(nodes);

        try {
            for (int i = 0; i < testCases.size(); i++) {
                TestCase testCase = testCases.get(i);

                String filePath = String.format("%s/Test%03d", directory, i + 1);

                Files.write(directory.resolve(String.format("%s.in", filePath)), testCase.getInput().getBytes(StandardCharsets.UTF_8));
                Files.write(directory.resolve(String.format("%s.out", filePath)), testCase.getOutput().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WriteFileException(ex.getMessage());
        }

        return null;
    }

    private Path loadDirectoryChooser() throws Exception {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose folder");
        chooser.setInitialDirectory(ApplicationContext.getDirectory().toFile());

        SyncValue<File> wrapper = new SyncValue<>();

        Platform.runLater(() -> wrapper.setValue(chooser.showDialog(window)));

        File file = wrapper.getValue();
        if (Objects.isNull(file) || !file.isDirectory()) {
            throw new NoDirectorySelectedException();
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

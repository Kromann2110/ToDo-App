import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

// A simple Todo application with three columns:
// ToDos -> In Progress -> Done
// Users can move items between columns and delete them
public class TodoApp extends Application {

    // The three list views that display our todos
    private ListView<String> todoList;
    private ListView<String> inProgressList;
    private ListView<String> doneList;

    // The data behind each list
    private ObservableList<String> todos = FXCollections.observableArrayList();
    private ObservableList<String> inProgress = FXCollections.observableArrayList();
    private ObservableList<String> done = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the window
        primaryStage.setTitle("My ToDo application");

        // Add some example todos to start with
        todos.addAll("Todo 10", "Todo 11", "Todo 12", "New Todo Item");
        inProgress.addAll("Todo 6", "Todo 8", "Todo 9");
        done.addAll("Todo 1", "Todo 4", "Todo 3", "Todo 5");

        // Create the three main list views
        todoList = createListView(todos);
        inProgressList = createListView(inProgress);
        doneList = createListView(done);

        // Create the column headers with nice styling
        Label todoHeader = createHeader("ToDos");
        Label progressHeader = createHeader("In progress");
        Label doneHeader = createHeader("Done");

        // Create navigation buttons between ToDos and In Progress
        Button moveToProgressBtn = createNavigationButton(">");
        Button moveBackToTodoBtn = createNavigationButton("<");
        VBox todoToProgressButtons = createButtonColumn(moveToProgressBtn, moveBackToTodoBtn);

        // Create navigation buttons between In Progress and Done
        Button moveToDoneBtn = createNavigationButton(">");
        Button moveBackToProgressBtn = createNavigationButton("<");
        VBox progressToDoneButtons = createButtonColumn(moveToDoneBtn, moveBackToProgressBtn);

        // Organize each column with its header and list
        VBox todoColumn = createColumn(todoHeader, todoList);
        VBox progressColumn = createColumn(progressHeader, inProgressList);
        VBox doneColumn = createColumn(doneHeader, doneList);

        // Arrange all columns and buttons horizontally
        HBox mainContent = new HBox(15);
        mainContent.getChildren().addAll(
                todoColumn,
                todoToProgressButtons,
                progressColumn,
                progressToDoneButtons,
                doneColumn
        );
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(20, 20, 10, 20));

        // Create bottom buttons
        Button createNewBtn = new Button("Create new ToDo");
        Button deleteBtn = new Button("Delete");
        createNewBtn.setPrefSize(150, 30);
        deleteBtn.setPrefSize(100, 30);

        HBox bottomButtons = new HBox(10, createNewBtn, deleteBtn);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(10));

        // Put everything together in the main layout
        BorderPane root = new BorderPane();
        root.setCenter(mainContent);
        root.setBottom(bottomButtons);
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Connect buttons to their actions
        moveToProgressBtn.setOnAction(e -> moveTodoToProgress());
        moveBackToTodoBtn.setOnAction(e -> moveProgressBackToTodo());
        moveToDoneBtn.setOnAction(e -> moveProgressToDone());
        moveBackToProgressBtn.setOnAction(e -> moveDoneBackToProgress());
        createNewBtn.setOnAction(e -> createNewTodo());
        deleteBtn.setOnAction(e -> deleteSelectedItem());

        // Show the window
        Scene scene = new Scene(root, 700, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Creates a ListView with consistent styling
    private ListView<String> createListView(ObservableList<String> items) {
        ListView<String> listView = new ListView<>(items);
        listView.setPrefSize(200, 300);
        return listView;
    }

    // Creates a header label with consistent styling
    private Label createHeader(String text) {
        Label header = new Label(text);
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        return header;
    }

    // Creates a navigation button (< or >)
    private Button createNavigationButton(String symbol) {
        Button button = new Button(symbol);
        button.setPrefSize(40, 30);
        return button;
    }

    // Creates a vertical column of two buttons
    private VBox createButtonColumn(Button topButton, Button bottomButton) {
        VBox buttonColumn = new VBox(5, topButton, bottomButton);
        buttonColumn.setAlignment(Pos.CENTER);
        return buttonColumn;
    }

    // Creates a column with a header and list
    private VBox createColumn(Label header, ListView<String> list) {
        VBox column = new VBox(10, header, list);
        column.setAlignment(Pos.TOP_CENTER);
        return column;
    }

    // Move selected item from ToDos to In Progress
    private void moveTodoToProgress() {
        moveItem(todoList, todos, inProgress);
    }

    // Move selected item from In Progress back to ToDos
    private void moveProgressBackToTodo() {
        moveItem(inProgressList, inProgress, todos);
    }

    // Move selected item from In Progress to Done
    private void moveProgressToDone() {
        moveItem(inProgressList, inProgress, done);
    }

    // Move selected item from Done back to In Progress
    private void moveDoneBackToProgress() {
        moveItem(doneList, done, inProgress);
    }

    // Helper method to move an item from one list to another
    private void moveItem(ListView<String> fromList, ObservableList<String> fromData, ObservableList<String> toData) {
        String selectedItem = fromList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            fromData.remove(selectedItem);
            toData.add(selectedItem);
        }
    }

    // Opens a dialog to create a new todo item
    private void createNewTodo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Todo");
        dialog.setHeaderText("Create a new todo");
        dialog.setContentText("Todo text:");

        dialog.showAndWait().ifPresent(todoText -> {
            if (!todoText.trim().isEmpty()) {
                todos.add(todoText.trim());
            }
        });
    }

    // Deletes the currently selected item from any of the three lists
    private void deleteSelectedItem() {
        // Check the ToDos list first
        if (todoList.getSelectionModel().getSelectedItem() != null) {
            String selected = todoList.getSelectionModel().getSelectedItem();
            todos.remove(selected);
        }
        // Then check the In Progress list
        else if (inProgressList.getSelectionModel().getSelectedItem() != null) {
            String selected = inProgressList.getSelectionModel().getSelectedItem();
            inProgress.remove(selected);
        }
        // Finally check the Done list
        else if (doneList.getSelectionModel().getSelectedItem() != null) {
            String selected = doneList.getSelectionModel().getSelectedItem();
            done.remove(selected);
        }
    }
}
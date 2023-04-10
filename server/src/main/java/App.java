import exceptions.ExitObliged;
import managers.*;

import utility.*;
import commands.*;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Thread {
    public static int PORT = 6086;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;
    private static final Printable console = new BlankConsole();

    static final Logger rootLogger = LogManager.getRootLogger();

    public static void main(String[] args) {
        if(args.length != 0){
            try{
                PORT = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        CollectionManager collectionManager = new CollectionManager();
        FileManager fileManager = new FileManager(console, collectionManager);
        try{
            App.rootLogger.info("Создание объектов");
            fileManager.findFile();
            fileManager.createObjects();
            App.rootLogger.info("Создание объектов успешно завершено");
        } catch (ExitObliged e){
            console.println(ConsoleColors.toColor("До свидания!", ConsoleColors.YELLOW));
            App.rootLogger.error("Ошибка во времени создания объектов");
            return;
        }

        CommandManager commandManager = new CommandManager(fileManager);
        commandManager.addCommand(List.of(
                new History(commandManager),
                new Help(commandManager),
                new ExecuteScript(),
                new Exit(),
                new Sleep(),
                new Ping(),
                new Register(),
                new Info(collectionManager),
                new Show(collectionManager),
                new AddElement(collectionManager),
                new Update(collectionManager),
                new RemoveById(collectionManager),
                new Clear(collectionManager),
                new AddIfMax(collectionManager),
                new RemoveGreater(collectionManager),
                new RemoveAllByAverageMark(collectionManager),
                new CountByAverageMark(collectionManager),
                new CountLessThanExpelledStudents(collectionManager)
        ));
        App.rootLogger.debug("Создан объект менеджера команд");
        App.rootLogger.debug("Создан объект обработчика запросов");
        Server server = new Server(PORT, CONNECTION_TIMEOUT, commandManager, fileManager);
        App.rootLogger.debug("Создан объект сервера");
        server.run();
    }
}
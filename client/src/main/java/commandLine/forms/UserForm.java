package commandLine.forms;

import dtp.User;
import exceptions.ExceptionInFileMode;
import commandLine.*;
import models.*;
import utility.ConsoleColors;
import utility.ExecuteFileManager;

/**
 * Форма для создания юзера
 */
public class UserForm extends Form<User>{

    private final Printable console;
    private final UserInput scanner;

    public UserForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link Person}
     * @return объект класса {@link Person}
     */
    @Override
    public User build() {
        return new User(
                askLogin(),
                askPassword()
        );
    }

    public boolean askIfLogin(){
        console.print("У вас уже есть аккаунт? [yn]  ");
        for(;;) {
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input){
                case "y", "yes", "да", "д" -> {
                    return true;
                }
                case "n", "no", "нет", "н" -> {
                    return false;
                }
                default -> console.printError("Ответ не распознан");
            }
        }
    }

    private String askLogin(){
        String login;
        while (true){
            console.println(ConsoleColors.toColor("Введите ваш логин", ConsoleColors.GREEN));
            login = scanner.nextLine().trim();
            if (login.isEmpty()){
                console.printError("Имя не может быть пустым");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
            else{
                return login;
            }
        }
    }

    private String askPassword(){
        String pass;
        while (true){
            console.println(ConsoleColors.toColor("Введите пароль", ConsoleColors.GREEN));
            pass = scanner.nextLine().trim();
            if (pass.isEmpty()){
                console.printError("Имя не может быть пустым");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
            else{
                return pass;
            }
        }
    }
}

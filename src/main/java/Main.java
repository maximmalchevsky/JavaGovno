import config.Config;
import gui.LibraryApp;
import repository.Database;
import repository.Repository;
import service.Service;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Config cfg = Config.load();
        Database db = Database.newDatabase(cfg);
        Repository repo   = new Repository(db);
        Service    service = new Service(repo);
        SwingUtilities.invokeLater(() -> {
            new LibraryApp(service).setVisible(true);
        });
    }
}

package app;

import app.controllers.MainViewController;
import app.views.MainView;

public class RayTracerDesktopApp {
    public static void main(String[] args) {
         MainViewController mainViewController = new MainViewController();
         MainView mainView = new MainView(mainViewController);
    }
}

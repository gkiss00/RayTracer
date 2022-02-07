package app.views;

import app.controllers.MainViewController;
import rayTracer.enums.FilterTypeEnum;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainView extends JFrame {
    //Controller
    private MainViewController controller;

    //Labels
    private final JLabel dimensionLabel = new JLabel("Dimensions");
    private final JLabel widthLabel = new JLabel("width:");
    private final JLabel heightLabel = new JLabel("height:");
    private final JLabel antiAliasingLabel = new JLabel("Anti-aliasing");
    private final JLabel reflectiveIndexLabel = new JLabel("Reflective index");
    private final JLabel finalFilterLabel = new JLabel("Final filter");

    //Inputs
    private final JTextField widthTextField = new JTextField(25);
    private final JTextField heightTextField = new JTextField(25);
    private final JSlider antiAliasingSlider = new JSlider(0, 10, 1);
    private final JTextField reflectiveIndexTextField = new JTextField(25);
    private final JComboBox<FilterTypeEnum> finalFilterComboBox = new JComboBox<>(FilterTypeEnum.values());

    //Values
    private final SimpleIntegerProperty widthValue = new SimpleIntegerProperty();
    private final SimpleIntegerProperty heightValue = new SimpleIntegerProperty();
    private final SimpleIntegerProperty antiAliasingValue = new SimpleIntegerProperty();
    private final SimpleIntegerProperty reflectiveIndexValue = new SimpleIntegerProperty();
    private final ObjectProperty<FilterTypeEnum> finalFilterValue = new SimpleObjectProperty<>();

    //Run
    private final JButton run = new JButton("Run");
    public MainView(MainViewController controller) {
        super();
        this.controller = controller;
        setup();
    }

    public MainView(MainViewController controller, String title) {
        super(title);
        this.controller = controller;
        setup();
    }

    private void setup() {
        addEventListener();
        this.setSize(800, 600);
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);

        panel.add(dimensionLabel);
        JPanel dimensionPanel = new JPanel();
        BoxLayout dimensionPanelLayout = new BoxLayout(dimensionPanel, BoxLayout.X_AXIS);
        dimensionPanel.setLayout(dimensionPanelLayout);
        dimensionPanel.add(widthLabel);
        dimensionPanel.add(widthTextField);
        dimensionPanel.add(heightLabel);
        dimensionPanel.add(heightTextField);
        panel.add(dimensionPanel);

        panel.add(antiAliasingLabel);
        JSlider slider = new JSlider(0, 10, 1);
        antiAliasingSlider.setMajorTickSpacing(5);
        antiAliasingSlider.setMinorTickSpacing(1);
        antiAliasingSlider.setPaintTicks(true);
        antiAliasingSlider.setPaintLabels(true);
        panel.add(antiAliasingSlider);

        panel.add(reflectiveIndexLabel);
        panel.add(reflectiveIndexTextField);

        panel.add(finalFilterLabel);
        panel.add(finalFilterComboBox);

        panel.add(run);

        ImagePanel image = new ImagePanel("Image.png");
        image.setPreferredSize(new Dimension(800, 400));
        panel.add(image);

        this.add(panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        configureBinding();
        setDefaultValues();
    }

    private final void addEventListener() {
        widthTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println("keyTyped: " + e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("KeyPressed: " + e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("keyReleased: " + e.getKeyChar());
                try {
                    Integer.parseInt(widthTextField.getText());
                    widthValue.setValue(Integer.parseInt(widthTextField.getText()));
                } catch (Exception exception) {
                    widthTextField.setText(widthValue.getValue() == 0 ? "" : widthValue.getValue().toString());
                }
            }
        });

        heightTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println("keyTyped: " + e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("KeyPressed: " + e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("keyReleased: " + e.getKeyChar());
                try {
                    Integer.parseInt(heightTextField.getText());
                    heightValue.setValue(Integer.parseInt(heightTextField.getText()));
                } catch (Exception exception) {
                    heightTextField.setText(heightValue.getValue() == 0 ? "" : heightValue.getValue().toString());
                }
            }
        });

        antiAliasingSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                antiAliasingValue.setValue(antiAliasingSlider.getValue());
            }
        });

        reflectiveIndexTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println("keyTyped: " + e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("KeyPressed: " + e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("keyReleased: " + e.getKeyChar());
                try {
                    Integer.parseInt(reflectiveIndexTextField.getText());
                    reflectiveIndexValue.setValue(Integer.parseInt(reflectiveIndexTextField.getText()));
                } catch (Exception exception) {
                    reflectiveIndexTextField.setText(reflectiveIndexValue.getValue() == 0 ? "" : reflectiveIndexValue.getValue().toString());
                }
            }
        });

        finalFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalFilterValue.setValue((FilterTypeEnum)finalFilterComboBox.getSelectedItem());
            }
        });

        run.addActionListener(e -> {
            controller.process();
        });
    }

    private void configureBinding(){
        widthValue.bindBidirectional(controller.widthProperty());
        heightValue.bindBidirectional(controller.heightProperty());
        antiAliasingValue.bindBidirectional(controller.antiAliasingProperty());
        reflectiveIndexValue.bindBidirectional(controller.reflectiveIndexProperty());
        finalFilterValue.bindBidirectional(controller.finalFilterProperty());
    }

    private void setDefaultValues() {
        widthTextField.setText(widthValue.getValue().toString());
        heightTextField.setText(heightValue.getValue().toString());
        antiAliasingSlider.setValue(antiAliasingValue.getValue());
        reflectiveIndexTextField.setText(reflectiveIndexValue.getValue().toString());
    }
}

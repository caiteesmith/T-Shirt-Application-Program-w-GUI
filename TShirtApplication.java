/* Program Information:
Author: Caitlyn Smith
Course: CISS 111-300
Email: c-smith54@hvcc.edu

Program Objective:
Create a program and JavaFX GUI for the Computer Club's t-shirt application

Program Description:
This program consists of a JavaFX GUI which presents to the user
an opportunity to order t-shirts from the computer club. In this
program, the user can select the shirt size and quantity, add it
to an order, review that order in a text area, and "check out" by
selecting a file to print the order to. The user must select a file
that contains pricing. The user can completely clear the form and
start fresh. There is also a help button provided to the user to
inform on how to use this program.
 */

package program09.smithcaitlyn_program09;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;

public class TShirtApplication extends Application {
    //Accessible TextField, Labels, ArrayLists, Data Types
    private TextField xsInput,smInput,mdInput,lgInput,xlInput,xxlInput;
    private Label xsSum,smSum,mdSum,lgSum,xlSum,xxlSum;
    private TextArea orderReview;
    private final ArrayList<Integer> xsArray = new ArrayList<>();
    private final ArrayList<Integer> smArray = new ArrayList<>();
    private final ArrayList<Integer> mdArray = new ArrayList<>();
    private final ArrayList<Integer> lgArray = new ArrayList<>();
    private final ArrayList<Integer> xlArray = new ArrayList<>();
    private final ArrayList<Integer> xxlArray = new ArrayList<>();
    private double xsPrice, smPrice, mdPrice, lgPrice, xlPrice, xxlPrice;
    private int xsQuantity, smQuantity, mdQuantity, lgQuantity, xlQuantity, xxlQuantity;
    private double xsTotal, smTotal, mdTotal, lgTotal, xlTotal, xxlTotal, subTotal, salesTax, grandTotal;

    @Override
    public void start(Stage stage) {
        //Labels
        Label programLabel = new Label("Computer Club T-Shirt Application");
        programLabel.setId("header");
        programLabel.setWrapText(true);

        //Header Labels
        Label sizeLabel = new Label("Size");
        sizeLabel.setId("subLabels");
        Label quantityLabel = new Label("Quantity");
        quantityLabel.setId("subLabels");
        Label sumLabel = new Label("Total");
        sumLabel.setId("subLabels");

        //Size Labels
        Label xsLabel = new Label("X-Small");
        Label smLabel = new Label("Small");
        Label mdLabel = new Label("Medium");
        Label lgLabel = new Label("Large");
        Label xlLabel = new Label("X-Large");
        Label xxlLabel = new Label("2X-Large");

        //Order Review
        Label orderLabel = new Label("Please Review Your Order:");
        orderReview = new TextArea();
        orderReview.setPrefHeight(350);
        orderReview.setWrapText(true);

        //Quantity text fields and the sum of their arrays
        xsInput = new TextField();
        xsInput.setMaxWidth(55);
        xsInput.setText("0");
        xsSum = new Label("0");

        smInput = new TextField();
        smInput.setMaxWidth(55);
        smInput.setText("0");
        smSum = new Label("0");

        mdInput = new TextField();
        mdInput.setMaxWidth(55);
        mdInput.setText("0");
        mdSum = new Label("0");

        lgInput = new TextField();
        lgInput.setMaxWidth(55);
        lgInput.setText("0");
        lgSum = new Label("0");

        xlInput = new TextField();
        xlInput.setMaxWidth(55);
        xlInput.setText("0");
        xlSum = new Label("0");

        xxlInput = new TextField();
        xxlInput.setMaxWidth(55);
        xxlInput.setText("0");
        xxlSum = new Label("0");

        //BUTTONS
        //Order button
        Button order = new Button("Add to Order");
        order.setOnAction(new OrderButtonHandler());

        //Checkout button
        Button checkout = new Button("Checkout");
        FileChooser checkoutFile = new FileChooser();
        checkoutFile.setTitle("Checkout");
        checkout.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    File file = checkoutFile.showSaveDialog(stage);

                    try {
                        PrintWriter writer = new PrintWriter(file);
                        writer.println(
                            "Size   \tQuantity   \tPrice           Total" +
                            "\n------------------------------------------------------------" +
                            "\nXS: \t" + xsQuantity + "\t\t$" + String.format("%.2f",xsPrice) + "\t\t$" + String.format("%.2f",xsTotal) +
                            "\nSM: \t" + smQuantity + "\t\t$" + String.format("%.2f",smPrice) + "\t\t$" + String.format("%.2f",smTotal) +
                            "\nMD: \t" + mdQuantity + "\t\t$" + String.format("%.2f",mdPrice) + "\t\t$" + String.format("%.2f",mdTotal) +
                            "\nLG: \t" + lgQuantity + "\t\t$" + String.format("%.2f",lgPrice) + "\t\t$" + String.format("%.2f",lgTotal) +
                            "\nXL: \t" + xlQuantity + "\t\t$" + String.format("%.2f",xlPrice) + "\t\t$" + String.format("%.2f",xlTotal) +
                            "\nXXL: \t" + xxlQuantity + "\t\t$" + String.format("%.2f",xxlPrice) + "\t\t$" + String.format("%.2f",xxlTotal) +
                            "\n\t\t\t\t\t--------------------" +
                            "\n\t\t\t\t\tSubtotal: $" + String.format("%.2f",subTotal) +
                            "\n\t\t\t\t\tTax (8.0%): $" + String.format("%.2f",salesTax) +
                            "\n\t\t\t\t\tOrder Total: $" + String.format("%.2f",grandTotal));
                        writer.close();
                    } catch (IOException | NullPointerException ex) {
                        System.out.println("");
                    }
                }
            }
        );

        //Review button
        Button review = new Button("Review Order");
        review.setOnAction(new ReviewButtonHandler());

        //Clear button
        Button clear = new Button("Clear");
        clear.setOnAction(new ClearButtonHandler());

        //Help button
        Button help = new Button("Help");
        help.setOnAction(new HelpButtonHandler());

        //Select File button
        Button fileButton = new Button("Select Pricing");
        FileChooser fileChooser = new FileChooser();
        fileButton.setOnAction(
            new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                File file = fileChooser.showOpenDialog(stage);
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line = reader.readLine();
                        ArrayList<String> priceArray = new ArrayList<>();

                        while (line != null) {
                            priceArray.add(line);
                            line = reader.readLine();
                        }

                        reader.close();

                        String xsLine, smLine, mdLine, lgLine, xlLine, xxlLine;
                        String xsParse, smParse, mdParse, lgParse, xlParse, xxlParse;

                        xsLine = priceArray.get(0);
                        xsParse = xsLine.replaceAll("[^0-9.]", "");
                        xsPrice = Double.parseDouble(xsParse);

                        smLine = priceArray.get(1);
                        smParse = smLine.replaceAll("[^0-9.]", "");
                        smPrice = Double.parseDouble(smParse);

                        mdLine = priceArray.get(2);
                        mdParse = mdLine.replaceAll("[^0-9.]", "");
                        mdPrice = Double.parseDouble(mdParse);

                        lgLine = priceArray.get(3);
                        lgParse = lgLine.replaceAll("[^0-9.]", "");
                        lgPrice = Double.parseDouble(lgParse);

                        xlLine = priceArray.get(4);
                        xlParse = xlLine.replaceAll("[^0-9.]", "");
                        xlPrice = Double.parseDouble(xlParse);

                        xxlLine = priceArray.get(5);
                        xxlParse = xxlLine.replaceAll("[^0-9.]", "");
                        xxlPrice = Double.parseDouble(xxlParse);
                    } catch (IOException | NullPointerException ex) {
                        System.out.println("");
                    }
                }
            }
        );

        //GridPane Setup
        GridPane gridPane = new GridPane();
        gridPane.setId("gridPane");
        gridPane.setPadding(new Insets(10,25,25,25));
        gridPane.setVgap(15);
        gridPane.setHgap(5);

        //Add GridPanes
        gridPane.add(programLabel, 0, 0, 3, 1);

        gridPane.add(sizeLabel, 0, 1);
        gridPane.add(quantityLabel, 1, 1);
        gridPane.add(sumLabel, 2, 1);

        gridPane.add(xsLabel, 0, 2);
        gridPane.add(xsInput, 1, 2);
        gridPane.add(xsSum, 2, 2);

        gridPane.add(smLabel, 0, 3);
        gridPane.add(smInput, 1, 3);
        gridPane.add(smSum, 2, 3);

        gridPane.add(mdLabel, 0, 4);
        gridPane.add(mdInput, 1, 4);
        gridPane.add(mdSum, 2, 4);

        gridPane.add(lgLabel, 0, 5);
        gridPane.add(lgInput, 1, 5);
        gridPane.add(lgSum, 2, 5);

        gridPane.add(xlLabel, 0, 6);
        gridPane.add(xlInput, 1, 6);
        gridPane.add(xlSum, 2, 6);

        gridPane.add(xxlLabel, 0, 7);
        gridPane.add(xxlInput, 1, 7);
        gridPane.add(xxlSum, 2, 7);

        gridPane.add(order, 0, 8);
        gridPane.add(review, 1, 8);
        gridPane.add(checkout, 2, 8);

        gridPane.add(fileButton, 0, 9);
        gridPane.add(clear, 1, 9);
        gridPane.add(help, 2, 9);

        gridPane.add(orderLabel, 0, 10, 3, 1);

        gridPane.add(orderReview, 0, 11, 3, 3);

        //Setting scene, alignment, padding
        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add("stylesheet.css");

        //Set scene, size, and call
        stage.setTitle("Computer Club T-Shirt Application");
        stage.setHeight(800);
        stage.setWidth(350);
        stage.setScene(scene);
        stage.show();
    }

    //Add to Order button handler
    class OrderButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            int xs, sm, md, lg, xl, xxl;

            //Process user input
            try {
                xs = Integer.parseInt(xsInput.getText());
                sm = Integer.parseInt(smInput.getText());
                md = Integer.parseInt(mdInput.getText());
                lg = Integer.parseInt(lgInput.getText());
                xl = Integer.parseInt(xlInput.getText());
                xxl = Integer.parseInt(xxlInput.getText());

                //X-Small Shirts
                xsArray.add(xs);
                xsSum.setText("" + xsArray);
                xsInput.setText("0");

                //Small Shirts
                smArray.add(sm);
                smSum.setText("" + smArray);
                smInput.setText("0");

                //Medium Shirts
                mdArray.add(md);
                mdSum.setText("" + mdArray);
                mdInput.setText("0");

                //Large Shirts
                lgArray.add(lg);
                lgSum.setText("" + lgArray);
                lgInput.setText("0");

                //X-Large Shirts
                xlArray.add(xl);
                xlSum.setText("" + xlArray);
                xlInput.setText("0");

                //XX-Large Shirts
                xxlArray.add(xxl);
                xxlSum.setText("" + xxlArray);
                xxlInput.setText("0");
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                System.out.println("");
            }
        }
    }

    //Order Review button handler
    class ReviewButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            xsQuantity = 0;
            smQuantity = 0;
            mdQuantity = 0;
            lgQuantity = 0;
            xlQuantity = 0;
            xxlQuantity = 0;

            //Summing array for each size/quantity
            for (Integer integer : xsArray) {
                xsQuantity += integer;
            }

            for (Integer integer : smArray) {
                smQuantity += integer;
            }

            for (Integer integer : mdArray) {
                mdQuantity += integer;
            }

            for (Integer integer : lgArray) {
                lgQuantity += integer;
            }

            for (Integer integer : xlArray) {
                xlQuantity += integer;
            }

            for (Integer integer : xxlArray) {
                xxlQuantity += integer;
            }

            //Summing total of each size, quantity, and price
            xsTotal = xsQuantity * xsPrice;
            smTotal = smQuantity * smPrice;
            mdTotal = mdQuantity * mdPrice;
            lgTotal = lgQuantity * lgPrice;
            xlTotal = xlQuantity * xlPrice;
            xxlTotal = xxlQuantity * xxlPrice;

            //Summing subtotal and grand total
            subTotal = xsTotal + smTotal + mdTotal + lgTotal + xlTotal + xxlTotal;
            salesTax = subTotal * 0.08;
            grandTotal = subTotal + salesTax;

            //Output for order review
            orderReview.setText(
                    "Size   \tQuantity   \tPrice           \tTotal" +
                    "\nXS: \t" + xsQuantity + "\t\t\t$" + String.format("%.2f",xsPrice) + "\t\t$" + String.format("%.2f",xsTotal) +
                    "\nSM: \t" + smQuantity + "\t\t\t$" + String.format("%.2f",smPrice) + "\t\t$" + String.format("%.2f",smTotal) +
                    "\nMD: \t" + mdQuantity + "\t\t\t$" + String.format("%.2f",mdPrice) + "\t\t$" + String.format("%.2f",mdTotal) +
                    "\nLG: \t" + lgQuantity + "\t\t\t$" + String.format("%.2f",lgPrice) + "\t\t$" + String.format("%.2f",lgTotal) +
                    "\nXL: \t\t" + xlQuantity + "\t\t\t$" + String.format("%.2f",xlPrice) + "\t\t$" + String.format("%.2f",xlTotal) +
                    "\nXXL: \t" + xxlQuantity + "\t\t\t$" + String.format("%.2f",xxlPrice) + "\t\t$" + String.format("%.2f",xxlTotal) +
                    "\n\t\t\t\t\t--------------------" +
                    "\n\t\t\t\t\tSubtotal:\t$" + String.format("%.2f",subTotal) +
                    "\n\t\t\t\t\tTax (8.0%): $" + String.format("%.2f",salesTax) +
                    "\n\t\t\t\t\tOrder Total: $" + String.format("%.2f",grandTotal));
        }
    }

    //Clear button handler
    class ClearButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            //Clearing quantity arrays
            xsArray.removeAll(xsArray);
            smArray.removeAll(smArray);
            mdArray.removeAll(mdArray);
            lgArray.removeAll(lgArray);
            xlArray.removeAll(xlArray);
            xxlArray.removeAll(xxlArray);

            //Clearing quantity input
            xsInput.setText("0");
            smInput.setText("0");
            mdInput.setText("0");
            lgInput.setText("0");
            xlInput.setText("0");
            xxlInput.setText("0");
            orderReview.clear();

            //Clearing quantities
            xsSum.setText("0");
            smSum.setText("0");
            mdSum.setText("0");
            lgSum.setText("0");
            xlSum.setText("0");
            xxlSum.setText("0");
        }
    }

    //Help button handler
    static class HelpButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Help Information");
            alert.setHeaderText("How to order your t-shirt(s)");
            alert.setContentText("Choose and enter the quantity of the computer club t-shirts you require." +
                    "\n- Select 'Add to Order' to add your selection to your order." +
                    "\n- Select 'Review Order' to review your order." +
                    "\n- Select 'Checkout' to check out." +
                    "\n- Select 'Clear Order' to clear the form." +
                    "\n- Select 'Select File' to choose which price file to be used." +
                    "\n- Select 'Help' to see this help alert again." +
                    "\n" +
                    "\nProgram Credit:" +
                    "\nCaitlyn Smith, c-smith54@hvcc.edu");
            alert.showAndWait();
        }
    }

    //Call FX event
    public static void main(String[] args) {
        launch();
    }
}
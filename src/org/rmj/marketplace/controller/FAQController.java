/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.marketplace.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import org.rmj.marketplace.model.ScreenInterface;
import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agent.MsgBox;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.marketplace.base.LTransaction;
import org.rmj.marketplace.base.FAQuestions;
import org.rmj.marketplace.bubble.BubbleSpec;
import org.rmj.marketplace.bubble.BubbledLabel;
import org.rmj.marketplace.model.FAQuestionsModel;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FAQController implements Initializable, ScreenInterface {
    private String recdstat;
    private GRider oApp;
    private FAQuestions oTrans;
    private LTransaction  oListener;
    private boolean pbLoaded = false;
    private int pnRow = -1;
    private int pnEditMode;
    private final String style = "";
    
    
    @FXML
    private Button btnSend;
    @FXML
    private TextField txtField01;
    @FXML
    private TabPane tabPaneSelection;
    @FXML
    private Tab tabReplied;
    @FXML
    private Tab tabUnReplied;
    @FXML
    private TableView tblReplied;
    @FXML
    private TableColumn repliedIndex01;
    @FXML
    private TableColumn repliedIndex02;
    @FXML
    private TableColumn repliedIndex03;
    @FXML
    private TableColumn repliedIndex04;
    @FXML
    private TableView tblUnReplied;
    @FXML
    private TableColumn tblunRepliedIndex01;
    @FXML
    private TableColumn tblunRepliedIndex02;
    @FXML
    private TableColumn tblunRepliedIndex03;
    @FXML
    private TableColumn tblunRepliedIndex04;
    @FXML
    private ListView lvMessageBody;
    @FXML
    private Label lblCustomerName;
    @FXML
    private TextField txtSeeks10;
    @FXML
    private TextField txtSeeks11;

    
    private FilteredList<FAQuestionsModel> filteredData;

    private final ObservableList<FAQuestionsModel> data_faq = FXCollections.observableArrayList();
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         oListener = new LTransaction() {
             @Override
             public void MasterRetreive(int fnIndex, Object foValue) {
                 System.out.println("fnIndex = " + fnIndex);
                 System.out.println("foValue = " + (String) foValue);
             }
         };
        oTrans = new FAQuestions(oApp, oApp.getBranchCode(), false);
        oTrans.setListener(oListener);
        oTrans.setTranStat(1);
        oTrans.setWithUI(true);
        pbLoaded = true;
        recdstat = "1";
        loadProducts();
        initGrid();
        initGrid1();
        pnEditMode = EditMode.UNKNOWN;
        btnSend.setOnAction(this::cmdButton_Click);
        
        tabPaneSelection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
        @Override
        public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
            if(newTab == tabReplied) {
                oTrans.setTranStat(1);
                recdstat = "1";
                
                }
            else{
                 oTrans.setTranStat(0);
                 lvMessageBody.getItems().clear();
                 recdstat = "0";
                 
                }
            clear();
            loadProducts();
            }
        });
    }    

    
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private void loadProducts(){
        int lnCtr;
        try {
            data_faq.clear();
            if (oTrans.LoadList("", true)){//true if by barcode; false if by description
                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++){
                    data_faq.add(new FAQuestionsModel(String.valueOf(lnCtr),
                            (String) oTrans.getDetail(lnCtr, "sListngID"),
                            oTrans.getDetail(lnCtr, "nEntryNox").toString(),
                            oTrans.getDetail(lnCtr, "sQuestion").toString(),
                            oTrans.getDetail(lnCtr, "sReplyxxx").toString(),
                            oTrans.getDetail(lnCtr, "nPriority").toString(),
                            oTrans.getDetail(lnCtr, "sCreatedx").toString(),
                            oTrans.getDetail(lnCtr, "dCreatedx").toString(),
                            oTrans.getDetail(lnCtr, "sRepliedx").toString(),
                            (String) oTrans.getDetail(lnCtr, "dRepliedx"),
                            (String) oTrans.getDetail(lnCtr, "cReadxxxx"),
                            (String) oTrans.getDetail(lnCtr, "dReadxxxx"),
                            (String) oTrans.getDetail(lnCtr, "sReadxxxx"),
                            (String) oTrans.getDetail(lnCtr, "cRecdStat"),
                            oTrans.getDetail(lnCtr, "dTimeStmp").toString(),
                            (String) oTrans.getDetail(lnCtr, "sImagesxx"),
                            (String) oTrans.getDetail(lnCtr, "sCompnyNm")));
                  System.out.println("sReadxxxx. --> " +  (String) oTrans.getDetail(lnCtr, 11));
                    
                    
                }
                if ("1".equals(recdstat) ) {
                        initGrid();
                    } else {
                        initGrid1();
                    }
                
            } 
        } catch (SQLException ex) {
            System.out.println("SQLException" + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("NullPointerException" + ex.getMessage());
        } catch (DateTimeException ex) {
//            MsgBox.showOk(ex.getMessage());
            System.out.println("DateTimeException" + ex.getMessage());
        } 
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        try {
            switch (lsButton){
                case "btnSend": 
                    if(pnRow>=0){
                        oTrans.setMaster(5, txtField01.getText());
//                        oTrans.setMaster(9, oApp.getUserID());
//                        oTrans.setMaster(10, oApp.getServerDate());
                        oTrans.setMaster(14, 1);
                        if (oTrans.SaveTransaction()){
                            MsgBox.showOk("Transaction save successfully");
                            clear();
                            loadProducts();
                            loadDetail();
                        } else {
                           MsgBox.showOk(oTrans.getMessage());
                        }
                    }else{
                        MsgBox.showOk("No record selected.");
                    }
                        
                    break;
                case "tabReplied":
                     oTrans.setTranStat(1);
                    MsgBox.showOk("1");
                    break;
                case "tabUnReplied":
                     oTrans.setTranStat(0);
                    MsgBox.showOk("0");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MsgBox.showOk(e.getMessage());
        }
    } 
   private void clear(){
        lvMessageBody.getItems().clear();
        txtField01.clear();
        lblCustomerName.setText("Customer Name:");
   }
    private void initGrid() {
        
        repliedIndex01.setStyle("-fx-alignment: CENTER;");
        repliedIndex02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        repliedIndex03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        repliedIndex04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
       
        repliedIndex01.setCellValueFactory(new PropertyValueFactory<>("repliedIndex01"));
        repliedIndex02.setCellValueFactory(new PropertyValueFactory<>("repliedIndex07"));
        repliedIndex03.setCellValueFactory(new PropertyValueFactory<>("repliedIndex17"));
        repliedIndex04.setCellValueFactory(new PropertyValueFactory<>("repliedIndex04"));
      
        tblReplied.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblReplied.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        filteredData = new FilteredList<>(data_faq, b -> true);
        autoSearch(txtSeeks10);
        autoSearch(txtSeeks11);
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<FAQuestionsModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tblReplied.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tblReplied.setItems(sortedData);
    }
    
    private void initGrid1() {
        tblunRepliedIndex01.setStyle("-fx-alignment: CENTER;" + style);
        tblunRepliedIndex02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;"+ style);
        tblunRepliedIndex03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;"+ style);
        tblunRepliedIndex04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;"+ style);
        
        tblunRepliedIndex01.setCellValueFactory(new PropertyValueFactory<>("repliedIndex01"));
        tblunRepliedIndex02.setCellValueFactory(new PropertyValueFactory<>("repliedIndex07"));
        tblunRepliedIndex03.setCellValueFactory(new PropertyValueFactory<>("repliedIndex17"));
        tblunRepliedIndex04.setCellValueFactory(new PropertyValueFactory<>("repliedIndex04"));
      
        tblUnReplied.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblUnReplied.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        filteredData = new FilteredList<>(data_faq, b -> true);
        autoSearch(txtSeeks10);
        autoSearch(txtSeeks11);
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<FAQuestionsModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tblUnReplied.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tblUnReplied.setItems(sortedData);
  
    }
    
     private void autoSearch(TextField txtField){
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        boolean fsCode = true;
        txtField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(clients-> {
                    // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                        return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if(lnIndex == 10){
                    return (clients.getRepliedIndex06().toLowerCase().contains(lowerCaseFilter)); // Does not match.

                }else {
                    return (clients.getRepliedIndex17().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                }
            });
        });
        if(lnIndex == 98){

        }if(lnIndex == 99){

        }
    } 
    
    @FXML
    private void tblUnReplied_Clicked(MouseEvent event) {
        
        if(event.getClickCount()<=1){
            if(!tblUnReplied.getItems().isEmpty()){
            pnRow = tblUnReplied.getSelectionModel().getSelectedIndex();
            try {
                if (oTrans.OpenTransaction(data_faq.get(pnRow).getRepliedIndex02(),data_faq.get(pnRow).getRepliedIndex03())){
                    pnEditMode = oTrans.getEditMode();
                    if (oTrans.UpdateTransaction()){
                        pnEditMode = oTrans.getEditMode();
                        
                        if(oTrans.ReadReview()){
                            loadProducts();
                        }
                        loadDetail();
                    } else {
                        MsgBox.showOk(oTrans.getMessage());
                    }
                } else {
                    MsgBox.showOk(oTrans.getMessage());
                }
                tblUnReplied.setOnKeyReleased((KeyEvent t)-> {
                     
                    try {
                        KeyCode key = t.getCode();
                        switch (key){
                            case DOWN:
                                pnRow = tblUnReplied.getSelectionModel().getSelectedIndex();

                               
                                if (pnRow == tblUnReplied.getItems().size()) {
                                    pnRow = tblUnReplied.getItems().size();
                                    if (oTrans.OpenTransaction(data_faq.get(pnRow).getRepliedIndex02(),data_faq.get(pnRow).getRepliedIndex03())){
                                        if (oTrans.UpdateTransaction()){
                                            pnEditMode = oTrans.getEditMode();
                                            if(oTrans.ReadReview()){
                                                loadProducts();
                                            }
                                            
                                            loadDetail();
                                        } else {
                                            MsgBox.showOk(oTrans.getMessage());
                                        }
                                    } else {
                                        MsgBox.showOk(oTrans.getMessage());
                                    }

                                }else {
                                    if (oTrans.OpenTransaction(data_faq.get(pnRow).getRepliedIndex02(),data_faq.get(pnRow).getRepliedIndex03())){
                                        if (oTrans.UpdateTransaction()){
                                            pnEditMode = oTrans.getEditMode();
                        
                                            if(oTrans.ReadReview()){
                                                loadProducts();
                                            }
                                           
                                            loadDetail();
                                        } else {
                                            MsgBox.showOk(oTrans.getMessage());
                                        }
                                    } else {
                                        MsgBox.showOk(oTrans.getMessage());
                                    }
                                }
                                break;
                            case UP:
                                int pnRows = 0;
                                int x = 1;
                                pnRows = tblUnReplied.getSelectionModel().getSelectedIndex();
                                pnRow = pnRows;
                               
                                if (oTrans.OpenTransaction(data_faq.get(pnRow).getRepliedIndex02(),data_faq.get(pnRow).getRepliedIndex03())){
                                    if (oTrans.UpdateTransaction()){
                                        pnEditMode = oTrans.getEditMode();
                        
                                        if(oTrans.ReadReview()){
                                            loadProducts();
                                        }

                                        loadDetail();
                                    } else {
                                        MsgBox.showOk(oTrans.getMessage());
                                    }
                                } else {
                                    MsgBox.showOk(oTrans.getMessage());
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (SQLException ex) {
                       Logger.getLogger(FAQController.class.getName()).log(Level.SEVERE, null, ex);
                   }
                }); 
                } catch (SQLException ex) {
                   Logger.getLogger(FAQController.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
        }
        
        
    }
    @FXML
    private void tblReplied_Clicked(MouseEvent event) {
        
        if(event.getClickCount()<=1){
            if(!tblReplied.getItems().isEmpty()){
            pnRow = tblReplied.getSelectionModel().getSelectedIndex();
            try {
              
                if (oTrans.OpenTransaction(data_faq.get(pnRow).getRepliedIndex02(),data_faq.get(pnRow).getRepliedIndex03())){
                    pnEditMode = oTrans.getEditMode();
                    if (oTrans.UpdateTransaction()){
                        pnEditMode = oTrans.getEditMode();
                        
                        if(oTrans.ReadReview()){
                            loadProducts();
                        }
                        loadDetail();
                    } else {
                        MsgBox.showOk(oTrans.getMessage());
                    }
                } else {
                    MsgBox.showOk(oTrans.getMessage());
                }
                tblReplied.setOnKeyReleased((KeyEvent t)-> {
                     
                    try {
                        KeyCode key = t.getCode();
                        switch (key){
                            case DOWN:
                                pnRow = tblReplied.getSelectionModel().getSelectedIndex();

                               
                                if (pnRow == tblReplied.getItems().size()) {
                                    pnRow = tblReplied.getItems().size();
                                    if (oTrans.OpenTransaction(data_faq.get(pnRow).getRepliedIndex02(),data_faq.get(pnRow).getRepliedIndex03())){
                                        if (oTrans.UpdateTransaction()){
                                            pnEditMode = oTrans.getEditMode();
                                            if(oTrans.ReadReview()){
                                                loadProducts();
                                            }
                                            
                                            loadDetail();
                                        } else {
                                            MsgBox.showOk(oTrans.getMessage());
                                        }
                                    } else {
                                        MsgBox.showOk(oTrans.getMessage());
                                    }

                                }else {
                                    if (oTrans.OpenTransaction(data_faq.get(pnRow).getRepliedIndex02(),data_faq.get(pnRow).getRepliedIndex03())){
                                        if (oTrans.UpdateTransaction()){
                                            pnEditMode = oTrans.getEditMode();
                        
                                            if(oTrans.ReadReview()){
                                                loadProducts();
                                            }
                                           
                                            loadDetail();
                                        } else {
                                            MsgBox.showOk(oTrans.getMessage());
                                        }
                                    } else {
                                        MsgBox.showOk(oTrans.getMessage());
                                    }
                                }
                                break;
                            case UP:
                                int pnRows = 0;
                                int x = 1;
                                pnRows = tblReplied.getSelectionModel().getSelectedIndex();
                                pnRow = pnRows;
                               
                                if (oTrans.OpenTransaction(data_faq.get(pnRow).getRepliedIndex02(),data_faq.get(pnRow).getRepliedIndex03())){
                                    if (oTrans.UpdateTransaction()){
                                        pnEditMode = oTrans.getEditMode();
                        
                                        if(oTrans.ReadReview()){
                                            loadProducts();
                                        }

                                        loadDetail();
                                    } else {
                                        MsgBox.showOk(oTrans.getMessage());
                                    }
                                } else {
                                    MsgBox.showOk(oTrans.getMessage());
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (SQLException ex) {
                       Logger.getLogger(FAQController.class.getName()).log(Level.SEVERE, null, ex);
                   }
                }); 
                } catch (SQLException ex) {
                   Logger.getLogger(FAQController.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
        }
        
        
    }
    public void loadDetail(){
//        taMessages.setText(data.get(pnRow).getRepliedIndex04()); 
        lblCustomerName.setText(data_faq.get(pnRow).getRepliedIndex17());
        
        lvMessageBody.getItems().clear();
        pnEditMode = EditMode.UPDATE;
        addToChat();
        if(!data_faq.get(pnRow).getRepliedIndex04().trim().isEmpty()){
           addToReply();
        }
        
        boolean isReply = (Integer.parseInt(data_faq.get(pnRow).getRepliedIndex14())>0);

        txtField01.setDisable(isReply);
        btnSend.setDisable(isReply);
        
        
    }
    public synchronized void addToChat() {
        
        BubbledLabel bl6 = new BubbledLabel();
        bl6.setText(data_faq.get(pnRow).getRepliedIndex04());
        bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,null, null)));
        HBox x = new HBox();
        bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
        x.getChildren().add(bl6);
        x.setStyle("-fx-opacity:1.0");
        lvMessageBody.getItems().add(x);
//        Task<HBox> othersMessages = new Task<HBox>() {
//            @Override
//            public HBox call() throws Exception {
//                
//                BubbledLabel bl6 = new BubbledLabel();
//                bl6.setText(data.get(pnRow).getRepliedIndex04());
//                bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,null, null)));
//                HBox x = new HBox();
//                bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
//                x.getChildren().add(bl6);
//                x.setStyle("-fx-opacity:1.0");
//                
//                return x;
//            }
//        };
//
//        othersMessages.setOnSucceeded(event -> {
//            lvMessageBody.getItems().add(othersMessages.getValue());
//        });
//
//        Thread t = new Thread(othersMessages);
//        t.setDaemon(true);
//        t.start();
    }
    public synchronized void addToReply() {
//        Task<HBox> yourMessages = new Task<HBox>() {
//            @Override
//            public HBox call() throws Exception {
//               
//                BubbledLabel bl6 = new BubbledLabel();
//                bl6.setText(data.get(pnRow).getRepliedIndex06());
//                
//                bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
//                        null, null)));
//                HBox x = new HBox();
//                x.setMaxWidth(lvMessageBody.getWidth() - 30);
//                x.setAlignment(Pos.TOP_RIGHT);
//                x.setStyle("-fx-opacity:1.0");
//                bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
//                x.getChildren().add(bl6);
//
//                return x;
//            }
//        };
//        yourMessages.setOnSucceeded(event -> lvMessageBody.getItems().add(yourMessages.getValue()));
//
//        Thread t = new Thread(yourMessages);
//        t.setDaemon(true);
//        t.start();
//        
        BubbledLabel bl6 = new BubbledLabel();
        bl6.setText(data_faq.get(pnRow).getRepliedIndex05());

        bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                null, null)));
        HBox x = new HBox();
        x.setMaxWidth(lvMessageBody.getWidth() - 30);
        x.setAlignment(Pos.TOP_RIGHT);
        x.setStyle("-fx-opacity:1.0");
        bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
        x.getChildren().add(bl6);
        lvMessageBody.getItems().add(x);
    }
   
    
}

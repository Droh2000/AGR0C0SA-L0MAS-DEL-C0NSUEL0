/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import xmlNodeArray.*;

public class SaveScanDataTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectPalletId;
    protected PreparedStatement pstmtSelectPalletProduct;
    protected PreparedStatement pstmtSelectShippigOrderDetail;
    protected PreparedStatement pstmtSelectShippigOrderQty;
    protected PreparedStatement pstmtSelectShippigOrderInfo;
    protected PreparedStatement pstmtUser;
    protected PreparedStatement pstmtPalletInfo;
    protected PreparedStatement pstmtSelectActualStorage;
    protected PreparedStatement pstmtUpdatePalletInfo;
    protected PreparedStatement pstmtSelectShippingOrderDetailId;
    protected PreparedStatement pstmtInsertShippingOrderPallet;
    protected PreparedStatement pstmtDeleteInventory;
    protected PreparedStatement pstmtInsertInventoryMovement;
    protected PreparedStatement pstmtUpdateShippingOrder;
    protected PreparedStatement pstmtInsertShippingOrderHistory;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveScanDataTransaction() {
        super();
        SetTransactionType(SIDWebTransaction.SaveType);
    }

    /**
     * Checks to see if the node Array is supported
     *
     * @param nodeArray
     * @return <B>true</B> if the nodeArray is supported. <B>false</B> otherwise
     * @exception (none)
     */
    @Override
    public boolean Supports(xmlNodeArray nodeArray) {

        //Add tag names as comma separated Strings to the mandatoryTags array
        String[] mandatoryTags = {
            "idUser",
            "idShippingOrder",
            "palletData"
        };

        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<SaveScanDataTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveScanDataTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveScanDataTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveScanDataTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
                errArray.add("OPTIONAL_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        return true;
    }

    /**
     * Prepares the SQL statements to be executed
     *
     * @return <B>true</B> for successful preparation; <B>false</B> for
     * unsuccessful preparation
     * @exception (none)
     */
    @Override
    public synchronized boolean PrepareStatements() {
        //Note1 : Use PreparedStatements instead of Statements where ever possible
        //Note2 : If transaction contains no prepared statements, delete entire function
        //        Unless there are nested transaction, then Prepare will call those.
        try {
            Connection con = this.GetSIDDataBase().GetConnection();

            pstmtSelectPalletId = con.prepareStatement("Select "
                    + "PalletId "
                    + "FROM inventory "
                    + "Where PalletId = ? ");

            pstmtSelectPalletProduct = con.prepareStatement("Select "
                    + "palletproduct.ProductName "
                    + "FROM inventory inner join "
                    + "palletproduct on palletproduct.idPalletProduct = inventory.idPalletProduct "
                    + "Where PalletId = ? ");

            pstmtSelectShippigOrderDetail = con.prepareStatement("SELECT "
                    + "ProductName, "
                    + "sum(Quantity) as Qty "
                    + "FROM shippingorderdetail "
                    + "Where idShippingOrder = ? "
                    + "Group by ProductName "
                    + "Order by ProductName");

            pstmtSelectShippigOrderQty = con.prepareStatement("SELECT "
                    + "sum(Quantity) as Qty "
                    + "FROM shippingorderdetail "
                    + "Where idShippingOrder = ? ");

            pstmtSelectShippigOrderInfo = con.prepareStatement("select "
                    + "customer.CustomerName, "
                    + "PickupNumber "
                    + "from shippingorder inner join "
                    + "customer on customer.idCustomer = shippingorder.idCustomer "
                    + "where idShippingOrder = ? ");

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            pstmtPalletInfo = con.prepareStatement("select "
                    + "PalletId, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity "
                    + "from palletinfo "
                    + "where PalletId = ? ");

            pstmtSelectActualStorage = con.prepareStatement("select "
                    + "storage.StorageName, "
                    + "substorage.SubstorageName "
                    + "from inventory inner join "
                    + "storage on storage.idStorage = inventory.idStorage inner join "
                    + "substorage on substorage.idSubStorage = inventory.idSubstorage "
                    + "where PalletId = ? ");

            pstmtUpdatePalletInfo = con.prepareStatement("update palletinfo set "
                    + "Sale = 1, "
                    + "ModifiedDate = Now() "
                    + "where PalletId = ? ");

            pstmtSelectShippingOrderDetailId = con.prepareStatement("SELECT "
                    + "idShippingOrderDetail "
                    + "FROM shippingorderdetail "
                    + "Where idShippingOrder = ? "
                    + "and ProductName = ? ");

            pstmtInsertShippingOrderPallet = con.prepareStatement("Insert into shippingorderpallet ("
                    + "idShippingOrderDetail, "
                    + "idShippingOrder, "
                    + "PalletId  "
                    + ") values ("
                    + "?, "//1 idShippingOrderDetail
                    + "?, "//2 idShippingORder
                    + "?  "//3 PalletId
                    + ")");

            pstmtDeleteInventory = con.prepareStatement("delete from inventory "
                    + "where PalletId = ? ");

            pstmtInsertInventoryMovement = con.prepareStatement("insert into inventorymovement ("
                    + "PalletId, "
                    + "Process, "
                    + "MovementType, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity, "
                    + "StorageNameOut, "
                    + "SubstorageNameOut, "
                    + "StorageNameIn, "
                    + "SubstorageNameIn, "
                    + "Comments, "
                    + "User, "
                    + "InsertDate, "
                    + "Active "
                    + ") values("
                    + "?, "//1 PalletId
                    + "?, "//2 Process
                    + "?, "//3 MovementType
                    + "?, "//4 ProductName
                    + "?, "//5 Size
                    + "?, "//6 Color
                    + "?, "//7 Quantity
                    + "?, "//8 StorageNameOut
                    + "?, "//9 SubstorageNameOut
                    + "?, "//10 StorageNameIn
                    + "?, "//11 SubstorageNameIn
                    + "?, "//12 Comment
                    + "?, "//13 User
                    + "Now(), "
                    + "1 "
                    + ")");

            pstmtUpdateShippingOrder = con.prepareStatement("update shippingorder set  "
                    + "Status = ?, "
                    + "idUser = ?, "
                    + "ModifiedDate = Now() "
                    + "Where idShippingOrder = ? ");

            pstmtInsertShippingOrderHistory = con.prepareStatement("Insert into shippingorderhistory ( "
                    + "idShippingOrder, "
                    + "CustomerName, "
                    + "PickupNumber, "
                    + "Process, "
                    + "MovementType, "
                    + "Quantity,  "
                    + "Comments, "
                    + "User, "
                    + "InsertDate "
                    + ") Values( "
                    + "?, "// 1 idShippingOrder
                    + "?, "// 2 CustomerName
                    + "?, "// 3 PickupNumber
                    + "'Embarque', "// Process
                    + "'Embarcada', "// MovementType
                    + "?, "// 4 Quantity
                    + "?, "// 5 Comments
                    + "?, "// 6 User
                    + "Now() " // InsertDate
                    + ")");

            this.addPreparedStatement(pstmtSelectPalletId);
            this.addPreparedStatement(pstmtSelectPalletProduct);
            this.addPreparedStatement(pstmtSelectShippigOrderDetail);
            this.addPreparedStatement(pstmtSelectShippigOrderQty);
            this.addPreparedStatement(pstmtSelectShippigOrderInfo);
            this.addPreparedStatement(pstmtUser);
            this.addPreparedStatement(pstmtPalletInfo);
            this.addPreparedStatement(pstmtSelectActualStorage);
            this.addPreparedStatement(pstmtUpdatePalletInfo);
            this.addPreparedStatement(pstmtSelectShippingOrderDetailId);
            this.addPreparedStatement(pstmtInsertShippingOrderPallet);
            this.addPreparedStatement(pstmtDeleteInventory);
            this.addPreparedStatement(pstmtInsertInventoryMovement);
            this.addPreparedStatement(pstmtUpdateShippingOrder);
            this.addPreparedStatement(pstmtInsertShippingOrderHistory);
            
            return true;
        } catch (SQLException e) {
            System.out.println("SaveScanDataTransaction::PrepareStatements> SQLException: " + e.getMessage());
            return false;
        }
    }

    /**
     * executes sql statements using input arguments and returns result
     *
     * @return valid node array if successful else null
     * @exception SQLException if sql error occurs
     * @exception Exception if non sql error occurs
     */
    @Override
    public synchronized xmlNodeArray Execute() throws SQLException, Exception {
        Connection conn = null;
        xmlNodeArray resultArray = null;
        ResultSet rset = null;
        ResultSet rset2 = null;
        int rowsAffected = 0;
        int idUser = 0;
        String userName = "";
        String productName = "";
        String size = "";
        String color = "";
        int quantity = 0;
        String result = "";
        String palletData = "";
        int idShippingOrder = 0;
        String storageNameFrom = "";
        String substorageNameFrom = "";
        int orderQty = 0;
        int productNameQty = 0;
        String errorMsg1 = "";
        String errorMsg2 = "";
        String customerName = "";
        String pickupNumber = "";
        int idShippingOrderDetail = 0;

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            idShippingOrder = GetNodeArray().find("idShippingOrder").getIntValue();
            palletData = GetNodeArray().find("palletData").getStringValue();

            resultArray.add("idShippingOrder", idShippingOrder);
            
            String lines[] = palletData.split("\\r?\\n");

            //validar que los pallets esten en inventario
            result = "";
            for (String pallet : lines) {
                pstmtSelectPalletId.setString(1, pallet);
                rset = pstmtSelectPalletId.executeQuery();
                if (!rset.next()) {
                    result += rset.getString("PalletId") + ", ";
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
            }
            if (!result.equals("")) {
                conn.rollback();
                resultArray.add("error", "Los siguientes pallets no se encuentran en la localidad de inventario del crossdock.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Los siguientes pallets no se encuentran en la localidad de inventario del crossdock.");
                resultArray.add("RESPONSE_DETAIL", result);
                return resultArray;
            }

            //get shipping order total qty
            pstmtSelectShippigOrderQty.setInt(1, idShippingOrder);
            rset = pstmtSelectShippigOrderQty.executeQuery();
            if (rset.next()) {
                orderQty = rset.getInt("Qty");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            pstmtSelectShippigOrderInfo.setInt(1, idShippingOrder);
            rset = pstmtSelectShippigOrderInfo.executeQuery();
            if (rset.next()) {
                customerName = rset.getString("CustomerName");
                pickupNumber = rset.getString("PickupNumber");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("customer", customerName);
            

            //Get productName & Qty from shipping order
            xmlTable detailTab = new xmlTable();
            detailTab.addField("ProductName");
            detailTab.addField("Qty");
            pstmtSelectShippigOrderDetail.setInt(1, idShippingOrder);
            rset = pstmtSelectShippigOrderDetail.executeQuery();
            while (rset.next()) {
                detailTab.addRow();
                detailTab.setValue(detailTab.getRowsQty() - 1, "ProductName", rset.getString("ProductName"));
                detailTab.setValue(detailTab.getRowsQty() - 1, "Qty", rset.getInt("Qty"));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //validate scanned pallet Qty  Vs Order Qty
            if (lines.length > orderQty) {
                conn.rollback();
                resultArray.add("error", "Se escanearon mas pallets que los requeridos en el embarque.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Se escanearon mas pallets que los requeridos en el embarque.");
                resultArray.add("RESPONSE_DETAIL", "La cantidad de pallets escaneados no coincide con el embarque");
                return resultArray;
            } else if (lines.length < orderQty) {
                conn.rollback();
                resultArray.add("error", "Se escanearon menos pallets que los requeridos en el embarque.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Se escanearon menos pallets que los requeridos en el embarque.");
                resultArray.add("RESPONSE_DETAIL", "La cantidad de pallets escaneados no coincide con el embarque");
                return resultArray;
            }

            //get from scanned product name & qty 
            Map<String, Integer> scannedMap = new HashMap<>();
            for (String pallet : lines) {
                pstmtSelectPalletProduct.setString(1, pallet);
                rset = pstmtSelectPalletProduct.executeQuery();
                if (rset.next()) {
                    productName = rset.getString("ProductName");
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }

                if (scannedMap.containsKey(productName)) {
                    scannedMap.put(productName, scannedMap.get(productName) + 1);
                } else {
                    scannedMap.put(productName, 1);
                }
            }

            //validate each product name & qty from shipping order Vs Scanned pallets
            for (int i = 0; i < detailTab.getRowsQty(); i++) {
                productName = detailTab.getStringValue(i, "ProductName");
                quantity = detailTab.getIntValue(i, "Qty");

                if (scannedMap.containsKey(productName)) {
                    productNameQty = scannedMap.get(productName);
                    if (quantity != productNameQty) {
                        errorMsg1 += productName + ", ";
                    }
                } else {
                    errorMsg2 += productName + ", ";
                }
            }

            if (!errorMsg1.equals("")) {
                conn.rollback();
                resultArray.add("error", "No coincide la cantidad de pallets escaneadas del producto con la cantidad del embarque.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "No coincide la cantidad de pallets escaneadas del producto con la cantidad del embarque.");
                resultArray.add("RESPONSE_DETAIL", errorMsg1);
                return resultArray;
            } else if (!errorMsg2.equals("")) {
                conn.rollback();
                resultArray.add("error", "El producto del pallet escaneado no coincide con los productos del embarque: ");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "El producto del pallet escaneado no coincide con los productos del embarque: ");
                resultArray.add("RESPONSE_DETAIL", errorMsg2);
                return resultArray;
            }

            //User Name
            pstmtUser.setInt(1, idUser);
            rset = pstmtUser.executeQuery();
            if (rset.next()) {
                userName = rset.getString("User");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //recorrer pallet por pallet
            result = "";
            for (String pallet : lines) {

                //get productName Size, color & quantity
                pstmtPalletInfo.setString(1, pallet);
                rset = pstmtPalletInfo.executeQuery();
                if (rset.next()) {
                    productName = rset.getString("ProductName");
                    size = rset.getString("Size");
                    color = rset.getString("Color");
                    quantity = rset.getInt("Quantity");
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }

                //get actual storage / substorage
                pstmtSelectActualStorage.setString(1, pallet);
                rset2 = pstmtSelectActualStorage.executeQuery();
                if (rset2.next()) {
                    storageNameFrom = rset2.getString("StorageName");
                    substorageNameFrom = rset2.getString("SubstorageName");
                }
                if (rset2 != null) {
                    rset2.close();
                    rset2 = null;
                }

                //update palletinfo
                pstmtUpdatePalletInfo.setString(1, pallet);
                rowsAffected = pstmtUpdatePalletInfo.executeUpdate();
                if (rowsAffected > 0) {
                    result += "1";
                } else {
                    result += "0";
                }

                //insert shipping order pallet
                pstmtSelectShippingOrderDetailId.setInt(1, idShippingOrder);
                pstmtSelectShippingOrderDetailId.setString(2, productName);
                rset = pstmtSelectShippingOrderDetailId.executeQuery();
                if(rset.next()){
                    idShippingOrderDetail = rset.getInt("idShippingOrderDetail");
                }
                if(rset != null){
                    rset.close();
                    rset = null;
                }
                //insert
                pstmtInsertShippingOrderPallet.setInt(1, idShippingOrderDetail);
                pstmtInsertShippingOrderPallet.setInt(2, idShippingOrder);
                pstmtInsertShippingOrderPallet.setString(3, pallet);
                rowsAffected = pstmtInsertShippingOrderPallet.executeUpdate();
                if (rowsAffected > 0) {
                    result += "1";
                } else {
                    result += "0";
                }
                
                //delete inventory
                pstmtDeleteInventory.setString(1, pallet);
                rowsAffected = pstmtDeleteInventory.executeUpdate();
                if (rowsAffected > 0) {
                    result += "1";
                } else {
                    result += "0";
                }

                //insert inventory movement
                //Insert pallet Movement
                pstmtInsertInventoryMovement.setString(1, pallet);
                pstmtInsertInventoryMovement.setString(2, "Embarque");
                pstmtInsertInventoryMovement.setString(3, "Salida");
                pstmtInsertInventoryMovement.setString(4, productName);
                pstmtInsertInventoryMovement.setString(5, size);
                pstmtInsertInventoryMovement.setString(6, color);
                pstmtInsertInventoryMovement.setInt(7, quantity);
                pstmtInsertInventoryMovement.setString(8, storageNameFrom);
                pstmtInsertInventoryMovement.setString(9, substorageNameFrom);
                pstmtInsertInventoryMovement.setString(10, null);
                pstmtInsertInventoryMovement.setString(11, null);
                pstmtInsertInventoryMovement.setString(12, pickupNumber + " - " + customerName);
                pstmtInsertInventoryMovement.setString(13, userName);
                rowsAffected = pstmtInsertInventoryMovement.executeUpdate();
                if (rowsAffected > 0) {
                    result += "1";
                } else {
                    result += "0";
                }
            }

            if (!result.contains("0")) {
                //update stock transfer order  status
                pstmtUpdateShippingOrder.setString(1, "Embarcada");
                pstmtUpdateShippingOrder.setInt(2, idUser);
                pstmtUpdateShippingOrder.setInt(3, idShippingOrder);
                rowsAffected = pstmtUpdateShippingOrder.executeUpdate();

                if (rowsAffected > 0) {
                    //Insert order history
                    pstmtInsertShippingOrderHistory.setInt(1, idShippingOrder);
                    pstmtInsertShippingOrderHistory.setString(2, customerName);
                    pstmtInsertShippingOrderHistory.setString(3, pickupNumber);
                    pstmtInsertShippingOrderHistory.setInt(4, orderQty);
                    pstmtInsertShippingOrderHistory.setString(5, "Embarque a Cliente : " + customerName + " - " + pickupNumber);
                    pstmtInsertShippingOrderHistory.setString(6, userName);
                    rowsAffected = pstmtInsertShippingOrderHistory.executeUpdate();
                    if (rowsAffected > 0) {

                        conn.commit();
                        resultArray.add("Saved", "Yes");
                        resultArray.add("RESPONSE_CODE", "PASS");
                        resultArray.add("RESPONSE_MESSAGE", "Embarque registrado exitosamente.");
                        resultArray.add("RESPONSE_DETAIL", "");
                    } else {
                        conn.rollback();
                        resultArray.add("error", "El Embarque no pudo ser registrado.");
                        resultArray.add("RESPONSE_CODE", "FAIL");
                        resultArray.add("RESPONSE_MESSAGE", "El embarque no pudo ser registrado.");
                        resultArray.add("RESPONSE_DETAIL", "");
                    }
                } else {
                    conn.rollback();
                    resultArray.add("error", "El embarque no pudo ser registrado.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "El embarque no pudo ser registrado.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            } else {
                conn.rollback();
                resultArray.add("error", "El embarque no pudo ser registrado.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "El embarque no pudo ser registrado.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveScanDataTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveScanDataTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
            if (rset != null) {
                rset.close();
                rset = null;
            }
            if (rset2 != null) {
                rset2.close();
                rset2 = null;
            }
        }
    }

    /**
     * Generates an xmlNodeArray containing parameters for this transaction
     *
     * @return xmlNodeArray that contains parameters for the transaction
     * @exception (none)
     */
    @Override
    public xmlNodeArray GenerateTestParameters() {
        xmlNodeArray nodeArr = new xmlNodeArray();
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveScanDataTransaction");

        return nodeArr;

    }

    /**
     * The main method for the transaction. Creates a database connection and an
     * error Array, then executes the transaction and reports any errors
     *
     * @param argv argv[0] is an optional configuration file name
     * @exception (none)
     */
    public static void main(String[] argv) {
        try {
            SaveScanDataTransaction transaction = new SaveScanDataTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveScanDataTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveScanDataTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveScanDataTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveScanDataTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveScanDataTransaction - No results were returned.");
                            } else {
                                xmlNodeArray array = resultTransaction.GetResultArray();
                                String str = xmlNodeArray.xmlNodeArray2String(array);
                                array = xmlNodeArray.string2xmlNodeArray(str);
                                System.out.println(str);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("SaveScanDataTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

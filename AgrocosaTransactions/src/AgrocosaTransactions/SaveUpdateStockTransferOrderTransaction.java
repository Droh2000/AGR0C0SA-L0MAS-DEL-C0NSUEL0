/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveUpdateStockTransferOrderTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtUpdateStockTransferOrder;
    protected PreparedStatement pstmtUpdateWeightTransferOrder;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveUpdateStockTransferOrderTransaction() {
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
            "idStockTransferOrder",
            "TransportCompany",
            "TruckDriverName",
            "TruckPlate",
            "TruckBoxNumber",
            "TruckBoxPlate",
            "Crosser",
            "TypeBox",
            "BoxFeet",
            "Tara",
            "Bruto",
            //"Neto"
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
                System.out.println("<SaveUpdateStockTransferOrderTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveUpdateStockTransferOrderTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveUpdateStockTransferOrderTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveUpdateStockTransferOrderTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtUpdateStockTransferOrder = con.prepareStatement("update stocktransferorder set  "
                    + "TransportCompany = ?, "
                    + "TruckDriverName = ?, "
                    + "TruckPlate = ?, "
                    + "TruckBoxNumber = ?, "
                    + "TruckBoxPlate = ?, "
                    + "Crosser = ?, "
                    + "TypeBox = ?, "
                    + "BoxFeet = ?, "
                    + "ModifiedDate = Now() "
                    + "Where idStockTransferOrder = ? ");
            
            pstmtUpdateWeightTransferOrder = con.prepareStatement("update transportweightramp set  "
                    + "Gross = ?, "
                    + "Tare = ?, "
                    //+ "Net = ?, "
                    + "idUser = ?, "
                    + "ModifiedDate = Now() "
                    + "Where idStockTransferOrder = ? ");
      
            this.addPreparedStatement(pstmtUpdateStockTransferOrder);
            this.addPreparedStatement(pstmtUpdateWeightTransferOrder);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveUpdateStockTransferOrderTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int rowsAffected = 0;
        int idUser = 0;
        int idStockTransferOrder = 0;
        String transportCompany = "";
        String truckDriverName = "";
        String truckPlate = "";
        String truckBoxNumber = "";
        String truckBoxPlate = "";
        String crosser = "";
        String typeBox = "";
        String boxFeet = "";
        double tara = 0;
        double bruto = 0;
        //double neto = 0;

        try {
            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            idStockTransferOrder = GetNodeArray().find("idStockTransferOrder").getIntValue();

            if (this.GetNodeArray().existValue("TransportCompany")) {
                transportCompany = GetNodeArray().find("TransportCompany").getStringValue();
            }
            if (this.GetNodeArray().existValue("TruckDriverName")) {
                truckDriverName = GetNodeArray().find("TruckDriverName").getStringValue();
            }
            if (this.GetNodeArray().existValue("TruckPlate")) {
                truckPlate = GetNodeArray().find("TruckPlate").getStringValue();
            }
            if (this.GetNodeArray().existValue("TruckBoxNumber")) {
                truckBoxNumber = GetNodeArray().find("TruckBoxNumber").getStringValue();
            }
            if (this.GetNodeArray().existValue("TruckBoxPlate")) {
                truckBoxPlate = GetNodeArray().find("TruckBoxPlate").getStringValue();
            }
            if (this.GetNodeArray().existValue("Crosser")) {
                crosser = GetNodeArray().find("Crosser").getStringValue();
            }
            if (this.GetNodeArray().existValue("TypeBox")) {
                typeBox = GetNodeArray().find("TypeBox").getStringValue();
            }
            if (this.GetNodeArray().existValue("BoxFeet")) {
                boxFeet = GetNodeArray().find("BoxFeet").getStringValue();
            }
            if (this.GetNodeArray().existValue("Tara")) {
                tara = Double.parseDouble(GetNodeArray().find("Tara").getStringValue());
            }
            if (this.GetNodeArray().existValue("Bruto")) {
                bruto = Double.parseDouble(GetNodeArray().find("Bruto").getStringValue());
            }
            /*if (this.GetNodeArray().existValue("Neto")) {
                neto = Double.parseDouble(GetNodeArray().find("Neto").getStringValue());
            }*/

            //update stock transfer order  status
            pstmtUpdateStockTransferOrder.setString(1, transportCompany);
            pstmtUpdateStockTransferOrder.setString(2, truckDriverName);
            pstmtUpdateStockTransferOrder.setString(3, truckPlate);
            pstmtUpdateStockTransferOrder.setString(4, truckBoxNumber);
            pstmtUpdateStockTransferOrder.setString(5, truckBoxPlate);
            pstmtUpdateStockTransferOrder.setString(6, crosser);
            pstmtUpdateStockTransferOrder.setString(7, typeBox);
            pstmtUpdateStockTransferOrder.setString(8, boxFeet);
            pstmtUpdateStockTransferOrder.setInt(9, idStockTransferOrder);
            rowsAffected = pstmtUpdateStockTransferOrder.executeUpdate();

            if (rowsAffected > 0) {
                //Updated Weighed 
                pstmtUpdateWeightTransferOrder.setDouble(1, bruto);
                pstmtUpdateWeightTransferOrder.setDouble(2, tara);
                //pstmtUpdateWeightTransferOrder.setDouble(3, neto);
                pstmtUpdateWeightTransferOrder.setInt(3, idUser);
                pstmtUpdateWeightTransferOrder.setInt(4, idStockTransferOrder);
                rowsAffected = pstmtUpdateWeightTransferOrder.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "Embarque Actualizado Exitosamente.");
                    resultArray.add("RESPONSE_DETAIL", "");
                } else {
                    conn.rollback();
                    resultArray.add("error", "El Embarque no pudo ser actualizado.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "El Peso no pudo ser actualizado.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            } else {
                conn.rollback();
                resultArray.add("error", "El Embarque no pudo ser actualizado.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "El Peso no pudo ser actualizado.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveUpdateStockTransferOrderTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveUpdateStockTransferOrderTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveUpdateStockTransferOrderTransaction");

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
            SaveUpdateStockTransferOrderTransaction transaction = new SaveUpdateStockTransferOrderTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveUpdateStockTransferOrderTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveUpdateStockTransferOrderTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveUpdateStockTransferOrderTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveUpdateStockTransferOrderTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveUpdateStockTransferOrderTransaction - No results were returned.");
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
            System.out.println("SaveUpdateStockTransferOrderTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

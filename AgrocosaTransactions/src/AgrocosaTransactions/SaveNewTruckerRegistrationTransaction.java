/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveNewTruckerRegistrationTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtInsertStockTransferOrder;
    protected PreparedStatement pstmtInsertTransportEvaluation;
    protected PreparedStatement pstmtInsertTransportWeight;
    
    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveNewTruckerRegistrationTransaction() {
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
            "transportCompanyName",
            "truckBoxNumber",
            "truckBoxPlate",
            "truckPlate",
            "truckDriverName",
            "crosser",
            "typeBox",
            "boxFeet"
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
                System.out.println("<SaveNewStockTransferOrderTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveNewStockTransferOrderTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveNewStockTransferOrderTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveNewStockTransferOrderTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtInsertStockTransferOrder = con.prepareStatement("insert into stocktransferorder ( "
                    + "TransportCompany, "
                    + "TruckDriverName, "
                    + "TruckPlate, "
                    + "TruckBoxNumber, "
                    + "TruckBoxPlate, "
                    + "Crosser, "
                    + "TypeBox, "
                    + "BoxFeet, "
                    + "Proforma, "
                    + "Status, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active "
                    + ") values("
                    + "?, " //1 TransportCompany
                    + "?, " //2 TruckDriverName
                    + "?, " //3 TruckPlate
                    + "?, " //4 TruckBoxNumber
                    + "?, " //5 TruckBoxPlate
                    + "?, " // 6 Crosser
                    + "?, " // 7 TypeBox
                    + "?, " // 8 BoxFeet
                    + "null," // Proforma
                    + "'Nueva', " //Status
                    + "Now(), " // InsertDate
                    + "Now(), " // ModifiedDate
                    + "1 " // Active
                    + ")", Statement.RETURN_GENERATED_KEYS
            );
            
            pstmtInsertTransportEvaluation = con.prepareStatement("INSERT INTO transportevaluation ("
                    + "idStockTransferOrder, "
                    + "Evaluate, "
                    + "InsertDate, "
                    + "ModifiedDate "
                    + ") VALUES ("
                    + "?, " //1 idStockTransferOrder
                    + "b'0', " // Evaluate
                    + "Now(), " // InsertDate
                    + "Now()" // ModifiedDate
                    + ")"
            );
            
            pstmtInsertTransportWeight = con.prepareStatement("INSERT INTO transportweightramp ("
                    + "idStockTransferOrder, "
                    + "Weighed, "
                    + "InsertDate, "
                    + "ModifiedDate "
                    + ") VALUES ("
                    + "?, " // 1 idStockTransferOrder
                    + "b'0', " // Weighed
                    + "Now(), " // InsertDate
                    + "Now()" // ModifiedDate
                    + ")" 
            );
                    
            this.addPreparedStatement(pstmtInsertStockTransferOrder);
            this.addPreparedStatement(pstmtInsertTransportEvaluation);
            this.addPreparedStatement(pstmtInsertTransportWeight);
            
            return true;
        } catch (SQLException e) {
            System.out.println("SaveNewStockTransferOrderTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int rowsAffected = 0;
        String transportCompanyName = "";
        String truckDriverName = "";
        String truckPlate = "";
        String result = "";
        String truckBoxNumber = "";
        String truckBoxPlate = "";
        int idStockTransferOrder = 0;
        String crosser = "";
        String typeBox = "";
        String boxFeet = "";
       
        try {
            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            transportCompanyName = GetNodeArray().find("transportCompanyName").getStringValue();
            truckBoxNumber = GetNodeArray().find("truckBoxNumber").getStringValue();
            truckBoxPlate = GetNodeArray().find("truckBoxPlate").getStringValue();
            truckPlate = GetNodeArray().find("truckPlate").getStringValue();
            truckDriverName = GetNodeArray().find("truckDriverName").getStringValue();
            crosser = GetNodeArray().find("crosser").getStringValue();
            typeBox = GetNodeArray().find("typeBox").getStringValue();
            boxFeet = GetNodeArray().find("boxFeet").getStringValue();

            //insert stocktransferorder
            pstmtInsertStockTransferOrder.setString(1, transportCompanyName);
            pstmtInsertStockTransferOrder.setString(2, truckDriverName);
            pstmtInsertStockTransferOrder.setString(3, truckPlate);
            pstmtInsertStockTransferOrder.setString(4, truckBoxNumber);
            pstmtInsertStockTransferOrder.setString(5, truckBoxPlate);
            pstmtInsertStockTransferOrder.setString(6, crosser);
            pstmtInsertStockTransferOrder.setString(7, typeBox);
            pstmtInsertStockTransferOrder.setString(8, boxFeet);
            
            rowsAffected = pstmtInsertStockTransferOrder.executeUpdate();
            if (rowsAffected > 0) {
                
                rset = pstmtInsertStockTransferOrder.getGeneratedKeys();
                if(rset.next()){
                    idStockTransferOrder = rset.getInt(1);
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
                
                // Registrar la fila correspondiente donde se registrara la evaluacion del camion
                pstmtInsertTransportEvaluation.setInt(1, idStockTransferOrder);
                rowsAffected = pstmtInsertTransportEvaluation.executeUpdate();
                
                if(rowsAffected > 0){
                    pstmtInsertTransportWeight.setInt(1, idStockTransferOrder);
                    rowsAffected = pstmtInsertTransportWeight.executeUpdate();
                    
                    if(rowsAffected > 0){
                        result += "1";
                    }else{
                        result += "0";
                    }
                } else {
                    result += "0";
                }
            } else {
                result += "0";
            }

            if (!result.contains("0")) {
                if (rowsAffected > 0) {
                    conn.commit();
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "Embarque registrado exitosamente.");
                    resultArray.add("RESPONSE_DETAIL", "");
                } else {
                    conn.rollback();
                    resultArray.add("error", "El Embarque no pudo ser registrado.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "El Embarque no pudo ser registrado.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            } else {
                conn.rollback();
                resultArray.add("error", "El Embarque no pudo ser registrado.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "El Embarque no pudo ser registrado.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveNewStockTransferOrderTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveNewStockTransferOrderTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveNewStockTransferOrderTransaction");

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
            SaveNewStockTransferOrderTransaction transaction = new SaveNewStockTransferOrderTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveNewStockTransferOrderTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveNewStockTransferOrderTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveNewStockTransferOrderTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveNewStockTransferOrderTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveNewStockTransferOrderTransaction - No results were returned.");
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
            System.out.println("SaveNewStockTransferOrderTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
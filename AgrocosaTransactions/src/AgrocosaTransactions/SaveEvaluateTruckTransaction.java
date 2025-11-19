/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveEvaluateTruckTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtUpdateTruckerRegistration;
    
    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveEvaluateTruckTransaction() {
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
            "idStockTransferOrder",
            "truckBoxCleaning",
            "maintenanceConditions",
            "openWindows",
            "sealedPallets",
            "pestFree",
            "smellStrangeFree",
            "label",
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
                System.out.println("<SaveEvaluateTruckTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveEvaluateTruckTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveEvaluateTruckTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveEvaluateTruckTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtUpdateTruckerRegistration = con.prepareStatement("update transportevaluation set "
                    + "TruckBoxCleaning = ?, "
                    + "MaintenanceConditions = ?, "
                    + "OpenWindows = ?, "
                    + "SealedPallets = ?, "
                    + "PestFree = ?, "
                    + "SmellStrangeFree = ?, "
                    + "Label = ?, "
                    + "Evaluate = 1, "
                    + "idUser = ? "
                    + "where idStockTransferOrder = ? ");
            
            this.addPreparedStatement(pstmtUpdateTruckerRegistration);
            
            return true;
        } catch (SQLException e) {
            System.out.println("SaveEvaluateTruckTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String result = "";
        int idStockTransferOrder = 0;
        int idUser = 0;
        String truckBoxCleaning = null;
        String maintenanceConditions = null;
        String openWindows = null;
        String sealedPallets = null;
        String pestFree = null;
        String smellStrangeFree = null;
        String label = null;

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idStockTransferOrder = GetNodeArray().find("idStockTransferOrder").getIntValue();
            idUser = GetNodeArray().find("idUser").getIntValue();
            
            if (this.GetNodeArray().existValue("truckBoxCleaning")) {
                truckBoxCleaning = GetNodeArray().find("truckBoxCleaning").getStringValue();
            }
            if (this.GetNodeArray().existValue("maintenanceConditions")) {
                maintenanceConditions = GetNodeArray().find("maintenanceConditions").getStringValue();
            }
            if (this.GetNodeArray().existValue("openWindows")) {
                openWindows = GetNodeArray().find("openWindows").getStringValue();
            }
            if (this.GetNodeArray().existValue("sealedPallets")) {
                sealedPallets = GetNodeArray().find("sealedPallets").getStringValue();
            }
            if (this.GetNodeArray().existValue("pestFree")) {
                pestFree = GetNodeArray().find("pestFree").getStringValue();
            }
            if (this.GetNodeArray().existValue("smellStrangeFree")) {
                smellStrangeFree = GetNodeArray().find("smellStrangeFree").getStringValue();
            }
            if (this.GetNodeArray().existValue("label")) {
                label = GetNodeArray().find("label").getStringValue();
            }

            pstmtUpdateTruckerRegistration.setString(1, truckBoxCleaning);
            pstmtUpdateTruckerRegistration.setString(2, maintenanceConditions);
            pstmtUpdateTruckerRegistration.setString(3, openWindows);
            pstmtUpdateTruckerRegistration.setString(4, sealedPallets);
            pstmtUpdateTruckerRegistration.setString(5, pestFree);
            pstmtUpdateTruckerRegistration.setString(6, smellStrangeFree);
            pstmtUpdateTruckerRegistration.setString(7, label);
            pstmtUpdateTruckerRegistration.setInt(8, idUser);
            pstmtUpdateTruckerRegistration.setInt(9, idStockTransferOrder);
            rowsAffected = pstmtUpdateTruckerRegistration.executeUpdate();
            
            if (rowsAffected > 0) {
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
            }

            if (!result.contains("0")) {
                if (rowsAffected > 0) {
                    conn.commit();
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "Evaluacion registrada exitosamente.");
                    resultArray.add("RESPONSE_DETAIL", "");
                } else {
                    conn.rollback();
                    resultArray.add("error", "La Evaluacion no pudo ser registrada.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "La Evaluacion no pudo ser registrada.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            } else {
                conn.rollback();
                resultArray.add("error", "La Evaluacion no pudo ser registrada.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "La Evaluacion no pudo ser registrada.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveEvaluateTruckTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveEvaluateTruckTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.SaveEvaluateTruckTransaction");
        nodeArr.add("idUser", "1");
        nodeArr.add("idStockTransferOrder", "10");
        nodeArr.add("proforma", "190001");

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
            SaveEvaluateTruckTransaction transaction = new SaveEvaluateTruckTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.SaveEvaluateTruckTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveEvaluateTruckTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveEvaluateTruckTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.SaveEvaluateTruckTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveEvaluateTruckTransaction - No results were returned.");
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
            System.out.println("SaveEvaluateTruckTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
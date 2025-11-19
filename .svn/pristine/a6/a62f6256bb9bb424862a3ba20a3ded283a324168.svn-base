/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveVegetalNutritionTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtUpdate;
    protected PreparedStatement pstmtInsert;
    protected PreparedStatement pstmtValidate;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveVegetalNutritionTransaction() {
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
            "idFormulaDetail",
            "idFormula"
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
                System.out.println("<SaveVegetalNutritionTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveVegetalNutritionTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveVegetalNutritionTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveVegetalNutritionTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelect = con.prepareStatement(
                    "SELECT idSidconfig, Value "
                    + " FROM sidconfiguration "
                    + " where Tag = 'NutricionVegetal' "
                    + " and Active = 1 "
                    + " Order by Value");

            pstmtValidate = con.prepareStatement(""
                    + "select * "
                    + "from vegetalnutrition "
                    + "where idFormulaDetail = ? "
                    + "and active = 1 ");

            pstmtInsert = con.prepareStatement("Insert into vegetalnutrition ( "
                    + "idFormulaDetail, "
                    + "VegetalNutrition, "
                    + "Portion, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active "
                    + ")Values( "
                    + "?, " //1 idFormulaDetail
                    + "?, " //2 VegetalNutrition
                    + "?, " //3 Portion
                    + "?, " //4 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate                    
                    + "1 " //  Active                    
                    + ")");

            pstmtUpdate = con.prepareStatement("Update vegetalnutrition set "
                    + "Active = 0, "
                    + "ModifiedDate = Now(), "
                    + "idUser = ? " //1 idUser
                    + "where idFormulaDetail = ? " //2 idFormulaDetail
                    + "and Active = 1");

            this.addPreparedStatement(pstmtSelect);
            this.addPreparedStatement(pstmtValidate);
            this.addPreparedStatement(pstmtInsert);
            this.addPreparedStatement(pstmtUpdate);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveVegetalNutritionTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        ResultSet rset = null;
        xmlNodeArray resultArray = null;
        int idUser = 0;
        int idFormulaDetail = 0;
        int rowsAffected = 0;
        boolean boolExist = false;
        String param = "";
        double portion = 0;
        String idFormula = "";

        try {
            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            idFormulaDetail = GetNodeArray().find("idFormulaDetail").getIntValue();
            idFormula = GetNodeArray().find("idFormula").getStringValue();

            //validate if exist
            pstmtValidate.setInt(1, idFormulaDetail);
            rset = pstmtValidate.executeQuery();
            if (rset.next()) {
                boolExist = true;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (boolExist) {
                //Update - Active = 0
                pstmtUpdate.setInt(1, idUser);
                pstmtUpdate.setInt(2, idFormulaDetail);
                pstmtUpdate.executeUpdate();
            }

            rset = pstmtSelect.executeQuery();
            while (rset != null && rset.next()) {
                param = rset.getString("Value");
                if (this.GetNodeArray().existValue(param)) {
                    portion = GetNodeArray().find(param).getDoubleValue();
                    //insert
                    pstmtInsert.setInt(1, idFormulaDetail);
                    pstmtInsert.setString(2, param);
                    pstmtInsert.setDouble(3, portion);
                    pstmtInsert.setInt(4, idUser);
                    rowsAffected = pstmtInsert.executeUpdate();
                }else{
                   //insert
                    pstmtInsert.setInt(1, idFormulaDetail);
                    pstmtInsert.setString(2, param);
                    pstmtInsert.setDouble(3, 0);
                    pstmtInsert.setInt(4, idUser);
                    rowsAffected = pstmtInsert.executeUpdate();  
                }
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

           

            //commit
            if (rowsAffected > 0) {
                conn.commit();
                resultArray.add("idFormula", idFormula);
                resultArray.add("idFormulaDetail", idFormulaDetail);
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "La concentración de los elementos ha sido registrada.");
                resultArray.add("RESPONSE_DETAIL", "");
            } else {
                conn.rollback();
                resultArray.add("error", "La concentración de los elementos no ha sido regisstrada.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "La concentración de los elementos no ha sido regisstrada.");
                resultArray.add("RESPONSE_DETAIL", "");
            }
            return resultArray;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveVegetalNutritionTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveVegetalNutritionTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.SaveVegetalNutritionTransaction");

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
            SaveVegetalNutritionTransaction transaction = new SaveVegetalNutritionTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.SaveVegetalNutritionTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveVegetalNutritionTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveVegetalNutritionTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.SaveVegetalNutritionTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveVegetalNutritionTransaction - No results were returned.");
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
            System.out.println("SaveVegetalNutritionTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveSelectedSupervisorTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtUpdate;
    protected PreparedStatement pstmtInsert;
    protected PreparedStatement pstmtValidateExist;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveSelectedSupervisorTransaction() {
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
            "idSupervisor",
            "idCamp"
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
                System.out.println("<SaveSelectedSupervisorTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveSelectedSupervisorTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveSelectedSupervisorTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveSelectedSupervisorTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtValidateExist = con.prepareStatement("select * "
                    + "from campinfo "
                    + "where idCamp = ?");

            pstmtUpdate = con.prepareStatement("Update campinfo set "
                    + "idSupervisor = ?, " //1 idSupervisor
                    + "ModifiedDate = Now(), "
                    + "idUser = ? " //2 idUser
                    + "where idCamp = ? " //3 idCamp
                    + "and Active = 1");

            pstmtInsert = con.prepareStatement("Insert into campinfo ( "
                    + "idCamp, "
                    + "idSupervisor, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active "
                    + ")Values( "
                    + "?, " //1 idCamp
                    + "?, " //2 idSupervisor
                    + "?, " //3 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate                    
                    + "1 " //  Active                    
                    + ")");

            this.addPreparedStatement(pstmtValidateExist);
            this.addPreparedStatement(pstmtInsert);
            this.addPreparedStatement(pstmtUpdate);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveSelectedSupervisorTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int idSupervisor = 0;
        int idCamp = 0;
        int rowsAffected = 0;
        boolean boolExist = false;

        try {
            
            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            idSupervisor = GetNodeArray().find("idSupervisor").getIntValue();
            idCamp = GetNodeArray().find("idCamp").getIntValue();

            resultArray.add("SelectedSupervisor", idSupervisor);

            //validate camp
            pstmtValidateExist.setInt(1, idCamp);
            rset = pstmtValidateExist.executeQuery();
            if (rset.next()) {
                boolExist = true;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (boolExist) {
                
                //Update
                pstmtUpdate.setInt(1, idSupervisor);
                pstmtUpdate.setInt(2, idUser);
                pstmtUpdate.setInt(3, idCamp);
                rowsAffected = pstmtUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    resultArray.add("idCamp", idCamp);
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "El Supervisor ha sido registrado al campo de cultivo.");
                    resultArray.add("RESPONSE_DETAIL", "");
                } else {
                    conn.rollback();
                    resultArray.add("error", "El Supervisor no pudo ser registrado al campode cultivo.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "El Supervisor no pudo ser registrado al campode cultivo.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            } else {
                //Insert 
                pstmtInsert.setInt(1, idCamp);
                pstmtInsert.setInt(2, idSupervisor);
                pstmtInsert.setInt(3, idUser);
                rowsAffected = pstmtInsert.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    resultArray.add("idCamp", idCamp);
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "El Supervisor ha sido registrado al campo de cultivo.");
                    resultArray.add("RESPONSE_DETAIL", "");
                } else {
                    conn.rollback();
                    resultArray.add("error", "El Supervisor no pudo ser registrado al campo de cultivo.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "El Supervisor no pudo ser registrado al campo de cultivo.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            }

            return resultArray;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveSelectedSupervisorTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveSelectedSupervisorTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.SaveSelectedSupervisorTransaction");
        nodeArr.add("idUser", "1");
        nodeArr.add("idSupervisor", "3");
        nodeArr.add("idCamp", "1");
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
            SaveSelectedSupervisorTransaction transaction = new SaveSelectedSupervisorTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.SaveSelectedSupervisorTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveSelectedSupervisorTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveSelectedSupervisorTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.SaveSelectedSupervisorTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveSelectedSupervisorTransaction - No results were returned.");
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
            System.out.println("SaveSelectedSupervisorTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

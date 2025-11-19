package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class PestManagementTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtUpdate;
    protected PreparedStatement pstmtDelete;
    protected PreparedStatement pstmtInsert;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public PestManagementTransaction() {
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
        String[] mandatoryTags = {};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<PestManagementTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<PestManagementTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<PestManagementTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<PestManagementTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
     * @return <B>true</B> for successful preparation;
     * <B>false</B> for unsuccessful preparation
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
                    " select p.idPest, "
                    + " p.PestName, "
                    + "p.Description, "
                    + "p.Prevention, "
                    + "p.Medication, "
                    + " concat(u.FirstName,' ',u.LastName) User, "
                    + " date_format(p.InsertDate,'%d/%m/%Y %h:%i %p') as InsertDate, "
                    + " date_format(p.ModifiedDate,'%d/%m/%Y %h:%i %p') as ModifiedDate "
                    + " from pest p inner join "
                    + " user u on u.idUser = p.idUser "
                    + " Where p.Active = 1 "
                    + " Order by p.PestName");
            pstmtUpdate = con.prepareStatement("Update pest set "
                    + " PestName = ?, "
                    + " Description = ?, "
                    + " Prevention = ?, "
                    + " Medication = ?, "
                    + " idUser = ?, "
                    + " ModifiedDate = Now() "
                    + " Where idPest = ? ");
            pstmtDelete = con.prepareStatement("Update pest set "
                    + "ModifiedDate = Now(), "
                    + "Active = 0, "
                    + "idUser = ? "
                    + "where idPest = ? ");
            pstmtInsert = con.prepareStatement("Insert into pest ( "
                    + " PestName, "
                    + " Description, "
                    + " Prevention, "
                    + " Medication, "
                    + " idUser, "
                    + " InsertDate, "
                    + " ModifiedDate, "
                    + " Active "
                    + ")Values( "
                    + "?, " //1 PestName
                    + "?, " //2 Description
                    + "?, " //3 Prevention
                    + "?, " //4 Medication
                    + "?, " //5 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate
                    + "1 " //  Active                    
                    + ")");

            this.addPreparedStatement(pstmtSelect);
            this.addPreparedStatement(pstmtUpdate);
            this.addPreparedStatement(pstmtDelete);
            this.addPreparedStatement(pstmtInsert);

            return true;
        } catch (SQLException e) {
            System.out.println("PestManagementTransaction::PrepareStatements> SQLException: " + e.getMessage());
            return false;
        }
    }

    /**
     * executes sql statements using input arguments and returns result
     *
     * @param (none)
     * @return valid node array if successful else null
     * @exception SQLException if sql error occurs
     * @exception Exception if non sql error occurs
     */
    public synchronized xmlNodeArray Execute() throws SQLException, Exception {
        Connection conn = null;
        xmlNodeArray resultArray = null;
        ResultSet rset = null;
        String message = "OK";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();
            xmlNodeArray param = this.GetNodeArray();

            if (param.existValue("Method")) {
                String met = param.find("Method").getStringValue();

                if (met.equals("Save")) {
                    if (param.existValue("ID") && param.find("ID").getStringValue().equals("new")) {
                        //insert new 
                        pstmtInsert.setString(1, param.find("pestName").getStringValue());
                        pstmtInsert.setString(2, param.find("description").getStringValue());
                        pstmtInsert.setString(3, param.find("prevention").getStringValue());
                        pstmtInsert.setString(4, param.find("medication").getStringValue());
                        pstmtInsert.setInt(5, param.find("idUser").getIntValue());
                        pstmtInsert.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "La Plaga ha sido guardada existosamente.";
                    } else if (param.existValue("ID")) {
                        //update existing
                        pstmtUpdate.setString(1, param.find("pestName").getStringValue());
                        pstmtUpdate.setString(2, param.find("description").getStringValue());
                        pstmtUpdate.setString(3, param.find("prevention").getStringValue());
                        pstmtUpdate.setString(4, param.find("medication").getStringValue());
                        pstmtUpdate.setInt(5, param.find("idUser").getIntValue());
                        pstmtUpdate.setString(6, param.find("ID").getStringValue());
                        pstmtUpdate.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "La plaga ha sido actualizada exitosamente.";
                    } else {
                        resultArray.add("RESPONSE_CODE", "FAIL");
                        resultArray.add("RESPONSE_MESSAGE", "row ID was not provided");
                        resultArray.add("RESPONSE_DETAIL", "");
                        return resultArray;
                    }
                } else if (met.equals("Delete")) {
                    if (param.existValue("ID") && param.find("ID").getIntValue() > 0) {
                        //delete 
                        pstmtDelete.setInt(1, param.find("idUser").getIntValue());
                        pstmtDelete.setInt(2, param.find("ID").getIntValue());
                        pstmtDelete.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "La plaga ha sido borrada exitosamente.";
                    }
                }
                conn.commit();
            }
            rset = pstmtSelect.executeQuery();
            xmlTable tab = formatDataTable(rset);
            if (param.existValue("Method")) {
                String met = param.find("Method").getStringValue();
                if (met.equals("Add")) {
                    tab.addRow();
                    for (int x = 1; x < tab.getFieldsQty(); x++) {
                        tab.setValue(tab.getRowsQty() - 1, x, "");
                    }
                    tab.setValue(tab.getRowsQty() - 1, 0, "new");
                }
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("PestInfo_Table", tab);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("<PestManagementTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            e.printStackTrace();
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("<PestManagementTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception");
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            ex.printStackTrace();
            return resultArray;
        } finally {
            if (rset != null) {
                rset.close();
                rset = null;
            }
            CloseStatements();

        }
    }

    /**
     * Generates an xmlNodeArray containing parameters for this transaction
     *
     * @param (none)
     * @return xmlNodeArray that contains parameters for the transaction
     * @exception (none)
     */
    public xmlNodeArray GenerateTestParameters() {
        xmlNodeArray nodeArr = new xmlNodeArray();
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.PestManagementTransaction");
        nodeArr.add("Method", "Add");
        return nodeArr;
    }

    /**
     * The main method for the transaction. Creates a database connection and an
     * error Array, then executes the transaction and reports any errors
     *
     * @param argv argv[0] is an optional configuration file name
     * @return (none)
     * @exception (none)
     */
    public static void main(String[] argv) {
        try {
            PestManagementTransaction transaction = new PestManagementTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.PestManagementTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" PestManagementTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" PestManagementTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.PestManagementTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("PestManagementTransaction - No results were returned.");
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
            System.out.println("PestManagementTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

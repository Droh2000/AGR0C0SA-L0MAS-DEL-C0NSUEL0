package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveBinHistoryReportTransaction extends SIDWebTransaction {

    protected Statement pstmtSelect;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveBinHistoryReportTransaction() {
        super();
        SetTransactionType(SIDWebTransaction.RetrieveType);
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
                System.out.println("<RetrieveBinHistoryReportTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveBinHistoryReportTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveBinHistoryReportTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveBinHistoryReportTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
//            pstmtSelect = con.prepareStatement("select *, "
//                    + "date_format(InsertDate,'%d/%m/%Y %h:%i %p') as fInsertDate "
//                    + "from bininventorymovement "
//                    + "where idTask like ? "
//                    + "and MovementType like ? "
//                    + "and CropTypeName like ? "
//                    + "and CropName like ? "
//                    + "and StorageName like ? "
//                    + "and SubstorageName like ? "
//                    + "Order by InsertDate");
            pstmtSelect = con.createStatement();

//            this.addPreparedStatement(pstmtSelect);
            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveBinHistoryReportTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        xmlNodeArray resultArray = null;
        ResultSet rset = null;
        String message = "OK";
        String movementType = "%";
        String storageName = "%";
        String substorageName = "%";
        String cropTypeName = "%";
        String cropName = "%";
        String idTask = "";
        String qry = "";
        String startDate = "";
        String endDate = "";
        
        try {

            resultArray = new xmlNodeArray();

            qry = "select *, "
                    + "date_format(InsertDate,'%d/%m/%Y %h:%i %p') as fInsertDate "
                    + "from bininventorymovement "
                    + "where Active = 1 ";

            if (this.GetNodeArray().existValue("idTask")) {
                idTask = GetNodeArray().find("idTask").getStringValue();
                resultArray.add("SelectedIdTask", idTask);
                
                qry += " and idTask = " + idTask + " ";
            }

            if (this.GetNodeArray().existValue("movementType")) {
                movementType = GetNodeArray().find("movementType").getStringValue();
                resultArray.add("SelectedMovementType", movementType);
                
                qry += " and MovementType = '" + movementType + "' ";
            }

            if (this.GetNodeArray().existValue("cropTypeName")) {
                cropTypeName = GetNodeArray().find("cropTypeName").getStringValue();
                resultArray.add("SelectedCropTypeName", cropTypeName);
                
                qry += " and CropTypeName = '" + cropTypeName + "' ";
            }

            if (this.GetNodeArray().existValue("cropName")) {
                cropName = GetNodeArray().find("cropName").getStringValue();
                resultArray.add("SelectedCropName", cropName);
                
                qry += " and CropName = '" + cropName + "' ";
            }

            if (this.GetNodeArray().existValue("storageName")) {
                storageName = GetNodeArray().find("storageName").getStringValue();
                resultArray.add("SelectedStorageName", storageName);
                
                qry += " and StorageName = '" + storageName + "' ";
            }

            if (this.GetNodeArray().existValue("substorageName")) {
                substorageName = GetNodeArray().find("substorageName").getStringValue();
                resultArray.add("SelectedSubstorageName", substorageName);
                
                qry += " and SubstorageName = '" + substorageName + "' ";
            }
            
            if (this.GetNodeArray().existValue("startDate") && this.GetNodeArray().existValue("endDate")) {
                startDate = GetNodeArray().find("startDate").getStringValue();
                endDate = GetNodeArray().find("endDate").getStringValue();
                resultArray.add("SelectedStartDate", startDate);
                resultArray.add("SelectedEndDate", endDate);
                qry += " and InsertDate between STR_TO_DATE('" + startDate + " 00:00','%d/%m/%Y %H:%i') "
                        + "and STR_TO_DATE('" + endDate + " 23:59','%d/%m/%Y %H:%i') ";
            }
            
            qry += " Order by InsertDate";
            
            //inventory
            rset = pstmtSelect.executeQuery(qry);
            xmlTable guardTable = this.formatDataTable(rset);
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("BinHistory_Table", guardTable);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("<RetrieveBinHistoryReportTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("<RetrieveBinHistoryReportTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception");
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
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
     * @return xmlNodeArray that contains parameters for the transaction
     * @exception (none)
     */
    @Override
    public xmlNodeArray GenerateTestParameters() {
        xmlNodeArray nodeArr = new xmlNodeArray();
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveBinHistoryReportTransaction");
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
            RetrieveBinHistoryReportTransaction transaction = new RetrieveBinHistoryReportTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveBinHistoryReportTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveBinHistoryReportTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveBinHistoryReportTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveBinHistoryReportTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveBinHistoryReportTransaction - No results were returned.");
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
            System.out.println("RetrieveBinHistoryReportTransaction::main> caught exception " + e.getMessage());
        }
    }
}

package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveTaskInProcessTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveTaskInProcessTransaction() {
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
        String[] mandatoryTags = {"idUser", "taskType"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveTaskInProcessTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveTaskInProcessTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveTaskInProcessTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveTaskInProcessTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelect = con.prepareStatement("select task.idTask, "
                    + "task.TaskType, "
                    + "taskhistory.CampName, "
                    + "taskhistory.SectionName, "
                    + "IFNULL(taskhistory.LaborTypeName,'') as LaborTypeName, "
                    + "IFNULL(taskhistory.SectionCrop,'') as SectionCrop, "
                    + "date_format(task.TaskDate,'%d/%m/%Y') as TaskDate, "
                    + "IFNULL(task.Pressure,'') as Pressure, "
                    + "IFNULL(task.Formula,'') as Formula, "
                    + "task.Comments, "
                    + "task.Status, "
                    + "date_format(task.InsertDate,'%d/%m/%Y') as InsertDate, "
                    + "IFNULL(task.Manager,'') as Manager, "
                    + "task.WorkersQty, "
                    + "IFNULL(task.Temperature,'') as Temperature, "
                    + "taskhistory.User, "
                    + "taskhistory.SupervisorName "
                    + "from task inner join "
                    + "taskhistory on taskhistory.idTask = task.idTask inner join "
                    + "campinfo on campinfo.idCamp = campinfo.idCamp "
                    + "where taskhistory.Status = 'Nueva' "
                    + "and task.Active = 1 "
                    + "and task.Status in ('En Proceso') "                    
                    + "and task.idSupervisor = ? "
                    + "and task.TaskType = ? "
                    + "Group by task.idTask, "
                    + "task.TaskType, "
                    + "taskhistory.CampName, "
                    + "taskhistory.SectionName, "
                    + "IFNULL(taskhistory.LaborTypeName,''), "
                    + "IFNULL(taskhistory.SectionCrop,''), "
                    + "date_format(task.TaskDate,'%d/%m/%Y'), "
                    + "IFNULL(task.Pressure,''), "
                    + "IFNULL(task.Formula,''), "
                    + "task.Comments, "
                    + "task.Status, "
                    + "date_format(task.InsertDate,'%d/%m/%Y'), "
                    + "IFNULL(task.Manager,''), "
                    + "task.WorkersQty, "
                    + "IFNULL(task.Temperature,''), "
                    + "taskhistory.User, "
                    + "taskhistory.SupervisorName "
                    + "Order by idTask");
            
            this.addPreparedStatement(pstmtSelect);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveTaskInProcessTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int idUser = 0;
       String taskType = "";
       
        try {

            resultArray = new xmlNodeArray();
            idUser = GetNodeArray().find("idUser").getIntValue();
            taskType = GetNodeArray().find("taskType").getStringValue();
            
            pstmtSelect.setInt(1, idUser);
            pstmtSelect.setString(2, taskType);
            rset = pstmtSelect.executeQuery();
            xmlTable reportTable = this.formatDataTable(rset);
            resultArray.add("TaskReport_Table", reportTable);

            if (rset != null) {
                rset.close();
                rset = null;
            }

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("RetrieveTaskInProcessTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveTaskInProcessTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveTaskInProcessTransaction");
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
            RetrieveTaskInProcessTransaction transaction = new RetrieveTaskInProcessTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveTaskInProcessTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveTaskInProcessTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveTaskInProcessTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveTaskInProcessTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveTaskInProcessTransaction - No results were returned.");
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
            System.out.println("RetrieveTaskInProcessTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

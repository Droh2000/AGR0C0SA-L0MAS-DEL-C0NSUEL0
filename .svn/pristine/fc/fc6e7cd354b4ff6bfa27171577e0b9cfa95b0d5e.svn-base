package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveTaskCommentsTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtUserRole;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveTaskCommentsTransaction() {
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
        String[] mandatoryTags = {"idUser"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveTaskCommentsTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveTaskCommentsTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveTaskCommentsTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveTaskCommentsTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtUserRole = con.prepareStatement("select "
                    + "user.idUser, "
                    + "role.Role "
                    + "from user inner join "
                    + "role on role.idRole = user.idRole "
                    + "Where user.idUser = ? "
                    + "and user.Active = 1");
            
                    
            pstmtSelect = con.prepareStatement("SELECT "
                    + "task.idTask, "
                    + "task.TaskType, "
                    + "camp.CampName, "
                    + "date_format(task.TaskDate,'%d/%m/%Y %h:%i') as TaskDate, "
                    + "concat(supervisor.FirstName,' ',supervisor.LastName) Supervisor, "
                    + "taskcomment.Comments, "
                    + "date_format(taskcomment.InsertDate,'%d/%m/%Y %h:%i') as CommentsDate, "
                    + "role.Role  "
                    + "FROM task inner join  "
                    + "taskcomment on taskcomment.idTask = task.idTask inner join "
                    + "camp on camp.idCamp = task.idCamp inner join "
                    + "user supervisor on supervisor.idUser = task.idSupervisor inner join  "
                    + "role on role.idRole = supervisor.idRole "
                    + "Where Status = 'En Proceso' "
                    + "and role.Role like ? "
                    + "Order by idTask, taskcomment.InsertDate");

            this.addPreparedStatement(pstmtUserRole);
            this.addPreparedStatement(pstmtSelect);
            
            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveTaskCommentsTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String role = "%";


        try {

            resultArray = new xmlNodeArray();
            
            idUser = GetNodeArray().find("idUser").getIntValue();
            
            pstmtUserRole.setInt(1, idUser);
            rset = pstmtUserRole.executeQuery();
            if(rset.next()){
                role = rset.getString("Role").equals("Administrador") ? "%": rset.getString("Role");
            }
            if(rset != null){
                rset.close();
                rset = null;                        
            }
                    
            pstmtSelect.setString(1, role);
            rset = pstmtSelect.executeQuery();
            xmlTable reportTable = this.formatDataTable(rset);
            resultArray.add("TaskComments_Table", reportTable);

            if (rset != null) {
                rset.close();
                rset = null;
            }

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("RetrieveTaskCommentsTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveTaskCommentsTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveTaskCommentsTransaction");
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
            RetrieveTaskCommentsTransaction transaction = new RetrieveTaskCommentsTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveTaskCommentsTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveTaskCommentsTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveTaskCommentsTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveTaskCommentsTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveTaskCommentsTransaction - No results were returned.");
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
            System.out.println("RetrieveTaskCommentsTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

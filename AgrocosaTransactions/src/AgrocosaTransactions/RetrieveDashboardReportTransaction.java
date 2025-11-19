package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import java.text.DecimalFormat;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveDashboardReportTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectTask;
    protected PreparedStatement pstmtSelectPendingTask;
    protected PreparedStatement pstmtTaskByCamp;
    protected PreparedStatement pstmtTaskBySection;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveDashboardReportTransaction() {
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
                System.out.println("<RetrieveDashboardReportTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveDashboardReportTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveDashboardReportTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveDashboardReportTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelectTask = con.prepareStatement("select Status,count(*) as Qty "
                    + "from task "
                    + "where TaskType = ? "
                    + "Group by status "
                    + "Order by status ");

            pstmtSelectPendingTask = con.prepareStatement("select count(*) as Qty "
                    + "from task "
                    + "where TaskType = ? "
                    + "and Status in ('Nueva','Atendida','En Progreso')");
            
            pstmtTaskByCamp = con.prepareStatement("select "
                    + "task.tasktype, "
                    + "task.Status, "
                    + "camp.CampName, "
                    + "count(*) as Qty "
                    + "from task inner join "
                    + "camp on camp.idCamp = task.idCamp inner join "
                    + "tasksection on tasksection.idTask = task.idTask "
                    + "where task.Status in ('Terminada') "
                    + "and task.tasktype in ('Riego') "
                    + "Group by task.tasktype, task.Status, camp.CampName "
                    + "Order by task.taskType");

            pstmtTaskBySection = con.prepareStatement("select "
                    + "task.tasktype, "
                    + "task.Status, "
                    + "tasksection.SectionName, "
                    + "count(*) as Qty "
                    + "from task inner join "
                    + "camp on camp.idCamp = task.idCamp inner join "
                    + "tasksection on tasksection.idTask = task.idTask "
                    + "where task.Status in ('Terminada') "
                    + "and task.tasktype in ('Riego') "
                    + "Group by task.tasktype, task.Status, tasksection.SectionName "
                    + "Order by task.taskType");

            this.addPreparedStatement(pstmtSelectTask);
            this.addPreparedStatement(pstmtSelectPendingTask);
            this.addPreparedStatement(pstmtTaskByCamp);
            this.addPreparedStatement(pstmtTaskBySection);
            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveDashboardReportTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String status = "";
        double qty = 0;
        double total = 0;
        double perc = 0;
        

        try {

            resultArray = new xmlNodeArray();

            //Aplicacion
            xmlTable applicationTab = new xmlTable();
            applicationTab.addField("TaskType");
            applicationTab.addField("Qty");
            applicationTab.addField("Perc");
            //Total
            pstmtSelectTask.setString(1, "Aplicacion");
            rset = pstmtSelectTask.executeQuery();
            while (rset.next()) {
                total += rset.getDouble("Qty");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            //Info
            pstmtSelectTask.setString(1, "Aplicacion");
            rset = pstmtSelectTask.executeQuery();
            while (rset.next()) {
                status = rset.getString("Status");
                qty = rset.getDouble("Qty");

                applicationTab.addRow();
                applicationTab.setValue(applicationTab.getRowsQty() - 1, "TaskType", status);
                applicationTab.setValue(applicationTab.getRowsQty() - 1, "Qty", qty);
                perc = qty / total;
                DecimalFormat df = new DecimalFormat("####0.00");
                applicationTab.setValue(applicationTab.getRowsQty() - 1, "Perc", df.format(perc));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("ApplicationChart_Table", applicationTab);
            //------------------------------------------------------------------------------------------------
            //Cosecha
            xmlTable pickingTab = new xmlTable();
            pickingTab.addField("TaskType");
            pickingTab.addField("Qty");
            pickingTab.addField("Perc");
            qty = 0;
            total = 0;
            perc = 0;        
            //Total
            pstmtSelectTask.setString(1, "Cosecha");
            rset = pstmtSelectTask.executeQuery();
            while (rset.next()) {
                total += rset.getDouble("Qty");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            //Info
            pstmtSelectTask.setString(1, "Cosecha");
            rset = pstmtSelectTask.executeQuery();
            while (rset.next()) {
                status = rset.getString("Status");
                qty = rset.getDouble("Qty");

                pickingTab.addRow();
                pickingTab.setValue(pickingTab.getRowsQty() - 1, "TaskType", status);
                pickingTab.setValue(pickingTab.getRowsQty() - 1, "Qty", qty);
                perc = qty / total;
                DecimalFormat df = new DecimalFormat("####0.00");
                pickingTab.setValue(pickingTab.getRowsQty() - 1, "Perc", df.format(perc));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("PickingChart_Table", pickingTab);
            //------------------------------------------------------------------------------------------------
            //Jornal
            xmlTable journalTab = new xmlTable();
            journalTab.addField("TaskType");
            journalTab.addField("Qty");
            journalTab.addField("Perc");
            qty = 0;
            total = 0;
            perc = 0;        
            //Total
            pstmtSelectTask.setString(1, "Jornal");
            rset = pstmtSelectTask.executeQuery();
            while (rset.next()) {
                total += rset.getDouble("Qty");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            //Info
            pstmtSelectTask.setString(1, "Jornal");
            rset = pstmtSelectTask.executeQuery();
            while (rset.next()) {
                status = rset.getString("Status");
                qty = rset.getDouble("Qty");

                journalTab.addRow();
                journalTab.setValue(journalTab.getRowsQty() - 1, "TaskType", status);
                journalTab.setValue(journalTab.getRowsQty() - 1, "Qty", qty);
                perc = qty / total;
                DecimalFormat df = new DecimalFormat("####0.00");
                journalTab.setValue(journalTab.getRowsQty() - 1, "Perc", df.format(perc));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("JournalChart_Table", journalTab);
            //------------------------------------------------------------------------------------------------
            //Riego
            xmlTable irrigationTab = new xmlTable();
            irrigationTab.addField("TaskType");
            irrigationTab.addField("Qty");
            irrigationTab.addField("Perc");
            qty = 0;
            total = 0;
            perc = 0;        
            //Total
            pstmtSelectTask.setString(1, "Riego");
            rset = pstmtSelectTask.executeQuery();
            while (rset.next()) {
                total += rset.getDouble("Qty");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            //Info
            pstmtSelectTask.setString(1, "Riego");
            rset = pstmtSelectTask.executeQuery();
            while (rset.next()) {
                status = rset.getString("Status");
                qty = rset.getDouble("Qty");

                irrigationTab.addRow();
                irrigationTab.setValue(irrigationTab.getRowsQty() - 1, "TaskType", status);
                irrigationTab.setValue(irrigationTab.getRowsQty() - 1, "Qty", qty);
                perc = qty / total;
                DecimalFormat df = new DecimalFormat("####0.00");
                irrigationTab.setValue(irrigationTab.getRowsQty() - 1, "Perc", df.format(perc));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("IrrigationChart_Table", irrigationTab);
            //------------------------------------------------------------------------------------------------
            
            
            pstmtSelectPendingTask.setString(1, "Aplicacion");
            rset = pstmtSelectPendingTask.executeQuery();
            if(rset.next()){
                resultArray.add("PendingApplicationQty", rset.getString("Qty"));
            }
            if(rset != null){
                rset.close();
                rset = null;
            }
            
            pstmtSelectPendingTask.setString(1, "Cosecha");
            rset = pstmtSelectPendingTask.executeQuery();
            if(rset.next()){
                resultArray.add("PendingPickingQty", rset.getString("Qty"));
            }
            if(rset != null){
                rset.close();
                rset = null;
            }
            
            pstmtSelectPendingTask.setString(1, "Jornal");
            rset = pstmtSelectPendingTask.executeQuery();
            if(rset.next()){
                resultArray.add("PendingJournalQty", rset.getString("Qty"));
            }
            if(rset != null){
                rset.close();
                rset = null;
            }
            
            pstmtSelectPendingTask.setString(1, "Riego");
            rset = pstmtSelectPendingTask.executeQuery();
            if(rset.next()){
                resultArray.add("PendingIrrigationQty", rset.getString("Qty"));
            }
            if(rset != null){
                rset.close();
                rset = null;
            }
            
            //task by camp
            rset = pstmtTaskByCamp.executeQuery();
            xmlTable campTable = this.formatDataTable(rset);
            resultArray.add("TaskByCamp_Table", campTable);
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            //task by section
            rset = pstmtTaskBySection.executeQuery();
            xmlTable sectionTable = this.formatDataTable(rset);
            resultArray.add("TaskBySection_Table", sectionTable);
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            
            
            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("RetrieveDashboardReportTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveDashboardReportTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveDashboardReportTransaction");
        nodeArr.add("SelectedCamp", "Santa Rosa");
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
            RetrieveDashboardReportTransaction transaction = new RetrieveDashboardReportTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveDashboardReportTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveDashboardReportTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveDashboardReportTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveDashboardReportTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveDashboardReportTransaction - No results were returned.");
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
            System.out.println("RetrieveDashboardReportTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

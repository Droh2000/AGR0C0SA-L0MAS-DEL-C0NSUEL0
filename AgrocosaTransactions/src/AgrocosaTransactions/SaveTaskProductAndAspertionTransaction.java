/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SaveTaskProductAndAspertionTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtTaskInfo;

    protected PreparedStatement pstmtInsertTaskProduct;
    protected PreparedStatement pstmtDeleteTaskProduct;

    protected PreparedStatement pstmtInsertTaskHistory;

    protected PreparedStatement pstmtUser;

    protected PreparedStatement pstmtInsertTaskAspertion;
    protected PreparedStatement pstmtDeleteTaskAspertion;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveTaskProductAndAspertionTransaction() {
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
            "idTask"
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
                System.out.println("<SaveTaskProductAndAspertionTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveTaskProductAndAspertionTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveTaskProductAndAspertionTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveTaskProductAndAspertionTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtTaskInfo = con.prepareStatement("select *, "
                    + "date_format(TaskDate,'%d/%m/%Y') as fTaskDate "
                    + "from taskhistory "
                    + "where idTask = ? "
                    + "and status  = 'Nueva' ");

            pstmtInsertTaskProduct = con.prepareStatement("Insert into taskproduct ( "
                    + "idTask, "
                    + "Lot, "
                    + "StartTime, "
                    + "Valve, "
                    + "EndTime, "
                    + "TaskDate, "
                    + "ComercialName, "
                    + "Caducity, "
                    + "UOM, "
                    + "Ingredient, "
                    + "Dose, "
                    + "Lote, "
                    + "Total, "
                    + "Intervalo, "
                    + "Period, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active "
                    + ")Values( "
                    + "?, "//1 idTask
                    + "?, "//2 Lot
                    + "?, "//3 StartTime
                    + "?, "//4 Valve
                    + "?, "//5 EndTime
                    + "STR_TO_DATE(?,'%d/%m/%Y'), "//6 TaskDate
                    + "?, "//7 ComercialName
                    + "?, "//8 Caducity
                    + "?, "//9 UOM
                    + "?, "//10 Ingredient
                    + "?, "//11 Dose
                    + "?, "//12 Lote
                    + "?, "//13 Total
                    + "?, "//14 Interval
                    + "?, "//15 Period
                    + "?, "//16 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate
                    + "1 " //  Active                    
                    + ")");

            pstmtDeleteTaskProduct = con.prepareStatement("delete "
                    + "from taskproduct "
                    + "where idTask = ?");

            pstmtInsertTaskHistory = con.prepareStatement("insert into taskhistory ( "
                    + "idTask, "
                    + "TaskType, "
                    + "CampName, "
                    + "SectionName, "
                    + "LaborTypeName, "
                    + "SectionCrop, "
                    + "TaskDate, "
                    + "Status, "
                    + "Comments, "
                    + "User, "
                    + "InsertDate, "
                    + "SupervisorName "
                    + ") values( "
                    + "?, "//1 idTask
                    + "?, "//2 TaskTYpe
                    + "?, " //3 CAmpName
                    + "?, " //4 SectionName
                    + "?, " //5 LaborTypeName
                    + "?, " //6 SectionCrop
                    + "STR_TO_DATE(?,'%d/%m/%Y'), " //7 TaskDate
                    + "?, "//8 Status
                    + "?, "//8 Comments
                    + "?, "//10 User
                    + "Now(), "
                    + "? " //11 Supervisor Name
                    + ")");

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            pstmtInsertTaskAspertion = con.prepareStatement("Insert into taskaspertion ( "
                    + "idTask, "
                    + "DayCondition, "
                    + "AirVelocity, "
                    + "Temperature, "
                    + "Direction, "
                    + "Humidity, "
                    + "Beak, "
                    + "AspertionType, "
                    + "Pressure, "
                    + "TractorType, "
                    + "TractorVelocity, "
                    + "Expenditure, "
                    + "Owner, "
                    + "Comments, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active "
                    + ")Values( "
                    + "?, "//1 idTask
                    + "?, "//2 Condition
                    + "?, "//3 AirVelocity
                    + "?, "//4 Temperature
                    + "?, "//5 Direction
                    + "?, "//6 Humidity
                    + "?, "//7 Beak
                    + "?, "//8 AspertionType
                    + "?, "//9 Pressure
                    + "?, "//10 TractorType
                    + "?, "//11 TractorVelocity
                    + "?, "//12 Expenditure
                    + "?, "//13 Owner
                    + "?, "//14 Comments
                    + "?, "//15 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate
                    + "1 " //  Active                    
                    + ")");

            pstmtDeleteTaskAspertion = con.prepareStatement("delete "
                    + "from taskaspertion "
                    + "where idTask = ?");

            this.addPreparedStatement(pstmtTaskInfo);
            this.addPreparedStatement(pstmtInsertTaskProduct);
            this.addPreparedStatement(pstmtDeleteTaskProduct);
            this.addPreparedStatement(pstmtInsertTaskHistory);
            this.addPreparedStatement(pstmtUser);
            this.addPreparedStatement(pstmtInsertTaskAspertion);
            this.addPreparedStatement(pstmtDeleteTaskAspertion);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveTaskProductAndAspertionTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int idTask = 0;
        int idUser = 0;
        String userName = "";
        String taskType = "";
        String campName = "";
        String sectionName = "";
        String laborTypeName = "";
        String sectionCrop = "";
        String ftaskDate = "";
        String comments = "";
        String supervisorName = "";
        boolean boolSaveProductHistory = false;
        boolean boolSaveAspertionHistory = false;

        String lot = "";
        String startTime = "";
        String valve = "";
        String endTime = "";
        String taskDate = "";
        String comercialName = "";
        String caducity = "";
        String uom = "";
        String ingredient = "";
        String dose = "";
        String lote = "";
        String total = "";
        String interval = "";
        String period = "";

        String condition = "";
        String velocity = "";
        String temperature = "";
        String direction = "";
        String humidity = "";
        String beak = "";
        String aspertionType = "";
        String pressure = "";
        String tractorType = "";
        String tractorVelocity = "";
        String expenditure = "";
        String owner = "";
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat sdt = new SimpleDateFormat("h:mm a");
        java.util.Date date = new java.util.Date();

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            idTask = GetNodeArray().find("idTask").getIntValue();

            pstmtUser.setInt(1, idUser);
            rset = pstmtUser.executeQuery();
            if (rset.next()) {
                userName = rset.getString("User");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            pstmtTaskInfo.setInt(1, idTask);
            rset = pstmtTaskInfo.executeQuery();
            if (rset.next()) {
                taskType = rset.getString("TaskType");
                campName = rset.getString("CampName");
                sectionName = rset.getString("SectionName");
                laborTypeName = rset.getString("LaborTypeName");
                sectionCrop = rset.getString("SectionCrop");
                ftaskDate = rset.getString("fTaskDate");
                supervisorName = rset.getString("User");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //validate fields
            if (this.GetNodeArray().existValue("lot")) {
                lot = GetNodeArray().find("lot").getStringValue();
                resultArray.add("SelectedLot", lot);
            }
            if (this.GetNodeArray().existValue("startTime")) {
                startTime = GetNodeArray().find("startTime").getStringValue();
                resultArray.add("SelectedStartTime", startTime);
            }else{
                startTime =  sdt.format(date.getTime());
                resultArray.add("SelectedStartTime", startTime);                
            }
            if (this.GetNodeArray().existValue("valve")) {
                valve = GetNodeArray().find("valve").getStringValue();
                resultArray.add("SelectedValve", valve);
            }
            if (this.GetNodeArray().existValue("endTime")) {
                endTime = GetNodeArray().find("endTime").getStringValue();
                resultArray.add("SelectedEndTime", endTime);
            }else{                
                endTime = "";
                resultArray.add("SelectedEndTime", endTime);
            }
            if (this.GetNodeArray().existValue("taskDate")) {
                taskDate = GetNodeArray().find("taskDate").getStringValue();
                resultArray.add("SelectedTaskDate", taskDate);
            }else{                
                taskDate = sdf.format(date);
                resultArray.add("SelectedTaskDate", taskDate);                
            }
            if (this.GetNodeArray().existValue("comercialName")) {
                comercialName = GetNodeArray().find("comercialName").getStringValue();
                resultArray.add("SelectedComercialName", comercialName);
            }
            if (this.GetNodeArray().existValue("caducity")) {
                caducity = GetNodeArray().find("caducity").getStringValue();
                resultArray.add("SelectedCaducity", caducity);
            }
            if (this.GetNodeArray().existValue("uom")) {
                uom = GetNodeArray().find("uom").getStringValue();
                resultArray.add("SelectedUOM", uom);
            }
            if (this.GetNodeArray().existValue("ingredient")) {
                ingredient = GetNodeArray().find("ingredient").getStringValue();
                resultArray.add("SelectedIngredient", ingredient);
            }
            if (this.GetNodeArray().existValue("dose")) {
                dose = GetNodeArray().find("dose").getStringValue();
                resultArray.add("SelectedDose", dose);
            }
            if (this.GetNodeArray().existValue("lote")) {
                lote = GetNodeArray().find("lote").getStringValue();
                resultArray.add("SelectedLote", lote);
            }
            if (this.GetNodeArray().existValue("total")) {
                total = GetNodeArray().find("total").getStringValue();
                resultArray.add("SelectedTotal", total);
            }
            if (this.GetNodeArray().existValue("interval")) {
                interval = GetNodeArray().find("interval").getStringValue();
                resultArray.add("SelectedInterval", interval);
            }
            if (this.GetNodeArray().existValue("period")) {
                period = GetNodeArray().find("period").getStringValue();
                resultArray.add("SelectedPeriod", period);
            }

            //delete 
            pstmtDeleteTaskProduct.setInt(1, idTask);
            pstmtDeleteTaskProduct.executeUpdate();

            //Insert
            pstmtInsertTaskProduct.setInt(1, idTask);
            pstmtInsertTaskProduct.setString(2, lot);
            pstmtInsertTaskProduct.setString(3, startTime);
            pstmtInsertTaskProduct.setString(4, valve);
            pstmtInsertTaskProduct.setString(5, endTime);
            pstmtInsertTaskProduct.setString(6, taskDate);
            pstmtInsertTaskProduct.setString(7, comercialName);
            pstmtInsertTaskProduct.setString(8, caducity);
            pstmtInsertTaskProduct.setString(9, uom);
            pstmtInsertTaskProduct.setString(10, ingredient);
            pstmtInsertTaskProduct.setString(11, dose);
            pstmtInsertTaskProduct.setString(12, lote);
            pstmtInsertTaskProduct.setString(13, total);
            pstmtInsertTaskProduct.setString(14, interval);
            pstmtInsertTaskProduct.setString(15, period);
            pstmtInsertTaskProduct.setInt(16, idUser);
            rowsAffected = pstmtInsertTaskProduct.executeUpdate();
            if (rowsAffected > 0) {
                boolSaveProductHistory = true;
            }

            //ASPERTION
            //validate fields
            if (this.GetNodeArray().existValue("condition")) {
                condition = GetNodeArray().find("condition").getStringValue();
                resultArray.add("SelectedCondition", condition);
            }
            if (this.GetNodeArray().existValue("velocity")) {
                velocity = GetNodeArray().find("velocity").getStringValue();
                resultArray.add("SelectedVelocity", velocity);
            }
            if (this.GetNodeArray().existValue("temperature")) {
                temperature = GetNodeArray().find("temperature").getStringValue();
                resultArray.add("SelectedTemperature", temperature);
            }
            if (this.GetNodeArray().existValue("direction")) {
                direction = GetNodeArray().find("direction").getStringValue();
                resultArray.add("SelectedDirection", direction);
            }
            if (this.GetNodeArray().existValue("humidity")) {
                humidity = GetNodeArray().find("humidity").getStringValue();
                resultArray.add("SelectedHumidity", humidity);
            }
            if (this.GetNodeArray().existValue("beak")) {
                beak = GetNodeArray().find("beak").getStringValue();
                resultArray.add("SelectedBeak", beak);
            }
            if (this.GetNodeArray().existValue("aspertionType")) {
                aspertionType = GetNodeArray().find("aspertionType").getStringValue();
                resultArray.add("SelectedAspertionType", aspertionType);
            }
            if (this.GetNodeArray().existValue("pressure")) {
                pressure = GetNodeArray().find("pressure").getStringValue();
                resultArray.add("SelectedPressure", pressure);
            }
            if (this.GetNodeArray().existValue("tractorType")) {
                tractorType = GetNodeArray().find("tractorType").getStringValue();
                resultArray.add("SelectedTractorType", tractorType);
            }
            if (this.GetNodeArray().existValue("tractorVelocity")) {
                tractorVelocity = GetNodeArray().find("tractorVelocity").getStringValue();
                resultArray.add("SelectedTractorVelocity", tractorVelocity);
            }
            if (this.GetNodeArray().existValue("expenditure")) {
                expenditure = GetNodeArray().find("expenditure").getStringValue();
                resultArray.add("SelectedExpenditure", expenditure);
            }
            if (this.GetNodeArray().existValue("owner")) {
                owner = GetNodeArray().find("owner").getStringValue();
                resultArray.add("SelectedOwner", owner);
            }
            if (this.GetNodeArray().existValue("comments")) {
                comments = GetNodeArray().find("comments").getStringValue();
                resultArray.add("SelectedComments", comments);
            }

            //delete 
            pstmtDeleteTaskAspertion.setInt(1, idTask);
            pstmtDeleteTaskAspertion.executeUpdate();

            //insert
            pstmtInsertTaskAspertion.setInt(1, idTask);
            pstmtInsertTaskAspertion.setString(2, condition);
            pstmtInsertTaskAspertion.setString(3, velocity);
            pstmtInsertTaskAspertion.setString(4, temperature);
            pstmtInsertTaskAspertion.setString(5, direction);
            pstmtInsertTaskAspertion.setString(6, humidity);
            pstmtInsertTaskAspertion.setString(7, beak);
            pstmtInsertTaskAspertion.setString(8, aspertionType);
            pstmtInsertTaskAspertion.setString(9, pressure);
            pstmtInsertTaskAspertion.setString(10, tractorType);
            pstmtInsertTaskAspertion.setString(11, tractorVelocity);
            pstmtInsertTaskAspertion.setString(12, expenditure);
            pstmtInsertTaskAspertion.setString(13, owner);
            pstmtInsertTaskAspertion.setString(14, comments);
            pstmtInsertTaskAspertion.setInt(15, idUser);
            rowsAffected = pstmtInsertTaskAspertion.executeUpdate();
            if (rowsAffected > 0) {
                boolSaveAspertionHistory = true;
            }

            if (boolSaveProductHistory && boolSaveAspertionHistory) {
                pstmtInsertTaskHistory.setInt(1, idTask);
                pstmtInsertTaskHistory.setString(2, taskType);
                pstmtInsertTaskHistory.setString(3, campName);
                pstmtInsertTaskHistory.setString(4, sectionName);
                pstmtInsertTaskHistory.setString(5, laborTypeName);
                pstmtInsertTaskHistory.setString(6, sectionCrop);
                pstmtInsertTaskHistory.setString(7, ftaskDate);
                pstmtInsertTaskHistory.setString(8, "En Proceso");
                pstmtInsertTaskHistory.setString(9, comments);
                pstmtInsertTaskHistory.setString(10, userName);
                pstmtInsertTaskHistory.setString(11, supervisorName);
                rowsAffected = pstmtInsertTaskHistory.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit();
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "La informacion ha sido registrada exitosamente.");
                    resultArray.add("RESPONSE_DETAIL", "");
                } else {
                    conn.rollback();
                    resultArray.add("error", "La informacion no pudo ser registrada.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "La informacion no pudo ser registrada.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }

            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveTaskProductAndAspertionTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveTaskProductAndAspertionTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
//            System.out.println("<SaveTaskProductAndAspertionTransaction::Execute> exit");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.SaveTaskProductAndAspertionTransaction");

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
            SaveTaskProductAndAspertionTransaction transaction = new SaveTaskProductAndAspertionTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.SaveTaskProductAndAspertionTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveTaskProductAndAspertionTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveTaskProductAndAspertionTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.SaveTaskProductAndAspertionTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveTaskProductAndAspertionTransaction - No results were returned.");
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
            System.out.println("SaveTaskProductAndAspertionTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveTruckTransferOrderTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveTruckTransferOrderTransaction() {
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
                System.out.println("<RetrieveStockTransferOrderTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveStockTransferOrderTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveStockTransferOrderTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveStockTransferOrderTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelect = con.prepareStatement("SELECT "
                        + "s.idStockTransferOrder, "
                        + "IF(s.Proforma IS NULL, 'Pendiente De Asignar', s.Proforma) AS Proforma, "
                        + "s.TransportCompany, "
                        + "s.TruckDriverName, "
                        + "s.TruckPlate, "
                        + "s.TruckBoxNumber, "
                        + "s.TruckBoxPlate, "
                        + "s.Crosser,"
                        + "s.TypeBox,"
                        + "s.BoxFeet,"
                        + "s.Status, "
                        + "date_format(s.InsertDate,'%d/%m/%Y %h:%i %p') as InsertDate, "
                        + "date_format(s.ModifiedDate,'%d/%m/%Y %h:%i %p') as ModifiedDate, "
                        + "IFNULL(w.Gross,0) as Gross, "
                        + "w.Tare, "
                        + "IFNULL(w.Net,0) as Net "
                        + "FROM stocktransferorder s " 
                        + "INNER JOIN transportevaluation e ON e.idStockTransferOrder = s.idStockTransferOrder "
                        + "INNER JOIN transportweightramp w ON w.idStockTransferOrder = s.idStockTransferOrder "
                        + "WHERE s.Status = IFNULL(?, s.Status) " // 1 Status - Filtro de busquedad
                        + "and (? IS NULL OR s.Proforma)" // 2 Proforma - Filtro de busquedad
                        + "and e.Evaluate = 1 "
                        + "and w.Weighed = 1 "
                        + "and s.Active = 1 " 
                        + "and s.TransportCompany like ? " // 3 TransportCompany - Filtro de busquedad
                        + "and s.idStockTransferOrder = IFNULL(?, s.idStockTransferOrder) " // 4 idStockTransferOrder - Filtro de busquedad
                        // Filtro de la fecha toma todo el dia desde las 6 AM hasta la 5:59 del dia siguiente 
                        + "AND (? IS NULL OR (" // 5 s.InsertDate
                        + "s.InsertDate >= DATE_ADD(STR_TO_DATE(?, '%d/%m/%Y'), INTERVAL 6 HOUR) " // 6 s.InsertDate
                        + "AND s.InsertDate <  DATE_ADD(STR_TO_DATE(?, '%d/%m/%Y'), INTERVAL 29 HOUR)" // 7 s.InsertDate
                        + ")) "
                        + "ORDER BY s.InsertDate"
            );

            this.addPreparedStatement(pstmtSelect);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveStockTransferOrderTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String status = null;
        String transportCompanyName = "%";
        String idEmbarque = null;
        String date = null;
        String proforma = null;
        
        try {

            resultArray = new xmlNodeArray();

            if (this.GetNodeArray().existValue("status")) {
                status = GetNodeArray().find("status").getStringValue();
                resultArray.add("SelectedStatus", transportCompanyName);
            }

            if (this.GetNodeArray().existValue("transportCompanyName")) {
                transportCompanyName = GetNodeArray().find("transportCompanyName").getStringValue();
                resultArray.add("SelectedTransportCompanyName", transportCompanyName);
            }
            
            if (this.GetNodeArray().existValue("idEmbarque")) {
                idEmbarque = GetNodeArray().find("idEmbarque").getStringValue();
                resultArray.add("idEmbarque", idEmbarque);
            }
            
            if (this.GetNodeArray().existValue("mainDate")) {
                date = GetNodeArray().find("mainDate").getStringValue();
                resultArray.add("SelectedDate", date);
            }
            
            if (this.GetNodeArray().existValue("proforma")) {
                proforma = GetNodeArray().find("proforma").getStringValue();
                System.out.println("Valor de la proforma: "+GetNodeArray().find("proforma").getStringValue());
                resultArray.add("SelectedProforma", transportCompanyName);
            }

            //Supervisor
            pstmtSelect.setString(1, status);
            pstmtSelect.setString(2, proforma);
            pstmtSelect.setString(3, transportCompanyName);
            pstmtSelect.setString(4, idEmbarque);
            pstmtSelect.setString(5, date);
            pstmtSelect.setString(6, date);
            pstmtSelect.setString(7, date);
            rset = pstmtSelect.executeQuery();
            xmlTable xTable = this.formatDataTable(rset);
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("StockTransferOrderInfo_Table", xTable);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("<RetrieveStockTransferOrderTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("<RetrieveStockTransferOrderTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveStockTransferOrderTransaction");
        nodeArr.add("status", "Nueva");
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
            RetrieveStockTransferOrderTransaction transaction = new RetrieveStockTransferOrderTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveStockTransferOrderTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveStockTransferOrderTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveStockTransferOrderTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveStockTransferOrderTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveStockTransferOrderTransaction - No results were returned.");
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
            System.out.println("RetrieveStockTransferOrderTransaction::main> caught exception " + e.getMessage());
        }
    }
}
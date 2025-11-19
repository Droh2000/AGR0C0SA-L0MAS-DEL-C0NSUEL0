package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveStockTransferOrderByProformaTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectIdSTO;
    protected PreparedStatement pstmtSelectSTO;
    protected PreparedStatement pstmtSelectSTODetail;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveStockTransferOrderByProformaTransaction() {
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
        String[] mandatoryTags = {"proforma"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveStockTransferOrderByProformaTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveStockTransferOrderByProformaTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveStockTransferOrderByProformaTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveStockTransferOrderByProformaTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelectIdSTO = con.prepareStatement("SELECT "
                    + "idStockTransferOrder, "
                    + "Status "
                    + "FROM stocktransferorder "
                    + "Where Proforma = ? ");

            pstmtSelectSTO = con.prepareStatement("SELECT "
                    + "idStockTransferOrder, "
                    + "TransportCompany, "
                    + "TruckDriverName, "
                    + "TruckPlate, "
                    + "Proforma, "
                    + "Status, "
                    + "date_format(ModifiedDate,'%d/%m/%Y %h:%i %p') as ShipDate "
                    + "FROM stocktransferorder "
                    + "Where Proforma = ? ");

            pstmtSelectSTODetail = con.prepareStatement("SELECT "
                    + "idStockTransferOrder, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "count(*) as PalletQty "
                    + "FROM stocktransferorderdetail "
                    + "where idStockTransferOrder = ? "
                    + "Group by idStockTransferOrder, ProductName, Size, Color "
                    + "Order by idStockTransferOrder, ProductName, Size, Color");

            this.addPreparedStatement(pstmtSelectSTO);
            this.addPreparedStatement(pstmtSelectSTODetail);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveStockTransferOrderByProformaTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String proforma = "%";
        int idStockTransferOrder = 0;
        String status = "";

        try {

            resultArray = new xmlNodeArray();

            if (this.GetNodeArray().existValue("proforma")) {
                proforma = GetNodeArray().find("proforma").getStringValue();
                resultArray.add("SelectedProforma", proforma);
            }

            pstmtSelectIdSTO.setString(1, proforma);
            rset = pstmtSelectIdSTO.executeQuery();
            if (rset.next()) {
                idStockTransferOrder = rset.getInt("idStockTransferOrder");
                status = rset.getString("Status");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (status.equals("EnTransito")) {
                pstmtSelectSTO.setString(1, proforma);
                rset = pstmtSelectSTO.executeQuery();
                xmlTable xTable = this.formatDataTable(rset);
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
                resultArray.add("StockTransferOrderInfo_Table", xTable);

                pstmtSelectSTODetail.setInt(1, idStockTransferOrder);
                rset = pstmtSelectSTODetail.executeQuery();
                xmlTable dTable = this.formatDataTable(rset);
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
                resultArray.add("StockTransferOrderDetailInfo_Table", dTable);

                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", message);
                resultArray.add("RESPONSE_DETAIL", "");
            } else if (status.equals("Embarcada")) {
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Este numero de proforma " + proforma + "ya fue emabarcada anteriormente y no puede ser recibida nuevamente");
                resultArray.add("RESPONSE_DETAIL", "");

            } else if (status.equals("Cancelada")) {
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Este numero de proforma " + proforma + " fue cancelada y no puede ser recibida");
                resultArray.add("RESPONSE_DETAIL", "");

            } else if (status.equals("Recibida")) {
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Este numero de proforma " + proforma + "ya fue recibida anteriormente y no puede ser recibida nuevamente");
                resultArray.add("RESPONSE_DETAIL", "");

            } else {
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "El estatus de la proforma es invalido, no puede ser recibida");
                resultArray.add("RESPONSE_DETAIL", "Estatus: " + status );
            }

            return resultArray;
        } catch (SQLException e) {
            System.out.println("<RetrieveStockTransferOrderByProformaTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("<RetrieveStockTransferOrderByProformaTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveStockTransferOrderByProformaTransaction");
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
            RetrieveStockTransferOrderByProformaTransaction transaction = new RetrieveStockTransferOrderByProformaTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveStockTransferOrderByProformaTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveStockTransferOrderByProformaTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveStockTransferOrderByProformaTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveStockTransferOrderByProformaTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveStockTransferOrderByProformaTransaction - No results were returned.");
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
            System.out.println("RetrieveStockTransferOrderByProformaTransaction::main> caught exception " + e.getMessage());
        }
    }
}

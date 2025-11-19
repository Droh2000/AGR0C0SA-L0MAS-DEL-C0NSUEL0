package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveAjaxStockTransferOrderDetailTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectOrder;
    protected PreparedStatement pstmtSelectDetail;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveAjaxStockTransferOrderDetailTransaction() {
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
        String[] mandatoryTags = {"idStockTransferOrder"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveAjaxStockTransferOrderDetailTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveAjaxStockTransferOrderDetailTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveAjaxStockTransferOrderDetailTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveAjaxStockTransferOrderDetailTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelectOrder = con.prepareStatement("SELECT "
                    + "idStockTransferOrder, "
                    + "TransportCompany, "
                    + "TruckDriverName, "
                    + "TruckPlate, "
                    + "coalesce(Proforma,'') as Proforma, "
                    + "Status, "
                    //+ "concat(user.FirstName,' ',user.LastName) User, "
                    + "date_format(stocktransferorder.InsertDate,'%d/%m/%Y %h:%i %p') as InsertDate, "
                    + "date_format(stocktransferorder.ModifiedDate,'%d/%m/%Y %h:%i %p') as ModifiedDate "
                    + "FROM stocktransferorder "// inner join "
                    //+ "user on user.idUser = stocktransferorder.idUser "
                    + "Where idStockTransferOrder = ? ");

            pstmtSelectDetail = con.prepareStatement("SELECT "
                    + "stocktransferorder.idStockTransferOrder, "
                    + "stocktransferorder.TransportCompany, "
                    + "stocktransferorder.TruckDriverName, "
                    + "stocktransferorder.TruckPlate, "
                    + "stocktransferorder.Proforma, "
                    + "date_format(stocktransferorder.InsertDate,'%d/%m/%Y %h:%i %p') as ShippingDate, "
                    + "stocktransferorder.Status, "
                    + "stocktransferorderdetail.PalletId, "
                    + "stocktransferorderdetail.ProductName, "
                    + "stocktransferorderdetail.Size, "
                    + "stocktransferorderdetail.Color, "
                    + "stocktransferorderdetail.Quantity, "
                    + "stocktransferorderdetail.CampName "
                    + "FROM stocktransferorderdetail inner join "
                    + "stocktransferorder on stocktransferorder.idStockTransferOrder = stocktransferorderdetail.idStockTransferOrder "
                    + "Where stocktransferorder.idStockTransferOrder = ? ");

            this.addPreparedStatement(pstmtSelectOrder);
            this.addPreparedStatement(pstmtSelectDetail);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveAjaxStockTransferOrderDetailTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String result = "";
        int idStockTransferOrder = 0;

        try {
            resultArray = new xmlNodeArray();

            idStockTransferOrder = GetNodeArray().find("idStockTransferOrder").getIntValue();

            pstmtSelectOrder.setInt(1, idStockTransferOrder);
            rset = pstmtSelectOrder.executeQuery();
            result += "<table id=\"dtable\" class=\"table table-bordered table-striped table-hover\">"
                    + "<thead>"
                    + "<tr>"
                    + "<th>Embarque ID</th>"
                    + "<th>Transportista</th>"
                    + "<th>Nombre de Chofer</th>"
                    + "<th>Placas</th>"
                    + "<th>Proforma</th>"
                    + "<th>Fecha de Embarque</th>"
                    + "<th>Estatus</th>"
                    + "</thead>"
                    + "<tbody>";
            
            if (rset.next()) {
                result += "<tr>"
                        + "<td>" + rset.getString("idStockTransferOrder") +"</td>"
                        + "<td>" + rset.getString("TransportCompany") +"</td>"
                        + "<td>" + rset.getString("TruckDriverName") +"</td>"
                        + "<td>" + rset.getString("TruckPlate") +"</td>"
                        + "<td>" + rset.getString("Proforma") +"</td>"
                        + "<td>" + rset.getString("InsertDate") +"</td>"
                        + "<td>" + rset.getString("Status") +"</td>"
                        + "</tr>";
            }
            result += "</tbody></table><br>";
            //close
            if (rset != null) {
                rset.close();
                rset = null;
            }

            pstmtSelectDetail.setInt(1, idStockTransferOrder);
            rset = pstmtSelectDetail.executeQuery();
            result += "<table id=\"dtable\" class=\"table table-bordered table-striped table-hover\">"
                    + "<thead>"
                    + "<tr>"
                    + "<th>PalletID</th>"
                    + "<th>Producto</th>"
                    + "<th>Color</th>"
                    + "<th>Tamaño</th>"
                    + "<th>Cantidad</th>"
                    + "<th>Campo</th>"
                    + "</thead>"
                    + "<tbody>";
            
            while (rset.next()) {
                result += "<tr>"
                        + "<td>" + rset.getString("PalletId") +"</td>"
                        + "<td>" + rset.getString("ProductName") +"</td>"
                        + "<td>" + rset.getString("Size") +"</td>"
                        + "<td>" + rset.getString("Color") +"</td>"
                        + "<td>" + rset.getString("Quantity") +"</td>"
                        + "<td>" + rset.getString("CampName") +"</td>"
                        + "</tr>";
            }
            result += "</tbody></table><br>";
            //close
            if (rset != null) {
                rset.close();
                rset = null;
            }

            resultArray.add("Result", result);
            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("RetrieveAjaxStockTransferOrderDetailTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveAjaxStockTransferOrderDetailTransaction::Execute> Exception: " + ex.getMessage());
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

//            System.out.println("<RetrieveAjaxStockTransferOrderDetailTransaction::Execute> exit");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.RetrieveAjaxStockTransferOrderDetailTransaction");
//        nodeArr.add("idUser", "1");
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
            RetrieveAjaxStockTransferOrderDetailTransaction transaction = new RetrieveAjaxStockTransferOrderDetailTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.RetrieveAjaxStockTransferOrderDetailTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveAjaxStockTransferOrderDetailTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveAjaxStockTransferOrderDetailTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.RetrieveAjaxStockTransferOrderDetailTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveAjaxStockTransferOrderDetailTransaction - No results were returned.");
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
            System.out.println("RetrieveAjaxStockTransferOrderDetailTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

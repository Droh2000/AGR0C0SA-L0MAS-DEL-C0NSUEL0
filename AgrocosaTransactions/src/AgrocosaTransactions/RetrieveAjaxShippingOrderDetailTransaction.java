package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveAjaxShippingOrderDetailTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectOrder;
    protected PreparedStatement pstmtSelectDetail;
    protected PreparedStatement pstmtSelectPallet;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveAjaxShippingOrderDetailTransaction() {
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
        String[] mandatoryTags = {"idShippingOrder"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveAjaxShippingOrderDetailTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveAjaxShippingOrderDetailTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveAjaxShippingOrderDetailTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveAjaxShippingOrderDetailTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
                    + "shippingorder.idShippingOrder, "
                    + "customer.CustomerName, "
                    + "shippingorder.PickupNumber, "
                    + "date_format(shippingorder.ShippingDate,'%d/%m/%Y %h:%i %p') as ShippingDate, "
                    + "shippingorder.Status, "
                    + "shippingorder.Comments "
                    + "FROM shippingorder inner join "
                    + "customer on customer.idCustomer = shippingorder.idCustomer "
                    + "Where shippingorder.idShippingOrder = ? ");

            pstmtSelectDetail = con.prepareStatement("SELECT "
                    + "shippingorder.idShippingOrder, "
                    + "customer.CustomerName, "
                    + "shippingorder.PickupNumber, "
                    + "date_format(shippingorder.ShippingDate,'%d/%m/%Y %h:%i %p') as ShippingDate, "
                    + "shippingorder.Status, "
                    + "shippingorder.Comments, "
                    + "shippingorderdetail.ProductName, "
                    + "shippingorderdetail.Size, "
                    + "shippingorderdetail.Color, "
                    + "shippingorderdetail.Quantity "
                    + "FROM shippingorderdetail inner join "
                    + "shippingorder on shippingorder.idShippingOrder = shippingorderdetail.idShippingOrder inner join "
                    + "customer on customer.idCustomer = shippingorder.idCustomer "
                    + "Where shippingorder.idShippingOrder = ? ");

            pstmtSelectPallet = con.prepareStatement("SELECT "
                    + "shippingorderdetail.ProductName, "
                    + "shippingorderdetail.Color, "
                    + "shippingorderdetail.Size, "
                    + "shippingorderpallet.PalletId "
                    + "FROM shippingorderpallet inner join "
                    + "shippingorderdetail on shippingorderdetail.idShippingOrderDetail = shippingorderpallet.idShippingOrderDetail "
                    + "and shippingorderdetail.idShippingOrder = shippingorderpallet.idShippingOrder "
                    + "Where shippingorderdetail.idShippingOrder = ? "
                    + "Order by PalletId");
            
            this.addPreparedStatement(pstmtSelectOrder);
            this.addPreparedStatement(pstmtSelectDetail);
            this.addPreparedStatement(pstmtSelectPallet);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveAjaxShippingOrderDetailTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int idShippingOrder = 0;

        try {
            resultArray = new xmlNodeArray();

            idShippingOrder = GetNodeArray().find("idShippingOrder").getIntValue();

            pstmtSelectOrder.setInt(1, idShippingOrder);
            rset = pstmtSelectOrder.executeQuery();
            result += "<table id=\"dtable\" class=\"table table-bordered table-striped table-hover\">"
                    + "<thead>"
                    + "<tr>"
                    + "<th>Num de Orden</th>"
                    + "<th>Cliente</th>"
                    + "<th>Pickup#</th>"
                    + "<th>Fecha de Orden</th>"
                    + "<th>Estatus</th>"
                    + "<th>Comentarios</th>"
                    + "</thead>"
                    + "<tbody>";
            
            if (rset.next()) {
                result += "<tr>"
                        + "<td>" + rset.getString("idShippingOrder") +"</td>"
                        + "<td>" + rset.getString("CustomerName") +"</td>"
                        + "<td>" + rset.getString("PickupNumber") +"</td>"
                        + "<td>" + rset.getString("ShippingDate") +"</td>"
                        + "<td>" + rset.getString("Status") +"</td>"
                        + "<td>" + rset.getString("Comments") +"</td>"
                        + "</tr>";
            }
            result += "</tbody></table><br>";
            //close
            if (rset != null) {
                rset.close();
                rset = null;
            }

            pstmtSelectDetail.setInt(1, idShippingOrder);
            rset = pstmtSelectDetail.executeQuery();
            result += "<table id=\"dtable1\" class=\"table table-bordered table-striped table-hover\">"
                    + "<thead>"
                    + "<tr>"
                    + "<th>Producto</th>"
                    + "<th>Color</th>"
                    + "<th>Tamaño</th>"
                    + "<th>Cantidad</th>"
                    + "</thead>"
                    + "<tbody>";
            
            while (rset.next()) {
                result += "<tr>"
                        + "<td>" + rset.getString("ProductName") +"</td>"
                        + "<td>" + rset.getString("Color") +"</td>"
                        + "<td>" + rset.getString("Size") +"</td>"
                        + "<td>" + rset.getString("Quantity") +"</td>"
                        + "</tr>";
            }
            result += "</tbody></table><br>";
            //close
            if (rset != null) {
                rset.close();
                rset = null;
            }

            pstmtSelectPallet.setInt(1, idShippingOrder);
            rset = pstmtSelectPallet.executeQuery();
            result += "<table id=\"dtable2\" class=\"table table-bordered table-striped table-hover\">"
                    + "<thead>"
                    + "<tr>"
                    + "<th>Producto</th>"
                    + "<th>Color</th>"
                    + "<th>Tamaño</th>"
                    + "<th>PalletID</th>"
                    + "</thead>"
                    + "<tbody>";
            
            while (rset.next()) {
                result += "<tr>"
                        + "<td>" + rset.getString("ProductName") +"</td>"
                        + "<td>" + rset.getString("Color") +"</td>"
                        + "<td>" + rset.getString("Size") +"</td>"
                        + "<td>" + rset.getString("PalletId") +"</td>"
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
            System.out.println("RetrieveAjaxShippingOrderDetailTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveAjaxShippingOrderDetailTransaction::Execute> Exception: " + ex.getMessage());
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

//            System.out.println("<RetrieveAjaxShippingOrderDetailTransaction::Execute> exit");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.RetrieveAjaxShippingOrderDetailTransaction");
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
            RetrieveAjaxShippingOrderDetailTransaction transaction = new RetrieveAjaxShippingOrderDetailTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.RetrieveAjaxShippingOrderDetailTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveAjaxShippingOrderDetailTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveAjaxShippingOrderDetailTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.RetrieveAjaxShippingOrderDetailTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveAjaxShippingOrderDetailTransaction - No results were returned.");
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
            System.out.println("RetrieveAjaxShippingOrderDetailTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}

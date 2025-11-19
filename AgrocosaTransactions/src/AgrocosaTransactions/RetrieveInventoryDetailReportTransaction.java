package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveInventoryDetailReportTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtSelectTotal;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveInventoryDetailReportTransaction() {
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
                System.out.println("<RetrieveInventoryDetailReportTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveInventoryDetailReportTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveInventoryDetailReportTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveInventoryDetailReportTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
                    + "storage.StorageName, "
                    + "substorage.SubstorageName, "
                    + "palletproduct.ProductName, "
                    + "palletproduct.Size, "
                    + "palletproduct.Color, "
                    + "inventory.PalletId "
                    + "FROM inventory inner join "
                    + "storage on storage.idStorage = inventory.idStorage inner join "
                    + "substorage on substorage.idSubStorage = inventory.idSubstorage inner join "
                    + "palletproduct on palletproduct.idPalletProduct = inventory.idPalletProduct "
                    + "where PackingType = 'Pallet' "
                    + "and storage.StorageName like ? "
                    + "and substorage.SubstorageName like ? "
                    + "and palletproduct.ProductName like ? "
                    + "and inventory.PalletId like ? "
                    + "order by StorageName, SubstorageName, ProductName ");

            pstmtSelectTotal = con.prepareStatement("SELECT "
                    + "SUM(Quantity) as PalletQty "
                    + "FROM inventory inner join "
                    + "storage on storage.idStorage = inventory.idStorage inner join "
                    + "substorage on substorage.idSubStorage = inventory.idSubstorage inner join "
                    + "palletproduct on palletproduct.idPalletProduct = inventory.idPalletProduct "
                    + "where PackingType = 'Pallet' "
                    + "and storage.StorageName like ? "
                    + "and substorage.SubstorageName like ? "
                    + "and palletproduct.ProductName like ? "
                    + "and inventory.PalletId like ? ");

            this.addPreparedStatement(pstmtSelect);
            this.addPreparedStatement(pstmtSelectTotal);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveInventoryDetailReportTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String storageName = "%";
        String  substorageName= "%";
        String productName = "%";
        String palletId = "%";
        int total = 0;
        
        try {
            
            resultArray = new xmlNodeArray();

            if (this.GetNodeArray().existValue("productName")) {
                productName = GetNodeArray().find("productName").getStringValue();
                resultArray.add("SelectedProductName", productName);
            }

            if (this.GetNodeArray().existValue("storageName")) {
                storageName = GetNodeArray().find("storageName").getStringValue();
                resultArray.add("SelectedStorageName", storageName);
            }

            if (this.GetNodeArray().existValue("substorageName")) {
                substorageName = GetNodeArray().find("substorageName").getStringValue();
                resultArray.add("SelectedSubstorageName", substorageName);
            }
            
            if (this.GetNodeArray().existValue("palletId")) {
                palletId = "%" + GetNodeArray().find("palletId").getStringValue() + "%";
                resultArray.add("SelectedPalletId", palletId.replace("%", ""));
            }
            

            //inventory
            pstmtSelect.setString(1, storageName);
            pstmtSelect.setString(2, substorageName);
            pstmtSelect.setString(3, productName);            
            pstmtSelect.setString(4, palletId);            
            rset = pstmtSelect.executeQuery();
            xmlTable guardTable = this.formatDataTable(rset);
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("Inventory_Table", guardTable);


            //Total
            pstmtSelectTotal.setString(1, storageName);
            pstmtSelectTotal.setString(2, substorageName);
            pstmtSelectTotal.setString(3, productName);  
            pstmtSelectTotal.setString(4, palletId);
            rset = pstmtSelectTotal.executeQuery();
            if(rset.next()){
                total = rset.getInt("PalletQty");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("Total", total);

            
            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("<RetrieveInventoryDetailReportTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("<RetrieveInventoryDetailReportTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveInventoryDetailReportTransaction");
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
            RetrieveInventoryDetailReportTransaction transaction = new RetrieveInventoryDetailReportTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveInventoryDetailReportTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveInventoryDetailReportTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveInventoryDetailReportTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveInventoryDetailReportTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveInventoryDetailReportTransaction - No results were returned.");
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
            System.out.println("RetrieveInventoryDetailReportTransaction::main> caught exception " + e.getMessage());
        }
    }
}

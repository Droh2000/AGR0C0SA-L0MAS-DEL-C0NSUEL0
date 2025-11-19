package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveCrossdockPalletsTableTransaction extends SIDWebTransaction {
    static String CROSSDOCK = "Bodega";
    protected PreparedStatement pstmtSelectCrossdock;
    protected PreparedStatement pstmtSelectInventory;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveCrossdockPalletsTableTransaction() {
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
                System.out.println("<RetrieveCrossdockPalletsTableTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveCrossdockPalletsTableTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveCrossdockPalletsTableTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveCrossdockPalletsTableTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelectCrossdock = con.prepareStatement("Select "
                    + "storage.idStorage, "
                    + "storage.StorageName, "
                    + "substorage.idSubstorage, "
                    + "substorage.SubstorageName "
                    + "from storage inner join "
                    + "substorage on substorage.idStorage = storage.idStorage "
                    + "where storage.StorageName = '" + CROSSDOCK + "' "
                    + "and substorage.Active = 1 "
                    + "Order by substorage.SubstorageName");

            pstmtSelectInventory = con.prepareStatement("select  "
                    + "palletproduct.ProductName, "
                    + "inventory.PalletId "
                    + "from inventory inner join "
                    + "palletproduct on palletproduct.idPalletProduct = inventory.idPalletProduct "
                    + "where inventory.idStorage = ? "
                    + "and inventory.idSubstorage = ? "
                    + "Order by inventory.PalletId");
            
            this.addPreparedStatement(pstmtSelectCrossdock);
            this.addPreparedStatement(pstmtSelectInventory);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveCrossdockPalletsTableTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        ResultSet rset2 = null;
        String message = "OK";
        String data = "";
        int id = 0;

        try {

            resultArray = new xmlNodeArray();
            xmlTable xTab = new xmlTable();
            xTab.addField("StorageName");
            xTab.addField("SubstorageName");

            rset = pstmtSelectCrossdock.executeQuery();
            while (rset.next()) {
                xTab.addRow();
                xTab.setValue(xTab.getRowsQty() - 1, "StorageName", rset.getString("StorageName"));
                xTab.setValue(xTab.getRowsQty() - 1, "SubstorageName", rset.getString("SubstorageName"));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("Storage_Table", xTab);

            
            xmlTable iTab = new xmlTable();
            iTab.addField("TableValue");            
            
            rset = pstmtSelectCrossdock.executeQuery();
            while (rset.next()) {
                
                data = "";
                pstmtSelectInventory.setInt(1, rset.getInt("idStorage"));
                pstmtSelectInventory.setInt(2, rset.getInt("idSubstorage"));
                rset2 = pstmtSelectInventory.executeQuery();
                while(rset2.next()){
                    data += "<div id='i" + id++ + "' class='redips-drag gray'>" + rset2.getString("ProductName") + "-" + rset2.getString("PalletId") + "</div>";
                }
                if(rset2 != null){
                    rset2.close();
                    rset2 = null;
                }
                
                iTab.addRow();
                iTab.setValue(iTab.getRowsQty() - 1, "TableValue", data);
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            resultArray.add("Inventory_Table", iTab);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("<RetrieveCrossdockPalletsTableTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("<RetrieveCrossdockPalletsTableTransaction::Execute> Exception: " + ex.getMessage());
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
            if (rset2 != null) {
                rset2.close();
                rset2 = null;
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveCrossdockPalletsTableTransaction");
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
            RetrieveCrossdockPalletsTableTransaction transaction = new RetrieveCrossdockPalletsTableTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveCrossdockPalletsTableTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveCrossdockPalletsTableTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveCrossdockPalletsTableTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveCrossdockPalletsTableTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveCrossdockPalletsTableTransaction - No results were returned.");
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
            System.out.println("RetrieveCrossdockPalletsTableTransaction::main> caught exception " + e.getMessage());
        }
    }
}

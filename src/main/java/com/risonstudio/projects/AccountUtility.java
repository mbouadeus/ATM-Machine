package com.risonstudio.projects;

import java.util.Map;
import java.util.HashMap;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.EntityNotFoundException;

/**
 * Utility class to manage bank account information.
 * @author Steve Mbouadeu
 *
 */
public class AccountUtility {
	
	private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	private AccountUtility() {}
	
	/**
	 * Register a new user bank account into system.
	 * @param name
	 * @param pin
	 * @param initialCheckingBalance
	 * @param initialSavingsBalance
	 * @return
	 */
	public static String register(String name, String pin, String initialCheckingBalance, String initialSavingsBalance) {
		Entity account = new Entity("Account");
		
		String customerID = getNewCustomerID();
		account.setProperty("name", name);
		account.setProperty("customerID", customerID);
		account.setProperty("pin", pin);
		account.setProperty("checkingBalance", initialCheckingBalance);
		account.setProperty("savingsBalance", initialSavingsBalance);
		
		datastore.put(account);
		return KeyFactory.keyToString(account.getKey());
	}
	
	/**
	 * Authenticate user account. Denies access if credentials are invalid.
	 * @param customerID
	 * @param pin
	 * @return
	 */
	public static String authenticate(String customerID, String pin) {
		Query accounts = new Query("Account")
			.setFilter(new FilterPredicate("customerID", FilterOperator.EQUAL, customerID));
		
		PreparedQuery results = datastore.prepare(accounts);
		for (Entity account : results.asIterable()) {
			if (account.getProperty("pin").equals(pin)) return KeyFactory.keyToString(account.getKey());
		}
		return null;
	}
	
	/**
	 * Deposit funds into an account.
	 * @param accountKey
	 * @param accountTo
	 * @param amount
	 * @return
	 */
	public static String deposit(String accountKey, String accountTo, String amount) {
		int amt = Integer.valueOf(amount);
		changeAccount(accountKey, accountTo, amt);
		return getAccount(accountKey);
	}
	
	/**
	 * Withdraws funds from an account.
	 * @param accountKey
	 * @param accountFrom
	 * @param amount
	 * @return
	 */
	public static String withdraw(String accountKey, String accountFrom, String amount) {
		int amt = Integer.valueOf(amount);
		changeAccount(accountKey, accountFrom, -amt);
		return getAccount(accountKey);
	}
	
	/**
	 * Transfer funds between accounts.
	 * @param accountKey
	 * @param accountFrom
	 * @param accountTo
	 * @param amount
	 * @return
	 */
	public static String transfer(String accountKey, String accountFrom, String accountTo, String amount) {
		deposit(accountKey, accountTo, amount);
		withdraw(accountKey, accountFrom, amount);
		return getAccount(accountKey);
	}
	
	/**
	 * Change the balance of a specified account.
	 * @param accountKey
	 * @param accountTo
	 * @param amount
	 */
	private static void changeAccount(String accountKey, String accountTo, int amount) {
		Entity account = getAccountEntity(accountKey);
		
		if (account == null) return;
		
		// Decides which account to change and executes change request.
		if (accountTo.equalsIgnoreCase("checking")) {
			int currentAmount = Integer.valueOf((String) account.getProperty("checkingBalance"));
			account.setProperty("checkingBalance", String.valueOf(currentAmount + amount));
		} else if (accountTo.equalsIgnoreCase("savings")) {
			int currentAmount = Integer.valueOf((String) account.getProperty("savingsBalance"));
			account.setProperty("savingsBalance", String.valueOf(currentAmount + amount));
		}
		datastore.put(account);
	}
	
	/**
	 * Retrieves database account entity with account information.
	 * @param accountKey
	 * @return
	 */
	private static Entity getAccountEntity(String accountKey) {
		Key key = KeyFactory.stringToKey(accountKey);
		Entity account = null;
		
		try {
	      account = datastore.get(key);
	    } catch (EntityNotFoundException exception) {
	      System.out.println("The account requested wasn't found.");
	    }
		return account;
	}
	
	/**
	 * Retrieves general account information for ATM.
	 * @param accountKey
	 * @return
	 */
	public static String getAccount(String accountKey) {
		Entity account = getAccountEntity(accountKey);

		if (account == null) return null;
		
		Map<String, String> accountInfo = new HashMap<>();
		accountInfo.put("name", (String) account.getProperty("name"));
		accountInfo.put("checkingBalance", (String) account.getProperty("checkingBalance"));
		accountInfo.put("savingsBalance", (String) account.getProperty("savingsBalance"));
		accountInfo.put("customerID", (String) account.getProperty("customerID"));
		return new Gson().toJson(accountInfo);
	}
	
	/**
	 * Creates new 9 digit customer ID based on amount of pre-existing accounts.
	 * @return
	 */
	private static String getNewCustomerID() {
		int amount = 0;
		PreparedQuery results = datastore.prepare(new Query("Account"));
	
		for (Entity account : results.asIterable()) {
			amount++;
		}
		String id = String.valueOf(amount);
		
		// make id ideally of length 9
		if (id.length() > 9) return id;
		return new String(new char[9-id.length()]).replace("\0", "0") + id;
	}
}

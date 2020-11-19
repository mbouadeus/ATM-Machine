const accountKey = getCookie("accountKey");

// Check if key is present. If not, redirect to home page.
if (accountKey.length < 5)
	window.location = "/";
else
 	getAccount();

// Retrieves account information.
function getAccount() {
	$.post({
		url: "/getaccount",
		data: {accountKey: accountKey},
		success: (data) => {
		 	data = JSON.parse(data);
			console.log(data);
			storeAccount(data);
			setAccount();
		},
		error: () => {
			console.log("Account couldn't be retrieved.");
		}
	});
}

// Stores account information for future use in cookies.
function storeAccount(data) {
	// Get all account info.
	const checking = data['checkingBalance'];
	const savings = data['savingsBalance'];
	const name = data['name'];
	const id = data['customerID'];
	
	// Store them in cookies.
	setCookie("checking", checking, 5);
	setCookie("savings", savings, 5);
	setCookie("name", name, 5);
	setCookie("id", id, 5);
}

// Enters account information into apropriate fields.
function setAccount() {

	// Setting account balances and welcome message.
	$("#checking").val("$ " + getCookie("checking"));
	$("#savings").val("$ " + getCookie("savings"));
	$("#name").text("Hello, " + getCookie("name"));
	
	// Display registration message for first time accounts.
	if (getCookie("justRegistered")) {
		displayIDMessage();
		setCookie("justRegistered", "", 5);
	}
	
	// Reset all amount fields to empty.
	$("#deposit-amount").val("");
	$("#withdraw-amount").val("");
	$("#transfer-amount").val("");
}

// Display new registration message.
function displayIDMessage() {
	const id = getCookie("id");
	if (id) {
		$("#new-registration-msg").removeClass("d-none");
		$("#customerID").text(id);
	}
}

// Validate deposit fields.
function validateDeposit(amount) {
	const errors = [];

	// Check if valid amount.
	if (!amount || isNaN(amount) || Number(amount) < 0) {
		errors.push("Invalid deposit amount.");
	}

	// display errors if any.
	$("#alerts").empty();
	errors.forEach(error => {
		$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ error +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
	});
	
	return errors;
}

// Validate withdraw fields.
function validateWithdraw(amount, balance) {
	const errors = [];
	
	// Check if valid amount.
	if (!amount || isNaN(amount) || Number(amount) < 0) {
		errors.push("Invalid withdraw amount.");
	}
	
	// Check if sufficient funds.
	if (amount > balance) {
		errors.push("Insufficient funds.");
	}
	
	// display errors if any.
	$("#alerts").empty();
	errors.forEach(error => {
		$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ error +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
	});
	
	return errors;
}

// Validate transfer fields.
function validateTransfer(from, to, amount, balance) {
	const errors = [];
	
	// Check if accounts are the same.
	if (from === to) {
		errors.push("You must select two different account to perform a transfer.");
	}
	
	// Check if valid amount.
	if (!amount || isNaN(amount) || Number(amount) < 0) {
		errors.push("Invalid transfer amount.");
	}
	
	// Check if sufficient funds.
	if (amount > balance) {
		errors.push("Insufficient funds.");
	}
	
	// display errors if any.
	$("#alerts").empty();
	errors.forEach(error => {
		$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ error +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
	});
	
	return errors;
}

// Deposit funds to account.
function deposit() {
	// Retrieve deposit fields.
	const accountTo = $("#deposit-to").val();
	const amount = $("#deposit-amount").val();
	
	// Check if fields are valid.
	const errors = validateDeposit(amount);
	
	if (errors.length > 0) return;
	
	// Submit deposit.
	$.post({
		url: "/deposit",
		data: {accountKey: accountKey, accountTo: accountTo, amount: amount},
		success: (data) => {
		 	data = JSON.parse(data);
			console.log(data);
			storeAccount(data);
			setAccount();
		},
		error: () => {
		$("#alerts").empty();
		$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ "Deposit request failed." +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
		}
	});
}

// Withdraw funds from account.
function withdraw() {
	// Retrieve withdraw fields.
	const accountFrom = $("#withdraw-from").val();
	const amount = $("#withdraw-amount").val();

	// Check if fields are valid.
	const errors = validateWithdraw(amount, getCookie(accountFrom));
	
	if (errors.length > 0) return;
	
	// Submit withdraw.
	$.post({
		url: "/withdraw",
		data: {accountKey: accountKey, accountFrom: accountFrom, amount: amount},
		success: (data) => {
		 	data = JSON.parse(data);
			console.log(data);
			storeAccount(data);
			setAccount();
		},
		error: () => {
			$("#alerts").empty();
			$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ "Withdraw request failed." +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
		}
	});
}

// Withdraw funds between accounts.
function transfer() {
	// Retrieve withdraw fields.
	const accountFrom = $("#transfer-from").val();
	const accountTo = $("#transfer-to").val();
	const amount = $("#transfer-amount").val();
	
	// Check if fields are valid.
	const errors = validateTransfer(accountFrom, accountTo, amount, getCookie(accountFrom));
	
	if (errors.length > 0) return;
	
	// Submit transfer.
	$.post({
		url: "/transfer",
		data: {accountKey: accountKey, accountFrom: accountFrom, accountTo: accountTo, amount: amount},
		success: (data) => {
		 	data = JSON.parse(data);
			console.log(data);
			storeAccount(data);
			setAccount();
		},
		error: () => {
			$("#alerts").empty();
			$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ "Transfer request failed." +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
		}
	});
}

// Log user out.
function logout() {
	setCookie("accountKey", "", 5);
	window.location = "/";
}

// Set a cookie.
function setCookie(cname, cvalue, exdays) {
  var d = new Date();
  d.setTime(d.getTime() + (exdays*24*60*60*1000));
  var expires = "expires="+ d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

// Get a cookie.
function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i <ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

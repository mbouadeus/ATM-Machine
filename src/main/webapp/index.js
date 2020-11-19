const accountKey = getCookie("accountKey");

// Check if key is present. If it is, redirect to atm page.
if (accountKey.length > 5) {
	window.location = "/atm.html";
}

// Validate registration fields.
function validateRegistration(name, pin, checkBal, savBal) {
	const errors = [];
	
	// Check if name is valid.
	if (name === "" || !(/^[a-zA-Z\s]*$/.test(name))) {
		errors.push("Invalid Full Name.");
	}
	
	// Check if pin is valid.
	if (pin.length < 4 || !(/^\d+$/.test(pin))) {
		errors.push("Invalid pin. Please make sure it's 4 or more numbers.");
	}
	
	// Check if checking deposit amount is valid.
	if (isNaN(checkBal) || Number(checkBal) < 0) {
		errors.push("Invalid Checking Deposit.");
	}
	
	// Check if savings deposit amount is valid.
	if (isNaN(savBal) || Number(savBal) < 0) {
		errors.push("Invalid Savings Deposit.");
	}

	// Display errors if any.
	$("#alerts").empty();
	errors.forEach(error => {
		$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ error +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
	});
	
	return errors;
}


// Validate login fields.
function validateLogin(customerID, pin) {
	const errors = [];
	
	// Check if customer id is valid.
	if (customerID.length !== 9 || !(/^\d+$/.test(customerID))) {
		errors.push("Invalid customer ID. Please make sure to your 9 number ID.");
	}
	
	// Check if pin is valid.
	if (pin.length < 4 || !(/^\d+$/.test(pin))) {
		errors.push("Invalid pin. Please make sure it's 4 or more numbers.");
	}
	
	// Display errors if any.
	$("#alerts").empty();
	errors.forEach(error => {
		$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ error +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
	});
	
	return errors;
}

// Register a new user account.
function registration() {
	// Retrieve registration fields.
	const name = $("#full-name").val();
	const pin = $("#register-pin").val();
	const checkingBal =  $("#checking-deposit").val();
	const savingsBal =  $("#savings-deposit").val();
	
	// Validate registration fields.
	const errors = validateRegistration(name, pin, checkingBal, savingsBal);
	
	if (errors.length > 0) return;
	
	// Submit registration.
	$.post({
		url: "/register",
		data: {name: name, pin: pin, initialCheckingBalance: checkingBal, initialSavingsBalance: savingsBal},
		success: (data) => {
			console.log(data);
			setCookie("accountKey", data, 5);
			setCookie("justRegistered", true, 5);
			location.reload();
		},
		error: () => {
			$("#alerts").empty();
			$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ "Registration Failed." +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
		}
	});
}

// Log in user.
function login() {
	// Retrieve login fields.
	const customerID = $("#customerID").val();
	const pin = $("#login-pin").val();
	
	// Validate login fields.
	const errors = validateLogin(customerID, pin);
	
	if (errors.length > 0) return;
	
	// Submit login for credentials.
	$.post({
		url: "/authenticate",
		data: {customerID: customerID, pin: pin},
		success: (data) => {
			console.log(data);
			setCookie("accountKey", data, 5);
			location.reload();
		},
		error: () => {
			$("#alerts").empty();
			$("#alerts").append('<div class="alert alert-danger alert-dismissible fade show" role="alert">'+ "Invalid Credentials" +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
		}
	});
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

// Set a cookie.
function setCookie(cname, cvalue, exdays) {
  var d = new Date();
  d.setTime(d.getTime() + (exdays*24*60*60*1000));
  var expires = "expires="+ d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

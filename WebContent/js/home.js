/**
 * Clear specified input value, if the current input value is val
 * 
 * @param input_id input id
 * @param val given val
 * @return void
 * @author zdxue
 */
function clearInput(input_id, val) {
	var inputVal = document.getElementById(input_id).value;
	if(inputVal == val) {
		document.getElementById(input_id).value='';
	}
}

/**
 * Reset input value to the given value, if the current input value was empty
 * 
 * @param input_id input id
 * @param val given value
 * @return void
 * @author zdxue
 */
function resetInput(input_id, val) {
	var inputVal = document.getElementById(input_id).value;
	var reg = /^[\s]+$/;
	if(inputVal.length == 0 || reg.test(inputVal)) {
		document.getElementById(input_id).value=val;	
	}	
}

/**
 * Goto the specified page
 * 
 * @param url given page url
 * @return void
 * @author zdxue
 */
function goPage(url) {
	location.href=url;	//open new page in current window
	//window.open(url);  //open new page in new window
}
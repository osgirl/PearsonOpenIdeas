/*
 * Manage forms 
 */

/**
* @author: Mandeep Singh
*
**/

(function ($, window, document, undefined) {

	/**
	* if an error occurs the textarea is outlined red
	**/
	function highlightContributorTextArea(){
		if($("#contributor-form .form-textarea + .form_row_description + .form_row .form_rightcol.form_error").text().length > 1){
			$("#contributor-form_comments").css("border", "1px solid red");
		}
	}
	
	/**
	* Add a left pointing arrow
	* to the login form
	**/
	function loginFormError(){
		if($("#login .form_error").text().length > 1 && $("#login .form_error .left-arrow").length < 2){
			$("#login .form_error").prepend($("<div class='left-arrow' style='display:block;'></div>"));
		}
	}
	
	$(document).ready(function(){
		highlightContributorTextArea();
		
		if($("#login").length > 0){
			loginFormError();
			setInterval(loginFormError, 500);
		}

	}); //end of document.ready

})(jQuery, this, this.document);
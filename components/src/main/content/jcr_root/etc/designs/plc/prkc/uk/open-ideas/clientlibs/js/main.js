
var Pearson = Pearson || {};

$(document).ready(function(){
	if(self == top){
		document.documentElement.style.display = 'block';
	}else{
//		top.location = self.location;
        document.documentElement.style.display = 'block';
	}

	//check for iPad
	if(navigator.userAgent.match(/iPad/i) != null){
		$("body").addClass("iPad");
	}
	if((navigator.userAgent.match(/Windows NT 5.1/i) != null || navigator.userAgent.match(/Windows NT 5.2/i) != null) && navigator.userAgent.toLowerCase().indexOf('firefox') > -1 ){
	    $("body").addClass("ext-winXp");
	}
	//check for android
	var ua = navigator.userAgent.toLowerCase();
	var isAndroid = ua.indexOf("android") > -1;
	if(isAndroid){
		$("body").addClass("android");
	}
	//check for ie version
	if (navigator.appVersion.indexOf("MSIE 10") != -1)
	{
		$("body").addClass("ext-ie10");
	}
	if (navigator.appVersion.indexOf("MSIE 9") != -1)
	{
		$("body").addClass("ext-ie9");
	}
	if (navigator.appVersion.indexOf("MSIE 8") != -1)
	{
		$("body").addClass("ext-ie8");
	}
	if(navigator.userAgent.indexOf("Safari") > -1 && navigator.userAgent.indexOf("Chrome") < 0){
		$("body").addClass("ext-safari");
	}
	if(navigator.userAgent.indexOf("Chrome") > -1){
		$("body").addClass("ext-chrome");
	}

	/**
	 * Truncate Explore themes
	 */
	 truncateExploreThemes();

	 if($.cookie('login-name')) {
	 	var loggedInName = $.cookie('login-name');
	 } else {
	 	var loggedInName = "User";
	 }

	 var isLoggedIn = false;
	 
	 if($.cookie('login-token')){
	 	isLoggedIn = true;
	 }

	 
	 $('.country-select').uniform();
	 $("#contributor-form_subject").uniform();
	 $("#sign_up_form_country").uniform();
	 $("#profile_country").uniform();
	 $("#no-search-results-dropdown").uniform();
	 
	 /**
	  * Placeholder Items Fix
	  */
	 //top nav search box
	 var searchElement = $("#searchbox");
	 placeHolderFix(searchElement);

	 searchElement = $("#reset_password_email");
	 searchElement.attr("placeholder", "Email Address");
	 placeHolderFix(searchElement);
	 
	 /**
	  * Using the cookie, display
	  * the logged in or login top nav
	  * items. (Depending on if user is
	  * logged in)
	  */
	 if(isLoggedIn){
         // Add name, then show menu item for logged in users
         $('.logged-in-name').text("Hello, " + loggedInName);
         $('.logged-in-li').removeClass("hidden");
	 } else {
         // Show signup and login menu items for unauthenticated users
         $(".signup-li").removeClass("hidden");
         $(".login-li").removeClass("hidden");
	 }
	 
	/** 
	* Header menu click to show dropdowns
	* Hide dropdowns when clicking away or
	* on another item.
	**/
	var all_drop = $('.header-menu ul li a').next('.drop');
	var see_all_sectors = $("#col2 .see-all-sectors");
	var see_all_themes = $("#col1 .see-all-themes");
	var see_all_regions = $("#col3 .see-all-regions");
	
	$('.drop').bind("click", function(e){
		e.stopPropagation();
	});

	$('.header-menu ul li a').click(function(event){
		$(document).unbind("click");
		resetBackground();
		var this_drop = $(this).next(".drop");
		if(is_open(this_drop)){
			close_drop(this_drop);
		}else{
			close_all_but_this_drop(all_drop, this_drop);
		}
		$(this_drop).bind('mouseout',function(e){
	        $(document).bind("click",function(e){
	        	$(".drop").removeClass("open");
	        	$(".drop").addClass("close");
	        	resetBackground();
	        });
	    });
   });

	/**
	 * See/Hide All theme/sectors
	 */
	var open_sectors = false;
	var open_themes = false;
	var open_regions = false;
	
	$(see_all_sectors).click(function(e){
		var this_drop = $(this).parents(".drop");
		if($('.see-all-sectors li a').text().match(/all/i) != null){
			$('.see-all-sectors li a').text("See less");
		}else{
			$('.see-all-sectors li a').text("See all");
		}
		//this refers to the ul element
		if(!open_sectors){
			open_sector_links();
			close_all_but_this_drop(all_drop, this_drop);
			open_sectors = true;
		}else{
			close_sector_links();
			close_all_but_this_drop(all_drop, this_drop);
			open_sectors = false;
		}
		return false;
	});
	
	$(see_all_themes).click(function(e){
		var this_drop = $(this).parents(".drop");
		if($('.see-all-themes li a').text().match(/all/i) != null){
			$('.see-all-themes li a').text("See less");
		}else{
			$('.see-all-themes li a').text("See all");
		}
		//this refers to the ul element
		if(!open_themes){
			open_theme_links();
			close_all_but_this_drop(all_drop, this_drop);
			open_themes = true;
		}else{
			close_theme_links();
			close_all_but_this_drop(all_drop, this_drop);
			open_themes = false;
		}
		return false;
	});
	
	$(see_all_regions).click(function(e){
		var this_drop = $(this).parents(".drop");
		if($('.see-all-regions li a').text().match(/all/i) != null){
			$('.see-all-regions li a').text("See less");
		}else{
			$('.see-all-regions li a').text("See all");
		}
		//this refers to the ul element
		if(!open_regions){
			open_region_links();
			close_all_but_this_drop(all_drop, this_drop);
			open_regions = true;
		}else{
			close_region_links();
			close_all_but_this_drop(all_drop, this_drop);
			open_regions = false;
		}
		return false;
	});

	/**
	 * Hompage Promoboxes
	 * Add an even or odd class to each box
	 * for styling correctly. li:nth child is a alternative
	 * but, is not supported in IE-8 and below.
	 */
	var promo_boxes = $(".stories .homepagePromo");
	for (var i=0, len=promo_boxes.length; i<len; i++) {
	  var promo_box = promo_boxes[i];
	  if (i%2 === 0) {
		  promo_box.className += " even";
	  } else {
		  promo_box.className += " odd";
	  }
	}
	
	/**
	 * Append left arrow to form errors
	 */
	appendLeftArrowtoErrors();
	
	highlightTopNavIcons();
		
	display_form_errors();

}); //end of document.ready

	/**
	 * This function adds the left
	 * pointing arrow to the error boxes
	 * on the profile,signup pages.
	 */
	function appendLeftArrowtoErrors(){
		$(".contributor-page .form_error").prepend($("<div class='left-arrow'></div>"));
	}
	
	/**
	@param drp: the dropdown to open
	**/
	function open_drop(drp){
		var mobileSize = 900;
		var currentSize = $(window).innerWidth();
		drp.removeClass("close");
		drp.addClass("open");
		if(drp.attr("id") == "research-dropdown"){
			if(currentSize <= mobileSize){
				$(".research-li").css("background", "url('/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/down_arrow.png')#3a3a3a no-repeat scroll 97% 50%");
			    $(".research-li").addClass("login-open");
			}else{
				if( !$("body").hasClass("lt-ie9")){
					$(".research-li").css("background", "url('/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/down_arrow.png')#3a3a3a no-repeat scroll 85% 50%");
				    $(".research-li").addClass("login-open");
				}
			}
		}else if(drp.attr("id") == "search-dropdown"){
			if(currentSize <= mobileSize){
				$(".search-li").css("background", "url('/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/down_arrow.png')#3a3a3a no-repeat scroll 97% 50%");
				 $(".search-li").addClass("login-open");
			}else{
				if( !$("body").hasClass("lt-ie9")){
					$(".search-li").css("background", "url('/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/down_arrow.png')#3a3a3a no-repeat scroll 85% 50%");
					$(".search-li").addClass("login-open");
				}
			}
		}else if(drp.attr("id") == "login-dropdown"){
			if( !$("body").hasClass("lt-ie9")){
				$(".login-li").css("background", "url('/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/down_arrow.png')#3a3a3a no-repeat scroll 73% 50%");
				$(".login-li").addClass("login-open");
				$(".logged-in-li").css("background", "url('/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/down_arrow.png')#3a3a3a no-repeat scroll 73% 50%");
				$(".logged-in-li").addClass("login-open");
			}
		}else if(drp.attr("id") == "share-dropdown"){
			if( !$("body").hasClass("lt-ie9")){
				$(".share-li").css("background", "#3a3a3a");
				$(".share-li").addClass("login-open");
			}
		}
		
	}
	/**
	@param drp: the dropdown to close
	**/
	function close_drop(drp){
		drp.addClass("close");
		drp.removeClass("open");
	}
	/**
	@param drp: check if drp is already open
	**/
	function is_open(drp){
		if(drp.hasClass("open")){
			return true;
		}else{
			return false;
		}
	}
	/**
	@param drpList: list of all dropdown in the header nav menu
	@param thisDrp: the drop to keep open
	**/
	function close_all_but_this_drop(drpList, thisDrp){
		var thisDropId = thisDrp.attr("id");
		$(drpList).each(function(index){
			if($(this).attr("id") != thisDrp.attr("id")){
				close_drop($(this));
			}else{
				open_drop($(this));
			}
		});
	}
	/**
	 * remove dark background from top nav
	 */
	function resetBackground(){
		$(".login-open").attr("style", "");
    	$(".login-open").css("background", "url(/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/down_arrow.png') no-repeat scroll 73% 50%");
    	$(".login-open").removeClass("login-open");
	}
	/**
	 * Open/Close See All dropdowns
	 */
	function open_sector_links(){
		$("#col2 .leftboxlist li.tag").removeClass("hide");
		$("#col2 .leftboxlist li.tag").addClass("show");
	}
	
	function close_sector_links(){
		$("#col2 .leftboxlist li.tag").removeClass("show");
		$("#col2 .leftboxlist li.tag").addClass("hide");		
	}
	
	function open_theme_links(){
		$("#col1 .rightboxlist li.tag").removeClass("hide");
		$("#col1 .rightboxlist li.tag").addClass("show");
	}
	
	function close_theme_links(){
		$("#col1 .rightboxlist li.tag").removeClass("show");
		$("#col1 .rightboxlist li.tag").addClass("hide");		
	}

	function open_region_links(){
		$("#col3 .far-rightboxlist li.tag").removeClass("hide");
		$("#col3 .far-rightboxlist li.tag").addClass("show");
	}
	
	function close_region_links(){
		$("#col3 .far-rightboxlist li.tag").removeClass("show");
		$("#col3 .far-rightboxlist li.tag").addClass("hide");		
	}

	(function($) {
	    $.timeSinceTweet = function(time) {
	        var date = new Date(time);
	        var diff = ((new Date()).getTime() - date.getTime()) / 1000;
	        var day_diff = Math.floor(diff / 86400);
	        if (day_diff < 0 || day_diff >= 31 || isNaN(day_diff)) {
	            return "View tweet";
	        }
	        if(day_diff == 0) {
	            if(diff < 60) {
	                return "about " + Math.ceil(diff) + " seconds ago";
	            }
	            else if(diff < 120) {
	                return "about " +  "1 minute ago";
	            }
	            else if(diff < 3600) {
	                return "about " + Math.floor( diff / 60 ) + " minutes ago";
	            }
	            else if(diff < 7200) {
	                return "about " +  "1 hour ago";
	            }
	            else if(diff < 86400) {
	                return "about " + Math.floor( diff / 3600 ) + " hours ago";
	            }
	        }
	        if(day_diff == 1) {
	            return "Yesterday";
	        }
	        else if(day_diff < 7) {
	            return day_diff + " days ago";
	        }
	        else if(day_diff < 31) {
	            return Math.ceil( day_diff / 7 ) + " weeks ago";
	        }
	        else {
	            return "View Tweet";
	        }    
	    }
	})(jQuery);
	
	function truncateExploreThemes(){
		var listOfThemes = $(".rightboxlist li a");
		$.each(listOfThemes, function(index){
			var themeText = $(this).text();
			var themeTextLength = themeText.length;
			var textSizeLimit = 35;
			if(themeTextLength > textSizeLimit){
				themeText = themeText.substring(0, (textSizeLimit-3));
				themeText += "...";
				$(this).text(themeText);
			}
		});
	}
	
	/**
	* Highlight the homepage
	* button in the topNav when on the homepage. 
	*/
	function highlightTopNavIcons(){
		var url = document.URL;
		if ($(".homepage")[0]){
			$(".home-li").css("background", "#393a3a");
		}
		if ($(".events-page")[0]){
			$(".about-li").css("background", "#393a3a");
		}
		if ($(".landing-title").text() == "Contribute" || $(".landing-title").text() == "Contact Us"){
			$(".contribute-li").css("background", "#393a3a");
		}
		if ($(".landing-title").text() == "Sign Up"){
			$(".signup-li").css("background", "#393a3a");
		}
	}
	
	/**
	* Placeholder fix
	*/
	function placeHolderFix(element){
		var originalValue;
		var is_chrome = window.chrome;
		var is_safari = navigator.userAgent.indexOf("Safari") > -1;
		var is_firefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
		if(is_chrome || is_firefox || is_safari){
			$(element).val("");
		}
	    if(navigator.appVersion.indexOf("MSIE") !== -1){
	            originalValue = $(element).val();
	    }else{
	            originalValue = $(element).attr('placeholder');
	    }
	    var placeAttr = $(element).attr('placeholder');
	    $(element).focus(function() {
	                    $(element).attr('placeholder', '');

	                    if(navigator.appVersion.indexOf("MSIE") !== -1 && $(element).val() == originalValue){
	                            $(element).val(""); 
	                    }
	            });
	    $(element).blur(function(){
	            $(element).attr('placeholder', placeAttr);
	            if(navigator.appVersion.indexOf("MSIE") !== -1 && $(element).val() == ""){
	                    $(element).attr('value', originalValue);
	            }
	    });
	}
	
	function display_form_errors(){
		$(".form_row .form_error").each(function(index){
			if($(this).text().length > 1){
				$(this).parent().prev().prev().find("input").css("border", "1px solid red");
				if($(this).parent().parent().hasClass("password")){
					$(".password").find("input").css("border", "1px solid red");
				}
				$("#sign_up_form").height(725);
				$("#profile").height(651);
				if ($('body').is('.ext-ie9, .ext-ie10')) {
					$("#sign_up_form").height(735);
			    }
				if ($('body').is('.ext-ie8')) {
					$("#profile").height(661);
			    }
				
				if($("#login").length > 0){
					$("#login").find("input").css("border", "1px solid red");
				}
				
			}
		});
		
		if($("#reset_password .text div.form_error").text().length > 5){
			$("#reset_password .form + .text + .submit").css("top", "-4px");
			$("#reset_password .form + .text .form_error").css("top", "1px");
			$("#reset_password .form + .text .form_error").css("left", "460px");
		}
		
	}
	/**
	* this shows and hides the  video transcript on our article pages
	*/
	function showHide(shID) {
    	if (document.getElementById(shID)) {
    		if (document.getElementById(shID+'-show').style.display != 'none') {
    			document.getElementById(shID+'-show').style.display = 'none';
    			document.getElementById(shID).style.display = 'block';
    		}
    		else {
    			document.getElementById(shID+'-show').style.display = 'inline';
    			document.getElementById(shID).style.display = 'none';
    		}
    	}
    }
	


/*
 * Get the orientation of the iPad and Android devices
 */


/**
 * @file
 * A JavaScript file detecting the 
 * orientation of a Android or 
 * iPhone device.
 */

(function ($, window, document, undefined) {
	
	function orientation_changed ()
	{
		if ( is_portrait() )
		{
			$("body").removeClass("landscape");
		}
		else if ( is_landscape() )
		{
			  $("body").addClass("landscape");
		}
		clearTimeout(window.t);
		delete window.t;
	}

	function is_landscape()
	{
		var uagent = navigator.userAgent.toLowerCase();
		if ( uagent.search('ipad') > -1 )
		{
			var r = ( window.orientation == 90 || window.orientation == -90 );
		}
		else
		{
			var r = ( screen.width > screen.height );
		}
		return r;
	}

	function is_portrait()
	{
		var uagent = navigator.userAgent.toLowerCase();
		if ( uagent.search('ipad') > -1 )
		{
			var r = ( window.orientation == 0 || window.orientation == 180 );
		}
		else
		{
			var r = ( screen.width < screen.height );
		}
		return r;
	}	

    $(document).ready(function(){
		window.t = undefined;
		window.onorientationchange = function (event)
		{
			window.t = setTimeout('orientation_changed();', 250);
		}
    }); //end of document.ready


})(jQuery, this, this.document);








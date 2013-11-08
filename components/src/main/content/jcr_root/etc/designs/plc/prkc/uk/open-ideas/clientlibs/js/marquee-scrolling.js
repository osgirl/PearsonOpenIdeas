(function($) {

    /***********************************************
     * Cross browser Marquee II- © Dynamic Drive (www.dynamicdrive.com)
     * This notice MUST stay intact for legal use
     * Visit http://www.dynamicdrive.com/ for this script and 100s more.
     ***********************************************/

    var delayb4scroll=2000 //Specify initial delay before marquee starts to scroll on page (2000=2 seconds)
    var marqueespeed=2 //Specify marquee scroll speed (larger is faster 1-10)
    var pauseit=1 //Pause marquee onMousever (0=no. 1=yes)?

////NO NEED TO EDIT BELOW THIS LINE////////////

    var copyspeed = marqueespeed;
    var pausespeed = (pauseit==0)? copyspeed: 0
    var actualheight = '';
    var cross_marquee = jQuery('#all-articles-marquee');
    var marqueeheight = jQuery('#twitter-feed-container').offset();
    var actualheight = jQuery(cross_marquee).offset();  //height of marquee content (much of which is hidden from view)


    function scrollmarquee() {


        //if (parseInt(cross_marquee.style.top) > (actualheight*(-1)+8)) //if scroller hasn't reached the end of its height

        if ( parseInt(jQuery(cross_marquee).css("top") ) > ( actualheight*(-1)+8) ) { //if scroller hasn't reached the end of its height

            //cross_marquee.style.top=parseInt(cross_marquee.style.top)-copyspeed+"px" //move scroller upwards

            //var calcTop = parseInt(jQuery(cross_marquee).css("top") ) - copyspeed+"px" ;

            jQuery(cross_marquee).css({'top':(parseInt(jQuery(cross_marquee).css("top") ) - copyspeed+"px")});

        }

        else {//else, reset to original position

            //cross_marquee.style.top=parseInt(marqueeheight)+8+"px"
            //var calcTop = parseInt(marqueeheight)+8+"px";

            jQuery(cross_marquee).css({'top':(parseInt(marqueeheight)+8+"px")});
            //jQuery(cross_marquee).css("top", calcTop);

        }
    }

    function initializemarquee() {

        alert('page is ready for scrolling....');


        //var cross_marquee = jQuery('#all-articles-marquee');
        jQuery(cross_marquee).css('top', '0');

        if (window.opera || navigator.userAgent.indexOf("Netscape/7")!=-1){ //if Opera or Netscape 7x, add scrollbars to scroll and exit

            jQuery(cross_marquee).css('height', marqueeheight + 'px');
            jQuery(cross_marquee).css('overflow', 'scroll');

            //cross_marquee.style.height=marqueeheight+"px"
            //cross_marquee.style.overflow="scroll"
            return ;
        }

        //setTimeout( setInterval( scrollmarquee, 30), delayb4scroll )
        setTimeout( setInterval( scrollmarquee, 3000), delayb4scroll )

    }


    document.addEventListener( "DOMContentLoaded", initializemarquee, true );


//    if (window.addEventListener)
//        window.addEventListener("load", initializemarquee, false);
//    else if (window.attachEvent)
//        window.attachEvent("onload", initializemarquee);
//    else if (document.getElementById)
//        window.onload=initializemarquee





})($CQ || $);
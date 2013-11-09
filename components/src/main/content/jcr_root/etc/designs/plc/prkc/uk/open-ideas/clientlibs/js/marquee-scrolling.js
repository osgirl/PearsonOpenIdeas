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
    //var actualheight = '';
    var cross_marquee = jQuery('#all-articles-marquee');

    //var marqueeheight = jQuery('#twitter-feed-container').offset().top;
    var marqueeheight = 55;


    //var actualheight = jQuery('#all-articles-marquee').offset().top;  //height of marquee content (much of which is hidden from view)
    var actualheight = 351;



    function initializemarquee() {



        //var cross_marquee = jQuery('#all-articles-marquee');


        jQuery('#all-articles-marquee').css('top', 0);

        if (window.opera || navigator.userAgent.indexOf("Netscape/7")!=-1){ //if Opera or Netscape 7x, add scrollbars to scroll and exit

            jQuery('#all-articles-marquee').css("top").css('height', marqueeheight + 'px');
            jQuery('#all-articles-marquee').css("top").css('overflow', 'scroll');

            //cross_marquee.style.height=marqueeheight+"px"
            //cross_marquee.style.overflow="scroll"
            return ;
        }

        //setTimeout('lefttime=setInterval("scrollmarquee()",30)', delayb4scroll)
        //setTimeout( setInterval( scrollmarquee(), 300), delayb4scroll )

        setTimeout( setInterval( scrollmarquee, 3000), delayb4scroll );

    }


    function scrollmarquee() {

        var topOffset = parseInt(jQuery('#all-articles-marquee').position().top);

        if (topOffset >(actualheight*(-1)+8) )  { //if scroller hasn't reached the end of its height

            console.log('scrollling down...' + topOffset);
            topOffset = topOffset - 100;

            jQuery('#all-articles-marquee').css({'top': topOffset});
        }
        else {//else, reset to original position
            console.log('reset position now..');
            jQuery('#all-articles-marquee').css({'top': 0 });
        }

        //jQuery('#all-articles-marquee').css('top', -50);
    }


    document.addEventListener( "DOMContentLoaded", initializemarquee, true );


//    if (window.addEventListener)
//        window.addEventListener("load", initializemarquee, false);
//    else if (window.attachEvent)
//        window.attachEvent("onload", initializemarquee);
//    else if (document.getElementById)
//        window.onload=initializemarquee





})($CQ || $);




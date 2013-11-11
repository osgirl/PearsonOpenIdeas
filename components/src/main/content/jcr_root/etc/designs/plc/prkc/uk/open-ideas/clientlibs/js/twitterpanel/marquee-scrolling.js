(function($) {

    /***********************************************
     *
     *
     *
     *
     ***********************************************/

    var delayb4scroll = 3000 //Specify initial delay before marquee starts to scroll on page (2000=2 seconds)
    var marqueespeed = 1 //Specify marquee scroll speed (larger is faster 1-10)
    var pauseit = 1 //Pause marquee onMousever (0=no. 1=yes)?

////NO NEED TO EDIT BELOW THIS LINE////////////

    var copyspeed = marqueespeed;
    var maxTweets;
    var twitterArticleHeight;
    var actualheight;

    function initializemarquee() {

        maxTweets = $('#twitter-feed-container').data('numberOfTweets');
        twitterArticleHeight = jQuery('.twitter-article').height();
        actualheight = (maxTweets-1) * twitterArticleHeight;

        jQuery('#all-articles-marquee').css('top', 0);

        if (window.opera || navigator.userAgent.indexOf("Netscape/7")!=-1){ //if Opera or Netscape 7x, add scrollbars to scroll and exit
            jQuery('#all-articles-marquee').css("top").css('height', twitterArticleHeight + 'px');
            jQuery('#all-articles-marquee').css("top").css('overflow', 'scroll');
            return ;
        }
        setTimeout( setInterval( scrollmarquee, 5000), delayb4scroll );
    }

    function scrollmarquee() {

        //scroll only if there are tweets to display
        if (jQuery('#all-articles-marquee').length === 0){
            return;
        }

        var topOffset = parseInt(jQuery('#all-articles-marquee').position().top);

        if (topOffset >(actualheight*(-1)+8) )  { //if scroller hasn't reached the end of its height
            topOffset = topOffset - twitterArticleHeight;
            jQuery('#all-articles-marquee').css({'top': topOffset});
        }
        else {//else, reset to original position
            jQuery('#all-articles-marquee').css({'top': 0 });
        }
    }

    document.addEventListener( "DOMContentLoaded", initializemarquee, true );

})($CQ || $);




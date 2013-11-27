(function($) {

    /***********************************************
     *
     *  Script used for vertical scrolling for tweets inside the twitter panel (based on : http://www.javascriptkit.com/howto/cscroll.shtml).
     *
     *  maxTweets = the number of maximum tweets retrieved through data attribute from the twitterpanel component(default = 5).
     *  delayb4scroll = a small delay the first time when refreshing the page before starting to scroll the tweets.
     *  twitterArticleHeight = the height per tweet, used to calculate the tweet scroll height.
     *  actualheight = the height of all tweets.
     *
     *
     ***********************************************/

    //var marqueespeed = 1 //Specify marquee scroll speed (larger is faster 1-10)
    //var pauseit = 1 //Pause marquee onMousever (0=no. 1=yes)?
    //var copyspeed = marqueespeed;


    var delayb4scroll = 3000 //Specify initial delay before marquee starts to scroll on page (2000=2 seconds)
    var maxTweets;
    var twitterArticleHeight;
    var actualheight;

    function initializemarquee() {

        maxTweets = $('#twitter-feed-container').data('tweetCount');
        twitterArticleHeight = jQuery('.twitter-article').height();
        actualheight = (maxTweets-1) * twitterArticleHeight;

        jQuery('#all-articles-marquee').css('top', 0);

        //if Opera or Netscape 7x, add scrollbars to scroll and exit
        if ( window.opera || navigator.userAgent.indexOf("Netscape/7") !=-1 ) {
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

        //if scroller hasn't reached the end of its height scrolling up with height of twitterArticleHeight
        if (topOffset > ( actualheight*(-1)+8) )  {
            topOffset = topOffset - twitterArticleHeight;
            jQuery('#all-articles-marquee').css({'top': topOffset});
        }
        //reset to original position
        else {
            jQuery('#all-articles-marquee').css({'top': 0 });
        }
    }

    //document.addEventListener( "DOMContentLoaded", initializemarquee, false );
    $( document ).ready(function() {
        initializemarquee();
    });
})($CQ || $);




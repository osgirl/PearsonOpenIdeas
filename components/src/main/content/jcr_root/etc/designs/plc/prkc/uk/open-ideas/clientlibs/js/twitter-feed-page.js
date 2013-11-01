(function($) {


    $(function() {

        var tweets = $("#twitter-feed");
        var twitterAccounts = tweets.attr("data-twitter-accounts");
        var maxTweetcount = 5;


        if(typeof(twitterAccounts) != 'undefined' && twitterAccounts.length > 0 ) {

            var arrayTwitterAccounts = twitterAccounts.split(",");

            $.ajax({
                url: '/bin/twitterServlet',
                data: {arrayTwitterAccounts : arrayTwitterAccounts, maxTweetcount: maxTweetcount},
                dataType: 'json',
                success: function(data) {
                    // loop around the result
                    $(".loading", tweets).remove();
                    var rescount = data['results'].length;
                    for (var res=0 ; res< data['results'].length; res++) {
                        var text = data['results'][res]['text'];
                        var from_user = data['results'][res]['user']['name'];
                        var user_screenName = data['results'][res]['user']['screenName'];
                        var created_at = $.timeSinceTweet(data['results'][res]['createdAt']);
                        var id_str = data['results'][res]['id'];
                        var profile_image_url = data['results'][res]['user']['profileImageUrl'];
                        //Tidy up the text by adding hyperlinks and the date posted
                        text = text.replace(/((ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?)/gi,'<a href="$1">$1</a>')
                                .replace(/(^|\s)#(\w+)/g,'$1<a href="http://search.twitter.com/search?q=%23$2">#$2</a>')
                                .replace(/(^|\s)@(\w+)/g,'$1<a href="http://twitter.com/$2">@$2</a>');

                        text = text + '<br /><a href="http://www.twitter.com/' + user_screenName + '/status/' + id_str + '" class="datelink" target="_blank">' + created_at + '</a></div>';
                        var html = "<article class='twitter-article'>";
                            html += "<div class='header-article'>";
                                html += "<img src=\"" + profile_image_url + "\" alt=\"" + user_screenName + " avatar\" />"
                                html += "<div class='twitter-name'>" + from_user + "</div><br>";
                                html += "<div class='twitter-screen-name'>" + "@" + user_screenName + "</div>";
                                html += "<div class='twitter-image'><img src='/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/twitter_icon.png'/></div>";
                            html += "</div>";
                            html += "<p class='twitter-paragraph'>" + text +"</p>";
                        html += "</article>";
                        tweets.append(html);
                        // After last tweet added call inner scroll function

//                        if(res == (rescount -1)) {
//                            if (tweets.is("[data-scroll=true]")) {
//                                tweets.slimScroll({
//                                    height: '150px'
//                                });
//                            }
//                        }

                    }
                }
            });
        }

    });


})($CQ || $);

(function($) {
    $.timeSinceTweet = function(time) {
        var date = new Date(time);
        var diff = ((new Date()).getTime() - date.getTime()) / 1000;
        var day_diff = Math.floor(diff / 86400);

        if (day_diff < 0 || day_diff >= 31 || isNaN(day_diff)) {
            return "View tweet";
        }

        if (new Date().toDateString() === time.toDateString()){
            return date.toString("hh:mm tt");
        }
        else {
            return date.toDateString();
        }

        // Look also on main.js for timeSinceTweet script...
    }
})($CQ || $);
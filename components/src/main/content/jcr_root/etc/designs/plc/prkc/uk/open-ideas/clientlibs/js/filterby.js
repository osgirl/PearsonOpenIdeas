/*
 * Functionality for the Filter By menu
 */


/**
 * @file
 * A JavaScript file for the Filter By widget
 * Drivers interactivity with the Filter By widget
 */

(function ($, Pearson, window, document, undefined) {

    var $widget = {},
        $widget_window = {},
        anchor_filter_value = null,
        click = window.Touch ? "touchstart" : "click";

    /*
     * Select Filter form category/sector field based on URL
     */
    function setRadioDefault() {
        $(".radio-item", $widget_window).each(function() {
            var $self = $(this);
            if (this.checked) {
                $self.closest(".gc").addClass("selected");
                anchor_filter_value = this.value;
                refreshSelectOptions(this.value);
            }
        });
    }

    function clearForm() {
        $form = $("form", $widget_window);
        $form.find('input:text, input:password, input:file, textarea').val('');
        $form.find('select').each(function(){
            this.selectedIndex = 0;
            $.uniform.update(this);
        });
        $form.find('input:radio, input:checkbox')
            .removeAttr('checked').removeAttr('selected');
        $(".gc", $form).removeClass("selected");
        $(".gc, option").show();
        $form.submit();
    }

    function initRadioFilters(){
        var items = $("li.gc", $widget_window);

        items.on(click, function() {
            var $item = $(this);
            // Deselect non-selected items
            items.removeClass("selected").not($item).find("input").attr('checked', false);
            // Select the clicked item and mark the radio as checked
            $item.addClass("selected");
            $item.find("input")[0].checked = true;
            $item.find("input").trigger("change");
        });
    }

    function windowToggle(eventObj) {
        if ($widget_window.hasClass("open")) {
            $widget_window.removeClass("open");
            $widget_window.off("clickout");
        }
        else {
            $widget_window.addClass("open");
            $widget_window.on("clickout", function(e) {
                windowToggle(e);
            });
        }

    }

    function refreshSelectOptions(selectedValue, exclude) {
        var includeList = Pearson.relationshipJsonData[selectedValue];
        var anchorIncludeList = includeList;
        if (anchor_filter_value) {
            anchorIncludeList = Pearson.relationshipJsonData[anchor_filter_value];
        }
        $("select", $widget_window).not(exclude).each(function(){
            var select = this;
            $("option", this).each(function(){
                var $option = $(this);
                if (this.index === 0 || (($.inArray(this.value, includeList) >= 0 || selectedValue === "All") && ($.inArray(this.value, anchorIncludeList) >= 0 || anchor_filter_value === null))) {
                    this.disabled = false;
                    $option.show();
                }
                else {
                    this.disabled = true;
                    if (this.selected == true) {
                        this.selected = false;
                        $.uniform.update(select);
                    }
                    $option.hide();
                }
            });
        });
    }

    function refreshRadioOptions(selectedValue) {
        var includeList = Pearson.relationshipJsonData[selectedValue];
        $("input:radio", $widget_window).each(function(){
            var $radio_wrapper = $(this).closest(".gc");
            if (selectedValue !== "All" && $.inArray(this.value, includeList) < 0) {
                $radio_wrapper.hide();
            }
            else {
                $radio_wrapper.show();
            }
        });
    }

    function attachChangeEvents() {
        // Attach events that update filter options when selecting new values
        $(".radio-item", $widget_window).change(function(){
            anchor_filter_value = this.value;
            refreshSelectOptions(this.value);
        });
        $("select", $widget_window).change(function(){
            refreshSelectOptions(this.value, this);
            refreshRadioOptions(this.value);
        });
    }


    $(document).ready(function(){

        $widget = $(".filterby");
        $widget_window = $(".filterby-window", $widget);

        // Attach events for sector filters
        initRadioFilters();

        // Trigger active category filter as selected
        setRadioDefault();

        // Attach change events to each filter for updating other options on the fly
        attachChangeEvents();

        // Toggle open close using filter by link
        $(".filterby-open", $widget).on(click, function(e) {
            windowToggle(e);
            e.stopPropagation();
        });

        // Attach event to clear form button
        $(".reset-filter-button", $widget_window).on(click, function(e){
            clearForm();
            e.preventDefault();
        });


    }); //end of document.ready


})(jQuery, Pearson, this, this.document);








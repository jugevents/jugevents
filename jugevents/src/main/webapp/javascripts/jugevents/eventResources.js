function bindUI() {
    $('resourceForm').observe('submit', manageResource);
    $('newResourceLink').observe('click', addNewResource);
    $('resourceType').observe('change', showResourceFields);
}

function manageResource(event) {
    event.stop();
    $('resourceForm').disable();
    var resourceType = $F('resourceType');
    if (resourceType == 'link') {
        AjaxMethodsJS.manageEventLinkResource($F('resourceId'), $F('eventId'), $F('linkUrl'), $F('linkDescription'), true,
        function (data) {
            if ($('addResourceButton').visible()) {
                var newResource = Builder.build(data);
                $('resources').appendChild(newResource);
                Effect.toggle(newResource);
            } else {
                $('res'+$F('resourceId')).replace(data);
                new Effect.Highlight($('res'+$F('resourceId')));
            }
            $('resourceForm').enable();
            clearEventResourceFields();
            $('linkUrl').focus();
            $('addResourceButton').show();
            $('updateResourceButton').hide();
        });    
    }
    if (resourceType == 'flickr') {
        AjaxMethodsJS.manageEventFlickrResource($F('resourceId'), $F('eventId'), $F('flickrTag'), $F('flickrDescription'), true,
        function (data) {
            if ($('addResourceButton').visible()) {
                var newResource = Builder.build(data);
                $('resources').appendChild(newResource);
                new PeriodicalExecuter(function(pe) {
                    if (typeof b_txt != 'undefined') {
                        newResource.down('.flickr_badge_source').insert({before: b_txt});
                        Effect.toggle(newResource);
                        pe.stop();
                    }
                }, 1);
            } else {
                var resourceId = $F('resourceId');
                $('res'+resourceId).replace(data);
                new PeriodicalExecuter(function(pe) {
                    if (typeof b_txt != 'undefined') {
                        $('res'+resourceId).down('.flickr_badge_source').insert({before: b_txt});
                        new Effect.Highlight($('res'+resourceId));
                        pe.stop();
                    }
                }, 1);
            }
            $('resourceForm').enable();
            clearEventResourceFields();
            $('flickrTag').focus();
            $('addResourceButton').show();
            $('updateResourceButton').hide();
        });    
    }    
    if (resourceType == 'slideshare') {
        AjaxMethodsJS.manageEventSlideShareResource($F('resourceId'), $F('eventId'), $F('slideshareId'), $F('slideshareDescription'), true,
        function (data) {
            if ($('addResourceButton').visible()) {
                var newResource = Builder.build(data);
                $('resources').appendChild(newResource);
                Effect.toggle(newResource);
            } else {
                $('res'+$F('resourceId')).replace(data);
                new Effect.Highlight($('res'+$F('resourceId')));
            }
            $('resourceForm').enable();
            clearEventResourceFields();
            $('slideshareId').focus();
            $('addResourceButton').show();
            $('updateResourceButton').hide();
        });    
    }
    if (resourceType == 'archive') {
        AjaxMethodsJS.manageEventArchiveVideoResource($F('resourceId'), $F('eventId'), $F('archiveFlashVideoUrl'), $F('archiveDetailsUrl'), $F('archiveDescription'), true,
        function (data) {
            if ($('addResourceButton').visible()) {
                var newResource = Builder.build(data);
                $('resources').appendChild(newResource);
                Effect.toggle(newResource);
            } else {
                $('res'+$F('resourceId')).replace(data);
                new Effect.Highlight($('res'+$F('resourceId')));
            }
            $('resourceForm').enable();
            clearEventResourceFields();
            $('archiveDetailsUrl').focus();
            $('addResourceButton').show();
            $('updateResourceButton').hide();
        });    
    }
    if (resourceType == 'youtube') {
        AjaxMethodsJS.manageEventYouTubeResource($F('resourceId'), $F('eventId'), $F('youtubeId'), $F('youtubeDescription'), true,
        function (data) {
            if ($('addResourceButton').visible()) {
                var newResource = Builder.build(data);
                $('resources').appendChild(newResource);
                Effect.toggle(newResource);
            } else {
                $('res'+$F('resourceId')).replace(data);
                new Effect.Highlight($('res'+$F('resourceId')));
            }
            $('resourceForm').enable();
            clearEventResourceFields();
            $('youtubeId').focus();
            $('addResourceButton').show();
            $('updateResourceButton').hide();
        });    
    }
}

function addNewResource(event) {
    event.stop();
    clearEventResourceFields();
    $('addResourceButton').show();
    $('updateResourceButton').hide();
    $('resourceType').enable();
    $('resourceFormDiv').show();
    new Effect.ScrollTo('resourceFormDiv', {offset: -24});
}

function modifyEventResource(resourceId) {
    AjaxMethodsJS.fillEventResourceForm(resourceId);
}

function showResourceAddForm() {
    $('addResourceButton').hide();
    $('updateResourceButton').show();
    $('resourceFormDiv').show();
    showResourceFields();
    new Effect.ScrollTo('resourceFormDiv', {offset: -24});    
}

function deleteEventResource(resourceId) {
    AjaxMethodsJS.deleteEventResource(resourceId,
    function (data) {
        if (data) Effect.toggle($('res'+resourceId));
    });
}

function clearEventResourceFields() {
    $('resourceId').clear();
    $('linkUrl').clear();
    $('linkDescription').clear();  
    $('flickrTag').clear();
    $('flickrDescription').clear();  
    $('slideshareId').clear();
    $('slideshareDescription').clear();  
    $('archiveDetailsUrl').clear();
    $('archiveFlashVideoUrl').clear();
    $('archiveDescription').clear();  
    $('youtubeId').clear();
    $('youtubeDescription').clear();  
}

function showResourceFields(event) {
    $('linkFields').hide();
    $('linkLink').hide();
    $('flickrFields').hide();    
    $('flickrLink').hide();
    $('slideshareFields').hide();    
    $('slideshareLink').hide();
    $('archiveFields').hide();    
    $('archiveLink').hide();
    $('youtubeFields').hide();    
    $('youtubeLink').hide();
    $($('resourceType').value+'Fields').show();
    $($('resourceType').value+'Link').show();
}

dwr.util.setEscapeHtml(false);
document.observe('dom:loaded', bindUI);
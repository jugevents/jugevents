function bindUI() {
    $('resourceForm').observe('submit', manageResource);
    $('newResourceLink').observe('click', addNewResource);
}

function manageResource(event) {
    event.stop();
    $('resourceForm').disable();
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

function addNewResource(event) {
    event.stop();
    clearEventResourceFields();
    $('addResourceButton').show();
    $('updateResourceButton').hide();
    $('resourceFormDiv').show();
    new Effect.ScrollTo('resourceFormDiv', {offset: -24});
}

function modifyLinkResource(resourceId) {
    eventResourceBo.fillEventResourceForm(resourceId);
}

function showResourceAddForm() {
    $('addResourceButton').hide();
    $('updateResourceButton').show();
    $('resourceFormDiv').show();
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
}

dwr.util.setEscapeHtml(false);
document.observe('dom:loaded', bindUI);
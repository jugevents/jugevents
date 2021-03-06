<%@ include file="../common.jspf" %>
    <style type="text/css" media="screen">
        .person {
            width: 300px;
            height: 25px;
            border: 1px solid #999;
            padding: 10px;
            font-family: Arial, "MS Trebuchet", sans-serif;
        }

        .winner {
            background: yellow;
        }
        
        #slotMachine {
            background: white url("${cp}/images/winners/mask.png") no-repeat top center;
            width: 100%;
            padding: 0;
        }
        
        #theWheel {
            width: 320px;
            margin: 0 auto 0 auto;
            padding: 40px 0 15px 0;
        }
        
        #p1 {
          background: #e0e0e0 url( "${cp}/images/winners/p1.png" ) repeat-x bottom left;
          text-align: center;
          font-size: 8pt;
        }
        #p2 {
          background: #f0f0f0 url( "${cp}/images/winners/p2.png" ) repeat-x top left;
          text-align: center;
          font-size: 10pt;
        }
        #p3 {
          text-align: center;          
          font-size: 12pt;
        }
        #p4 {
          background: #f0f0f0 url( "${cp}/images/winners/p4.png" ) repeat-x bottom left;
          text-align: center;
          font-size: 10pt;
        }
        #p5 {
          background: #f0f0f0 url( "${cp}/images/winners/p5.png" ) repeat-x top left;
          text-align: center;
          font-size: 8pt;
        }
        
    </style>
    <jwr:script src="${cp}/dwr/interface/AjaxMethodsJS.js" />
    <script type="text/javascript" charset="utf-8">
        var eventId = ${event.id};
        var people = new Array();
        var intervalId;

        function doShift() {
            var firstPerson = people.shift();
            people[people.size()] = firstPerson;
            updateDivs();

            if (people[2].winner == true) {
                clearInterval(intervalId);
                $('p3').addClassName('winner');
                new Effect.Pulsate('p3', {from: 0.6, pulses: 8, afterFinish: function() {
                    $('winnersList').childElements().each(function(item) {
                        item.remove();
                    })

                    AjaxMethodsJS.findAllWinnersForEvent(eventId, function(data) {
                        $A(data).each(function(winner) {
                            Element.insert($('winnersList'), new Element('li', {'id':'winner'+winner.id}).update(winner.firstName + ' ' + winner.lastName + '  [<a href="#" onclick="deleteWinner('+winner.id+', \'winner'+winner.id+'\')">X</a>]'));
                        })
                    });
                }});
            }
        }        

        function updateDivs() {
            $('p1').innerHTML = people[0].firstName + ' ' + people[0].lastName;
            $('p2').innerHTML = people[1].firstName + ' ' + people[1].lastName;
            $('p3').innerHTML = people[2].firstName + ' ' + people[2].lastName;
            $('p4').innerHTML = people[3].firstName + ' ' + people[3].lastName;
            $('p5').innerHTML = people[4].firstName + ' ' + people[4].lastName;
        }

        function chooseWinner() {
            $('p3').removeClassName('winner');
                    AjaxMethodsJS.chooseWinnerForEvent(eventId, function(data) {
                        people = $A(data);
                        updateDivs();
                        intervalId = setInterval(doShift, 500);
                    });
        }

        function deleteWinner(id, domId) {
            Effect.SwitchOff(domId);
            AjaxMethodsJS.setWinner(id, false)
        }
    </script>
            <h1><spring:message code="PrizeDrawingFor"/> <fmt:formatDate value="${event.startDate}" type="date" dateStyle="full" /></h1>
            <div class="secondaryMenu">
                <a href="${cp}/event/show.html?id=${event.id}"><spring:message code="BackToTheEvent"/></a>
            </div>
            <br/>
            <div id="slotMachine">
                <div id="theWheel">
                    <div id="p1"
                         class="person">${nonWinningParticipants[0].firstName} ${nonWinningParticipants[0].lastName}</div>
                    <div id="p2"
                         class="person">${nonWinningParticipants[1].firstName} ${nonWinningParticipants[1].lastName}</div>
                    <div id="p3"
                         class="person">${nonWinningParticipants[2].firstName} ${nonWinningParticipants[2].lastName}</div>
                    <div id="p4"
                         class="person">${nonWinningParticipants[3].firstName} ${nonWinningParticipants[3].lastName}</div>
                    <div id="p5"
                         class="person">${nonWinningParticipants[4].firstName} ${nonWinningParticipants[4].lastName}</div>
                </div>
                <p style="text-align: center; padding-bottom: 35px;">
                    <input type="button" value="<spring:message code="SpinTheWheel"/>" onclick="chooseWinner()"/>
                </p>
            </div>
            <h2><spring:message code="Winners"/></h2>
            <ul id="winnersList">
                <c:forEach var="participant" items="${winningParticipants}">
                    <li id="winner${participant.id}"><c:out value="${participant.firstName} ${participant.lastName}"/> [<a href="#" onclick="deleteWinner(${participant.id}, 'winner${participant.id}'); return false;">X</a>]</li>
                </c:forEach>
            </ul>

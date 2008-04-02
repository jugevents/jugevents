<%@ include file="../../common.jspf" %>
<h1>Details Jugger</h1>

<table>
    <tr>
        <td><spring:message code="juggerRegistrationFirstName"/></td>
        <td>${jugger.firstName}</td>
        
    </tr>
    <tr>
        <td><spring:message code="juggerRegistrationLastName"/></td>
        <td>${jugger.lastName}</td>
        
    </tr>
    <tr>
        <td>email</td>
        <td>${jugger.email}</td>
        
    </tr>
    
    <tr>
        <td>username</td>
        <td>${jugger.user.username}</td>    						 
    </tr>
    
    <tr>
        <td><spring:message code="juggerRegistrationCountry"/></td>                        
        <td>${jugger.jug.country.englishName}</td>    
    </tr>
    <tr>
        <td><spring:message code="juggerRegistrationJUGName"/></td>
        <td>${jugger.jug.name}</td>                        
    </tr>     
    <tr>
        <td>enabled</td>
        <td>${jugger.user.enabled}</td>                        
    </tr>                    
</table>

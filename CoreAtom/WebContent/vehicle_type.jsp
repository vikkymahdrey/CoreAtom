<%-- 
    Document   : vehicle_type
    Created on : Oct 19, 2012, 11:09:44 AM
    Author     : muhammad
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<title>Vehicle Type</title>
<script type="text/javascript">
            function validate()
            {  
                var vehicleType=$("input[id=vehicleType]").val();
                var seat=$("input[id=seat]").val();
                var sittingCapacity=$("input[id=sittingCapacity]").val();
                try{            
            if(vehicleType=="")
                        {
                        alert("Vehicle should not be blank !");
                        return false;
                        }
             else if(seat.trim()==""  )
                        {
                        alert("Seat should not be blank !") ;                           
                        return false;
                        }
            
             else if(sittingCapacity.trim()==""  )
                        {
                        alert("Seat capacity should not be blank !")                            
                        return false;                                
                        }
             else if( isNaN(seat))
  						{
			     		 alert("Seat is invalid !") ;                           
			      		return false;
     		 			}
            if( isNaN(sittingCapacity))
            {
            alert("Enter a Valid Seat Capacity")                            
            return false;                                
            }
              else
                  {
                  return true;
                  }
            return false;    
            }catch(e)
                {
                    alert(e);
                    return false;
                }
            }
        </script>
</head>

<body>
	<%
        long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
       
            empid = Long.parseLong(employeeId);
            %><%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h3>Vehicle Type</h3>
			<hr />
			<form name="vehicleType" action="VehicleType"
				onsubmit="return validate()">

				<table width="70%">
					<tr>
						<td align="center">Vehicle</td>
						<td><input type="text" name="vehicleType" id="vehicleType" /></td>
					</tr>
					<tr>
						<td align="center">Seat</td>
						<td><input type="text" name="seat" id="seat" /></td>
					</tr>
					<tr>
						<td align="center">Seat Capacity</td>
						<td><input type="text" name="sittingCapacity"
							id="sittingCapacity" /></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" class="formbutton" name="submitbtn"
							value="Submit" /> <input type="button" class="formbutton"
							onclick="javascript:history.go(-1);" value="Back" /></td>
					</tr>
				</table>
			</form>

			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>

</html>

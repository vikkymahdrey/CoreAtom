use atomcore; 
select employeeId, trip_log, avgTime, displayName, personnelNo from (
select employeeId, trip_log, ifnull(time(avg(totalTime)),time('0:0')) avgTime from
(
select vtp.tripId, vtp.startTime, vts.employeeId, vts.inTime reachTime, td.trip_date, td.trip_log, 
if(timediff(vts.inTime,vtp.startTime) >0,timediff(vts.inTime,vtp.startTime)  , timediff('24:00', -timediff(vts.inTime,vtp.startTime)) ) totalTime
 from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId
join vendor_trip_sheet vts on vtp.tripId=vts.tripId where td.trip_log='IN'
union
select vtp.tripId, vtp.startTime, vts.employeeId, vts.outTime reachTime, td.trip_date, td.trip_log, 
if(timediff(vts.outTime,vtp.startTime) >0,timediff(vts.outTime,vtp.startTime)  , timediff('24:00', -timediff(vts.outTime,vtp.startTime)) ) totalTime
 from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId
join vendor_trip_sheet vts on vtp.tripId=vts.tripId where td.trip_log='OUT'
) empInTrip where trip_date between '2013-09-01' and '2013-10-01' group by employeeId, trip_log 
 ) e join employee on e.employeeId=id
  where site=1 and trip_log='IN'
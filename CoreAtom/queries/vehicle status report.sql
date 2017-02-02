use atomcore;
/* vehicle status report without filter */ 
select  regNo, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime from (
select v.regNo, trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime
  from  trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vehicles v on td.vehicle=v.id where td.vehicle is not null 
 and   trip_date group by td.trip_date, td.vehicle) tv group by regNo;
  
/* vehicle status report with time slote filtering  */
select regNo, concat(startTime,concat( ' - ',  endTime)) optionLabel, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime
, startTime, endTime 
 from (
select v.regNo,  trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime
, ts.startTime, ts.endTime from trip_details td join timesloat ts on td.trip_time between ts.startTime and ts.endTime   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vehicles v on td.vehicle=v.id where td.vehicle is not null and trip_date group by td.trip_date, td.vehicle
, ts.startTime, ts.endTime 
) tv   group by regNo,   startTime, endTime ;

 
 

/* vehicle status report with distance grid filter  */
select regNo, concat(cast(startKm as char),concat( ' - ', cast(  endKm as char))) optionLabel, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime
, startKm, endKm
 from (
  select v.regNo,  trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime
, dg.startKm, dg.endKm  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  join   distance_grid dg on vtp.distanceCovered between dg.startKm and dg.endKm  join vehicles v on td.vehicle=v.id   where td.vehicle is not null and td.trip_date
group by v.regNo, trip_date, dg.startKm, dg.endKm
) tv  group by regNo, startKm, endKm;

select * from distance_grid;



/* vehicle status report with time of the day filter  */
select  regNo, label optionLabel, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime
 
 from (
select v.regNo,  trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime
, ts.label from trip_details td join time_of_day ts on td.trip_time between ts.startTime and ts.endTime   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId   join vehicles v on td.vehicle=v.id   where td.vehicle is not null and td.trip_date
  group by td.trip_date, v.regNo
, ts.label
) tv where trip_date group by regNo, label ;
 

 
  
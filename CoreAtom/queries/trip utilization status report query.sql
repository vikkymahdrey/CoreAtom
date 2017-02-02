 /* pendingDownloaded joined left with startedStopped starts*/
 select pendingDownloaded.trip_date,  ifnull( pendingCount,0) pendingCount, ifnull( downloadedCount,0) downloadedCount, ifnull( startedCount,0) startedCount, ifnull( stoppedCount ,0)  stoppedCount from (
select trip_date, sum(pendingCount) pendingCount, sum(downloadedCount) downloadedCount from (
/* pending joined left with downloaded starts */
select pendingTrip.trip_date, ifnull(pendingCount,0) pendingCount, ifnull(downloadedCount,0) downloadedCount from (
select  trip_date, count(*) pendingCount from trip_details td where td.id not  in (select tripId from vendor_trip_sheet_parent) and siteId=1 group by trip_date 
) pendingTrip left join  (
select  trip_date, count(*) downloadedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and siteId=1 group by trip_date  
) downloadedTrip on pendingTrip.trip_date=downloadedTrip.trip_date /* pending joined left with downloaded */
union
select downloadedTrip.trip_date, ifnull(pendingCount,0) pendingCount, ifnull(downloadedCount,0) downloadedCount from (
 /* pending joined left & right (full join) with downloaded  starts*/
select  trip_date, count(*) pendingCount from trip_details td where td.id not  in (select tripId from vendor_trip_sheet_parent) and siteId=1 group by trip_date 
) pendingTrip right join  (
select  trip_date, count(*) downloadedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and siteId=1 group by trip_date  
) downloadedTrip on pendingTrip.trip_date=downloadedTrip.trip_date /* pending joined right with downloaded ends */
) pendingDownloaded group by trip_date /* pending joined left & right (full join) with downloaded */
) pendingDownloaded             
 left join 
  (
      /* started joined left & right (full join ) with stopped*/
select trip_date, sum(startedCount) startedCount, sum(stoppedCount) stoppedCount from (
/* started joined left with stopped starts*/
select startedTrip.trip_date, ifnull(startedCount,0) startedCount, ifnull(stoppedCount,0) stoppedCount from (
select  trip_date, count(*) startedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.status='started' and siteId=1 group by trip_date  
) startedTrip left join  (
select  trip_date, count(*) stoppedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.status='stopped' and siteId=1 group by trip_date  
) stoppedTrip on startedTrip.trip_date=stoppedTrip.trip_date /* started joined left with stopped ends*/
union
select stoppedTrip.trip_date, ifnull(startedCount,0) startedCount, ifnull(stoppedCount,0) stoppedCount from (
/* started joined right with stopped starts*/
select  trip_date, count(*) startedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.status='started' and siteId=1 group by trip_date  
) startedTrip right join  (
select  trip_date, count(*) stoppedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.status='stopped' and siteId=1 group by trip_date  
) stoppedTrip on startedTrip.trip_date=stoppedTrip.trip_date /* started joined right with stopped  ends*/
) startedStopped group by trip_date          /* started joined left & right (full join ) with stopped*/
 ) startedStopped

on pendingDownloaded.trip_date=startedStopped.trip_date   /* pendingDownloaded joined left with startedStopped ends*/


union


 /* pendingDownloaded joined right with startedStopped starts*/
 select startedStopped.trip_date, ifnull(  pendingCount,0) pendingCount, ifnull(  downloadedCount,0) downloadedCount,  ifnull( startedCount ,0) startedCount, ifnull( stoppedCount,0) stoppedCount   from (
select trip_date, sum(pendingCount) pendingCount, sum(downloadedCount) downloadedCount from (
/* pending joined left with downloaded starts */
select pendingTrip.trip_date, ifnull(pendingCount,0) pendingCount, ifnull(downloadedCount,0) downloadedCount from (
select  trip_date, count(*) pendingCount from trip_details td where td.id not  in (select tripId from vendor_trip_sheet_parent) and siteId=1 group by trip_date 
) pendingTrip left join  (
select  trip_date, count(*) downloadedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and siteId=1 group by trip_date  
) downloadedTrip on pendingTrip.trip_date=downloadedTrip.trip_date /* pending joined left with downloaded ends*/
union
select downloadedTrip.trip_date, ifnull(pendingCount,0) pendingCount, ifnull(downloadedCount,0) downloadedCount from (
 /* pending joined left & right (full join) with downloaded  starts*/
select  trip_date, count(*) pendingCount from trip_details td where td.id not  in (select tripId from vendor_trip_sheet_parent) and siteId=1 group by trip_date 
) pendingTrip right join  (
select  trip_date, count(*) downloadedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and siteId=1 group by trip_date  
) downloadedTrip on pendingTrip.trip_date=downloadedTrip.trip_date /* pending joined right with downloaded ends */
) pendingDownloaded group by trip_date /* pending joined left & right (full join) with downloaded */
) pendingDownloaded             
 right join 
  (
      /* started joined left & right (full join ) with stopped*/
select trip_date, sum(startedCount) startedCount, sum(stoppedCount) stoppedCount from (
/* started joined left with stopped starts*/
select startedTrip.trip_date, ifnull(startedCount,0) startedCount, ifnull(stoppedCount,0) stoppedCount from (
select  trip_date, count(*) startedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.status='started' and siteId=1 group by trip_date  
) startedTrip left join  (
select  trip_date, count(*) stoppedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.status='stopped' and siteId=1 group by trip_date  
) stoppedTrip on startedTrip.trip_date=stoppedTrip.trip_date /* started joined left with stopped ends*/
union
select   stoppedTrip.trip_date, ifnull(startedCount,0) startedCount, ifnull(stoppedCount,0) stoppedCount from (
/* started joined right with stopped starts*/
select  trip_date, count(*) startedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.status='started' and siteId=1 group by trip_date  
) startedTrip right join  (
select  trip_date, count(*) stoppedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.status='stopped' and siteId=1 group by trip_date  
) stoppedTrip on startedTrip.trip_date=stoppedTrip.trip_date /* started joined right with stopped  ends*/
) startedStopped group by trip_date          /* started joined left & right (full join ) with stopped*/
 ) startedStopped

on pendingDownloaded.trip_date=startedStopped.trip_date   /* pendingDownloaded joined right with startedStopped ends*/